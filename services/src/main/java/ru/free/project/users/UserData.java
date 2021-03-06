package ru.free.project.users;

import lombok.NonNull;

import java.util.List;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface UserData {

    /**
     * @return авторизирован ли пользователь
     */
    boolean isAnonymous();

    /**
     * @return идентификатор
     */
    Long getId();

    /**
     * @return email
     */
    String getEmail();

    /**
     * @return nickname
     */
    @NonNull
    String getNickname();

    /**
     * @return пароль
     */
    String getPassword();

    /**
     * @return роли пользователя
     */
    List<UserRole> getRoles();
}
