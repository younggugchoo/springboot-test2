package com.example.test2.springboot.config.auth;

import com.example.test2.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomeOAuth2UserService customeOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                        .and()
                                .authorizeRequests()
                                        .antMatchers("/", "/css/**", "/images/**", "/js/**", "h2-console/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                        .and().logout().logoutSuccessUrl("/")
                        .and().oauth2Login()
                            .defaultSuccessUrl("/", true)
                            .userInfoEndpoint().userService(customeOAuth2UserService);

    }


}
