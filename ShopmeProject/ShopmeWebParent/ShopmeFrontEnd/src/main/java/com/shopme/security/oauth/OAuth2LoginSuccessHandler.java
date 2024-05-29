package com.shopme.security.oauth;

import com.shopme.admin.entity.AuthenticationType;
import com.shopme.admin.entity.Customer;
import com.shopme.customer.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    @Lazy
    private CustomerService service;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOAuth2User customerOAuth2User = (CustomerOAuth2User) authentication.getPrincipal();

        String name = customerOAuth2User.getName();
        String email = customerOAuth2User.getEmail();
        String clientName = customerOAuth2User.getClientName();

        AuthenticationType authenticationType = getAuthenticationType(clientName);
        Customer customerByEmail = service.getByEmail(email);
        if(customerByEmail == null){
            service.addNewOAuth2Customer(name,email,authenticationType);
        }
        else {
            service.updateAuthentication(authenticationType,customerByEmail);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private AuthenticationType getAuthenticationType(String clientName) {
        if(clientName.equals("Google")){
            return AuthenticationType.GOOGLE;
        }
        else if(clientName.equals("Facebook")){
            return AuthenticationType.FACEBOOK;
        }
        else {
            return AuthenticationType.DATABASE;
        }
    }
}
