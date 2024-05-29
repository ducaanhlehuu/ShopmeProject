package com.shopme.shoppingcart;

import com.shopme.Util;
import com.shopme.address.AddressService;
import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.product.Product;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired private ShoppingCartService cartService;
    @Autowired private CustomerService customerService;
    @Autowired private AddressService addressService;

    @GetMapping("/cart")
    public String viewCartPage(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        float estimatedTotal = 0.0F;
        List<CartItem> cartItems = cartService.listCartItems(customer);
        for(CartItem cartItem : cartItems){
            estimatedTotal+= cartItem.getTotalPayment();

        }
        Address defaultAddress = addressService.getDefaultAddress(customer);
        boolean supportShipping = false;
        boolean usePrimaryAddressAsDefault = false;
        if(defaultAddress != null){
            supportShipping =true;
        }
        else if(customer.getWardId()!=null){
            usePrimaryAddressAsDefault = true;
            supportShipping = true;
        }
        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported",supportShipping);
        model.addAttribute("estimatedTotal", Product.formatPrice(estimatedTotal));
        model.addAttribute("cartItems",cartItems);
        return "cart/shopping_cart";
    }
    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Util.getEmailOfAuthenticatedCustomer(request);
        if(email==null){
            throw new CustomerNotFoundException("Người dùng chưa đăng nhập");
        }

        return customerService.getByEmail(email);
    }
}
