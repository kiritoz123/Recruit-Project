package org.lib.rms_jobs.controllers;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.constant.RequestPath;
import org.lib.rms_jobs.dto.request.FieldRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.dto.response.FieldResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.service.FieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(RequestPath.API_V1_FIELD)
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @GetMapping
    public ResponseEntity<?> getFields(@RequestBody UserRequest r){
        PaginationResponse<FieldResponse> res =  fieldService.getFields(r);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/group")
    public ResponseEntity<?> getGroupField(@RequestParam Long id){
        FieldResponse res =  fieldService.getGroupField(id);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add-child")
    public ResponseEntity<?> addChildField(@RequestParam Long id, @RequestBody FieldRequest fr){
        fieldService.addChildField(id, fr);
        ApiResponse<String> res = ApiResponse.success("Added child field");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/add-parent")
    public ResponseEntity<?> addParent(@RequestBody FieldRequest r){
        fieldService.addParentField(r);
        ApiResponse<String> res = ApiResponse.success("Added parent field");
        return ResponseEntity.ok(res);
    }

    @PutMapping
    public ResponseEntity<?> updateField(@RequestParam Long id,@RequestBody FieldRequest r){
        fieldService.updateField(id,r);
        ApiResponse<String> res = ApiResponse.success("Updated field successfully");
        return ResponseEntity.ok(res);
    }
}
