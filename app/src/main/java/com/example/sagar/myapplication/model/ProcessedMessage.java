package com.example.sagar.myapplication.model;

import java.util.Map;

public class ProcessedMessage {
    private Map<String, Integer> values;

    public ProcessedMessage() {}

    public Map<String, Integer> getValues() {
        return values;
    }

    public void setValues(Map<String, Integer> values) {
        this.values = values;
    }
}
