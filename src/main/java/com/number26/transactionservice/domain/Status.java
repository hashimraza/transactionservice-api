/**
 * 
 */
package com.number26.transactionservice.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author HashimR
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class Status {

    public static final String OK = "ok";
    public static final String ERROR = "error";
    public static final String AMOUNT_EMPTY = "Amount empty.";
    public static final String TYPE_EMPTY = "Type empty.";
    public static final String PARENT_ID_NOT_EXISTS = "Parent transaction id does not exists.";
    public static final String PARENT_ID_SAME = "Parent transaction id same as transaction id.";

    private String status;
    private String reason;

    /**
     * @param string
     */
    public Status(String status) {
        this.status = status;
    }

    /**
     * @param reason
     * @param status
     * 
     */
    public Status(String status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    /**
     * 
     */
    public Status() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason
     *            the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

}
