package com.shopme.address;

import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Integer> {

    public List<Address> findAddressByCustomer(Customer customer);

    public Address findAddressByIdAndCustomer(Integer addressId, Customer customer);

    @Query("select a from Address a where a.id = ?1")
    public Address findByIdAndCustomerId(Integer addressId);

    // Ko ro tai sao phai co customerId o day nua,
    @Query("delete from Address a where a.id = ?1")
    @Modifying
    public void deleteByIdAndCustomerId(Integer addressId);

    @Query("update Address  a set a.defaultForShipping= true where a.id = ?1")
    @Modifying
    public void setAsDefault(Integer addressId);

    @Query("update Address a set a.defaultForShipping= false where a.id != ?1 and a.customer.id = ?2")
    @Modifying
    public void setNonDefaultForOthers(Integer defaultAddressId, Integer customerId);

    @Query("Select a from Address a where a.customer.id = ?1 and a.defaultForShipping = true")
    public Address findDefaultByCustomer(Integer customerId);


}
