package com.shopme.admin.category;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public String getAllCategory(Model model,@Param("keyword") String keyword){
        return listByPage(model,1,keyword==null?null:keyword);
    }
    @GetMapping("/categories/new")
    public String newCategory(Model model){
        model.addAttribute("category",new Category());
        model.addAttribute("listCategories",categoryService.listAll());
        model.addAttribute("PageTitle", "Tạo danh mục mới");
        return "category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@RequestParam("fileImage") MultipartFile imageFile, Category category,
                               RedirectAttributes redirectAttributes) throws IOException {
        boolean isNew = category.getId() ==null;
        if(category.getParent()!=null && category.getId()!=null && category.getId()==category.getParent().getId()){
            redirectAttributes.addFlashAttribute("message","Không thể chọn danh mục cha là chính mình");
            return "redirect:/categories";

        }

        if(!imageFile.isEmpty()){
            System.out.println(category.toString());
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory = categoryService.save(category);
            String uploadDir = "../category-images/" + savedCategory.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,imageFile);
        }
        else{
            if(category.getImage().isEmpty())
                category.setImage("abc.png");
            categoryService.save(category);
        }
        if(isNew) {
            redirectAttributes.addFlashAttribute("message","Tạo mới danh mục thành công");
        }
        else {
            redirectAttributes.addFlashAttribute("message","Đã sửa danh mục ID: " + category.getId() );
        }
        return "redirect:/categories?keyword="+category.getAlias();
    }

    @GetMapping("/categories/update/{id}")
    public String newCategory(Model model, @PathVariable("id") Integer id, RedirectAttributes ra){
        try{
            Category category = categoryService.getCategory(id);
            List<Category> listCategories = categoryService.listAll();
            listCategories.remove(category);
            model.addAttribute("listCategories",listCategories);
            model.addAttribute("category",category);
            model.addAttribute("PageTitle", "Sửa danh mục (ID: " + id + " )");
            return "category/category_form";
        } catch (CategoryNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/{id}/update_status/{enabled}")
    public String updateStatus(@PathVariable("id") Integer id,
                               @PathVariable("enabled") boolean status,RedirectAttributes ra){
        categoryService.updateStatus(status, id);
        String message = "Danh mục ID " + id + (status? " đã được kích hoạt": " đã bị khóa");
        ra.addFlashAttribute("message",message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String updateStatus(@PathVariable("id") Integer id, RedirectAttributes ra){
        try {
            categoryService.deleteCategory(id);
            String message = "Danh mục ID " + id + " đã bị xóa";
            String imageDir = "../category-images/" + id;
            FileUploadUtil.removeDir(imageDir);
            ra.addFlashAttribute("message",message);
        } catch (CategoryNotFoundException e) {
            ra.addFlashAttribute("message",e.getMessage());
        }
        return "redirect:/categories";
    }
    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(Model model, @PathVariable("pageNum") Integer pageNum,@Param("keyword") String keyword){
        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> categories = categoryService.listByPage(pageNum,pageInfo,keyword);
        model.addAttribute("categories",categories);

        model.addAttribute("totalPages",pageInfo.getTotalPages());
        model.addAttribute("totalElements",pageInfo.getTotalElements());
        model.addAttribute("numberOfElements",pageInfo.getNumberOfElements());
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("keyword",keyword);


        long startCount = (pageNum - 1) * categoryService.CATEGORY_PER_PAGE + 1;
        long endCount = Math.min(startCount + categoryService.CATEGORY_PER_PAGE - 1, pageInfo.getTotalElements());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);

        return "category/category";
    }
}
