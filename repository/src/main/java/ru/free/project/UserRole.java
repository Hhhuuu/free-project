package ru.free.project;

import javax.persistence.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Table(name = "USERS_ROLES")
@Entity
@IdClass(UserRoleId.class)
public class UserRole {
    /**
     * id пользователя
     */
    @Id
    @Column(name = "USER_ID")
    private Long userId;

    /**
     * id роли
     */
    @Id
    @Column(name = "ROLE_ID")
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
