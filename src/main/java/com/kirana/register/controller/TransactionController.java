package com.kirana.register.controller;

import com.kirana.register.dto.TransactionDTO;
import com.kirana.register.model.Report;
import com.kirana.register.model.Transaction;
import com.kirana.register.service.TransactionService;
import com.kirana.register.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ReportService reportService;

    // Record a transaction
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> recordTransaction(@RequestBody TransactionDTO transactionDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) auth.getPrincipal()).getUsername();

        Transaction transaction = new Transaction();
        transaction.setCustomerUserName(transactionDTO.getCustomerUserName());
        transaction.setUserName(email);
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setCurrency(transactionDTO.getCurrency());
        transaction.setType(transactionDTO.getType());
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction savedTransaction = transactionService.recordTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    // Generate financial report
//    @GetMapping("/reports")
//    public ResponseEntity<Report> generateReport(
//            @RequestParam("startDate") String startDate,
//            @RequestParam("endDate") String endDate,
//            @RequestParam("currency") String currency) {
//        LocalDateTime start = LocalDateTime.parse(startDate);
//        LocalDateTime end = LocalDateTime.parse(endDate);
//        Report report = reportService.generateReport(customerUserName, start, end, currency);
//        return ResponseEntity.ok(report);
//    }
}
