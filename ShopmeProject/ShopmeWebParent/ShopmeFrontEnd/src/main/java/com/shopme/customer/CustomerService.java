package com.shopme.customer;

import com.shopme.admin.entity.AuthenticationType;
import com.shopme.admin.entity.Customer;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CustomerRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public boolean isEmailUnique(String email){
        if(email==null || email.length()==0)
            return false;
        Customer customerByEmail = repo.findCustomerByEmail(email);
        return customerByEmail==null;
    }

    public void registerCustomer(Customer customer){
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        repo.save(customer);

    }
    public boolean verifyCustomer( String verificationCode){

        Customer customer = repo.findCustomerByVerificationCode(verificationCode);
        if (customer==null||customer.isEnabled()){
            return false;
        }
        else  {
            repo.enabled(customer.getId());
            return true;
        }
    }

    public void updateAuthentication(AuthenticationType type, Customer customer){
        if(customer.getAuthenticationType() == null || !customer.getAuthenticationType().equals(type)){
            repo.updateAuthenticationType(type,customer.getId());
        }
    }
    public Customer getByEmail(String email){
        return repo.findCustomerByEmail(email);
    }

    public void addNewOAuth2Customer(String name, String email,AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setFullName(name);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(authenticationType);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");
        customer.setDistrict("");
        customer.setProvince("");
        customer.setWard("");
        repo.save(customer);
    }

    public void update(Customer customerInForm) {
        Customer customerInDB = repo.findById(customerInForm.getId()).get();

        if (customerInDB.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (!customerInForm.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            } else {
                customerInForm.setPassword(customerInDB.getPassword());
            }
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }

        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
//        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        repo.save(customerInForm);
    }
}
