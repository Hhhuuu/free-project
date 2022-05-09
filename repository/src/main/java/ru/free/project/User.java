package ru.free.project;

import javax.persistence.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Table(name = "USERS")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name="user_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;
    private String nickname;
    private String email;
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String firstName) {
        this.nickname = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}