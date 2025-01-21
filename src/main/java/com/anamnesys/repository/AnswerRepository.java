package com.anamnesys.repository;

import com.anamnesys.controller.dto.AnswerResponseDTO;
import com.anamnesys.repository.model.AnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<AnswerModel, String> {

    @Query("SELECT new com.anamnesys.controller.dto.AnswerResponseDTO(a.answer, a.createdAt, p.name, p.email, p.phone, r.name, u.name) " +
            "FROM AnswerModel a " +
            "JOIN RecordSendModel sr ON sr.id = a.id " +
            "JOIN PatientModel p ON p.id = sr.clientId " +
            "JOIN RecordModel r ON r.id = sr.recordId " +
            "JOIN UserModel u ON u.id = sr.userId " +
            "WHERE a.id = :answerId")
    Optional<AnswerResponseDTO> findAnswerDetailsById(@Param("answerId") String answerId);
}
