package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.entity.User;
import org.lib.rms_jobs.repository.UserRepository;
import org.lib.rms_jobs.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @ phongtq
 */

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public String sendPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email not found");
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setTokenExpirationDate(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        return "http://localhost:8088/api/v1/auth/reset-password?token=" + token;
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token);
        if (user == null || user.getTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired password reset token");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setTokenExpirationDate(null);
        userRepository.save(user);
    }
}
