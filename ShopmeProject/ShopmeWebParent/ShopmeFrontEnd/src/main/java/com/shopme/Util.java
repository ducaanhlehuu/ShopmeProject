package com.shopme;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.MailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.text.DecimalFormat;
import java.util.Properties;

public class Util {
    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(MailSettingBag mailSettings){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailSettings.getHost());
        mailSender.setPort(Integer.parseInt(mailSettings.getPort()));
        mailSender.setUsername(mailSettings.getUsername());
        mailSender.setPassword(mailSettings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", mailSettings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", mailSettings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }
    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {

        Object principal = request.getUserPrincipal();
        if (principal == null) return null;

        String customerEmail = null;

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
            customerEmail = oauth2User.getEmail();
        }

        return customerEmail;
    }

    public static String formatMoney(float amount){
        String pattern = "###,###,### đ";
        DecimalFormat formatter = new DecimalFormat(pattern);
        return formatter.format(amount);
    }


}
