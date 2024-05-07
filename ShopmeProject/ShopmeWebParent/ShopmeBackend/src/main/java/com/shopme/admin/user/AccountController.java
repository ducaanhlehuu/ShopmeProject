package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.entity.User;
import com.shopme.admin.security.ShopmeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user",user);
        return "/users/account_form";
    }
    @GetMapping("/account/change_password")
    public String changePasswordForm(@AuthenticationPrincipal ShopmeUserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        model.addAttribute("id",user.getId());
        return "change_password";
    }
    @PostMapping("/account/change_password/update")
    public String changePassword(@Param("id") Integer id, @RequestParam("password") String oldPassword, RedirectAttributes redirectAttributes,
                                 @RequestParam("newPassword") String newPassword, Model model) {
        model.addAttribute("id",id);
        try{
            User user = userService.getUser(id);
            if(passwordEncoder.matches(oldPassword,user.getPassword())){
                redirectAttributes.addFlashAttribute("message","Update password successfully");
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.save(user);
            }
            else {
                redirectAttributes.addFlashAttribute("message","Password isn't duplicated");
            }
        }
        catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage() );
            return "redirect:/account/change_password";
        }
        return "redirect:/account/change_password";
    }
    @PostMapping("/account/save")
    public String saveAccount(User user, @RequestParam("image") MultipartFile multipartFile,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal ShopmeUserDetails shopmeUserDetails) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "user-photos/" + user.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            user.setPhotos(fileName);
            User savedUser = userService.updateAccount(user);
        }
        else {
            if(user.getPhotos().isEmpty())
                user.setPhotos(null);
            userService.updateAccount(user);
        }
        // Cap nhat ten cho User Details de hien thi tren goc phai phia tren man
        shopmeUserDetails.setFirstName(user.getFirstName());
        shopmeUserDetails.setLastName(user.getLastName());
        redirectAttributes.addFlashAttribute("message","Update account details successfully");
        return "redirect:/account";
    }
}
