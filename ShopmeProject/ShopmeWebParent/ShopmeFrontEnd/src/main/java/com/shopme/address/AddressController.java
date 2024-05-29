package com.shopme.address;

import com.shopme.Util;
import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.Customer;
import com.shopme.customer.CustomerService;
import com.shopme.shoppingcart.CustomerNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        List<Address> listAddresses = addressService.getAllAddress(customer);

        boolean usePrimaryAddressAsDefault = true;
        for (Address address : listAddresses) {
            if (address.isDefaultForShipping()) {
                usePrimaryAddressAsDefault = false;
                break;
            }
        }

        model.addAttribute("listAddresses", listAddresses);
        model.addAttribute("customer", customer);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

        return "address/address";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
        String email = Util.getEmailOfAuthenticatedCustomer(request);
        if(email==null){
            throw new CustomerNotFoundException("Người dùng chưa đăng nhập");
        }

        return customerService.getByEmail(email);
    }

    @GetMapping("/address_book/new")
    public String addNewAddressPage(Model model){
        model.addAttribute("address",new Address());
        model.addAttribute("pageTitle","Thêm địa chỉ giao hàng mới");
        return "address/address_form";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        address.setCustomer(customer);
        addressService.saveAddress(address);
        ra.addFlashAttribute("message", "Địa chỉ mới đã được lưu thành công");

        String isFromCheckout = request.getParameter("redirect");
        if(isFromCheckout.equals("checkout")){
            return "redirect:/checkout";
        }
        else if(isFromCheckout.equals("cart")){
            return "redirect:/cart";
        }

        return "redirect:/address_book";
    }

    @GetMapping("/address_book/edit/{id}")
    public String editAddress(@PathVariable("id") Integer addressId, Model model,
                              HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        Address address = addressService.getAddress(addressId);

        model.addAttribute("address", address);
        model.addAttribute("pageTitle", "Sửa địa chỉ (ID: " + addressId + ")");

        return "address/address_form";
    }

    @GetMapping("/address_book/delete/{id}")
    public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
                                HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.deleteAddress(addressId);

        ra.addFlashAttribute("message", "Địa chỉ ID " + addressId + " đã bị xóa.");

        return "redirect:/address_book";
    }


    @GetMapping("/address_book/default/{id}")
    public String setAddressDefault(@PathVariable("id") Integer addressId, HttpServletRequest request) throws CustomerNotFoundException {
        Customer customer = getAuthenticatedCustomer(request);
        addressService.setAddressAsDefault(addressId,customer.getId());

        String isFromCheckout = request.getParameter("redirect");
        if(isFromCheckout.equals("checkout")){
            return "redirect:/checkout";
        }
        else if(isFromCheckout.equals("cart")){
            return "redirect:/cart";
        }

        return "redirect:/address_book";
    }
}
