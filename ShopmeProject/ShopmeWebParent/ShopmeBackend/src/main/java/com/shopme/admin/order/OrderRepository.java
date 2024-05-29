package com.shopme.admin.order;

import com.shopme.admin.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends CrudRepository<Order, Integer>, PagingAndSortingRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
            + " o.fullName LIKE %?1% OR"
            + " o.phoneNumber LIKE %?1% OR"
            + " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
            + " o.province LIKE %?1% OR o.district LIKE %?1% OR o.ward LIKE %?1% OR"
            + " CAST(o.paymentMethod as string) LIKE %?1% OR CAST(o.orderStatus as string) LIKE %?1% OR"
            + " o.customer.fullName LIKE %?1% OR CAST(o.orderTime as string) LIKE %?1%")
    public Page<Order> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
