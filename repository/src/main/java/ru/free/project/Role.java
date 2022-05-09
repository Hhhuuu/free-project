package ru.free.project;

import javax.persistence.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Table(name = "ROLES")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(name="roles_id_seq", sequenceName = "roles_id_seq", allocationSize = 1)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}