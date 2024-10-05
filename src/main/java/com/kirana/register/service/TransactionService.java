package com.kirana.register.service;

import com.kirana.register.model.Transaction;
import com.kirana.register.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction recordTransaction(Transaction transaction) {
        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            logger.info("Transaction recorded successfully: {}", savedTransaction);
            return savedTransaction;
        } catch (Exception e) {
            logger.error("Error recording transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to record transaction. Please try again later.");
        }
    }
}
