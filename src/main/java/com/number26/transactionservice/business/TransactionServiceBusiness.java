package com.number26.transactionservice.business;

import java.util.List;

import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

/**
 * 
 * @author Hashim
 *
 */
public interface TransactionServiceBusiness {

    Status save(long transactionId, Transaction transaction);

    Transaction getById(long transactionId);

    List<Long> getIdsByType(String type);

    Sum getSumOfAmountOfAllChilds(long transactionId);

}
