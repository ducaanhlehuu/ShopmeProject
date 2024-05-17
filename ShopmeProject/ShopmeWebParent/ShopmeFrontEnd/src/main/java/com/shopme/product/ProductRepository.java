package com.shopme.category;

import com.shopme.admin.entity.Category;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Query("SELECT c from Category c where c.enabled = true order by c.name asc")
    public List<Category> findAllCategoryEnabled();

    @Query("SELECT c from Category c where c.enabled =true and c.alias=?1 ")
    public Category findByAliasEnabled(String alias);
}
