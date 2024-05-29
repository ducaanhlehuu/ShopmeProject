package com.shopme.product;

import com.shopme.admin.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository  repo;
    public static final Integer PRODUCTS_PER_PAGE = 5;
    public static final Integer SEARCH_RESULTS_PER_PAGE = 6;

    public Page<Product> listByPage(Integer pageNum, Integer categoryId){
        String categoryIdMatch = "-" + categoryId +"-";
        Pageable pageable = PageRequest.of(pageNum,PRODUCTS_PER_PAGE);
        return repo.listByCategory(categoryId,categoryIdMatch,pageable);
    }

    public Product get(String alias) throws ProductNotFoundException {
        try {
            return repo.findByAlias(alias);
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }
    }

    public Page<Product> search(String keyword, int pageNum){
        Pageable pageable = PageRequest.of(pageNum-1,SEARCH_RESULTS_PER_PAGE);
        return repo.search(keyword,pageable);
    }

}
