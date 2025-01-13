package com.realnet.Billing.Dto;

import lombok.Data;

@Data
public class ServiceOrderDto {
    private Long id;
    private String orderType;
    private String orderNo;
    private String orderGenerationDate;
    private String serviceRequestBy;
    private String serviceRenderedFrom;
    private String serviceRenderedTo;
    private String contactNumber;
    private String poNumber;
    private String status;
    private String remarks;
    private String deliveryTerms;
    // Other attributes as needed

    // Getters and setters
}
