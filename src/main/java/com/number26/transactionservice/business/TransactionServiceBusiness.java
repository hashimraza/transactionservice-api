package com.number26.transactionservice.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.number26.transactionservice.data.TransactionServiceData;
import com.number26.transactionservice.domain.Transaction;

@Service
public class TransactionServiceBusiness {

    private TransactionServiceData inMemoryTransactions;

    /**
     * @param transaction
     */
    public void save(Transaction transaction) {
        inMemoryTransactions.put(transaction.getId(), transaction);
    }

    /**
     * @param transactionId
     * @return
     */
    public Transaction getById(long transactionId) {
        return inMemoryTransactions.get(transactionId);
    }

    /**
     * @param type
     * @return
     */
    public List<Long> getIdsByType(String type) {
        return inMemoryTransactions.getTransactionIdsByType(type);
    }

    /**
     * @param transactionId
     * @return
     */
    public Double getSumOfAmountOfAllChilds(long transactionId) {
        return inMemoryTransactions.getSumOfAmountOfAllChildTransactions(transactionId);
    }

    /**
     * @param inMemoryTransactions
     *            the inMemoryTransactions to set
     */
    @Autowired
    public void setInMemoryTransactions(TransactionServiceData inMemoryTransactions) {
        this.inMemoryTransactions = inMemoryTransactions;
    }

}
