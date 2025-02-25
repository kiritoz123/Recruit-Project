package org.lib.rms_jobs.utils;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {
    public PageRequest getPageable(int page, int limit, String fieldName, String sortType) {
        Sort sort = Sort.by(fieldName).ascending();
        if (sortType.trim().equalsIgnoreCase("DESC")) {
            sort = Sort.by(fieldName).descending();
        }
        return PageRequest.of(page - 1, limit, sort);
    }
}
