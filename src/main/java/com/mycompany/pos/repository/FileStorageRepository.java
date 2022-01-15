package com.mycompany.pos.repository;

import com.mycompany.pos.domain.FileStorage;
import com.mycompany.pos.domain.enumeration.FileStorageStatus;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FileStorage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    FileStorage findByHashId(String hashId);

    List<FileStorage> findAllByFileStorageStatus(FileStorageStatus status);
}
