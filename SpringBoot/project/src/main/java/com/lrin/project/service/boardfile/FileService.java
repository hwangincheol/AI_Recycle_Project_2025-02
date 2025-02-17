package com.lrin.project.service.boardfile;

import com.lrin.project.entity.boardfile.FileEntity;
import com.lrin.project.repository.boardfile.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    // 파일 저장 디렉토리 application.properties에 설정된 값을 가져온다.
    // file.upload-dir=/uploads/ -> uploadDir 변수에 "/uploads/" 값이 저장
    @Value("${file.upload-dir}")
    private String uploadDir; // 업로드 디렉터리 경로

    @Autowired
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    // MultipartFile : 업로드된 파일 처리하는 인터페이스
    // throws IOException : 파일 저장 중 발생하는 입출력 제외
    public FileEntity saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("파일이 비어 있습니다.");
        }

        // 파일 이름 생성 및 저장 경로 설정
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // 디렉토리 생성
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();  // 디렉토리 없으면 생성
        }

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        // DB에 파일 정보 저장
        FileEntity fileEntity = new FileEntity(
                fileName,
                file.getContentType(),
                filePath.toString(),
                file.getSize()
        );

        return fileRepository.save(fileEntity);  // 저장된 파일 정보 반환
    }

    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            // 파일 시스템에서 파일 삭제
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("파일 삭제 성공: " + filePath);
            } else {
                logger.error("파일 삭제 실패: " + filePath);
            }
        } else {
            logger.error("파일이 존재하지 않음: " + filePath);
        }
    }

}
