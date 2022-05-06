package ru.free.project.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.free.project.responsewrapper.BaseResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
public interface CustomResponseHandler {

    ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    default void setFailResponse(HttpServletResponse response, RuntimeException exception, String message) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(OBJECT_MAPPER.writeValueAsString(BaseResponse.failResponse(new Exception(message))));
    }

    default void setGoodResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(OBJECT_MAPPER.writeValueAsString(BaseResponse.goodResponse(Optional.ofNullable(message).orElse(null))));
    }

    default void setGoodResponseWithBody(HttpServletResponse response, Object body) throws IOException {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().print(OBJECT_MAPPER.writeValueAsString(BaseResponse.goodResponse(body)));
    }
}
