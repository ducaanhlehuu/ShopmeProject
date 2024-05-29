package com.shopme.admin.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "postal_code",nullable = false,length = 15)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "default_address")
    private boolean defaultForShipping;

    public Address() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isDefaultForShipping() {
        return defaultForShipping;
    }

    public void setDefaultForShipping(boolean defaultForShipping) {
        this.defaultForShipping = defaultForShipping;
    }

    @Override
    public String toString() {
        return "Họ và tên: " + fullName + ", Số điện thoại: " + phoneNumber + ", Địa chỉ nhà: " + addressLine1
                + ", "+ ward + ", " + district +", " + province;
    }
}
