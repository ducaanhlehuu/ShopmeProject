package com.shopme.shoppingcart;

import com.shopme.Util;
import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.product.Product;
import com.shopme.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShoppingCartRestController {
    @Autowired private ShoppingCartService cartService;
    @Autowired private CustomerService customerService;

    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable(name = "productId") Integer productId
            ,@PathVariable(name = "quantity") Integer quantity,HttpServletRequest request){

       try{
           Customer customer = getAuthenticatedCustomer(request);
           Integer updatedQuantity = cartService.addProduct(productId,quantity,customer);
           return updatedQuantity + " sản phẩm đã được thêm vào giỏ hàng!";
       }
       catch (CustomerNotFoundException e){
           return "Bạn phải đăng nhập để mua hàng";
       }
       catch (ShoppingCartException ex){
           return ex.getMessage();
       }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Util.getEmailOfAuthenticatedCustomer(request);
        if(email==null){
            System.out.println(email);
            throw new CustomerNotFoundException("Người dùng chưa đăng nhập");
        }

        return customerService.getByEmail(email);
    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateCart(@PathVariable(name = "productId") Integer productId
            ,@PathVariable(name = "quantity") Integer quantity,HttpServletRequest request){

        try{
            Customer customer = getAuthenticatedCustomer(request);
            cartService.updateQuantity(productId,quantity,customer);
            return getTotalPayment(customer);
        }
        catch (CustomerNotFoundException e){
            return "Bạn phải đăng nhập để mua hàng";
        }
    }

    @PostMapping("/cart/remove/{productId}")
    public String RemoveCartItem(@PathVariable(name = "productId") Integer productId
            ,HttpServletRequest request){

        try{
            Customer customer = getAuthenticatedCustomer(request);
            cartService.removeCartItem(productId,customer);
            return getTotalPayment(customer);
        }
        catch (CustomerNotFoundException e){
            return "Bạn phải đăng nhập để mua hàng";
        }
    }

    private String getTotalPayment(Customer customer){
        float totalPrice =0.0F;
        List<CartItem> cartItems = cartService.listCartItems(customer);
        for(CartItem cartItem:cartItems){
            totalPrice+=cartItem.getTotalPayment();
        }
        return Product.formatPrice(totalPrice);
    }
}
