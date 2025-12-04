package com.skillswap.credits_service.repository;

import com.skillswap.credits_service.entities.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionRecord, Long> {
    List<TransactionRecord> findByFromUserOrToUserOrderByCreatedAtDesc(String fromUser, String toUser);
}
