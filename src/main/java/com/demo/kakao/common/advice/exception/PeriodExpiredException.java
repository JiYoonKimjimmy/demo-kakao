package com.demo.kakao.common.advice.exception;

public class PeriodExpiredException extends RuntimeException {

    public PeriodExpiredException(String msg, Throwable t) { super(msg, t); }

    public PeriodExpiredException(String msg) { super(msg); }

    public PeriodExpiredException() { super(); }

}
