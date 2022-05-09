package ru.free.project.users;

import ru.free.project.exceptions.CommonException;

import java.util.Optional;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface UserManagerService {

    /**
     * Получение пользователя по id
     *
     * @param id идентификатор
     * @return пользователь
     * @throws CommonException если не задан id
     */
    Optional<UserData> getUserById(Long id) throws CommonException;

    /**
     * Получение пользователя по nickname
     *
     * @param nickname никнейм
     * @return пользователь
     * @throws CommonException если не задан никнейм
     */
    Optional<UserData> getUserByNickname(String nickname) throws CommonException;

    /**
     * Получение пользователя по email
     *
     * @param email email
     * @return пользователь
     * @throws CommonException если не задан email
     */
    Optional<UserData> getUserByEmail(String email) throws CommonException;

    /**
     * Поиск пользователя по email или nickname
     *
     * @param username имя из авторизации
     * @return пользователь
     * @throws CommonException если некорректно указан username
     */
    Optional<UserData> findUserByUsername(String username) throws CommonException;
}
