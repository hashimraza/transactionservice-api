package com.number26.transactionservice.business;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.number26.transactionservice.data.TransactionServiceData;
import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

/**
 * 
 * @author Hashim
 *
 */
@Service
public class TransactionServiceBusinessImpl implements TransactionServiceBusiness {

    private TransactionServiceData inMemoryTransactions;

    /**
     * @param transactionId
     * @param transaction
     * @return
     */
    public Status save(long transactionId, Transaction transaction) {
        Status status = new Status();
        if (transactionValid(transactionId, transaction, status)) {
            transaction.setId(transactionId);
            inMemoryTransactions.save(transaction.getId(), transaction);
            status.setStatus(Status.OK);
        } else {
            status.setStatus(Status.ERROR);
        }
        return status;
    }

    /**
     * @param transactionId
     * @param transaction
     * @param status
     * @return
     */
    private boolean transactionValid(long transactionId, Transaction transaction, Status status) {
        String reason = "";
        boolean valid = true;
        if (transaction.getAmount() == null) {
            reason = Status.AMOUNT_EMPTY;
            valid = false;
        } else if (StringUtils.isEmpty(transaction.getType())) {
            reason = Status.TYPE_EMPTY;
            valid = false;
        } else if (transaction.getParentId() != null) {
            if (transactionId == transaction.getParentId()) {
                reason = Status.PARENT_ID_SAME;
                valid = false;
            } else if (!inMemoryTransactions.contains(transaction.getParentId())) {
                reason = Status.PARENT_ID_NOT_EXISTS;
                valid = false;
            }
        }
        status.setReason(reason);
        return valid;
    }

    /**
     * @param transactionId
     * @return
     */
    public Transaction getById(long transactionId) {
        return inMemoryTransactions.getTransaction(transactionId);
    }

    /**
     * @param type
     * @return
     */
    public List<Long> getIdsByType(String type) {
        return inMemoryTransactions.getIdsByType(type);
    }

    /**
     * @param transactionId
     * @return
     */
    public Sum getSumOfAmountOfAllChilds(long transactionId) {
        return new Sum(inMemoryTransactions.getSumOfAmountOfAllChildren(transactionId));
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
