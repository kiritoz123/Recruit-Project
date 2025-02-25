package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @ phongtq
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    User findByResetPasswordToken(String resetPasswordToken);

    User findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.isActive = :status WHERE u.id = :userId")
    void updateUserStatus(@Param("userId") Long id, @Param("status") Boolean status);

    User findUserById(Long userId);
}
