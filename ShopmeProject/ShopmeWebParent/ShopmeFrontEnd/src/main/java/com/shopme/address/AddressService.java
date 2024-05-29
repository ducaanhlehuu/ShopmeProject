package com.shopme.address;

import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {
    @Autowired private AddressRepository repository;

    public List<Address> getAllAddress(Customer customer){
        return repository.findAddressByCustomer(customer);
    }

    public void saveAddress(Address address){
        repository.save(address);
    }
    public Address getAddress(Integer addressId){
        return repository.findByIdAndCustomerId(addressId);
    }
    public void deleteAddress(Integer addressId){
        repository.deleteByIdAndCustomerId(addressId);
    }

    public void setAddressAsDefault(Integer addressId, Integer customerId) {
        repository.setAsDefault(addressId);
        repository.setNonDefaultForOthers(addressId,customerId);
    }

    public Address getDefaultAddress(Customer customer){
        return repository.findDefaultByCustomer(customer.getId());
    }
}

