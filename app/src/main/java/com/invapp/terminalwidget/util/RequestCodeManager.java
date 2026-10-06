package com.invapp.terminalwidget.util;

public class RequestCodeManager {

    private static int nextRequestCode = 1;

    public static synchronized int getRequestCode() {
        return nextRequestCode++;
    }

}
