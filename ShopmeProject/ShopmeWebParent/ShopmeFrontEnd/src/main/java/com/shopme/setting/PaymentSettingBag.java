package com.shopme.setting;

import com.shopme.admin.entity.setting.Setting;
import com.shopme.admin.entity.setting.SettingBag;

import java.util.List;

public class PaymentSettingBag extends SettingBag {

    public PaymentSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }
    public String getPaypalApiUrl(){
        return getValue("PAYPAL_API_URL");
    }
    public String getPaypalClientId(){
        return getValue("PAYPAL_CLIENT_ID");
    }
    public String getPaypalClientSecret(){
        return getValue("PAYPAL_CLIENT_SECRET");
    }

}
