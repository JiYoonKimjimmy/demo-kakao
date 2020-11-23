package com.demo.kakao.common.advice;

import com.demo.kakao.common.advice.exception.*;
import com.demo.kakao.common.response.CommonResult;
import com.demo.kakao.common.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    private final MessageSource messageSource;

    // 시스템 Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }
    // Inconsistent Exception
    @ExceptionHandler(InconsistentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult inconsistentException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("inconsistent.code")), getMessage("inconsistent.msg"));
    }
    // Owner Received Exception
    @ExceptionHandler(OwnerReceivedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult ownerReceivedException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("ownerReceived.code")), getMessage("ownerReceived.msg"));
    }
    // Not Exist In Room Exception
    @ExceptionHandler(NotExistInRoomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult notExistInRoom(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notExistInRoom.code")), getMessage("notExistInRoom.msg"));
    }
    // Duplication Received Exception
    @ExceptionHandler(DuplicateReceivedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult duplicateReceivedException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("duplicateReceived.code")), getMessage("duplicateReceived.msg"));
    }
    // Time Expired Exception
    @ExceptionHandler(TimeExpiredException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult timeExpiredException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("timeExpired.code")), getMessage("timeExpired.msg"));
    }
    // Not Valid User Exception
    @ExceptionHandler(NotValidUserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult notValidUserException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notValidUser.code")), getMessage("notValidUser.msg"));
    }
    // Not Valid Token Exception
    @ExceptionHandler(NotValidTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult notValidTokenException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("notValidToken.code")), getMessage("notValidToken.msg"));
    }
    // Period Expired Exception
    @ExceptionHandler(PeriodExpiredException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult periodExpiredException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("periodExpired.code")), getMessage("periodExpired.msg"));
    }

    // code 정보에 해당하는 메시지를 조회
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code 정보 및 추가 parameter 로 현재 지역에 맞는 메시지를 조회
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
