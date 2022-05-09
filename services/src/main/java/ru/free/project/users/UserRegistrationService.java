package ru.free.project.users;

import ru.free.project.exceptions.CommonException;

/**
 * Сервис для регистрации пользователей
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface UserRegistrationService {
    /**
     * Регистрация нового пользователя
     *
     * @param registrationData данные для регистрации
     * @return созданные пользователь
     * @throws CommonException если не удалось создать пользователя
     */
    UserData register(UserRegistrationData registrationData) throws CommonException;
}