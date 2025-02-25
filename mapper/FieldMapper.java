package org.lib.rms_jobs.mapper;

import org.lib.rms_jobs.dto.FieldDTO;
import org.lib.rms_jobs.dto.response.FieldResponse;
import org.lib.rms_jobs.entity.Field;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    FieldMapper INSTANCE = Mappers.getMapper(FieldMapper.class);

    Field fieldDTOtoField(FieldDTO fieldDTO);

    FieldDTO fieldToFieldDTO(Field field);

    FieldResponse fieldtoFieldResponse(Field field);

    List<FieldResponse> toFieldResponseList(List<Field> fields);
}
