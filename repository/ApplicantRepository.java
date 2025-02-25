package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.entity.Applicant;
import org.lib.rms_jobs.entity.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @ phongtq
 */

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query("SELECT a FROM Applicant a " +
            "JOIN UserRole u ON u.roleId = 'ROLE_APPLICANT' " +
            "WHERE (COALESCE(:search, '') = '' OR " +
            "(LOWER(a.userName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.email) LIKE LOWER(CONCAT('%', :search, '%'))))")
    Page<Applicant> getApplicants(@Param("search") String search, Pageable pageable);

    @Query("SELECT a FROM Applicant a " +
            "JOIN UserRole u ON u.roleId = 'ROLE_APPLICANT' " +
            "WHERE (COALESCE(:skill, '') = '' OR LOWER(a.skill) LIKE LOWER(CONCAT('%', :skill, '%'))) " +
            "AND (COALESCE(:education, '') = '' OR LOWER(a.education) LIKE LOWER(CONCAT('%', :education, '%')))")
    Page<Applicant> getApplicantsBySearch(@Param("skill") String skill,
                                     @Param("education") String education,
                                     Pageable pageable);

    Optional<Applicant> findByUserName(String username);
}
