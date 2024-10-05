package com.kirana.register.controller;

import com.kirana.register.dto.TransactionDTO;
import com.kirana.register.exception.CustomException;
import com.kirana.register.model.Transaction;
import com.kirana.register.service.TransactionService;
import com.kirana.register.service.ReportService;
import com.kirana.register.util.CurrencyConversionUtil;
import com.kirana.register.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private CurrencyConversionUtil currencyConversionUtil;

    // Record a transaction
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> recordTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            logger.info("Starting to record a transaction for customer: {}", transactionDTO.getCustomerUserName());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = ((UserDetails) auth.getPrincipal()).getUsername();


            // Validation: Amount should be positive
            if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Invalid transaction amount: {}. Amount should be positive.", transactionDTO.getAmount());
                throw new CustomException("Transaction amount must be positive", "INVALID_TRANSACTION_AMOUNT");
            }

            // Validation: Customer username should be in a valid email format
            if (!ValidationUtil.isValidEmail(transactionDTO.getCustomerUserName())) {
                logger.error("Invalid customer email format: {}", transactionDTO.getCustomerUserName());
                throw new CustomException("Invalid customer email format", "INVALID_CUSTOMER_EMAIL_FORMAT");
            }

            // Validation: Check if the currency is valid
            if (!currencyConversionUtil.isCurrencyValid(transactionDTO.getCurrency())) {
                logger.error("Invalid currency: {}", transactionDTO.getCurrency());
                throw new CustomException("Invalid currency provided", "INVALID_CURRENCY");
            }

            // Map DTO to Transaction entity
            Transaction transaction = new Transaction();
            transaction.setCustomerUserName(transactionDTO.getCustomerUserName());
            transaction.setUserName(email);
            transaction.setDescription(transactionDTO.getDescription());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setCurrency(transactionDTO.getCurrency());
            transaction.setType(transactionDTO.getType());
            transaction.setTransactionDate(LocalDateTime.now());

            // Record the transaction
            Transaction savedTransaction = transactionService.recordTransaction(transaction);
            logger.info("Transaction successfully recorded for customer: {}", transactionDTO.getCustomerUserName());

            return ResponseEntity.ok(savedTransaction);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid transaction data provided for customer: {}", transactionDTO.getCustomerUserName(), e);
            throw new CustomException("Invalid transaction data", "INVALID_TRANSACTION_DATA");
        } catch (Exception e) {
            logger.error("Error occurred while recording transaction for customer: {}", transactionDTO.getCustomerUserName(), e);
            throw new CustomException("Transaction recording failed", "TRANSACTION_RECORD_FAILED");
        }
    }
}
