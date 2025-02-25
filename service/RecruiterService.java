package org.lib.rms_jobs.service;

import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.PaginationResponse;

public interface RecruiterService {
    PaginationResponse<ApplicantDTO> searchApplicant(ApplicantSearchRequest applicantRequest);
}
