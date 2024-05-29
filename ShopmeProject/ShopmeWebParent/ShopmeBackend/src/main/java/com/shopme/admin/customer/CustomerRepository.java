package com.shopme.admin.customer;

import com.shopme.admin.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends CrudRepository<Customer,Integer>, PagingAndSortingRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.fullName,' ', "
            + "c.addressLine1, ' ', c.addressLine2, ' ', c.province, ' ', c.district, ' ',c.ward,"
            + "' ', c.postalCode) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    public Customer findByEmail(String email);

    public Long countById(Integer id);
}
