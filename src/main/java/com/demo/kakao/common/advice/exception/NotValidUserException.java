package com.demo.kakao.common.advice.exception;

public class NotValidUserException extends RuntimeException {

    public NotValidUserException(String msg, Throwable t) { super(msg, t); }

    public NotValidUserException(String msg) { super(msg); }

    public NotValidUserException() { super(); }

}
