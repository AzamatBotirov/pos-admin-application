package com.mycompany.pos.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.pos.IntegrationTest;
import com.mycompany.pos.domain.FileStorage;
import com.mycompany.pos.domain.enumeration.FileStorageStatus;
import com.mycompany.pos.repository.FileStorageRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FileStorageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileStorageResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final String DEFAULT_HASH_ID = "AAAAAAAAAA";
    private static final String UPDATED_HASH_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_UPLOAD_PATH = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_PATH = "BBBBBBBBBB";

    private static final FileStorageStatus DEFAULT_FILE_STORAGE_STATUS = FileStorageStatus.ACTIVE;
    private static final FileStorageStatus UPDATED_FILE_STORAGE_STATUS = FileStorageStatus.DRAFT;

    private static final String ENTITY_API_URL = "/api/file-storages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileStorageMockMvc;

    private FileStorage fileStorage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileStorage createEntity(EntityManager em) {
        FileStorage fileStorage = new FileStorage()
            .fileName(DEFAULT_FILE_NAME)
            .extension(DEFAULT_EXTENSION)
            .fileSize(DEFAULT_FILE_SIZE)
            .hashId(DEFAULT_HASH_ID)
            .contentType(DEFAULT_CONTENT_TYPE)
            .uploadPath(DEFAULT_UPLOAD_PATH)
            .fileStorageStatus(DEFAULT_FILE_STORAGE_STATUS);
        return fileStorage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileStorage createUpdatedEntity(EntityManager em) {
        FileStorage fileStorage = new FileStorage()
            .fileName(UPDATED_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .fileSize(UPDATED_FILE_SIZE)
            .hashId(UPDATED_HASH_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadPath(UPDATED_UPLOAD_PATH)
            .fileStorageStatus(UPDATED_FILE_STORAGE_STATUS);
        return fileStorage;
    }

    @BeforeEach
    public void initTest() {
        fileStorage = createEntity(em);
    }

    @Test
    @Transactional
    void createFileStorage() throws Exception {
        int databaseSizeBeforeCreate = fileStorageRepository.findAll().size();
        // Create the FileStorage
        restFileStorageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileStorage)))
            .andExpect(status().isCreated());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeCreate + 1);
        FileStorage testFileStorage = fileStorageList.get(fileStorageList.size() - 1);
        assertThat(testFileStorage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileStorage.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFileStorage.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testFileStorage.getHashId()).isEqualTo(DEFAULT_HASH_ID);
        assertThat(testFileStorage.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testFileStorage.getUploadPath()).isEqualTo(DEFAULT_UPLOAD_PATH);
        assertThat(testFileStorage.getFileStorageStatus()).isEqualTo(DEFAULT_FILE_STORAGE_STATUS);
    }

    @Test
    @Transactional
    void createFileStorageWithExistingId() throws Exception {
        // Create the FileStorage with an existing ID
        fileStorage.setId(1L);

        int databaseSizeBeforeCreate = fileStorageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileStorageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileStorage)))
            .andExpect(status().isBadRequest());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFileStorages() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        // Get all the fileStorageList
        restFileStorageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileStorage.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].extension").value(hasItem(DEFAULT_EXTENSION)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].hashId").value(hasItem(DEFAULT_HASH_ID)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].uploadPath").value(hasItem(DEFAULT_UPLOAD_PATH)))
            .andExpect(jsonPath("$.[*].fileStorageStatus").value(hasItem(DEFAULT_FILE_STORAGE_STATUS.toString())));
    }

    @Test
    @Transactional
    void getFileStorage() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        // Get the fileStorage
        restFileStorageMockMvc
            .perform(get(ENTITY_API_URL_ID, fileStorage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileStorage.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.extension").value(DEFAULT_EXTENSION))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.hashId").value(DEFAULT_HASH_ID))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.uploadPath").value(DEFAULT_UPLOAD_PATH))
            .andExpect(jsonPath("$.fileStorageStatus").value(DEFAULT_FILE_STORAGE_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFileStorage() throws Exception {
        // Get the fileStorage
        restFileStorageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileStorage() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();

        // Update the fileStorage
        FileStorage updatedFileStorage = fileStorageRepository.findById(fileStorage.getId()).get();
        // Disconnect from session so that the updates on updatedFileStorage are not directly saved in db
        em.detach(updatedFileStorage);
        updatedFileStorage
            .fileName(UPDATED_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .fileSize(UPDATED_FILE_SIZE)
            .hashId(UPDATED_HASH_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadPath(UPDATED_UPLOAD_PATH)
            .fileStorageStatus(UPDATED_FILE_STORAGE_STATUS);

        restFileStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFileStorage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFileStorage))
            )
            .andExpect(status().isOk());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
        FileStorage testFileStorage = fileStorageList.get(fileStorageList.size() - 1);
        assertThat(testFileStorage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileStorage.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileStorage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileStorage.getHashId()).isEqualTo(UPDATED_HASH_ID);
        assertThat(testFileStorage.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileStorage.getUploadPath()).isEqualTo(UPDATED_UPLOAD_PATH);
        assertThat(testFileStorage.getFileStorageStatus()).isEqualTo(UPDATED_FILE_STORAGE_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileStorage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileStorage))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileStorage))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileStorage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileStorageWithPatch() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();

        // Update the fileStorage using partial update
        FileStorage partialUpdatedFileStorage = new FileStorage();
        partialUpdatedFileStorage.setId(fileStorage.getId());

        partialUpdatedFileStorage
            .fileSize(UPDATED_FILE_SIZE)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileStorageStatus(UPDATED_FILE_STORAGE_STATUS);

        restFileStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileStorage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileStorage))
            )
            .andExpect(status().isOk());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
        FileStorage testFileStorage = fileStorageList.get(fileStorageList.size() - 1);
        assertThat(testFileStorage.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileStorage.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testFileStorage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileStorage.getHashId()).isEqualTo(DEFAULT_HASH_ID);
        assertThat(testFileStorage.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileStorage.getUploadPath()).isEqualTo(DEFAULT_UPLOAD_PATH);
        assertThat(testFileStorage.getFileStorageStatus()).isEqualTo(UPDATED_FILE_STORAGE_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFileStorageWithPatch() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();

        // Update the fileStorage using partial update
        FileStorage partialUpdatedFileStorage = new FileStorage();
        partialUpdatedFileStorage.setId(fileStorage.getId());

        partialUpdatedFileStorage
            .fileName(UPDATED_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .fileSize(UPDATED_FILE_SIZE)
            .hashId(UPDATED_HASH_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .uploadPath(UPDATED_UPLOAD_PATH)
            .fileStorageStatus(UPDATED_FILE_STORAGE_STATUS);

        restFileStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileStorage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileStorage))
            )
            .andExpect(status().isOk());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
        FileStorage testFileStorage = fileStorageList.get(fileStorageList.size() - 1);
        assertThat(testFileStorage.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileStorage.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testFileStorage.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testFileStorage.getHashId()).isEqualTo(UPDATED_HASH_ID);
        assertThat(testFileStorage.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFileStorage.getUploadPath()).isEqualTo(UPDATED_UPLOAD_PATH);
        assertThat(testFileStorage.getFileStorageStatus()).isEqualTo(UPDATED_FILE_STORAGE_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileStorage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileStorage))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileStorage))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileStorage() throws Exception {
        int databaseSizeBeforeUpdate = fileStorageRepository.findAll().size();
        fileStorage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileStorageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileStorage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileStorage in the database
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFileStorage() throws Exception {
        // Initialize the database
        fileStorageRepository.saveAndFlush(fileStorage);

        int databaseSizeBeforeDelete = fileStorageRepository.findAll().size();

        // Delete the fileStorage
        restFileStorageMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileStorage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileStorage> fileStorageList = fileStorageRepository.findAll();
        assertThat(fileStorageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
