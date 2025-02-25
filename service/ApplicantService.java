package org.lib.rms_jobs.service;

import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantRequest;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ApplicantService {
    void updateApplicantCv(Long id, MultipartFile file);

    ApplicantDTO getProfile(Long id);

    void updateApplicant(ApplicantRequest applicantRequest);
}
