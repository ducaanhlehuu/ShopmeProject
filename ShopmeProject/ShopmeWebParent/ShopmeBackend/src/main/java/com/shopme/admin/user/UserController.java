package com.shopme.admin.user;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.entity.Role;
import com.shopme.admin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listPageByNumber(1,model,"id","asc",null);
    }
    @GetMapping("/users/new")
    public String getUserForm(Model model){
        User new_user = new User();
        List<Role> allRole = userService.listRoleAll();
        model.addAttribute("listRoles",allRole);
        model.addAttribute("user",new_user);
        model.addAttribute("PageTitle","Create New User");
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(Integer id, User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "user-photos/" + user.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            user.setPhotos(fileName);
            User savedUser = userService.saveUser(user);

        }
        else {
            if(user.getPhotos().isEmpty())
                user.setPhotos(null);
            userService.saveUser(user);
        }

        String keyword = user.getEmail().replaceFirst("@.*","");
        return "redirect:/users/page/1?sortField=Id&sortDir=asc&keyword=" + keyword;
    }
    @GetMapping("/users/update/{id}")
    public String updateUser(Model model,@PathVariable(name = "id") Integer user_id,RedirectAttributes redirectAttributes){
        try {
            User myuser = userService.getUser(user_id);
            model.addAttribute("user",myuser);
            List<Role> allRole = userService.listRoleAll();
            model.addAttribute("listRoles",allRole);
            model.addAttribute("PageTitle","Edit User (ID: "+user_id+")");
        }
        catch (UserNotFoundException e){
            redirectAttributes.addFlashAttribute("message",e.getMessage() );
            return "redirect:/users";
        }
        return "user_form";
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id") Integer id,RedirectAttributes redirectAttributes){
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message","Delete Successfully User ID: "+ id);
        }
        catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(name = "id") Integer id,
                                      @PathVariable(name = "status") boolean enabled,
                                      RedirectAttributes redirectAttributes) throws UserNotFoundException {
        userService.updateEnabledStatus(id,enabled);
        String message = "The user ID " + id + " has been " + (enabled?"enabled":"disabled");
        redirectAttributes.addFlashAttribute("message",message);
        String keyword = userService.getUser(id).getEmail().replaceFirst("@.*","");
        return "redirect:/users/page/1?sortField=Id&sortDir=asc&keyword=" + keyword;
    }
    @GetMapping("/users/page/{pageNum}")
    public String listPageByNumber(@PathVariable(name = "pageNum") int pageNum, Model model,
                                   @Param("sortField") String sortField,
                                   @Param("sortDir") String sortDir,
                                   @Param("keyword") String keyword){

        pageNum = pageNum<=0 ? 1 : pageNum;

        System.out.println(sortField);
        System.out.println(sortDir);
        Page<User> page = userService.listPageUser(pageNum,sortField,sortDir,keyword);
        page.getContent().forEach(p->{
            System.out.println(p.toString());
        });
        long startCount = (pageNum - 1) * userService.USER_PER_PAGE + 1;
        long endCount = Math.min(startCount + userService.USER_PER_PAGE - 1,page.getTotalElements());

        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("listUsers",page.getContent());
        model.addAttribute("page",page);
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("keyword",keyword);
        return "users";
    }
}

