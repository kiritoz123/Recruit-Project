package org.lib.rms_jobs.service.impl;

import lombok.RequiredArgsConstructor;
import org.lib.rms_jobs.dto.ApplicantDTO;
import org.lib.rms_jobs.dto.request.ApplicantSearchRequest;
import org.lib.rms_jobs.dto.response.PaginationResponse;
import org.lib.rms_jobs.entity.Applicant;
import org.lib.rms_jobs.mapper.UserMapper;
import org.lib.rms_jobs.repository.ApplicantRepository;
import org.lib.rms_jobs.repository.RecruiterRepository;
import org.lib.rms_jobs.service.RecruiterService;
import org.lib.rms_jobs.utils.PageableUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;

    private final ApplicantRepository applicantRepository;

    private final PageableUtil pageableUtil;

    private final UserMapper userMapper;


    @Override
    public PaginationResponse<ApplicantDTO> searchApplicant(ApplicantSearchRequest r) {
        PageRequest pageRequest = pageableUtil.getPageable(r.getPage(),r.getLimit(),"id", "ASC");

        Page<Applicant> aPage = applicantRepository.getApplicantsBySearch(r.getSkill(),r.getEducation(),pageRequest);

        List<ApplicantDTO> applicantDTOS = aPage.stream().map(this.userMapper::applicantToApplicantDTO)
                .collect(Collectors.toList());

        PageImpl<ApplicantDTO> f = new PageImpl<>(applicantDTOS, pageRequest, aPage.getTotalElements());

        return PaginationResponse.<ApplicantDTO>builder().data(f.getContent())
                .page(aPage.getNumber())
                .size(aPage.getSize())
                .totalPage(aPage.getTotalPages())
                .totalSize(aPage.getTotalElements())
                .build();
    }
}
