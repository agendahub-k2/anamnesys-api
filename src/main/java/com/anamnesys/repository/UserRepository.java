package com.anamnesys.repository;

import com.anamnesys.repository.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE UserModel u SET u.password = :password, u.updateAt = :updateAt WHERE u.email = :email and u.id = :id")
    void updatePasswordByEmailAndId(@Param("password") String password,
                               @Param("updateAt") LocalDateTime updateAt,
                               @Param("email") String email,
                               @Param("id") Long id);

}
