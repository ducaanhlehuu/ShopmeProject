package com.shopme.shoppingcart;

import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.product.Product;
import com.shopme.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;

    public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
        Product product = new Product(productId);
        Integer newQuantity = quantity;
        CartItem cartItem = cartItemRepository.findCartItemsByCustomerAndProduct(customer,product);
        if(cartItem!=null){
            newQuantity+= cartItem.getQuantity();
            if(newQuantity>5){
                throw new ShoppingCartException("Không thể thêm "+quantity+ " sản phẩm vì đã tồn tại " + cartItem.getQuantity() +
                                                " sản phẩm trong giỏ hàng");
            }
        }
        else {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        return newQuantity;
    };

    public List<CartItem> listCartItems(Customer customer){
        return cartItemRepository.findCartItemByCustomer(customer);
    }

    public float updateQuantity(Integer productId, Integer quantity, Customer customer){
        cartItemRepository.updateCartItemQuantity(quantity,customer.getId(),productId);
        Product product = productRepository.findById(productId).get();
        float subTotal = product.getDiscountPriceNumber() * quantity;
        return subTotal;
    }

    public void removeCartItem(Integer productId, Customer customer){
        cartItemRepository.deleteCartItemByCustomerAndProduct(customer.getId(),productId);
    }
    public void deleteCartItemByCustomer(Customer customer){
        cartItemRepository.deleteCartItemsByCustomerId(customer.getId());
    }


}
