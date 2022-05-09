package ru.free.project;

import ru.free.project.users.UserData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Аутентификация после регистрации
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface AuthenticationAfterRegistration {

    /**
     * Аутентификация клиента после регистрации
     *
     * @param request  запрос
     * @param response ответ
     * @param userData данные пользователя
     * @param password пароль
     */
    void onAuthentication(HttpServletRequest request, HttpServletResponse response, UserData userData, String password);
}
