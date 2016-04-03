/**
 * 
 */
package com.number26.transactionservice.data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.number26.transactionservice.domain.Transaction;

/**
 * Data layer where temporarily storing transactions in a ConcurrentHashMap
 * 
 * @author HashimR
 *
 */
@Repository
public class TransactionServiceDataImpl extends ConcurrentHashMap<Long, Transaction> implements TransactionServiceData {

    private static final long serialVersionUID = 1L;

    public Transaction save(long transactionId, Transaction transaction) {
        return this.put(transactionId, transaction);
    }

    public Transaction getTransaction(long transactionId) {
        return this.get(transactionId);
    }

    public boolean contains(long transactionId) {
        return this.containsKey(transactionId);
    }

    public void clearData() {
        this.clear();
    }

    public List<Long> getIdsByType(String type) {
        return this.entrySet().stream().filter(t -> t.getValue().getType().equals(type)).map(t -> t.getValue().getId())
            .collect(Collectors.toList());
    }

    /**
     * @param transactionId
     * @return
     */
    public Double getSumOfAmountOfAllChildren(long transactionId) {
        return this.entrySet().stream()
            .filter(t -> t.getValue().getParentId() == null ? false : t.getValue().getParentId() == transactionId)
            .mapToDouble(t -> t.getValue().getAmount()).sum();
    }
}
