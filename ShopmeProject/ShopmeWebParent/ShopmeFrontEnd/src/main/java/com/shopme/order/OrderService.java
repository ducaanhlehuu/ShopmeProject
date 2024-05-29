package com.shopme.order;

import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.CartItem;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.order.Order;
import com.shopme.admin.entity.order.OrderDetail;
import com.shopme.admin.entity.order.OrderStatus;
import com.shopme.admin.entity.order.PaymentMethod;
import com.shopme.admin.entity.product.Product;
import com.shopme.checkout.CheckoutInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems
            , PaymentMethod paymentMethod, CheckoutInfo checkoutInfo){
        Order newOrder = new Order();
        newOrder.setOrderTime(new Date());
        newOrder.setOrderStatus(OrderStatus.NEW);
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
        newOrder.setCustomer(customer);
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setTax(0.0F);

        if(address==null){
            newOrder.copyAddressFromCustomer();
        }
        else {
            newOrder.copyAddressFromAddress(address);
        }
        Set<OrderDetail> orderDetails = newOrder.getOrderDetails(); // Chua co gi nha

        for(CartItem item: cartItems){
            Product product = item.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setProductCost(product.getCost() * item.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPriceNumber());
            orderDetail.setSubTotal(item.getTotalPayment());
            orderDetail.setShippingCost(item.getShippingCost());
            orderDetails.add(orderDetail);
        }

        return orderRepository.save(newOrder);
    }
}
