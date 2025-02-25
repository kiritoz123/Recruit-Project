package org.lib.rms_jobs.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FilenameUtils;
import org.lib.rms_jobs.exception.CloudinaryFuncException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ phongtq
 */

@Component
public class CloudinaryUtils {

    @Value("${com.app.cloudinary.file}")
    private long MAX_FILE_SIZE;

    public static final String CV_PATTERN = "([^\\s]+(\\.(?i)(pdf))$)";

    public static final String DATE_FORMAT = "yyyyMMddHHmmss";

    public static final String FILE_NAME_FORMAT = "%s_%s";

    public boolean isAllowedExtension(final String fileName, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(fileName);
        return matcher.matches();
    };

    public void assertAllowed(final MultipartFile file, final String pattern) {
        final long size = file.getSize();
        if(size > MAX_FILE_SIZE) {
            throw new CloudinaryFuncException("Max file size is 2MB " + MAX_FILE_SIZE);
        }
        String fileName = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(fileName);
        if (!isAllowedExtension(fileName, pattern)) {
            throw new CloudinaryFuncException("Only pdf files are allowed");
        }
    };

    public String getFileName(final String name) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String date = dateFormat.format(System.currentTimeMillis());
        return String.format(FILE_NAME_FORMAT, date, name);
    }

}
