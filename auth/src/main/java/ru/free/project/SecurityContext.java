package ru.free.project;

import ru.free.project.exceptions.CommonException;
import ru.free.project.users.UserData;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface SecurityContext {
    /**
     * @return информация об аутентификации
     */
    UserData getUser() throws CommonException;

    /**
     * @param userData данные о пользователе
     */
    void putIfNotPresent(UserData userData);
}
