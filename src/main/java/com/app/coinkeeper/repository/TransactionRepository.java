package com.app.coinkeeper.repository;

import com.app.coinkeeper.entity.Transaction;
import com.app.coinkeeper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);
}