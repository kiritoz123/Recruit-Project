package org.lib.rms_jobs.controllers;

import org.lib.rms_jobs.dto.request.JobRequest;
import org.lib.rms_jobs.dto.request.JobSearchRequest;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.security.CustomUserDetail;
import org.lib.rms_jobs.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/jobs")
public class  JobController {

    @Autowired
    JobService jobService;

    @GetMapping
    public ResponseEntity<?> findAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetail userDetails) {
        PaginationResponse<?> response = jobService.findAllJobs(page, size, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findJobById(@PathVariable Long id,
                                         @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<?> response = jobService.findJobById(id, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest,
                                       @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<?> response = jobService.createJob(jobRequest,userDetails);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody JobRequest jobRequest,
                                       @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<?> response = jobService.updateJob(id, jobRequest, userDetails);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id,
                                       @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<Void> response = jobService.deleteJob(id, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/salary")
    public ResponseEntity<?> findJobBySalary(@RequestParam double min,
                                             @RequestParam double max,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<?> response = jobService.findJobsBySalaryRange(min, max, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findJobByFilter(@RequestBody JobSearchRequest jobSearchRequest,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) {
        ApiResponse<?> response = jobService.findJobsByFilter(jobSearchRequest, userDetails);
        return ResponseEntity.ok(response);
    }
}


