package com.hbq.biddingsystem.exception;

public class OperationNotAllowedException extends Exception {
    public OperationNotAllowedException(String s) {
        super(s);
    }

    public OperationNotAllowedException() {
        super();
    }

    public OperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotAllowedException(Throwable cause) {
        super(cause);
    }

    protected OperationNotAllowedException(String message, Throwable cause, boolean enableSuppression,
                                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
