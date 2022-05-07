package ru.free.project;

import java.io.Serializable;

/**
 * Ключ для таблицы связки
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public class UserRoleId implements Serializable {
    private Long userId;
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
