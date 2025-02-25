package org.lib.rms_jobs.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.rms_jobs.constant.RoleEnum;
import org.lib.rms_jobs.dto.RecruiterDTO;
import org.lib.rms_jobs.dto.UserDTO;
import org.lib.rms_jobs.dto.request.ChangePasswordRequest;
import org.lib.rms_jobs.dto.request.ForgotPasswordRequest;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.lib.rms_jobs.entity.User;
import org.lib.rms_jobs.entity.Recruiter;
import org.lib.rms_jobs.entity.Applicant;
import org.lib.rms_jobs.entity.UserRole;
import org.lib.rms_jobs.entity.VerificationToken;
import org.lib.rms_jobs.exception.CommonException;
import org.lib.rms_jobs.mapper.UserMapper;
import org.lib.rms_jobs.repository.UserRepository;
import org.lib.rms_jobs.repository.UserRoleRepository;
import org.lib.rms_jobs.repository.VerificationTokenRepository;
import org.lib.rms_jobs.repository.RecruiterRepository;
import org.lib.rms_jobs.repository.ApplicantRepository;
import org.lib.rms_jobs.service.EmailService;
import org.lib.rms_jobs.service.UserService;
import org.lib.rms_jobs.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ phongtq
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    private final VerificationTokenRepository tokenRepository;

    private final EmailService emailService;

    private final JwtUtils jwtUtils;

    private final PasswordResetService passwordResetService;

    private final UserMapper userMapper;

    private final RecruiterRepository recruiterRepository;

    private final ApplicantRepository applicantRepository;

    @Value("${url_verify}")
    private String verificationUrl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUp(UserDTO userDTO, RoleEnum role) {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new CommonException("ERR_USER_EXISTS", "Username : " + userDTO.getUserName());
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CommonException("ERR_USER_EXISTS", "Email: " + userDTO.getEmail());
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(false);
        userRepository.save(user);
        UserRole userRole = new UserRole();
        userRole.setRoleId(role.getValue());
        userRole.setUserId(user.getId());
        userRoleRepository.save(userRole);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        verificationUrl += token;
        Map<String, Object> variables = new HashMap<>();
        variables.put("role", userRole.getRoleId());
        variables.put("url", verificationUrl);
        try {
            emailService.sendEmailVerificationUrl(user.getEmail(), "Xác thực tài khoản", variables);
        } catch (MessagingException e) {
            log.error("Error sending email to {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }

    @Override
    public void updateStatus(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException("ERR_USER_FOUND", "With id : " + id));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecruiter(RecruiterDTO rDTO) {
        Recruiter recruiter = userMapper.recruiterDTOToRecruiter(rDTO);
        recruiterRepository.save(recruiter);
    }

    @Override
    public void changePassword(String token, ChangePasswordRequest changePasswordRequest) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        User user;
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new BadCredentialsException("New Password do not match");
        } else {
            user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new CommonException("ERR_USER_FOUND", "With uername: " + userName));
            boolean isMatch = passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword());
            if (!isMatch) {
                throw new BadCredentialsException("Current Password do not match");
            }
            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getCurrentPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new BadCredentialsException("The new password cannot be the same as the old password.");
            }
        }

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        passwordResetService.sendPasswordResetToken(request.getEmail());
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CommonException("ERR_USER_FOUND", "With id: " + id));
        user.setIsDeleted(false);
        userRepository.save(user);
    }

    @Override
    public Page<RecruiterResponse> getRecruiters(Pageable pageable, String searchName) {
        Page<Recruiter> recruiters = recruiterRepository.getRecruiters(searchName, pageable);

        List<RecruiterResponse> recruiterResponses = recruiters.stream()
                .map(this.userMapper::recruiterToRecruiterResponse).collect(Collectors.toList());
        return new PageImpl<>(recruiterResponses, pageable, recruiters.getTotalElements());
    }

    @Override
    public Page<ApplicantResponse> getApplicants(Pageable pageable, String searchName) {
        Page<Applicant> applicants = applicantRepository.getApplicants(searchName, pageable);

        List<ApplicantResponse> recruiterResponses = applicants.stream()
                .map(this.userMapper::applicantToApplicantResponse).collect(Collectors.toList());
        return new PageImpl<>(recruiterResponses, pageable, applicants.getTotalElements());
    }

    @Override
    public Object getUserDetails(Long id) {
        Object o = userRepository.findById(id).orElseThrow(() -> new CommonException("ERR_USER_NOT_FOUND","User not found"));
        if(o instanceof Applicant) {
            return this.userMapper.applicantToApplicantResponse((Applicant) o);
        }else if(o instanceof Recruiter) {
            return this.userMapper.recruiterToRecruiterResponse((Recruiter) o);
        }else{
            throw new CommonException("ERR_USER_NOT_FOUND", "Cannot find this account");
        }
    }
}
