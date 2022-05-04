package ru.free.project.exceptions;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class CommonRuntimeException extends RuntimeException {
    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
