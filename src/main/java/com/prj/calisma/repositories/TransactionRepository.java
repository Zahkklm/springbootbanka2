package com.prj.calisma.repositories;

import com.prj.calisma.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountIdOrderByInitiationDate(long id);  
    
}
