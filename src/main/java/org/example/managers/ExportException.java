package org.example.managers;

public class ExportException extends Exception {
    public ExportException(String msg, Exception ex) {
        super(msg,ex);
    }
}
