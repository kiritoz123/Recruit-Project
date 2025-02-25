package org.lib.rms_jobs.repository;

import org.lib.rms_jobs.dto.response.FieldResponse;
import org.lib.rms_jobs.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    @Query("SELECT f FROM Field f where f.parentId IS NULL " +
            "AND (:search is NULL OR :search = '' OR f.name LIKE CONCAT('%', :search, '%'))")
    Page<Field> getFields(@Param("search") String search,Pageable pageable);

    @Query("Select f from Field f where f.parentId =:id")
    List<Field> findByParentId(@Param("id") Long parentId);
}
