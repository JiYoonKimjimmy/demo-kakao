package com.demo.kakao.common.advice.exception;

public class NotExistInRoomException extends RuntimeException {

    public NotExistInRoomException(String msg, Throwable t) { super(msg, t); }

    public NotExistInRoomException(String msg) { super(msg); }

    public NotExistInRoomException() { super(); }

}
