package com.shopme.admin.product;

import com.shopme.admin.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product,Integer>, CrudRepository<Product,Integer> {
    @Query("update Product p set p.enabled=?2 where p.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean status);

    Long countById(Integer id);

    @Query("SELECT p from Product p where p.name like %?1% or p.shortDescription like %?1%  or p.fullDescription like %?1% or p.brand.name like %?1% or p.category.name like %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Integer categoryId, String CategoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentIDs LIKE %?2%)"
            + " And (p.name like %?3% or p.shortDescription like %?3%  or p.fullDescription like %?3% or p.brand.name like %?3% or p.category.name like %?3%)")
    public Page<Product> SearchInCategory(Integer categoryId, String CategoryIdMatch,String keyword, Pageable pageable);

}