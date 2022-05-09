package ru.free.project.users;

import lombok.Data;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Data
public class UserRegistrationRq {
    private String nickname;
    private String email;
    private String password;
    private String repeatPassword;
}