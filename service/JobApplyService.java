package org.lib.rms_jobs.service;

import org.lib.rms_jobs.entity.JobApply;

import java.util.List;

public interface JobApplyService {
    List<JobApply> getInterviewExpired();
}
