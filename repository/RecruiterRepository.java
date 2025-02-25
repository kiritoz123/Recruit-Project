package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.entity.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    @Query("SELECT r FROM Recruiter r " +
            "JOIN UserRole u ON u.roleId = 'ROLE_RECRUITER' " +
            "WHERE (COALESCE(:search, '') = '' OR " +
            "(LOWER(r.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(r.email) LIKE LOWER(CONCAT('%', :search, '%'))))")
    Page<Recruiter> getRecruiters(@Param("search") String search, Pageable pageable);
}
