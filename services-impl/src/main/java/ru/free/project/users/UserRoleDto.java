package ru.free.project.users;

import ru.free.project.Role;

/**
 * Роль пользователя
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
class UserRoleDto implements UserRole {
    private final Long id;
    private final String name;

    public UserRoleDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
