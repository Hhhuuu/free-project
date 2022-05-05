package ru.free.project;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ru.free.project.responsewrapper.BaseResponse;

import java.io.OutputStream;
import java.util.Objects;

/**
 * Форматирование ответа перед отправкой
 *
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@ControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (HttpStatus.valueOf(((ServletServerHttpResponse) response).getServletResponse().getStatus()).is4xxClientError()) {
            return BaseResponse.failResponse(new Exception("Страница не найдена"));
        } else if (Objects.isNull(body)) {
            return BaseResponse.goodResponse(null);
        } else if (formattingIsNeeded(body)) {
            return BaseResponse.goodResponse(body);
        }
        return body;
    }

    private static boolean formattingIsNeeded(Object entity) {
        if (entity == null) {
            return true;
        } else if (isBaseResponse(entity.getClass())) {
            return false;
        } else {
            return !isStreamingOutput(entity.getClass()) && entity.getClass() != byte[].class;
        }
    }

    private static boolean isBaseResponse(Class<?> clazz) {
        return BaseResponse.class.isAssignableFrom(clazz);
    }

    private static boolean isStreamingOutput(Class<?> clazz) {
        return OutputStream.class.isAssignableFrom(clazz);
    }
}
