package com.number26.transactionservice.business;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.number26.transactionservice.configuration.ApplicationConfigurationTest;
import com.number26.transactionservice.data.TransactionServiceData;
import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

@ContextConfiguration(classes = ApplicationConfigurationTest.class)
public class TransactionServiceBusinessTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private TransactionServiceBusiness business;

    @Autowired
    private TransactionServiceData inMemoryTransactions;

    @AfterMethod
    private void emptyInMemoryTransactions() {
        inMemoryTransactions.clearData();
    }

    @Test
    public void testSaveTransaction() {
        Status status = business.save(1, getTransaction(100d, "cars", null));

        Assert.assertEquals(status.getStatus(), Status.OK);
        Assert.assertEquals(inMemoryTransactions.getTransaction(1l).getAmount(), 100d);
        Assert.assertEquals(inMemoryTransactions.getTransaction(1l).getType(), "cars");
    }

    @Test
    public void testSaveTransactionWithoutAmount() {
        Status status = business.save(1, getTransaction(null, "cars", null));

        Assert.assertEquals(status.getStatus(), Status.ERROR);
        Assert.assertEquals(status.getReason(), Status.AMOUNT_EMPTY);
    }

    @Test
    public void testSaveTransactionWithoutType() {
        Status status = business.save(1, getTransaction(100d, null, null));

        Assert.assertEquals(status.getStatus(), Status.ERROR);
        Assert.assertEquals(status.getReason(), Status.TYPE_EMPTY);
    }

    @Test
    public void testSaveTransactionWithParentIdSameAsTransactionId() {
        Status status = business.save(1, getTransaction(100d, "cars", 1l));

        Assert.assertEquals(status.getStatus(), Status.ERROR);
        Assert.assertEquals(status.getReason(), Status.PARENT_ID_SAME);
    }

    @Test
    public void testSaveTransactionWithParentIdNotExistent() {
        Status status = business.save(1, getTransaction(100d, "cars", 2l));

        Assert.assertEquals(status.getStatus(), Status.ERROR);
        Assert.assertEquals(status.getReason(), Status.PARENT_ID_NOT_EXISTS);
    }

    @Test
    public void testGetById() {
        Status status = business.save(1, getTransaction(100d, "cars", null));
        Transaction transaction = business.getById(1l);

        Assert.assertEquals(status.getStatus(), Status.OK);
        Assert.assertEquals(transaction.getAmount(), 100d);
        Assert.assertEquals(transaction.getType(), "cars");
    }

    @Test
    public void testGetByIdNotExists() {
        Transaction transaction = business.getById(1l);
        Assert.assertNull(transaction);
    }

    @Test
    public void testGetIdsByType() {
        business.save(1, getTransaction(100d, "cars", null));
        business.save(2, getTransaction(100d, "cars", null));
        business.save(3, getTransaction(100d, "shopping", null));

        List<Long> cars = business.getIdsByType("cars");
        List<Long> shopping = business.getIdsByType("shopping");
        List<Long> market = business.getIdsByType("market");

        Assert.assertEquals(cars.size(), 2);
        Assert.assertEquals(cars, Arrays.asList(1l, 2l));
        Assert.assertEquals(shopping.size(), 1);
        Assert.assertEquals(shopping, Arrays.asList(3l));
        Assert.assertEquals(market.size(), 0);
    }

    @Test
    public void testGetSumOfAmountOfAllChilds() {
        business.save(1, getTransaction(100d, "cars", null));
        business.save(2, getTransaction(200d, "cars", 1l));
        business.save(3, getTransaction(300d, "shopping", 1l));
        business.save(4, getTransaction(400d, "shopping", 2l));

        Sum sumOfOne = business.getSumOfAmountOfAllChilds(1l);
        Sum sumOfTwo = business.getSumOfAmountOfAllChilds(2l);
        Sum sumOfThree = business.getSumOfAmountOfAllChilds(3l);

        Assert.assertEquals(sumOfOne.getSum(), 500d);
        Assert.assertEquals(sumOfTwo.getSum(), 400d);
        Assert.assertEquals(sumOfThree.getSum(), 0d);
    }

    /**
     * @param d
     * @param string
     * @param object
     * @return
     */
    private Transaction getTransaction(Double amount, String type, Long parentId) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setParentId(parentId);
        return transaction;
    }

}
