package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class WebSecurityConfig {
    @Bean
    public UserDetailsService getUserDetailsService() {
        return new ShopmeUserDetailsService();
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/users/**").hasAuthority("Admin")
                        .requestMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
                        .requestMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor","Saleperson","Shipper")
                        .anyRequest().authenticated()
        ).formLogin(form->form
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
        );
        httpSecurity.logout(logout->logout.permitAll())
                .rememberMe(rem->rem.key("F4E69D731615D9FB5281A6EDC5A169B8")
                        .tokenValiditySeconds(10000).
                        userDetailsService(getUserDetailsService()));
        return httpSecurity.build();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**","fontawesome/**");
    }

}
