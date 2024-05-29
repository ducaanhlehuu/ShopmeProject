package com.shopme.security;

import com.shopme.security.oauth.CustomerOAuth2Service;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Autowired
    CustomerOAuth2Service oAuth2Service;
    @Autowired
    OAuth2LoginSuccessHandler loginSuccessHandler;
    @Autowired
    DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDetailsService customerUserDetailsService(){
        return new CustomerUserDetailsService();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customerUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.authorizeHttpRequests(
                auth -> auth.requestMatchers("/customers","/account_details","/update_account_details","/cart/**","/address_book/**","/check_out","place_order").authenticated()
                        .anyRequest().permitAll()
        ).formLogin(
                form -> form.loginPage("/login")
                        .usernameParameter("email")
                        .successHandler(databaseLoginSuccessHandler)
                        .permitAll()
        ).oauth2Login(oauth2 ->oauth2.loginPage("/login")
                        .userInfoEndpoint(u->u.userService(oAuth2Service))
                        .successHandler(loginSuccessHandler))
        .logout(
                logout ->logout.permitAll()
        ).rememberMe(rem->rem.key("F4E69D731615D9FB5281A6EDC5A169B8")
                .tokenValiditySeconds(10000).
                userDetailsService(customerUserDetailsService()))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        return httpSecurity.build();
    }
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**","fontawesome/**");
    }

}
