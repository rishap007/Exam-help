package com.eduplatform.service;

// TODO: Create the FileInfoDto class in the dto/response package
// import com.eduplatform.dto.response.FileInfoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * File Service Interface
 * Defines the business logic for file management (e.g., uploading to S3/MinIO).
 */
public interface FileService {

    String uploadProfilePicture(UUID userId, MultipartFile file);

    String uploadCourseThumbnail(UUID courseId, MultipartFile file);

    List<String> uploadLessonMaterials(UUID lessonId, List<MultipartFile> files);

    String uploadAssignmentSubmission(UUID submissionId, MultipartFile file);

    void deleteFile(String fileUrl);

    String generatePresignedUrl(String fileName);

    void validateFile(MultipartFile file, String fileType);

    // FileInfoDto getFileInfo(String fileUrl);
}
