package org.lib.rms_jobs.service;

import org.lib.rms_jobs.constant.RoleEnum;
import org.lib.rms_jobs.dto.RecruiterDTO;
import org.lib.rms_jobs.dto.UserDTO;
import org.lib.rms_jobs.dto.request.ChangePasswordRequest;
import org.lib.rms_jobs.dto.request.ForgotPasswordRequest;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @ phongtq
 */

public interface UserService {
    void signUp(UserDTO userDTO, RoleEnum role);

    void updateStatus(Long id);

    void updateRecruiter(RecruiterDTO rDTO);

    void changePassword(String token, ChangePasswordRequest changePasswordRequest);

    void forgotPassword(ForgotPasswordRequest request);

    void deleteUser(Long id);

    Page<RecruiterResponse> getRecruiters(Pageable pageable, String searchName);
    Page<ApplicantResponse> getApplicants(Pageable pageable, String searchName);

    Object getUserDetails(Long id);
}
