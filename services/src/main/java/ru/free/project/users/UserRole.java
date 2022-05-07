package ru.free.project.users;

/**
 * Данные о роли пользователя
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface UserRole {

    /**
     * @return идентификатор
     */
    Long getId();

    /**
     * @return наименование
     */
    String getName();
}
