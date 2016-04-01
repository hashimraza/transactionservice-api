/**
 * 
 */
package com.number26.transactionservice.domain;

/**
 * @author HashimR
 *
 */
public class Status {
    
    public static final String OK = "ok";
    public static final String ERROR = "error";

    private String status;

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

}
