package ru.free.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.free.project.TestService;
import ru.free.project.TextDto;
import ru.free.project.exceptions.CommonException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping(value = "/", headers = {"Accept=application/json"}, produces = {APPLICATION_JSON_VALUE})
    public TextDto sayHello() throws CommonException {
        TextDto textDto = new TextDto();
        textDto.setTitle(testService.getSomeMethod());
        return textDto;
    }

    @GetMapping(value = "/api/void", headers = {"Accept=application/json"}, produces = {APPLICATION_JSON_VALUE})
    public void sayVoid() throws CommonException {
        return;
    }

    @GetMapping(value = "/api/null", headers = {"Accept=application/json"}, produces = {APPLICATION_JSON_VALUE})
    public String sayNull() throws CommonException {
        return "asds";
    }

    @GetMapping(value = "/api/error", headers = {"Accept=application/json"}, produces = {APPLICATION_JSON_VALUE})
    public void sayError() throws CommonException {
        throw new CommonException("Ошибка");
    }
}
