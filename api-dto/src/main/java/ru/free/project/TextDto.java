package ru.free.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextDto {
    private String title;
    private String description;
}
