package com.shopme.admin.order;

import com.shopme.admin.customer.CustomerNotFoundException;
import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 10;

    @Autowired
    private OrderRepository orderRepo;

    public Page<Order> listByPage(int pageNum, String keyword){

        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNum - 1,ORDERS_PER_PAGE, sort);
        if(keyword!=null) {
            return orderRepo.findAll(keyword,pageable);
        }
        return orderRepo.findAll(pageable);
    }


    public Order get(Integer id) throws OrderNotFoundException {
        try {
            return orderRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new OrderNotFoundException("Không tìm thấy Order ID " + id);
        }
    }
    public void delete(Integer id) throws OrderNotFoundException {
        Long count = orderRepo.countById(id);
        if (count == null || count == 0) {
            throw new OrderNotFoundException("Không tìm thấy đơn hàng nào có ID " + id);
        }

        orderRepo.deleteById(id);
    }

    public void save(Order orderInForm){
        Order orderInDb = orderRepo.findById(orderInForm.getId()).get();

        orderInForm.setOrderTime(orderInDb.getOrderTime());
        orderInForm.setCustomer(orderInDb.getCustomer());
        orderRepo.save(orderInForm);
    }
}
