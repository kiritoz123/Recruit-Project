package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lib.rms_jobs.dto.request.FieldRequest;
import org.lib.rms_jobs.dto.request.UserRequest;
import org.lib.rms_jobs.dto.response.FieldResponse;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.entity.Field;
import org.lib.rms_jobs.entity.JobPosition;
import org.lib.rms_jobs.exception.CommonException;
import org.lib.rms_jobs.mapper.FieldMapper;
import org.lib.rms_jobs.repository.FieldRepository;
import org.lib.rms_jobs.repository.JobPositionRepository;
import org.lib.rms_jobs.service.FieldService;
import org.lib.rms_jobs.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ phongtq
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;

    private final FieldMapper fieldMapper;

    private final PageableUtil pageableUtil;
    private final JobPositionRepository jobPositionRepository;


    @Override
    public PaginationResponse<FieldResponse> getFields(UserRequest r) {
        PageRequest pageRequest = pageableUtil.getPageable(
                r.getPage(), r.getLimit(), r.getFieldName(), r.getSortType()
        );
        Page<Field> fieldPage = fieldRepository.getFields(r.getSearchName(),pageRequest);

        List<FieldResponse> fields = fieldPage.stream().map(this.fieldMapper::fieldtoFieldResponse)
                .collect(Collectors.toList());

        PageImpl<FieldResponse> f = new PageImpl<>(fields, pageRequest, fieldPage.getTotalElements());

        return PaginationResponse.<FieldResponse>builder().data(f.getContent())
                .page(fieldPage.getNumber())
                .size(fieldPage.getSize())
                .totalPage(fieldPage.getTotalPages())
                .totalSize(fieldPage.getTotalElements())
                .build();
    }

    @Override
    public List<Field> getChild(Long id) {
        return fieldRepository.findByParentId(id).stream()
                .peek(child -> child.setChilds(getChild(child.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public FieldResponse getGroupField(Long id) {
        Field parentField = fieldRepository.findById(id).orElseThrow(() ->
                new CommonException("ERR_FILED_FOUND","Field not found"));
        List<Field> children = fieldRepository.findByParentId(parentField.getId()).stream()
                .peek(child -> child.setChilds(getChild(child.getId()))).collect(Collectors.toList());
        parentField.setChilds(children);
        return fieldMapper.fieldtoFieldResponse(parentField);
    }

    @Override
    public void addChildField(Long idParent, FieldRequest fr) {
        Field f = new Field();
        f.setParentId(idParent);
        f.setName(fr.getName());
        f.setDescription(fr.getDescription());
        f.setCreatedAt(LocalDateTime.now());
        f.setIsActive(true);
        fieldRepository.save(f);
    }

    @Override
    public void addParentField(FieldRequest fr) {
        Field field = new Field();
        field.setName(fr.getName());
        field.setDescription(fr.getDescription());
        field.setCreatedAt(LocalDateTime.now());
        field.setIsActive(true);
        fieldRepository.save(field);
    }

    @Override
    public void updateField(Long id, FieldRequest fr) {
        Field f = fieldRepository.findById(id).orElseThrow(() -> new CommonException("ERR_FILED_NOT_FOUND","Field not found"));
        f.setName(fr.getName());
        f.setDescription(fr.getDescription());
        f.setUpdatedAt(LocalDateTime.now());
        fieldRepository.save(f);
    }
}
