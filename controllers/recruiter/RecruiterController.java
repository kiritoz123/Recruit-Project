package org.lib.rms_jobs.controllers.recruiter;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.constant.RequestPath;
import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.service.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RequestPath.API_V1_RECRUITER)
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;

    @GetMapping("/getApplicants")
    public ResponseEntity<?> getApplicants(@RequestBody ApplicantSearchRequest searchRequest){
        PaginationResponse<ApplicantDTO> res =  recruiterService.searchApplicant(searchRequest);
        return ResponseEntity.ok(res);
    }
}
