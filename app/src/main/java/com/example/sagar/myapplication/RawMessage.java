package com.example.sagar.myapplication;

import java.util.HashMap;
import java.util.Map;

public class RawMessage {
    private Map<String, String> rawValues;

    public RawMessage() {
        this.rawValues = new HashMap<>();
    }

    public void addMessageValue(String key, String value) {
        rawValues.put(key, value);
    }
}
