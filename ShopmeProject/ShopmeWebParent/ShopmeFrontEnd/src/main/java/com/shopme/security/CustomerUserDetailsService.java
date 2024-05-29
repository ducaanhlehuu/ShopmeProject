package com.shopme.security;

import com.shopme.admin.entity.Customer;
import com.shopme.admin.entity.User;
import com.shopme.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository repository;

    public CustomerUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = repository.findCustomerByEmail(email);
        if(customer !=null){
            return new CustomerUserDetails(customer);
        }
        throw new UsernameNotFoundException("Could not found user email:" + email);
    }

}
