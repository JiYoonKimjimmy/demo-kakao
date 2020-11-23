package com.demo.kakao.common.advice.exception;

public class TimeExpiredException extends RuntimeException {

    public TimeExpiredException(String msg, Throwable t) { super(msg, t); }

    public TimeExpiredException(String msg) { super(msg); }

    public TimeExpiredException() { super(); }

}
