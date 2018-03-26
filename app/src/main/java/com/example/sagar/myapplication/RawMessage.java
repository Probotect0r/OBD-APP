package com.example.sagar.myapplication;

import java.util.HashMap;
import java.util.Map;

public class RawMessage {
    private Map<String, String> rawMessages;

    public RawMessage() {
        this.rawMessages = new HashMap<>();
    }

    public void addMessageValue(String key, String value) {
        rawMessages.put(key, value);
    }
}
