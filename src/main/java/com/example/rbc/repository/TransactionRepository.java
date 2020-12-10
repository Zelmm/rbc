package com.example.rbc.repository;

import com.example.rbc.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Query(value = "Select * from transactions where id in :ids", nativeQuery = true)
    List<TransactionEntity> getAllByIds(@Param("ids") List<Integer> ids);
}
