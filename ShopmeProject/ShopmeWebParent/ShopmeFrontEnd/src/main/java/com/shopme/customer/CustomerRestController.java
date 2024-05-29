package com.shopme.customer;

import com.shopme.Util;
import com.shopme.admin.entity.Customer;
import com.shopme.setting.MailSettingBag;
import com.shopme.setting.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@RestController
public class CustomerRestController {
    @Autowired private CustomerService service;
    @Autowired private SettingService settingService;

    @PostMapping("/customers/check_email_unique")
    public String checkEmailUnique(@Param("email") String email){
        return service.isEmailUnique(email) ? "OK" : "Duplicated";
    }

}
