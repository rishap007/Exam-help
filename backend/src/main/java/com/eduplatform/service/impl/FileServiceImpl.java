package com.eduplatform.service.impl;

import com.eduplatform.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String uploadProfilePicture(UUID userId, MultipartFile file) {
        log.info("Uploading profile picture for user: {}", userId);
        validateFile(file, "image");
        return "https://mock-storage.com/profile-pictures/" + userId + "/" + file.getOriginalFilename();
    }

    @Override
    public String uploadCourseThumbnail(UUID courseId, MultipartFile file) {
        log.info("Uploading course thumbnail for course: {}", courseId);
        validateFile(file, "image");
        return "https://mock-storage.com/course-thumbnails/" + courseId + "/" + file.getOriginalFilename();
    }

    @Override
    public List<String> uploadLessonMaterials(UUID lessonId, List<MultipartFile> files) {
        log.info("Uploading {} lesson materials for lesson: {}", files.size(), lessonId);
        return files.stream()
                .map(file -> "https://mock-storage.com/lesson-materials/" + lessonId + "/" + file.getOriginalFilename())
                .collect(Collectors.toList());
    }

    @Override
    public String uploadAssignmentSubmission(UUID submissionId, MultipartFile file) {
        log.info("Uploading assignment submission: {}", submissionId);
        validateFile(file, "document");
        return "https://mock-storage.com/submissions/" + submissionId + "/" + file.getOriginalFilename();
    }

    @Override
    public void deleteFile(String fileUrl) {
        log.info("Deleting file: {}", fileUrl);
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        log.info("Generating presigned URL for file: {}", fileName);
        return "https://mock-storage.com/presigned/" + fileName + "?expires=3600";
    }

    @Override
    public void validateFile(MultipartFile file, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        log.info("File validation passed: {}", file.getOriginalFilename());
    }
}
