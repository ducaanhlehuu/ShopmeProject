package com.shopme.shoppingcart;

import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.product.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Integer> {

    public List<CartItem> findCartItemByCustomer(Customer customer);

    public CartItem findCartItemsByCustomerAndProduct(Customer customer, Product product);

    @Query("UPDATE CartItem ct set ct.quantity =?1 where  ct.customer.id = ?2 and ct.product.id = ?3")
    @Modifying
    public void updateCartItemQuantity(Integer quantity, Integer customerId, Integer productId);

    @Query("delete CartItem ct where ct.customer.id=?1 and ct.product.id =?2")
    @Modifying
    public void deleteCartItemByCustomerAndProduct(Integer customerId, Integer productId);

    @Query("delete CartItem ct where ct.customer.id=?1")
    @Modifying
    public void deleteCartItemsByCustomerId(Integer customerId);
}
