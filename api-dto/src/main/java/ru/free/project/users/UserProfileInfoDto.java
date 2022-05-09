package ru.free.project.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileInfoDto {
    private Long id;
    private String nickname;
    private String email;
    private List<String> roles;
}