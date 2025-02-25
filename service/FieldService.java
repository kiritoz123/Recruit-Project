package org.lib.rms_jobs.service;

import org.lib.rms_jobs.dto.request.FieldRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.response.FieldResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.entity.Field;

import java.util.List;

/**
 * @ phongtq
 */

public interface FieldService {

    PaginationResponse<FieldResponse> getFields(UserRequest r);

    List<Field> getChild(Long id);

    FieldResponse getGroupField(Long id);

    void addChildField(Long idParent, FieldRequest fr);

    void addParentField(FieldRequest fr);

    void updateField(Long id, FieldRequest fr);
}
