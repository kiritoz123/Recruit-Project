package org.lib.rms_jobs.controllers;

import jakarta.validation.Valid;
import org.lib.rms_jobs.constant.RequestPath;
import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantRequest;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.security.CustomUserDetail;
import org.lib.rms_jobs.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(RequestPath.API_V1_APPLICANT)
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @PostMapping("/updateCv")
    public ResponseEntity<?> updateApplicantCv(@RequestParam("id") Long id, @RequestParam("file") MultipartFile file){
        applicantService.updateApplicantCv(id, file);
        ApiResponse<String> res = ApiResponse.success("Update CV successfully");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateApplicant(
            @Valid @RequestBody ApplicantRequest r
    ){
        applicantService.updateApplicant(r);
        ApiResponse<String> res = ApiResponse.<String>success("Update Profile successfully");
        return ResponseEntity.ok(res);
    }


    @GetMapping("profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetail userDetails){
        ApplicantDTO aDTo = applicantService.getProfile(userDetails.getUserId());
        ApiResponse<ApplicantDTO> res = ApiResponse.success(aDTo);
        return ResponseEntity.ok(res);
    }
}
