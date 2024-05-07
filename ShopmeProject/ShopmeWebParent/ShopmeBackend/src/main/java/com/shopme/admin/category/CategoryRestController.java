package com.shopme.admin.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
    private CategoryService service;
    @Autowired
    public CategoryRestController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/categories/check_duplicate")
    public String checkDuplicateEmail(@Param("name") String name,@Param("id") Integer id,@Param("alias") String alias){
        System.out.println(id + " - "+name +" - " + alias);
        return service.checkDuplicate(id, name,alias);
    }
}
