package ru.free.project.exceptionhandlres;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.free.project.responsewrapper.BaseResponse;
import ru.free.project.responsewrapper.Message;
import ru.free.project.exceptions.*;

/**
 * Обработка всех исключений
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.messageResponse(new Message(ex.getTitle(), ex.getMessage())), new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request) {
        if (ex instanceof CommonException || ex instanceof CommonRuntimeException) {
            return handleExceptionInternal(ex, BaseResponse.failResponse(ex), new HttpHeaders(), HttpStatus.OK, request);
        }
        log.error("Внезапная ошибка: ", ex);
        return handleExceptionInternal(ex, BaseResponse.failResponse(new Exception("Что-то пошло не так!", ex)), new HttpHeaders(), HttpStatus.OK, request);
    }
}
