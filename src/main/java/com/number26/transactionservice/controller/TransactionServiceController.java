/**
 * 
 */
package com.number26.transactionservice.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.number26.transactionservice.data.TransactionData;
import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

/**
 * @author HashimR
 *
 */
@Controller
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private static final TransactionData inMemoryTransactions = new TransactionData();

    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Status saveTransaction(@PathVariable long transactionId, @RequestBody Transaction transaction) {
        Status status = new Status();
        if (transaction.getAmount() == null || StringUtils.isEmpty(transaction.getType())) {
            status.setStatus(Status.ERROR);
        } else {
            transaction.setId(transactionId);
            inMemoryTransactions.put(transactionId, transaction);
            status.setStatus(Status.OK);
        }
        return status;
    }

    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Transaction getTransactionById(@PathVariable long transactionId) {
        return inMemoryTransactions.get(transactionId);
    }

    @RequestMapping(path = "/types/{type}", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getTransactionIdsByTypes(@PathVariable String type) {
        return inMemoryTransactions.getTransactionIdsByType(type);
    }

    @RequestMapping(path = "/sum/{transactionId}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Sum getSumOfAmountOfAllChildTransactions(@PathVariable long transactionId) {
        return new Sum(inMemoryTransactions.getSumOfAmountOfAllChildTransactions(transactionId));
    }

}
