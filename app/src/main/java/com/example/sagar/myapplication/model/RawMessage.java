package com.example.sagar.myapplication.model;

import java.util.HashMap;
import java.util.Map;

public class RawMessage {
    private String driveId;
    private Map<String, String> rawMessages;

    public RawMessage() {
        this.rawMessages = new HashMap<>();
    }

    public void addMessageValue(String key, String value) {
        rawMessages.put(key, value);
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }
}
