package com.example.camundademo.util;

import java.util.HashMap;
import java.util.Map;

public class BaseContextImpl implements BaseContext{
    private Map<Object, Object> businessProperties = new HashMap<>();

    public  BaseContextImpl(){}

    @Override
    public void addBusinessProperty(Object businessKey, Object businessValue) {
        this.businessProperties.put(businessKey, businessValue);
    }

    @Override
    public Object getBusinessProperty(Object businessKey) {
        return this.businessProperties.get(businessKey);
    }

    @Override
    public Map<Object, Object> getBusinessProperties() {
        return this.businessProperties;
    }

    @Override
    public void setBusinessProperties(Map<Object, Object> map) {
        this.businessProperties = map;
    }
}
