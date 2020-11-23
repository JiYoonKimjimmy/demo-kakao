package com.demo.kakao.common.advice.exception;

public class DuplicateReceivedException extends RuntimeException {

    public DuplicateReceivedException(String msg, Throwable t) { super(msg, t); }

    public DuplicateReceivedException(String msg) { super(msg); }

    public DuplicateReceivedException() { super(); }

}
