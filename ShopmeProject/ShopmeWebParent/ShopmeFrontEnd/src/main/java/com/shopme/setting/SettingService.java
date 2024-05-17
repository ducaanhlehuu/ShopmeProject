package com.shopme.admin.setting;

import com.shopme.admin.entity.setting.Setting;
import com.shopme.admin.entity.setting.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> listAllSetting(){
        return (List<Setting>) repo.findAll();
    }

    public void saveAllSetting(Iterable<Setting> settings) {
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSetting(){
        return (List<Setting>) repo.findByCategory(SettingCategory.MAIL_SERVER);
    }
    public List<Setting> getMailTemplateSetting(){
        return (List<Setting>) repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
