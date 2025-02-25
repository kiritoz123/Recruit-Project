package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.entity.User;
import org.lib.rms_jobs.entity.VerificationToken;
import org.lib.rms_jobs.exception.CommonException;
import org.lib.rms_jobs.repository.UserRepository;
import org.lib.rms_jobs.repository.VerificationTokenRepository;
import org.lib.rms_jobs.service.VerifycationService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifycationServiceImpl implements VerifycationService {

    private final VerificationTokenRepository verificationTokenRepository;

    private final UserRepository userRepository;

    @Override
    public void verifycation(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CommonException("ERR_TOKEN" ,"Invalid or expired token");
        }

        User user = verificationToken.getUser();
        user.setIsActive(true);
        userRepository.save(user);
        verificationTokenRepository.deleteById(user.getId());
    }
}
