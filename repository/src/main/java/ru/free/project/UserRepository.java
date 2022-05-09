package ru.free.project;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Поиск пользователя по email
     *
     * @param email email
     * @return пользователь
     */
    Optional<User> getUserByEmail(@Param("email") String email);

    /**
     * Поиск пользователя по nickname
     *
     * @param nickname nickname
     * @return пользователь
     */
    Optional<User> getUserByNickname(@Param("nickname") String nickname);

    /**
     * Изменение пароля пользователя
     *
     * @param userId   идентификатор пользователя
     * @param password новый пароль, зашифрованный
     */
    @Modifying
    @Query("update User user set user.password = :password where user.id = :userId")
    void changeUserPassword(@Param("userId") Long userId, @Param("password") String password);
}
