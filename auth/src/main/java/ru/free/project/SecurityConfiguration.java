package ru.free.project;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.free.project.users.UserManagerService;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
public class SecurityConfiguration {

    @Bean("daoAuthenticationProvider")
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("userDetailsService") UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean("userDetailsService")
    public UserDetailsService userDetailsService(UserManagerService userManagerService, SecurityContext securityContext) {
        return new DatabaseUserDetailsService(userManagerService, securityContext);
    }
}
