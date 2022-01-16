package com.mycompany.pos.web.rest;

import com.mycompany.pos.domain.FileStorage;
import com.mycompany.pos.repository.FileStorageRepository;
import com.mycompany.pos.service.FileStorageService;
import com.mycompany.pos.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.pos.domain.FileStorage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FileStorageResource {

    private final Logger log = LoggerFactory.getLogger(FileStorageResource.class);

    private static final String ENTITY_NAME = "fileStorage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${upload.folder}")
    private String uploadFolder;

    public final FileStorageService fileStorageService;

    private final FileStorageRepository fileStorageRepository;

    public FileStorageResource(FileStorageService fileStorageService, FileStorageRepository fileStorageRepository) {
        this.fileStorageService = fileStorageService;
        this.fileStorageRepository = fileStorageRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile multipartFile) {
        fileStorageService.save(multipartFile);
        return ResponseEntity.ok(multipartFile.getOriginalFilename() + "_fail saqlanqi");
    }

    @GetMapping("/preview/{hashId}")
    public ResponseEntity previewFile(@PathVariable String hashId) throws IOException {
        FileStorage fileStorage = fileStorageService.findByHashId(hashId);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(fileStorage.getHashId()))
            .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
            .contentLength(fileStorage.getFileSize())
            .body(new FileUrlResource(String.format("%s/%s", uploadFolder, fileStorage.getUploadPath())));
    }

    @GetMapping("/downland/{hashId}")
    public ResponseEntity downlandFile(@PathVariable String hashId) throws IOException {
        FileStorage fileStorage = fileStorageService.findByHashId(hashId);
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(fileStorage.getFileName()))
            .contentType(MediaType.parseMediaType(fileStorage.getContentType()))
            .contentLength(fileStorage.getFileSize())
            .body(new FileUrlResource(String.format("%s/%s", uploadFolder, fileStorage.getUploadPath())));
    }

    @DeleteMapping("/delete/{hashId}")
    public ResponseEntity delete(@PathVariable String hashId) {
        fileStorageService.delete(hashId);
        return ResponseEntity.ok("ocirildi");
    }

    /**
     * {@code POST  /file-storages} : Create a new fileStorage.
     *
     * @param fileStorage the fileStorage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileStorage, or with status {@code 400 (Bad Request)} if the fileStorage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-storages")
    public ResponseEntity<FileStorage> createFileStorage(@RequestBody FileStorage fileStorage) throws URISyntaxException {
        log.debug("REST request to save FileStorage : {}", fileStorage);
        if (fileStorage.getId() != null) {
            throw new BadRequestAlertException("A new fileStorage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileStorage result = fileStorageRepository.save(fileStorage);
        return ResponseEntity
            .created(new URI("/api/file-storages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /file-storages/:id} : Updates an existing fileStorage.
     *
     * @param id          the id of the fileStorage to save.
     * @param fileStorage the fileStorage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileStorage,
     * or with status {@code 400 (Bad Request)} if the fileStorage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileStorage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-storages/{id}")
    public ResponseEntity<FileStorage> updateFileStorage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileStorage fileStorage
    ) throws URISyntaxException {
        log.debug("REST request to update FileStorage : {}, {}", id, fileStorage);
        if (fileStorage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileStorage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileStorageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FileStorage result = fileStorageRepository.save(fileStorage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileStorage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /file-storages/:id} : Partial updates given fields of an existing fileStorage, field will ignore if it is null
     *
     * @param id          the id of the fileStorage to save.
     * @param fileStorage the fileStorage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileStorage,
     * or with status {@code 400 (Bad Request)} if the fileStorage is not valid,
     * or with status {@code 404 (Not Found)} if the fileStorage is not found,
     * or with status {@code 500 (Internal Server Error)} if the fileStorage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/file-storages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FileStorage> partialUpdateFileStorage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FileStorage fileStorage
    ) throws URISyntaxException {
        log.debug("REST request to partial update FileStorage partially : {}, {}", id, fileStorage);
        if (fileStorage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fileStorage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fileStorageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FileStorage> result = fileStorageRepository
            .findById(fileStorage.getId())
            .map(existingFileStorage -> {
                if (fileStorage.getFileName() != null) {
                    existingFileStorage.setFileName(fileStorage.getFileName());
                }
                if (fileStorage.getExtension() != null) {
                    existingFileStorage.setExtension(fileStorage.getExtension());
                }
                if (fileStorage.getFileSize() != null) {
                    existingFileStorage.setFileSize(fileStorage.getFileSize());
                }
                if (fileStorage.getHashId() != null) {
                    existingFileStorage.setHashId(fileStorage.getHashId());
                }
                if (fileStorage.getContentType() != null) {
                    existingFileStorage.setContentType(fileStorage.getContentType());
                }
                if (fileStorage.getUploadPath() != null) {
                    existingFileStorage.setUploadPath(fileStorage.getUploadPath());
                }
                if (fileStorage.getFileStorageStatus() != null) {
                    existingFileStorage.setFileStorageStatus(fileStorage.getFileStorageStatus());
                }

                return existingFileStorage;
            })
            .map(fileStorageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileStorage.getId().toString())
        );
    }

    /**
     * {@code GET  /file-storages} : get all the fileStorages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileStorages in body.
     */
    @GetMapping("/file-storages")
    public List<FileStorage> getAllFileStorages() {
        log.debug("REST request to get all FileStorages");
        return fileStorageRepository.findAll();
    }

    /**
     * {@code GET  /file-storages/:id} : get the "id" fileStorage.
     *
     * @param id the id of the fileStorage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileStorage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-storages/{id}")
    public ResponseEntity<FileStorage> getFileStorage(@PathVariable Long id) {
        log.debug("REST request to get FileStorage : {}", id);
        Optional<FileStorage> fileStorage = fileStorageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fileStorage);
    }

    /**
     * {@code DELETE  /file-storages/:id} : delete the "id" fileStorage.
     *
     * @param id the id of the fileStorage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-storages/{id}")
    public ResponseEntity<Void> deleteFileStorage(@PathVariable Long id) {
        log.debug("REST request to delete FileStorage : {}", id);
        fileStorageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
