package ru.free.project.exceptions;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class CommonException extends Exception {
    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }
}
