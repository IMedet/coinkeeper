package com.app.coinkeeper.service;


import com.app.coinkeeper.dto.TransactionDto;
import com.app.coinkeeper.entity.Category;
import com.app.coinkeeper.entity.Transaction;
import com.app.coinkeeper.entity.User;
import com.app.coinkeeper.repository.CategoryRepository;
import com.app.coinkeeper.repository.TransactionRepository;
import com.app.coinkeeper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionDto> getAllTransactionsForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        return transactionRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TransactionDto getTransaction(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        transactionRepository.delete(transaction);
        return null;
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setTitle(transactionDto.getTitle());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setType(transactionDto.getType());
        transaction.setUser(user);

        if (transactionDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            if (!category.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied to category");
            }

            transaction.setCategory(category);
        }

        transaction = transactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        transaction.setTitle(transactionDto.getTitle());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        transaction.setType(transactionDto.getType());

        if (transactionDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(transactionDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            if (!category.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("Access denied to category");
            }

            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }

        transaction = transactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    public void deleteTransaction(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }
    }

    private TransactionDto convertToDto (Transaction transaction){
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setTitle(transaction.getTitle());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setType(transaction.getType());

        if (transaction.getCategory() != null) {
            dto.setCategoryId(transaction.getCategory().getId());
        }

        return dto;
    }
}


