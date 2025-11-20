package com.example.CloudStorageDiploma.repositories;


import com.example.CloudStorageDiploma.entities.User;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Scope("request")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.login = :login")
    User findByLoginAndPassword(@Param("login") String login);
}
