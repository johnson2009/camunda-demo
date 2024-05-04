package com.example.camundademo.util;

import java.io.Serializable;
import java.util.Map;

public interface BaseContext extends Serializable {
    void addBusinessProperty(Object businessKey, Object businessValue);

    Object getBusinessProperty(Object businessKey);

    Map<Object, Object> getBusinessProperties();

    void setBusinessProperties(Map<Object, Object> map);
}
