package com.realnet.template.controller;

import java.util.List;
import java.util.Map;

public class ImportDataRequest {
    private List<Map<String, Object>> Site;
    private List<Map<String, Object>> Customer;

    public List<Map<String, Object>> getSite() {
        return Site;
    }

    public void setSite(List<Map<String, Object>> site) {
        Site = site;
    }

    public List<Map<String, Object>> getCustomer() {
        return Customer;
    }

    public void setCustomer(List<Map<String, Object>> customer) {
        Customer = customer;
    }
}
