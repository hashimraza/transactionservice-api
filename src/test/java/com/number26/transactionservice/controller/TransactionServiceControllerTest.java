package com.number26.transactionservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.number26.transactionservice.business.TransactionServiceBusiness;
import com.number26.transactionservice.domain.Status;
import com.number26.transactionservice.domain.Sum;
import com.number26.transactionservice.domain.Transaction;

public class TransactionServiceControllerTest {

    private final String mediaTypeApplicationJson = MediaType.APPLICATION_JSON_VALUE;

    private MockMvc mockMvc;

    @InjectMocks
    private TransactionServiceController transactionServiceController;

    @Mock
    private TransactionServiceBusiness business;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionServiceController).build();
    }

    @Test
    public void testSaveTransationPositiveWithoutParentId() throws Exception {
        Mockito.when(business.save(Matchers.anyLong(), Matchers.any())).thenReturn(getStatus("ok"));

        String requestBody = "{\"amount\":1000, \"type\":\"cars\"}";

        mockMvc
            .perform(
                put("/transactionservice/transaction/1").contentType(mediaTypeApplicationJson).content(requestBody))
            .andExpect(status().isOk()).andExpect(content().contentType(mediaTypeApplicationJson))
            .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    public void testSaveTransationWithSameParentIdAsTransactionId() throws Exception {
        Mockito.when(business.save(Matchers.anyLong(), Matchers.any())).thenReturn(
            getStatus("error", Status.PARENT_ID_SAME));

        String requestBody = "{\"amount\":1000, \"type\":\"cars\", \"parent_id\":1}";

        mockMvc
            .perform(
                put("/transactionservice/transaction/1").contentType(mediaTypeApplicationJson).content(requestBody))
            .andExpect(status().isBadRequest()).andExpect(content().contentType(mediaTypeApplicationJson))
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.reason").value(Status.PARENT_ID_SAME));
    }

    @Test
    public void testSaveTransationWithoutAmount() throws Exception {
        Mockito.when(business.save(Matchers.anyLong(), Matchers.any())).thenReturn(
            getStatus("error", Status.AMOUNT_EMPTY));

        String requestBody = "{\"type\":\"cars\"}";

        mockMvc
            .perform(
                put("/transactionservice/transaction/4").contentType(mediaTypeApplicationJson).content(requestBody))
            .andExpect(status().isBadRequest()).andExpect(content().contentType(mediaTypeApplicationJson))
            .andExpect(jsonPath("$.status").value("error")).andExpect(jsonPath("$.reason").value(Status.AMOUNT_EMPTY));
    }

    @Test
    public void testSaveTransationWithoutType() throws Exception {
        Mockito.when(business.save(Matchers.anyLong(), Matchers.any())).thenReturn(
            getStatus("error", Status.TYPE_EMPTY));

        String requestBody = "{\"amount\":1000}";

        mockMvc
            .perform(
                put("/transactionservice/transaction/5").contentType(mediaTypeApplicationJson).content(requestBody))
            .andExpect(status().isBadRequest()).andExpect(content().contentType(mediaTypeApplicationJson))
            .andExpect(jsonPath("$.status").value("error")).andExpect(jsonPath("$.reason").value(Status.TYPE_EMPTY));
    }

    @Test
    public void testGetTransationById() throws Exception {
        Mockito.when(business.getById(Matchers.anyLong())).thenReturn(getTransaction(1, 1000.0, "cars", null));

        mockMvc.perform(get("/transactionservice/transaction/1")).andExpect(status().isOk())
            .andExpect(content().contentType(mediaTypeApplicationJson)).andExpect(jsonPath("$.amount").value(1000.0))
            .andExpect(jsonPath("$.type").value("cars"));
    }

    @Test
    public void testGetTransationByIdAndTransactionNotExists() throws Exception {
        Mockito.when(business.getById(Matchers.anyLong())).thenReturn(null);

        mockMvc.perform(get("/transactionservice/transaction/1")).andExpect(status().isNoContent());
    }

    @Test
    public void testGetTransationByIdWithParentId() throws Exception {
        Mockito.when(business.getById(Matchers.anyLong())).thenReturn(getTransaction(1, 1000.0, "cars", 1l));

        mockMvc.perform(get("/transactionservice/transaction/1")).andExpect(status().isOk())
            .andExpect(content().contentType(mediaTypeApplicationJson)).andExpect(jsonPath("$.amount").value(1000.0))
            .andExpect(jsonPath("$.type").value("cars")).andExpect(jsonPath("$.parent_id").value(1));
    }

    @Test
    public void testGetTransationIdsByType() throws Exception {
        Mockito.when(business.getIdsByType(Matchers.anyString())).thenReturn(Arrays.asList(1l, 2l));

        mockMvc.perform(get("/transactionservice/types/cars")).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(jsonPath("$[0]").value(1))
            .andExpect(jsonPath("$[1]").value(2));
    }

    @Test
    public void testGetTransationIdsByTypeButNoTypesExists() throws Exception {
        Mockito.when(business.getIdsByType(Matchers.anyString())).thenReturn(null);

        mockMvc.perform(get("/transactionservice/types/cars")).andExpect(status().isNoContent());
    }

    @Test
    public void testGetSumOfAmountOfAllChildTransactions() throws Exception {
        Mockito.when(business.getSumOfAmountOfAllChilds(Matchers.anyLong())).thenReturn(new Sum(1000.0));

        mockMvc.perform(get("/transactionservice/sum/1")).andExpect(status().isOk())
            .andExpect(content().contentType(mediaTypeApplicationJson)).andExpect(jsonPath("$.sum").value(1000.0));
    }

    private Status getStatus(String status) {
        return new Status(status);
    }

    private Status getStatus(String status, String reason) {
        return new Status(status, reason);
    }

    /**
     * @param amount
     * @param type
     * @param parentId
     * @param i
     * @return
     */
    private Transaction getTransaction(long id, Double amount, String type, Long parentId) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setParentId(parentId);
        return transaction;
    }
}
