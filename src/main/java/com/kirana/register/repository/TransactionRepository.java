package com.kirana.register.repository;

import com.kirana.register.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    @Query("{ 'userName': ?0, 'transactionDate': { $gte: ?1, $lte: ?2 }, 'customerUserName': { $regex: ?3, $options: 'i' } }")
    List<Transaction> findTransactionsWithOptionalCustomer(String userName, LocalDateTime startDate, LocalDateTime endDate, String customerUserName);

}
