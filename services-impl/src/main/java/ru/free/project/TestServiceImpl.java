package ru.free.project;

import org.springframework.stereotype.Service;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Service("testServiceImpl")
public class TestServiceImpl implements TestService  {

    @Override
    public String getSomeMethod() {
        return "Hello, world!";
    }
}
