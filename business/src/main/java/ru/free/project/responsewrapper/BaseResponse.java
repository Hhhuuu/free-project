package ru.free.project.responsewrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Обертка над ответом
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class BaseResponse<T> {
    @JsonProperty(value = "status", required = true)
    private boolean status;
    @JsonProperty(value = "body")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T body;
    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Message message;

    public static <T> BaseResponse<T> goodResponse(T body) {
        final BaseResponse<T> response = new BaseResponse<>();
        response.status = true;
        response.body = body;
        return response;
    }

    public static <T> BaseResponse<T> goodResponse(T body, Message message) {
        final BaseResponse<T> response = new BaseResponse<>();
        response.status = true;
        response.body = body;
        response.message = message;
        return response;
    }

    public static BaseResponse<Object> messageResponse(Message message) {
        final BaseResponse<Object> response = new BaseResponse<>();
        response.status = false;
        response.body = null;
        response.message = message;
        return response;
    }

    public static BaseResponse<Object> failResponse(Exception ex) {
        final BaseResponse<Object> response = new BaseResponse<>();
        response.status = false;
        response.error = ex.getMessage();
        return response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
