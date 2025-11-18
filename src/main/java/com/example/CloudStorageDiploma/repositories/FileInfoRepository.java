package com.example.CloudStorageDiploma.repositories;

import com.example.CloudStorageDiploma.dto.FileInfoDto;
import com.example.CloudStorageDiploma.entities.FileInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    @Query("DELETE FROM FileInfo f WHERE f.fileName = :fileName AND f.userId = :userId")
    void deleteByFileNameAndUserId(@Param("fileName") String fileName,
                                   @Param("userId") long userId);

    @Query("UPDATE FileInfo f SET f.fileName = :newFileName WHERE f.fileName = :oldFileName AND f.userId = :userId")
    void updateFileName(@Param("oldFileName") String oldFileName,
                        @Param("newFileName") String newFileName,
                        @Param("userId") long userId);

    @Query("SELECT f.fileName AS filename,f.fileSize AS size FROM FileInfo f WHERE f.userId = :userId ORDER BY f.fileName LIMIT :limit OFFSET :offset")
    List<FileInfoDto> getFilesList(@Param("userId") long userId,
                                   @Param("limit") Integer limit,
                                   @Param("offset") Integer offset);

    @Query("SELECT f.fileName AS filename,f.fileSize AS size FROM FileInfo f WHERE f.userId = :userId")
    List<FileInfoDto> getFilesList(@Param("userId") long userId);


    @Query("SELECT f FROM FileInfo f WHERE f.fileName = :fileName AND f.userId = :userId")
    FileInfo findByFileNameAndUserId(@Param("fileName") String fileName,
                                     @Param("userId") long userId);


}
