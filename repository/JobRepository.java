package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {

    List<Job> findByJobType(String jobType);

    List<Job> findBySalaryFromBetween(Double minSalary, Double maxSalary);

    Job findJobById(Long id);

    @Query(value = "SELECT j FROM Job j WHERE "
                + "(:job_type IS NULL OR j.jobType = :job_type) "
                + "AND (:location IS NULL OR j.location = :location) "
                + "AND (:require IS NULL OR j.requirement = :require) "
                +"AND (:response IS NULL OR j.responsibility = :response) "
                + "AND (:open_count IS NULL OR j.openCount = :open_count) "
                + "AND (:salary_from IS NULL OR j.salaryFrom = :salary_from) "
                + "AND (:salary_to IS NULL OR j.salaryTo = :salary_to) "
                + "AND (:recruiter_id IS NULL OR j.recruiter = :recruiter_id) ")
    List<Job> findJobByFilter(@Param("job_type") String job_type,
                              @Param("location") String location,
                              @Param("require") String requirement,
                              @Param("response") String response,
                              @Param("open_count") Integer open_count,
                              @Param("salary_from") Double salary_from,
                              @Param("salary_to") Double salary_to,
                              @Param("recruiter_id") Long recruiter_id);

    @Query(value = "select * from jobs j where j.recruiter_id = ?1",nativeQuery = true)
    List<Job> findJobByRecruiter(Long id);
}
