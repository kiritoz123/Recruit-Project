package org.lib.rms_jobs.controllers;

import jakarta.validation.Valid;
import org.lib.rms_jobs.constant.RequestPath;
import org.lib.rms_jobs.constant.RoleEnum;
import org.lib.rms_jobs.dto.UserDTO;
import org.lib.rms_jobs.dto.request.LoginRequest;
import org.lib.rms_jobs.dto.request.ChangePasswordRequest;
import org.lib.rms_jobs.dto.request.ForgotPasswordRequest;
import org.lib.rms_jobs.dto.request.ResetPasswordRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.JwtResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.lib.rms_jobs.dto.response.ApiResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.service.AuthServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @ phongtq
 */

@RestController
@RequestMapping(RequestPath.API_V1_AUTH)
public class AuthController {

    @Autowired
    private AuthServie authServie;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse response = authServie.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        authServie.signUp(userDTO, RoleEnum.APPLICANT);
        ApiResponse<String> response = ApiResponse.success("Sign up successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("verify")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token) {
        authServie.verifyAccount(token);
        ApiResponse<String> response = ApiResponse.success("Verify account successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-pass")
    public ResponseEntity<?> changePass(@RequestHeader("Authorization") String token,
                                        @Valid @RequestBody ChangePasswordRequest changePass) {
        authServie.changePassword(token, changePass);
        ApiResponse<String> response = ApiResponse.success("Change password successful");
        return ResponseEntity.ok(response);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String resetLink = authServie.sendPasswordResetToken(request.getEmail());
        ApiResponse<String> response = ApiResponse.success("Password reset link has been set : " + resetLink);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
                                           @RequestBody ResetPasswordRequest request) {
        authServie.resetPassword(token, request);
        ApiResponse<String> response = ApiResponse.success("Reset password successful");
        return ResponseEntity.ok(response);
    }

    @PutMapping("update-status")
    public ResponseEntity<?> updateUserStatus(@RequestParam("id") Long id) {
        authServie.updateStatus(id);
        ApiResponse<String> response = ApiResponse.success("Update user status successful");
        return ResponseEntity.ok(response);
    }

    @PutMapping("delete-user")
    public ResponseEntity<?> deleteUser(@RequestParam("id") Long id) {
        authServie.updateStatus(id);
        ApiResponse<String> response = ApiResponse.success("Delete user status successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("getRecruiters")
    public ResponseEntity<?> getRecruiters(@RequestBody UserRequest r) {
        PaginationResponse<RecruiterResponse> response = authServie.getRecruiters(r);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getApplicants")
    public ResponseEntity<?> getApplicants(@RequestBody UserRequest r) {
        PaginationResponse<ApplicantResponse> response = authServie.getApplicants(r);
        return ResponseEntity.ok(response);
    }

}
