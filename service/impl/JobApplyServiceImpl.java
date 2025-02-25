package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.constant.ApplyStatus;
import org.lib.rms_jobs.entity.JobApply;
import org.lib.rms_jobs.repository.JobApplyRepository;
import org.lib.rms_jobs.service.JobApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplyServiceImpl implements JobApplyService {

    @Autowired
    private final JobApplyRepository jobApplyRepository;


    @Override
    public List<JobApply> getInterviewExpired() {
        return jobApplyRepository.findAllByStatus(ApplyStatus.UPDATED_INTERVIEW);
    }
}
