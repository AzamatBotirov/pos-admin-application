package com.mycompany.pos.domain;

import com.mycompany.pos.domain.enumeration.FileStorageStatus;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * FileStorage entity.\n@author The JHipster team.
 */
@ApiModel(description = "FileStorage entity.\n@author The JHipster team.")
@Entity
@Table(name = "file_storage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FileStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "hash_id")
    private String hashId;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "upload_path")
    private String uploadPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_storage_status")
    private FileStorageStatus fileStorageStatus;

    @ManyToOne
    private Product name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FileStorage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public FileStorage fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return this.extension;
    }

    public FileStorage extension(String extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public FileStorage fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getHashId() {
        return this.hashId;
    }

    public FileStorage hashId(String hashId) {
        this.setHashId(hashId);
        return this;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getContentType() {
        return this.contentType;
    }

    public FileStorage contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getUploadPath() {
        return this.uploadPath;
    }

    public FileStorage uploadPath(String uploadPath) {
        this.setUploadPath(uploadPath);
        return this;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public FileStorageStatus getFileStorageStatus() {
        return this.fileStorageStatus;
    }

    public FileStorage fileStorageStatus(FileStorageStatus fileStorageStatus) {
        this.setFileStorageStatus(fileStorageStatus);
        return this;
    }

    public void setFileStorageStatus(FileStorageStatus fileStorageStatus) {
        this.fileStorageStatus = fileStorageStatus;
    }

    public Product getName() {
        return this.name;
    }

    public void setName(Product product) {
        this.name = product;
    }

    public FileStorage name(Product product) {
        this.setName(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileStorage)) {
            return false;
        }
        return id != null && id.equals(((FileStorage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileStorage{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", extension='" + getExtension() + "'" +
            ", fileSize=" + getFileSize() +
            ", hashId='" + getHashId() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", uploadPath='" + getUploadPath() + "'" +
            ", fileStorageStatus='" + getFileStorageStatus() + "'" +
            "}";
    }
}
