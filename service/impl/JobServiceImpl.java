package org.lib.rms_jobs.service.impl;


import org.lib.rms_jobs.dto.request.JobRequest;
import org.lib.rms_jobs.dto.request.JobSearchRequest;
import org.lib.rms_jobs.dto.response.*;
import org.lib.rms_jobs.entity.Job;
import org.lib.rms_jobs.entity.Recruiter;
import org.lib.rms_jobs.exception.CommonException;
import org.lib.rms_jobs.repository.JobRepository;
import org.lib.rms_jobs.repository.RecruiterRepository;
import org.lib.rms_jobs.repository.UserRepository;
import org.lib.rms_jobs.security.CustomUserDetail;
import org.lib.rms_jobs.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private RecruiterRepository recruiterRepository;
    @Autowired
    private UserRepository userRepository;


    public PaginationResponse<?> findAllJobs(int page, int size, CustomUserDetail userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobsPage = jobRepository.findAll(pageable);
        List<Object> jobResponse = jobsPage.stream()
                .map(job -> {
                    if (userDetails.hasRole("ROLE_RECRUITER")) {
                        return JobResponseForRecruiter.toJobResponseRecruiter(job);
                    } else if (userDetails.hasRole("ROLE_ADMIN")) {
                        return JobResponse.toJobResponse(job);
                    } else  {
                        return JobResponseForApplicant.toJobResponseApplicant(job);
                    }
                })
                .toList();
        return PaginationResponse.builder()
                .data(jobResponse)
                .page(jobsPage.getNumber())
                .size(jobsPage.getSize())
                .totalPage(jobsPage.getTotalPages())
                .totalSize(jobsPage.getTotalElements())
                .build();
    }

    public ApiResponse<?> findJobById(Long id, CustomUserDetail userDetails) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new CommonException("ERR_JOB_NOT_FOUND", "Job not found"));
        Object jobResponse = userDetails.hasRole("ROLE_RECRUITER")
                ? JobResponseForRecruiter.toJobResponseRecruiter(job)
                : JobResponseForApplicant.toJobResponseApplicant(job);
        return ApiResponse.success(jobResponse);
    }

    public ApiResponse<?> createJob(JobRequest jobRequest, CustomUserDetail userDetails) {
        if (!userDetails.hasRole("ROLE_RECRUITER")) {
            throw new CommonException("ERR_UNAUTHORIZED_ACCESS", "User does not have recruiter privileges");
        }
        Recruiter recruiter = (Recruiter) userRepository.findUserById(userDetails.getUserId());
        if (recruiter == null) {
            return ApiResponse.<String>builder()
                    .statusCode("404")
                    .data("Recruiter not found")
                    .build();
        }
        Job job = JobRequest.toJob(jobRequest);
        job.setRecruiter(recruiter);
        Job savedJob = jobRepository.save(job);
        JobResponseForRecruiter jobResponse = JobResponseForRecruiter.toJobResponseRecruiter(savedJob);
        return ApiResponse.success(jobResponse);
    }

    public Recruiter findRecruiterById(Integer recruiterId) {
        return recruiterRepository.findById(recruiterId).orElse(null);
    }

    public ApiResponse<?> updateJob(Long id, JobRequest jobRequest, CustomUserDetail userDetails) {
        if (!userDetails.hasRole("ROLE_RECRUITER")) {
            throw new CommonException("ERR_UNAUTHORIZED_ACCESS", "User does not have recruiter privileges");
        }
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new CommonException("ERR_JOB_NOT_FOUND", "Job not found"));
        if (!job.getRecruiter().getId().equals(userDetails.getUserId())) {
            throw new CommonException("ERR_UNAUTHORIZED_MODIFICATION",
                    "Unauthorized access: Only the job owner can update this job");
        }
        job.setRequirement(jobRequest.getRequirement());
        job.setResponsibility(jobRequest.getResponsibility());
        job.setOpenCount(jobRequest.getOpenCount());
        job.setStartWorkTime(jobRequest.getStartWorkingTime());
        job.setSalaryFrom(jobRequest.getSalaryFrom());
        job.setSalaryTo(jobRequest.getSalaryTo());
        job.setJobType(jobRequest.getJobType());
        Job updatedJob = jobRepository.save(job);
        JobResponseForRecruiter jobResponse = JobResponseForRecruiter.toJobResponseRecruiter(updatedJob);
        return ApiResponse.success(jobResponse);
    }

    public ApiResponse<Void> deleteJob(Long id, CustomUserDetail userDetails) {
        if (!userDetails.hasRole("ROLE_RECRUITER")) {
            throw new CommonException("ERR_UNAUTHORIZED_ACCESS", "User does not have recruiter privileges");
        }
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new CommonException("ERR_JOB_NOT_FOUND", "Job not found"));
        if (!job.getRecruiter().getId().equals(userDetails.getUserId())) {
            throw new CommonException("ERR_UNAUTHORIZED_DELETION",
                    "Unauthorized access: Only the job owner can delete this job");
        }
        job.setIsDeleted(true);
        jobRepository.save(job);
        return ApiResponse.success(null);
    }

    public ApiResponse<?> findJobsBySalaryRange(double min, double max, CustomUserDetail userDetails) {
        List<Job> jobs = jobRepository.findBySalaryFromBetween(min, max);
        List<Object> jobResponses = jobs.stream()
                .map(job -> {
                    if (userDetails.hasRole("ROLE_RECRUITER")) {
                        return JobResponseForRecruiter.toJobResponseRecruiter(job);
                    } else if (userDetails.hasRole("ROLE_ADMIN")) {
                        return JobResponse.toJobResponse(job);
                    } else  {
                        return JobResponseForApplicant.toJobResponseApplicant(job);
                    }
                })
                .toList();
        return ApiResponse.success(jobResponses);
    }

    public ApiResponse<?> findJobsByFilter(JobSearchRequest jobRequest, CustomUserDetail userDetails) {
        List<Job> jobs = jobRepository.findJobByFilter(
                jobRequest.getJobType(),
                jobRequest.getLocation(),
                jobRequest.getRequirement(),
                jobRequest.getResponsibility(),
                jobRequest.getOpenCount(),
                jobRequest.getSalaryFrom(),
                jobRequest.getSalaryTo(),
                jobRequest.getRecruiterId()
        );
        List<Object> jobResponses = jobs.stream()
                .map(job -> {
                    if (userDetails.hasRole("ROLE_RECRUITER")) {
                        return JobResponseForRecruiter.toJobResponseRecruiter(job);
                    } else if (userDetails.hasRole("ROLE_ADMIN")) {
                        return JobResponse.toJobResponse(job);
                    } else  {
                        return JobResponseForApplicant.toJobResponseApplicant(job);
                    }
                })
                .toList();
        return ApiResponse.success(jobResponses);
    }

    public ApiResponse<?> getJobRecruiter(Long recruiterId, CustomUserDetail userDetails) {
        if (!userDetails.hasRole("ROLE_RECRUITER")) {
            throw new CommonException("ERR_UNAUTHORIZED_ACCESS", "User does not have recruiter privileges");
        }
        List<Job> jobs = jobRepository.findJobByRecruiter(recruiterId);
        List<Object> jobResponses = jobs.stream()
                .map(JobResponseForRecruiter::toJobResponseRecruiter)
                .collect(Collectors.toList());
        return ApiResponse.success(jobResponses);
    }
}

