package com.shopme.category;

import com.shopme.admin.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired private CategoryService categoryService;
    @GetMapping("/c/{category_alias}")
    public String viewCategoryPage(@PathVariable("category_alias") String alias, Model model){
        Category category = categoryService.findCategoryByAlias(alias);
        if(category==null){
            return "error/404";
        }
        List<Category> categoryParents = categoryService.getCategoryParent(category);

        model.addAttribute("pageTitle",category.getName());
        model.addAttribute("listCategoryParents", categoryParents);

        return "product_by_category";
    }
}
