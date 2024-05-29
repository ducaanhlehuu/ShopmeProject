package com.shopme.product;

import com.shopme.admin.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product,Integer> {
    @Query("SELECT p from Product p where p.enabled = true and( p.category.id = ?1 or p.category.allParentIDs like %?2% ) order by p.name asc")
    public Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    public Product findByAlias(String alias);

    @Query(value = "select * from products where products.enabled = true and " +
            "Match(name, short_description, full_description) against (?1)",nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);
}
