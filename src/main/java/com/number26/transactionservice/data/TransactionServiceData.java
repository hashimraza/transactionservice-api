/**
 * 
 */
package com.number26.transactionservice.data;

import java.util.List;

import com.number26.transactionservice.domain.Transaction;

/**
 * Data layer where temporarily storing transactions in a ConcurrentHashMap
 * 
 * @author HashimR
 *
 */
public interface TransactionServiceData {

    Transaction save(long transactionId, Transaction transaction);

    Transaction getTransaction(long transactionId);

    boolean contains(long transactionId);

    List<Long> getIdsByType(String type);

    Double getSumOfAmountOfAllChildren(long transactionId);

    void clearData();
}
