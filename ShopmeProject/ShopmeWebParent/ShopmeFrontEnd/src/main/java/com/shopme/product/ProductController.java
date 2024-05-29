package com.shopme.product;

import com.shopme.admin.entity.Category;
import com.shopme.admin.entity.product.Product;
import com.shopme.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired private CategoryService categoryService;
    @Autowired private ProductService productService;
    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model){
        return viewCategoryPage(alias,1,model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryPage(@PathVariable("category_alias") String alias,@PathVariable("pageNum") Integer pageNum, Model model){
        Category category = categoryService.findCategoryByAlias(alias);

        if(category==null){
            return "error/404";
        }

        List<Category> categoryParents = categoryService.getCategoryParent(category);

        Page<Product> pageProducts = productService.listByPage(pageNum - 1,category.getId());
        List<Product> listProducts = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("numberOfElements", pageProducts.getNumberOfElements());


        model.addAttribute("pageTitle",category.getName());
        model.addAttribute("listCategoryParents", categoryParents);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("category", category);

        return "products/product_by_category";
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model){
        try{
            Product product = productService.get(alias);
            List<Category> categoryParents = categoryService.getCategoryParent(product.getCategory());
            model.addAttribute("listCategoryParents", categoryParents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle",product.getName());
            return "products/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword, @PathVariable("pageNum") Integer pageNum, Model model) {
        Page<Product> productPage = productService.search(keyword,pageNum);

        long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if (endCount > productPage.getTotalElements()) {
            endCount = productPage.getTotalElements();
        }

        model.addAttribute("keyword",keyword);
        model.addAttribute("listProducts",productPage.getContent());
        model.addAttribute("pageTitle", keyword + " - Kết quả");

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("numberOfElements", productPage.getNumberOfElements());
        return "products/product_search";
    }

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword,Model model) {
        return searchByPage(keyword,1,model);
    }

}
