package ru.free.project;

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
}
