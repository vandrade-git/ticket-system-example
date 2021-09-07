package org.vandrade.support.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Author: Vitor Andrade
 * Date: 10/11/18
 * Time: 3:34 PM
 */

@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                    .antMatchers("/login**")
                    .permitAll()
                    .antMatchers("/**").authenticated()
                    .anyRequest()
                    .authenticated();
//                .and()
//                    .logout()
//                    //.logoutSuccessUrl("/")
//                    .deleteCookies("UI2SESSION")
//                    .permitAll();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/webjars/**");
    }
}
