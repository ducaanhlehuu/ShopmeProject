package com.shopme.setting;

import com.shopme.admin.entity.setting.Setting;
import com.shopme.admin.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class SettingService {
    @Autowired
    private SettingRepository repo;

    public MailSettingBag getEmailSettings() {
        List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new MailSettingBag(settings);
    }
    public PaymentSettingBag getPaymentSettings() {
        List<Setting> settings = repo.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
    }
}
