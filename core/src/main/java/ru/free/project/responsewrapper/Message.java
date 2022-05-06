package ru.free.project.responsewrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Сообщение пользователю
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class Message {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String body;

    public Message() {
    }

    public Message(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
