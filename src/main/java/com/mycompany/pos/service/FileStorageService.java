package com.mycompany.pos.service;

import com.mycompany.pos.domain.FileStorage;
import com.mycompany.pos.domain.enumeration.FileStorageStatus;
import com.mycompany.pos.repository.FileStorageRepository;
import com.mycompany.pos.web.rest.errors.BadRequestAlertException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileStorageService {

    @Value("${upload.folder}")
    private String uploadFolder;

    private final Hashids hashids;

    private final FileStorageRepository fileStorageRepository;

    public FileStorageService(FileStorageRepository fileStorageRepository) {
        this.fileStorageRepository = fileStorageRepository;
        this.hashids = new Hashids(getClass().getName(), 6);
    }

    public void save(MultipartFile multipartFile) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.setFileName(multipartFile.getOriginalFilename());
        fileStorage.setExtension(getExt(multipartFile.getOriginalFilename()));
        fileStorage.setFileSize(multipartFile.getSize());
        fileStorage.setContentType(multipartFile.getContentType());
        fileStorage.setFileStorageStatus(FileStorageStatus.DRAFT);
        fileStorageRepository.save(fileStorage);

        //        Date now = new Date();
        //        File uploadFolder = new File(String.format("%s/upload_files/%d/%d/%d/", this.uploadFolder,
        //            1900 + now.getYear(), 1 + now.getMonth(), now.getDate()));
        //        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
        //            System.out.println("Papkalar yaratildi ");
        //        }
        Date now = new Date();
        File uploadFolder = new File(
            String.format("%s/upload_files/%d/%d/%d", this.uploadFolder, 1900 + now.getYear(), 1 + now.getMonth(), now.getDate())
        );
        if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
            log.info("Papkalar yaratildi @######@");
        }

        fileStorage.setHashId(hashids.encode(fileStorage.getId()));
        fileStorage.setUploadPath(
            String.format(
                "upload_files/%d/%d/%d/%s.%s",
                1900 + now.getYear(),
                1 + now.getMonth(),
                now.getDate(),
                fileStorage.getHashId(),
                fileStorage.getExtension()
            )
        );
        fileStorageRepository.save(fileStorage);
        uploadFolder = uploadFolder.getAbsoluteFile();
        File file = new File(uploadFolder, String.format("%s.%s", fileStorage.getHashId(), fileStorage.getExtension()));
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public FileStorage findByHashId(String hashId) {
        return fileStorageRepository.findByHashId(hashId);
    }

    public void delete(String hashId) {
        FileStorage fileStorage = findByHashId(hashId);
        File file = new File(String.format("%s/%s", this.uploadFolder, fileStorage.getUploadPath()));
        if (file.delete()) {
            fileStorageRepository.delete(fileStorage);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteAllDraft() {
        fileStorageRepository.findAllByFileStorageStatus(FileStorageStatus.DRAFT).stream().map(this::definePath).peek(this::delete);
    }

    void delete(FileStorage fileStorage) {
        File file = new File(String.format("%s/%s", this.uploadFolder, fileStorage.getUploadPath()));
        if (file.delete()) {
            fileStorageRepository.delete(fileStorage);
        }
    }

    FileStorage definePath(FileStorage fileStorage) {
        return fileStorage;
    }

    private String getExt(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int bot = fileName.lastIndexOf('.');
            if (bot > 0 && bot <= fileName.length() - 2) {
                ext = fileName.substring(bot + 1);
            }
        }
        assert ext != null;
        if (ext.toLowerCase().equals("jpg")) {
            return ext;
        }

        log.info("Rasm formati jpg bo`lishi kerak!");
        throw new BadRequestAlertException("Rasm formati jpg bo`lishi kerak!", "FileStorage", "String");
    }
}
