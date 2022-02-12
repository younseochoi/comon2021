package com.cokung.comon.config;

import com.cokung.comon.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().disable();

        http.logout().logoutUrl("/user/logout")
                .deleteCookies(JwtUtil.ACCESS_TOKEN_NAME)
                .deleteCookies(JwtUtil.REFRESH_TOKEN_NAME)
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
    }
}
