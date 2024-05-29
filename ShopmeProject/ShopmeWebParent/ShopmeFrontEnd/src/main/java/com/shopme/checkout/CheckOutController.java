package com.shopme.checkout;

import com.shopme.Util;
import com.shopme.address.AddressService;
import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.order.Order;
import com.shopme.admin.entity.order.PaymentMethod;
import com.shopme.customer.CustomerService;
import com.shopme.order.OrderService;
import com.shopme.setting.MailSettingBag;
import com.shopme.setting.PaymentSettingBag;
import com.shopme.setting.SettingService;
import com.shopme.shoppingcart.CustomerNotFoundException;
import com.shopme.shoppingcart.ShoppingCartService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class CheckOutController {
    @Autowired
    private CheckoutService checkoutService;
    @Autowired private AddressService addressService;
    @Autowired private ShoppingCartService cartService;
    @Autowired private CustomerService customerService;
    @Autowired private OrderService orderService;
    @Autowired private SettingService settingService;

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        Address defaultAddress = addressService.getDefaultAddress(customer);
        String to_district = customer.getDistrictId();

        boolean supportShipping = false;
        boolean usePrimaryAddressAsDefault = false;
        // Kiem tra address nao dang duoc su dung
        if(defaultAddress != null){
            to_district = defaultAddress.getDistrictId();
            supportShipping =true;
            model.addAttribute("shippingAddress", defaultAddress.toString());
        }
        else if(customer.getWardId()!=null){
            usePrimaryAddressAsDefault = true;
            supportShipping = true;
            model.addAttribute("shippingAddress", customer.getAddress());
        }
        if(supportShipping==false){
            return "redirect:/cart";
        }

        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported",supportShipping);

        List<CartItem> cartItems = cartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems,to_district);
        PaymentSettingBag paymentSettingBag = settingService.getPaymentSettings();
        String paymentClientId = paymentSettingBag.getPaypalClientId();
        System.out.println(paymentClientId);
        model.addAttribute("clientId",paymentClientId);
        model.addAttribute("customer", customer);
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("cartItems", cartItems);

        return "checkout/checkout";
    }



    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Util.getEmailOfAuthenticatedCustomer(request);
        if(email==null){
            throw new CustomerNotFoundException("Người dùng chưa đăng nhập");
        }

        return customerService.getByEmail(email);
    }


    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Customer customer = getAuthenticatedCustomer(request);
        Address defaultAddress = addressService.getDefaultAddress(customer);
        String to_district = customer.getDistrictId();

        boolean usePrimaryAddressAsDefault = false;
        // Kiem tra address nao dang duoc su dung
        if(defaultAddress != null){
            to_district = defaultAddress.getDistrictId();
        }
        else if(customer.getWardId()!=null){
            usePrimaryAddressAsDefault = true;
        }

        List<CartItem> cartItems = cartService.listCartItems(customer);
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems,to_district);
        Order createdOrder = orderService.createOrder(customer,defaultAddress,cartItems,paymentMethod,checkoutInfo);
        cartService.deleteCartItemByCustomer(customer);
        sendOrderConfirmationEmail(request, createdOrder);
        return "checkout/order_completed";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order createdOrder) throws CustomerNotFoundException, MessagingException, UnsupportedEncodingException {
        Customer customer = getAuthenticatedCustomer(request);
        MailSettingBag mailSettingBag = settingService.getEmailSettings();

        JavaMailSenderImpl mailSender = Util.prepareMailSender(mailSettingBag);

        String toAddress = customer.getEmail();
        String subject = mailSettingBag.getOrderConfirmationSubject();
        String content = mailSettingBag.getOrderConfirmationContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

        helper.setFrom(mailSettingBag.getFromAddress(), mailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss , dd/MM/yyyy");

        content  = content.replace("[[orderId]]",String.valueOf(createdOrder.getId()))
                .replace("[[name]]",customer.getFullName())
                .replace("[[orderTime]]",dateFormatter.format(createdOrder.getOrderTime()))
                .replace("[[shippingAddress]]",createdOrder.getAllAddress())
                .replace("[[total]]",Util.formatMoney(createdOrder.getTotal()))
                .replace("[[paymentMethod]]",createdOrder.getPaymentMethod().toString());
        helper.setText(content, true);
        mailSender.send(message);

        System.out.println("to "+toAddress);
    }

}
