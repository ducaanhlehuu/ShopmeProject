package com.shopme.admin.entity.setting;

import java.util.List;

public class SettingBag {
    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public List<Setting> getListSettings() {
        return listSettings;
    }

    public Setting get(String key){
        int index = listSettings.indexOf(new Setting(key));
        if (index >= 0) {
            return listSettings.get(index);
        }
        return null;
    }
    public String getValue(String key){
        int index = listSettings.indexOf(new Setting(key));
        if (index >= 0) {
            return listSettings.get(index).getValue();
        }
        return null;
    }

    public void update(String key, String value) {
        Setting setting = get(key);
        if (setting != null && value != null) {
            setting.setValue(value);
        }
    }

}
