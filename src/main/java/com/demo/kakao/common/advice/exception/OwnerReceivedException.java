package com.demo.kakao.common.advice.exception;

public class OwnerReceivedException extends RuntimeException {

    public OwnerReceivedException(String msg, Throwable t) { super(msg, t); }

    public OwnerReceivedException(String msg) { super(msg); }

    public OwnerReceivedException() { super(); }

}
