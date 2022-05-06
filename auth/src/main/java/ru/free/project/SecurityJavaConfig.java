package ru.free.project;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.free.project.handlers.*;

/**
 * @author Popov Maxim <m_amapapa@mail.ru>
 */
@Configuration
@EnableWebSecurity
@ComponentScan({
        "ru.free.project",
        "ru.free.project.handlers"
})
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    //    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final CustomUrlAuthenticationSuccessHandler customSuccessHandler;
    private final CustomUrlAuthenticationFailureHandler customFailureHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    public SecurityJavaConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                              CustomUrlAuthenticationSuccessHandler customSuccessHandler,
//                              @Qualifier("daoAuthenticationProvider") DaoAuthenticationProvider daoAuthenticationProvider,
                              CustomUrlAuthenticationFailureHandler customFailureHandler,
                              CustomLogoutSuccessHandler customLogoutSuccessHandler,
                              CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
//        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.customSuccessHandler = customSuccessHandler;
        this.customFailureHandler = customFailureHandler;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider);
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(encoder().encode("adminPass"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/logout").permitAll()
                .antMatchers("/api/login").permitAll()
//                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/api/login")
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)
                .and()
                .logout().logoutUrl("/api/logout").logoutSuccessHandler(customLogoutSuccessHandler);
    }
}