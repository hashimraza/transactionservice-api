/**
 * 
 */
package com.number26.transactionservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.number26.transactionservice.business.TransactionServiceBusiness;
import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

/**
 * A RESTful controller that stores some transactions and returns information about those transactions. The transactions
 * to be stored have a type and an amount. The service supports returning all transactions of a type. Also, transactions
 * can be linked to each other (using a "parent_id"). It also has a mapping that returns the total amount involved for
 * all transactions linked to a particular transaction.
 * 
 * @author HashimR
 *
 */
@Controller
@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/transactionservice")
public class TransactionServiceController {

    private TransactionServiceBusiness business;

    /**
     * Method that accepts a PUT request and saves transaction
     * 
     * @param transactionId
     * @param transaction
     * @return
     */
    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Status>
        saveTransaction(@PathVariable long transactionId, @RequestBody Transaction transaction) {
        HttpStatus httpStatus = HttpStatus.OK;
        Status status = business.save(transactionId, transaction);
        if (status.getStatus().equals(Status.ERROR)) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<Status>(status, httpStatus);
    }

    /**
     * Method that returns a Transaction by id
     * 
     * @param transactionId
     * @return
     */
    @RequestMapping(path = "/transaction/{transactionId}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> getTransactionById(@PathVariable long transactionId) {
        HttpStatus httpStatus = HttpStatus.OK;
        Transaction transaction = business.getById(transactionId);
        if (transaction == null) {
            httpStatus = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<Transaction>(transaction, httpStatus);
    }

    /**
     * Method that returns all Transaction ids by type
     * 
     * @param type
     * @return
     */
    @RequestMapping(path = "/types/{type}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Long>> getTransactionIdsByTypes(@PathVariable String type) {
        HttpStatus httpStatus = HttpStatus.OK;
        List<Long> transactionIds = business.getIdsByType(type);
        if (CollectionUtils.isEmpty(transactionIds)) {
            httpStatus = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<List<Long>>(transactionIds, httpStatus);
    }

    /**
     * Method that returns sum of all children of a Transaction id
     * 
     * @param transactionId
     * @return
     */
    @RequestMapping(path = "/sum/{transactionId}", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Sum> getSumOfAmountOfAllChildTransactions(@PathVariable long transactionId) {
        HttpStatus httpStatus = HttpStatus.OK;
        Sum sum = business.getSumOfAmountOfAllChilds(transactionId);
        return new ResponseEntity<Sum>(sum, httpStatus);
    }

    /**
     * @param business
     *            the business to set
     */
    @Autowired
    public void setBusiness(TransactionServiceBusiness business) {
        this.business = business;
    }

}
