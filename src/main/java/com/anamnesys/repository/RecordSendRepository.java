package com.anamnesys.repository;

import com.anamnesys.domain.STATUS_RECORD;
import com.anamnesys.repository.model.RecordSendModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RecordSendRepository extends JpaRepository<RecordSendModel, String> {
    List<RecordSendModel> findByUserIdAndClientId(Long userId, Long clientId);

    @Query(value = "SELECT sr.status, sr.client_id, r.name, sr.created_at, sr.email, sr.whatsapp, p.name, sr.id, sr.return_dt " +
            "FROM send_record sr " +
            "INNER JOIN record r ON sr.record_id = r.id " +
            "INNER JOIN patient p ON p.id = sr.client_id " +
            "WHERE sr.user_id = :userId", nativeQuery = true)
    Page<Object[]> findSendRecordsByUserIdAndClientId(
            @Param("userId") Long userId,
            Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE RecordSendModel r SET r.status = :status, r.updateAt = CURRENT_TIMESTAMP WHERE r.id = :id")
    int updateStatusAndUpdateAt(@Param("id") String id, @Param("status") STATUS_RECORD status);

}
