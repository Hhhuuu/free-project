package ru.free.project.users;

import lombok.Data;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Data
public class ChangingPasswordRq {
    private String oldPassword;
    private String password;
    private String repeatPassword;
}