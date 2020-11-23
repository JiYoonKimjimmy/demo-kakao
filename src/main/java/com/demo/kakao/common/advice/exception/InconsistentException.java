package com.demo.kakao.common.advice.exception;

public class InconsistentException extends RuntimeException {

    public InconsistentException(String msg, Throwable t) { super(msg, t); }

    public InconsistentException(String msg) { super(msg); }

    public InconsistentException() { super(); }

}
