package ru.free.project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}