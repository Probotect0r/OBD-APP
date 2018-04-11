package com.example.sagar.myapplication;

/**
 * Created by achal on 2018-04-02.
 */

public class ErrorCard {

    private String errorNum;
    private String errorDesc;

    public ErrorCard (String num, String desc) {
        errorNum = num;
        errorDesc = desc;
    }


    public String getErrorNum() {
        return errorNum;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}
