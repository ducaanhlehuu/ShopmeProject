package com.shopme.customer;

import com.shopme.admin.entity.AuthenticationType;
import com.shopme.admin.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Integer> {
    public Customer findCustomerByEmail(String email);

    public Customer findCustomerByVerificationCode(String verificationCode);

    @Query("update Customer c set c.enabled = true, c.verificationCode = null where c.id = ?1")
    @Modifying
    public void enabled(Integer id);

    @Query("update Customer c set c.authenticationType = ?1 where c.id =?2")
    @Modifying
    public void updateAuthenticationType(AuthenticationType authenticationType,Integer id);
}
