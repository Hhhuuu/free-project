package ru.free.project;

import org.springframework.security.core.Authentication;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface SecurityContext {
    /**
     * @return информация об аутентификации
     */
    Authentication getAuthentication();
}
