package com.shopme.admin;


import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.product.Product;
import com.shopme.admin.entity.order.Order;
import com.shopme.admin.entity.order.OrderDetail;
import com.shopme.admin.entity.order.OrderStatus;
import com.shopme.admin.entity.order.PaymentMethod;
import com.shopme.admin.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateNewOrderWithSingleProduct() {
        Customer customer = entityManager.find(Customer.class, 7);
        Product product = entityManager.find(Product.class, 2);

        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        mainOrder.setShippingCost(10);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setTax(0);
        mainOrder.setSubtotal(product.getPrice());
        mainOrder.setTotal(product.getPrice() + 10);

        mainOrder.setPaymentMethod(PaymentMethod.COD);
        mainOrder.setOrderStatus(OrderStatus.NEW);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(1);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(mainOrder);
        orderDetail.setProductCost(product.getCost());
        orderDetail.setShippingCost(10);
        orderDetail.setQuantity(1);
        orderDetail.setSubTotal(product.getPrice());
        orderDetail.setUnitPrice(product.getPrice());

        mainOrder.getOrderDetails().add(orderDetail);

        Order savedOrder = repo.save(mainOrder);

    }
}
