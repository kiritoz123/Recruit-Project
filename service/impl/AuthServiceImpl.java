package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.constant.RoleEnum;
import org.lib.rms_jobs.dto.UserDTO;
import org.lib.rms_jobs.dto.request.LoginRequest;
import org.lib.rms_jobs.dto.request.ResetPasswordRequest;
import org.lib.rms_jobs.dto.request.ForgotPasswordRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.request.ChangePasswordRequest;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.JwtResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.lib.rms_jobs.entity.User;
import org.lib.rms_jobs.repository.UserRepository;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.security.CustomUserDetail;
import org.lib.rms_jobs.service.AuthServie;
import org.lib.rms_jobs.service.UserService;
import org.lib.rms_jobs.service.VerifycationService;
import org.lib.rms_jobs.utils.JwtUtils;
import org.lib.rms_jobs.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ phongtq
 */

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServie {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtil;

    private final UserRepository userRepository;

    private final PasswordResetService passwordResetService;

    private final UserService userService;

    private final VerifycationService verifycationService;

    private final PageableUtil pageableUtil;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        User user = userRepository.findByUserName(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new BadCredentialsException("User account is not verified");
        }

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(jwt, "Bearer");
    }

    @Override
    public void verifyAccount(String token) {
        verifycationService.verifycation(token);
    }

    @Override
    public void signUp(UserDTO userDTO, RoleEnum role) {
        userService.signUp(userDTO, role);
    }

    @Override
    public String sendPasswordResetToken(String email) {
        return passwordResetService.sendPasswordResetToken(email);
    }

    @Override
    public void resetPassword(String token, ResetPasswordRequest request) {
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new BadCredentialsException("Confirm passwords don't match");
        }
        passwordResetService.resetPassword(token, request.getNewPassword());
    }

    @Override
    public void changePassword(String token, ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(token,changePasswordRequest);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        passwordResetService.sendPasswordResetToken(request.getEmail());
    }

    public void updateStatus(Long id){
        userService.updateStatus(id);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public PaginationResponse<RecruiterResponse> getRecruiters(UserRequest r) {

        PageRequest pageRequest = pageableUtil.getPageable(r.getPage(), r.getLimit(), r.getFieldName(), r.getSortType());

        Page<RecruiterResponse> recruiterPage = userService.getRecruiters(pageRequest, r.getSearchName());

        return PaginationResponse.<RecruiterResponse>builder().data(recruiterPage.getContent())
                .page(recruiterPage.getNumber())
                .size(recruiterPage.getSize())
                .totalPage(recruiterPage.getTotalPages())
                .totalSize(recruiterPage.getTotalElements())
                .build();
    }

    @Override
    public PaginationResponse<ApplicantResponse> getApplicants(UserRequest r) {
        PageRequest pageRequest = pageableUtil.getPageable(r.getPage(), r.getLimit(), r.getFieldName(), r.getSortType());

        Page<ApplicantResponse> applicantPage = userService.getApplicants(pageRequest, r.getSearchName());

        return PaginationResponse.<ApplicantResponse>builder().data(applicantPage.getContent())
                .page(applicantPage.getNumber())
                .size(applicantPage.getSize())
                .totalPage(applicantPage.getTotalPages())
                .totalSize(applicantPage.getTotalElements())
                .build();
    }
}
