/**
 * 
 */
package com.number26.transactionservice.data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.number26.transactionservice.domain.Transaction;

/**
 * @author HashimR
 *
 */
public class TransactionData extends ConcurrentHashMap<Long, Transaction> {

    private static final long serialVersionUID = 1L;

    public List<Long> getTransactionIdsByType(String type) {
        return this.entrySet().stream().filter(t -> t.getValue().getType().equals(type)).map(t -> t.getValue().getId())
            .collect(Collectors.toList());
    }

    /**
     * @param transactionId
     * @return
     */
    public Double getSumOfAmountOfAllChildTransactions(long transactionId) {
        return this.entrySet().stream()
            .filter(t -> t.getValue().getParentId() == null ? false : t.getValue().getParentId() == transactionId)
            .mapToDouble(t -> t.getValue().getAmount()).sum();
    }
}
