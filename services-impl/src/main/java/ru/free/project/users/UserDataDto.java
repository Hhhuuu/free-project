package ru.free.project.users;

import ru.free.project.User;

import java.util.List;

/**
 * Информация о пользователе
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
class UserDataDto implements UserData {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String password;
    private final List<UserRole> roles;

    public UserDataDto(User user, List<UserRole> roles) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.roles = roles;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<UserRole> getRoles() {
        return roles;
    }
}
