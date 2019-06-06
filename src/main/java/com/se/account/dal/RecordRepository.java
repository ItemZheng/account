package com.se.account.dal;

import com.se.account.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> getAllByBalanceIdAndRemoved(long balanceId, boolean removed);
    Record getRecordByIdAndRemoved(long id, boolean removed);
    Record getRecordByPreDecreaseIdAndRemoved(long preDecreaseId, boolean removed);
}
