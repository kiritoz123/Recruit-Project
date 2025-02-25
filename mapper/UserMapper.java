package org.lib.rms_jobs.mapper;

import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.RecruiterDTO;
import org.lib.rms_jobs.dto.response.ApplicantResponse;
import org.lib.rms_jobs.dto.response.RecruiterResponse;
import org.lib.rms_jobs.entity.Applicant;
import org.lib.rms_jobs.entity.Recruiter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Recruiter recruiterDTOToRecruiter(RecruiterDTO recruiterDTO);

    RecruiterResponse recruiterToRecruiterResponse(Recruiter recruiter);

    ApplicantResponse applicantToApplicantResponse(Applicant applicant);

    RecruiterDTO recruiterToRecruiterDTO(Recruiter recruiter);

    ApplicantDTO applicantToApplicantDTO(Applicant applicant);
}
