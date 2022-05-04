package ru.free.project.exceptions;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class BusinessException extends CommonException {
    private final String title;

    public BusinessException(String title, String body) {
        super(body);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
