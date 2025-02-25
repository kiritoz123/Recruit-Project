package org.lib.rms_jobs.service;

import org.lib.rms_jobs.constant.RoleEnum;
import org.lib.rms_jobs.dto.UserDTO;
import org.lib.rms_jobs.dto.request.LoginRequest;
import org.lib.rms_jobs.dto.request.ResetPasswordRequest;
import org.lib.rms_jobs.dto.request.ChangePasswordRequest;
import org.lib.rms_jobs.dto.request.ForgotPasswordRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.JwtResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;

public interface AuthServie {
    public JwtResponse authenticate(LoginRequest loginRequest);
    public void verifyAccount(String token);
    public void signUp(UserDTO userDTO, RoleEnum role);
    String sendPasswordResetToken(String email);
    void resetPassword(String token, ResetPasswordRequest request);
    void changePassword(String token, ChangePasswordRequest changePasswordRequest);
    void forgotPassword(ForgotPasswordRequest request);
    void updateStatus(Long id);
    void deleteUser(Long id);
    PaginationResponse<RecruiterResponse> getRecruiters(UserRequest r);
    PaginationResponse<ApplicantResponse> getApplicants(UserRequest r);
}
