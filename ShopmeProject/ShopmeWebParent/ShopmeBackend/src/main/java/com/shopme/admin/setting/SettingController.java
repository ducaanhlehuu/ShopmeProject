package com.shopme.admin.setting;

import com.shopme.admin.entity.setting.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SettingController {
    @Autowired
    private SettingService service;

    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> listSettings = service.listAllSetting();
        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        return "settings/settings";
    }

    @PostMapping ("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> mailServerSettings = service.getMailServerSetting();

        for (Setting setting : mailServerSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAllSetting(mailServerSettings);

        ra.addFlashAttribute("message", "Đã lưu các cài đặt về máy chủ E-mail");

        return "redirect:/settings#mailServer";
    }
    @PostMapping ("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> mailTemplateSettings = service.getMailTemplateSetting();

        for (Setting setting : mailTemplateSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAllSetting(mailTemplateSettings);

        ra.addFlashAttribute("message", "Đã lưu các cài đặt về máy chủ E-mail");

        return "redirect:/settings#mailServer";
    }

    @PostMapping ("/settings/save_payment_setting")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> paymentSettings = service.getPaymentSetting();

        for (Setting setting : paymentSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAllSetting(paymentSettings);

        ra.addFlashAttribute("message", "Đã lưu các cài đặt về thanh toán");

        return "redirect:/settings#payment";
    }
}
