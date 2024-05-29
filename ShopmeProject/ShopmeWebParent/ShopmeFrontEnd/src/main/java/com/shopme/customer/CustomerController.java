package com.shopme.customer;

import com.shopme.Util;
import com.shopme.admin.entity.Customer;
import com.shopme.security.CustomerUserDetails;
import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.MailSettingBag;
import com.shopme.setting.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService service;
    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("pageTitile", "Đăng kí tài khoản mới");
        model.addAttribute("customer", new Customer());
        return "register/register_form";
    }
    @GetMapping("/verify")
    public String verifyPage(Model model, @Param("code") String code){

        boolean verified = service.verifyCustomer(code);
        model.addAttribute("pageTitle",verified?"Xác thực thành công":"Xác thực không thành công");
        return verified?"register/success":"register/fail";
    }


    @PostMapping("/customers/create_customer")
    public String createCustomer(Customer customer, Model model, HttpServletRequest request){
        service.registerCustomer(customer);
        try{
            sendVerificationEmail(request,customer);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("pageTitle","Đắng ký thành công");
        return "/register/success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        MailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl javaMailSender = Util.prepareMailSender(emailSettings);

        String toAddress = customer.getEmail();
        String subject = emailSettings.getCustomerVerifySubject();
        String content = emailSettings.getCustomerVerifyContent();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String verifyUrl = Util.getSiteUrl(request) + "/verify?code=" + customer.getVerificationCode();
        content = content.replace("[[name]]",customer.getFullName())
                .replace("[[URL]]",verifyUrl);
        helper.setText(content,true);
        javaMailSender.send(message);

        System.out.println("to "+toAddress);
        System.out.println("subject "+subject);
        System.out.println("url  "+verifyUrl);

    }

    @GetMapping("/account_details")
    public String viewAccountDetails(Model model, HttpServletRequest request){
        String email = Util.getEmailOfAuthenticatedCustomer(request);
        Customer customer = service.getByEmail(email);
        model.addAttribute("customer",customer);
        return "customer/account_form";
    }


    @PostMapping("/update_account_details")
    public String updateAccountDetails(Model model, Customer customer, RedirectAttributes ra,
                                       HttpServletRequest request) {
        service.update(customer);
        ra.addFlashAttribute("message", "Đã cập nhật tài khoản thành công.");

        updateNameForAuthenticatedCustomer(customer, request);

        String redirectOption = request.getParameter("redirect");
        String redirectURL = "redirect:/account_details";

        if ("address_book".equals(redirectOption)) {
            redirectURL = "redirect:/address_book";
        } else if ("cart".equals(redirectOption)) {
            redirectURL = "redirect:/cart";
        } else if ("checkout".equals(redirectOption)) {
            redirectURL = "redirect:/address_book?redirect=checkout";
        }

        return redirectURL;
    }

    private void updateNameForAuthenticatedCustomer(Customer customer, HttpServletRequest request) {
        Object principal = request.getUserPrincipal();

        if (principal instanceof UsernamePasswordAuthenticationToken
                || principal instanceof RememberMeAuthenticationToken) {
            CustomerUserDetails userDetails = getCustomerUserDetailsObject(principal);
            Customer authenticatedCustomer = userDetails.getCustomer();
            authenticatedCustomer.setFullName(customer.getFullName());

        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
            oauth2User.setFullName(customer.getFullName());
        }
    }

    private CustomerUserDetails getCustomerUserDetailsObject(Object principal) {
        CustomerUserDetails userDetails = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        } else if (principal instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken token = (RememberMeAuthenticationToken) principal;
            userDetails = (CustomerUserDetails) token.getPrincipal();
        }

        return userDetails;
    }
}
