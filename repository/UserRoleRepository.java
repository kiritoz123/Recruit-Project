package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ phongtq
 */

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
