package com.shopme.admin.category;

import com.shopme.admin.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category,Integer>, CrudRepository<Category,Integer> {
    @Query("Select c from Category c Where c.parent is NULL")
    public List<Category> listRootCategories(Sort sort);
    @Query("Select c from Category c Where c.parent is NULL")
    public Page<Category> listRootCategories(Pageable pageable);
    @Query("Select c from Category c Where c.name LIKE %?1% or c.alias like %?1%")
    public Page<Category> searchCategoriesByKeyword(Pageable pageable, String keyword);
    public Long countById(Integer id);
    public Category findByName(String name);

    public Category findByAlias(String name);

    @Query("update Category c Set c.enabled = ?1 where c.id = ?2")
    @Modifying
    public void updateEnabledStatus(boolean status, Integer id);
}
