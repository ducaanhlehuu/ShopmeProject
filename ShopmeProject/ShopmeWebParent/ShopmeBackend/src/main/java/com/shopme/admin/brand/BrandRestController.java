package com.shopme.admin.brand;



import com.shopme.admin.entity.Brand;
import com.shopme.admin.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {
    @Autowired
    private BrandService service;

    @PostMapping("/brands/check_duplicate")
    public String checkDuplicateEmail(@Param("name") String name,@Param("id") Integer id){

        return service.checkDuplicate(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name="id") Integer id) throws BrandNotFoundRestException {

        List<CategoryDTO> categoryDTOS = new ArrayList<CategoryDTO>();
        try{
            Brand brand = service.getBrand(id);
            Set<Category> categories = brand.getCategories();
            for(Category cat:categories){
                CategoryDTO dto = new CategoryDTO(cat.getId(),cat.getName());
                categoryDTOS.add(dto);
            }
            return categoryDTOS;
        } catch (BrandNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }
}
