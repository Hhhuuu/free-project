package ru.free.project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    /**
     * Поиск роли по имени
     *
     * @param name имя
     * @return роль
     */
    Optional<Role> findByName(String name);
}