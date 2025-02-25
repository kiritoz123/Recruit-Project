package org.lib.rms_jobs.service;

import jakarta.mail.MessagingException;

import java.util.Map;

/**
 * @ phongtq
 */

public interface EmailService {
    void sendEmailVerificationUrl(String to, String subject, Map<String, Object> variables) throws MessagingException;
}
