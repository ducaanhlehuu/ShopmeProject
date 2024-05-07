package com.shopme.admin.product;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.entity.*;
import com.shopme.admin.security.ShopmeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {
    private String RedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
    @Autowired
    private ProductService service;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/products")
    public String firstPage(Model model) {
        model.addAttribute("listProducts",service.listAll());
        return  listByPage(1,model,null,null);
    }
    @GetMapping("/products/new")
    public String newProductPage(Model model) {
        List<Brand> listBrands = brandService.listAllNameID();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("product", product);
        model.addAttribute("listBrands",listBrands);
        model.addAttribute("PageTitle","Create New Product");
        model.addAttribute("numberOfExtraImages", 0);

        return "products/product_form";
    }
    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("keyword") String keyword,@Param("categoryId") Integer categoryId
    ) {
        Page<Product> page = service.listByPage(pageNum, keyword,categoryId);
        List<Product> listProducts = page.getContent();
        List<Category> listCategories = categoryService.listAll();

        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        if(categoryId!=null && categoryId >=0){
            model.addAttribute("categoryId",categoryId);
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("numberOfElements",page.getNumberOfElements());
        return "products/product";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes ra,
                              @RequestParam("fileImage") MultipartFile mainImageFile,
                              @RequestParam("extraImage") MultipartFile[] extraImages,
                              @RequestParam(name = "detailNames",required = false) String[] detailNames,
                              @RequestParam(name = "detailValues",required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs",required = false) String[] imageIds,
                              @RequestParam(name = "imageNames",required = false) String[] imageNames) throws IOException {
        ra.addFlashAttribute("message","Lưu thành công sản phẩm mới: " + product.getShorterName());

        ProductSaveHelper.setProductDetails(product,detailNames,detailValues);
        ProductSaveHelper.setExistingImageName(product,imageIds,imageNames);
        ProductSaveHelper.setNewExtraImageNames(extraImages,product);


        if(!mainImageFile.isEmpty()){
            String fileName = StringUtils.cleanPath(mainImageFile.getOriginalFilename());
            product.setMainImage(fileName);
        }
        Product savedProduct = service.save(product);
        ProductSaveHelper.saveUploadedImages(mainImageFile,extraImages,savedProduct);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/update_status/{enabled}")
    public String updateStatus(@PathVariable("id") Integer id,
                               @PathVariable("enabled") boolean status,RedirectAttributes ra){
        service.updateStatus(status, id);
        String message = "Sản phẩm ID " + id + (status? " đã được kích hoạt": " đã bị khóa");
        ra.addFlashAttribute("message",message);
        return "redirect:/products";
    }
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id,RedirectAttributes ra) throws ProductNotFoundException {
        try{
            service.deleteProduct(id);
            String productImageDir = "../product-images/" + id ;
            FileUploadUtil.removeDir(productImageDir);
            ra.addFlashAttribute("message","Đã xóa Product với Id: " + id);
        }
        catch (Exception e){
            ra.addFlashAttribute("message","Lỗi: "+ e.getMessage());
        }
        return "redirect:/products";
    }
    @GetMapping("/products/update/{id}")
    public String editProduct(@PathVariable("id") Integer id, Model model,
                              RedirectAttributes ra, @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Product product = service.get(id);
            List<Brand> listBrands = brandService.listAllBrand();
            Integer numberOfExtraImages = product.getImages().size();
            boolean isReadOnlyForSalesperson = false;

//            if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
//                if (loggedUser.hasRole("Salesperson")) {
//                    isReadOnlyForSalesperson = true;
//                }
//            }

            model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
            model.addAttribute("product", product);
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("pageTitle", "Thay đổi thông tin sản phẩm (ID: " + id + ")");
            model.addAttribute("numberOfExtraImages", numberOfExtraImages);

            return "products/product_form";

        } catch (ProductNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());

            return "redirect:/products";
        }
    }
}
