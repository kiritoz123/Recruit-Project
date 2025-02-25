package org.lib.rms_jobs.service.impl;

import com.cloudinary.Cloudinary;
import org.lib.rms_jobs.dto.response.CloudinaryResponse;
import org.lib.rms_jobs.exception.CloudinaryFuncException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Transactional
    @Async
    public CompletableFuture<CloudinaryResponse> uploadFile(MultipartFile file, String fileName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                final Map result = this.cloudinary.uploader()
                        .upload(file.getBytes(),
                                Map.of("public_id", "rms/" + fileName));
                String url = result.get("secure_url").toString();
                String publicId = result.get("public_id").toString();
                return CloudinaryResponse.builder().publicId(publicId).url(url).build();
            } catch (Exception e) {
                throw new CloudinaryFuncException(e.getMessage());
            }
        });
    };
}
