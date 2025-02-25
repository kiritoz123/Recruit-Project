package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantRequest;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.CloudinaryResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.entity.Applicant;
import org.lib.rms_jobs.exception.CommonException;
import org.lib.rms_jobs.mapper.UserMapper;
import org.lib.rms_jobs.repository.ApplicantRepository;
import org.lib.rms_jobs.service.ApplicantService;
import org.lib.rms_jobs.utils.CloudinaryUtils;
import org.lib.rms_jobs.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    private final UserMapper userMapper;

    private final CloudinaryService cloudinaryService;

    private final CloudinaryUtils cloudinaryUtils;


    @Override
    public void updateApplicantCv(Long id, MultipartFile file) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new CommonException("ERR_USER_FOUND","Applicant not found"));
        cloudinaryUtils.assertAllowed(file, CloudinaryUtils.CV_PATTERN);
        String fileName = cloudinaryUtils.getFileName(file.getOriginalFilename());

        CompletableFuture<CloudinaryResponse> responseFuture = cloudinaryService.uploadFile(file, fileName);
        CloudinaryResponse response;
        try {
            response = responseFuture.get();
        } catch (Exception e) {
            throw new CommonException("ERR_FILE_UPLOADED","File upload failed: " + e.getMessage());
        }

        applicant.setApplicantCv(response.getPublicId());
        applicantRepository.save(applicant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApplicant(ApplicantRequest applicantRequest) {
        Applicant applicant = applicantRepository.findById(applicantRequest.getId())
                .orElseThrow(() -> new CommonException("ERR_USER_FOUND","User not found"));
        applicant.setSkill(applicantRequest.getSkill());
        applicant.setEducation(applicantRequest.getEducation());
        applicant.setPhone(applicantRequest.getPhone());
        applicant.setFullName(applicantRequest.getFullName());
        applicantRepository.save(applicant);
    }

    @Override
    public ApplicantDTO getProfile(Long id) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new CommonException("ERR_USER_FOUND","user not found"));
        return this.userMapper.applicantToApplicantDTO(applicant);
    }

}
