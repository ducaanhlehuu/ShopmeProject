package com.shopme.admin.brand;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.entity.Brand;
import com.shopme.admin.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {
    @Autowired
    private BrandService service;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/brands")
    public String listBrand(Model model){
        model.addAttribute("brands",service.listAllBrand());
        return "brands/brand";
    }

    @GetMapping("/brands/new")
    public String createNewBrand(Model model) {
        model.addAttribute("brand",new Brand());
        model.addAttribute("listCategories",categoryService.listAll());
        model.addAttribute("PageTitle", "Thêm nhan hàng");
        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(@RequestParam("fileImage") MultipartFile imageFile, Brand brand,
                               RedirectAttributes redirectAttributes) throws IOException {
        boolean isNew = brand.getId() ==null;

        if(!imageFile.isEmpty()){
            System.out.println(brand.toString());
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            brand.setLogo(fileName);
            Brand savedBrand = service.save(brand);
            String uploadDir = "../brand-logos/" + savedBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,imageFile);
        }
        else{
            if(brand.getLogo().isEmpty())
                brand.setLogo("abc.png");
            service.save(brand);
        }
        if(isNew) {
            redirectAttributes.addFlashAttribute("message","Tạo mới nhãn hàng thành công");
        }
        else {
            redirectAttributes.addFlashAttribute("message","Đã sửa nhãn hàng ID: " + brand.getId() );
        }
        return "redirect:/brands";
    }

    @GetMapping("/brands/delete/{id}")
    public String updateStatus(@PathVariable("id") Integer id, RedirectAttributes ra){
        try {
            service.deleteBrand(id);
            String message = "Danh mục ID " + id + " đã bị xóa";
            String imageDir = "../Brand-images/" + id;
            FileUploadUtil.removeDir(imageDir);
            ra.addFlashAttribute("message", message);
        } catch (BrandNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/brands";
    }
    @GetMapping("/brands/update/{id}")
    public String newBrand(Model model, @PathVariable("id") Integer id, RedirectAttributes ra){
        try{
            Brand brand = service.getBrand(id);
            model.addAttribute("brand",brand);
            model.addAttribute("PageTitle", "Sửa nhãn hàng (ID: " + id + " )");
            model.addAttribute("listCategories",categoryService.listAll());
            return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/brands";
        }
    }


}
