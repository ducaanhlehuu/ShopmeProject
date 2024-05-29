package com.shopme.admin.entity.order;

import com.shopme.admin.entity.Address;
import com.shopme.admin.entity.Customer;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date orderTime;

    private float shippingCost;
    private float productCost;
    private float subtotal;
    private float tax;
    private float total;
    private int deliverDays;
    private Date deliverDate;
    @Column(nullable = false,name = "full_name",length = 127)
    private String fullName;

    @Column(nullable = false,name = "phone_number",length = 15)
    private String phoneNumber;

    @Column(nullable = false,length = 64)
    private String addressLine1;

    @Column(length = 64)
    private String addressLine2;
    @Column(name = "province_id",length = 10)
    private String provinceId;

    @Column(name = "district_id",length = 10)
    private String districtId;

    @Column(name = "ward_id",length = 10)
    private String wardId;

    @Column(nullable = false,length = 45)
    private String ward;
    @Column(nullable = false,length = 45)
    private String district;
    @Column(nullable = false,length = 45)
    private String province;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public float getProductCost() {
        return productCost;
    }

    public void setProductCost(float productCost) {
        this.productCost = productCost;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getDeliverDays() {
        return deliverDays;
    }

    public void setDeliverDays(int deliverDays) {
        this.deliverDays = deliverDays;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    @Transient
    public String getAllAddress(){
        return "Họ và tên: " + fullName + ", Số điện thoại: " + phoneNumber + ", Địa chỉ nhà: " + addressLine1
                + ", "+ ward + ", " + district +", " + province;
    }

    public void copyAddressFromCustomer(){
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setPhoneNumber(customer.getPhoneNumber());
        setFullName(customer.getFullName());
        setProvinceId(customer.getProvinceId());
        setProvince(customer.getProvince());
        setDistrict(customer.getDistrict());
        setDistrictId(customer.getDistrictId());
        setWard(customer.getWard());
        setWardId(customer.getWardId());
    }
    public void copyAddressFromAddress(Address address){
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setPhoneNumber(address.getPhoneNumber());
        setFullName(address.getFullName());
        setProvinceId(address.getProvinceId());
        setProvince(address.getProvince());
        setDistrict(address.getDistrict());
        setDistrictId(address.getDistrictId());
        setWard(address.getWard());
        setWardId(address.getWardId());
    }
}
