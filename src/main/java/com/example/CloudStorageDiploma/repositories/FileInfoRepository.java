package com.example.CloudStorageDiploma.repositories;

import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.entities.FileInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Scope("request")
@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    @Modifying
    @Query("DELETE FROM FileInfo f WHERE f.fileName = :fileName AND f.userId = :userId")
    void deleteByFileNameAndUserId(@Param("fileName") String fileName,
                                   @Param("userId") long userId);

    @Modifying
    @Query("UPDATE FileInfo f SET f.fileName = :newFileName WHERE f.fileName = :oldFileName AND f.userId = :userId")
    int updateFileName(@Param("oldFileName") String oldFileName,
                       @Param("newFileName") String newFileName,
                       @Param("userId") long userId);

    @Query("SELECT new com.example.CloudStorageDiploma.dto.FileInfoDto(f.fileName, f.fileSize) FROM FileInfo f WHERE f.userId = :userId ORDER BY f.fileName LIMIT :limit OFFSET :offset")
    List<FileInfoDto> getFilesList(@Param("userId") long userId,
                                   @Param("limit") Integer limit,
                                   @Param("offset") Integer offset);

    @Query("SELECT new com.example.CloudStorageDiploma.dto.FileInfoDto(f.fileName, f.fileSize) FROM FileInfo f WHERE f.userId = :userId")
    List<FileInfoDto> getFilesList(@Param("userId") long userId);


    @Query("SELECT f FROM FileInfo f WHERE f.fileName = :fileName AND f.userId = :userId")
    FileInfo findByFileNameAndUserId(@Param("fileName") String fileName,
                                     @Param("userId") long userId);


}
