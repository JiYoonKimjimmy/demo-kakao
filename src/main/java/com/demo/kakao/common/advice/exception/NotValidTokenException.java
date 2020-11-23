package com.demo.kakao.common.advice.exception;

public class NotValidTokenException extends RuntimeException {

    public NotValidTokenException(String msg, Throwable t) { super(msg, t); }

    public NotValidTokenException(String msg) { super(msg); }

    public NotValidTokenException() { super(); }

}
