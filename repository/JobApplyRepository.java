package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.constant.ApplyStatus;
import org.lib.rms_jobs.entity.JobApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplyRepository extends JpaRepository<JobApply, Long> {

    List<JobApply> findAllByStatus(ApplyStatus status);
}
