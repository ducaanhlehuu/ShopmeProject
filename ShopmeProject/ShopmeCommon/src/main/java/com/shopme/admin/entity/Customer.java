package com.shopme.admin.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true,length = 45)
    private String email;
    @Column(nullable = false,length = 64)
    private String password;

    @Column(nullable = false,name = "full_name",length = 127)
    private String fullName;

    @Column(nullable = false,name = "phone_number",length = 15)
    private String phoneNumber;

    private boolean enabled;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

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

    @Column(length = 64,name = "verification_code")
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name="authentication_type",length = 15)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token",length = 45)
    private String resetPasswordToken;
    public Customer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
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

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
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

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", province='" + province + '\'' +
                '}';
    }
    @Transient
    public String getAddress(){
        String address = fullName;
        if(phoneNumber!=null && !phoneNumber.isEmpty())
            address+= ", Số điện thoại: " + phoneNumber;
        if(email!=null && !email.isEmpty())
            address+= ", Email: " + email;
        if(addressLine1!=null && !addressLine1.isEmpty())
            address+= ", Địa chỉ: " + addressLine1;
        if(province!=null && !province.isEmpty())
            address+= ", Tỉnh: " + province;
        if(district!=null && !district.isEmpty())
            address+= ", Quận/ huyện: " + district;
        if(ward!=null && !ward.isEmpty())
            address+= ", Xã/ phường: " + ward;
        return address;
    }

    @Transient
    public String getPhoneNumber4Paypal(){
        return "+84" + phoneNumber.substring(1);
    }
}
