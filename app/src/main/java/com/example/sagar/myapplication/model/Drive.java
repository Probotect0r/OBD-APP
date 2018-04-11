package com.example.sagar.myapplication.model;

import java.util.Date;

/**
 * Created by sagar on 3/26/18.
 */

public class Drive {
    private String id;
    private Date start;
    private Date end;

    public Drive() {
        this.start = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}
