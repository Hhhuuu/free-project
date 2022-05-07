package ru.free.project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleId> {
    List<UserRole> getUserRolesByUserId(Long userId);
}