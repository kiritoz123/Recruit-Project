package org.lib.rms_jobs.service;

import org.lib.rms_jobs.dto.request.JobRequest;
import org.lib.rms_jobs.dto.request.JobSearchRequest;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.entity.Recruiter;
import org.lib.rms_jobs.security.CustomUserDetail;

public interface JobService {
    PaginationResponse<?> findAllJobs(int page, int size, CustomUserDetail userDetails);
    ApiResponse<?> findJobById(Long id, CustomUserDetail userDetails);
    ApiResponse<?> createJob(JobRequest jobRequest, CustomUserDetail userDetails);
    Recruiter findRecruiterById(Integer recruiterId);
    ApiResponse<?> updateJob(Long id, JobRequest jobRequest, CustomUserDetail userDetails);
    ApiResponse<Void> deleteJob(Long id, CustomUserDetail userDetails);
    ApiResponse<?> findJobsBySalaryRange(double min, double max, CustomUserDetail userDetails);
    ApiResponse<?> findJobsByFilter(JobSearchRequest jobRequest, CustomUserDetail userDetails);
    ApiResponse<?> getJobRecruiter(Long recruiterId, CustomUserDetail userDetails);
}
