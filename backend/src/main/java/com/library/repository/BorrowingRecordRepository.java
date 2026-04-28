package com.library.repository;

import com.library.model.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    List<BorrowingRecord> findByUser_UserId(Long userId);
}
