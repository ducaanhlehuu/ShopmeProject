package com.shopme.admin.product;

import com.shopme.admin.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService {
    public static final int PRODUCTS_PER_PAGE = 5;

    @Autowired
    private ProductRepository repo;

    public List<Product> listAll() {
        return (List<Product>) repo.findAll();
    }

    public Page<Product> listByPage(Integer pageNum, String keyword,Integer categoryId) {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE,sort);

        if(keyword!=null && !keyword.isEmpty()){
            if(categoryId!=null && categoryId >0){
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return repo.SearchInCategory(categoryId,categoryIdMatch,keyword,pageable);
            }
            return repo.findAll(keyword,pageable);
        }
        if(categoryId!=null && categoryId >0){
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return repo.findAllInCategory(categoryId,categoryIdMatch,pageable);
        }
        return repo.findAll(pageable);
    }

    public Product save(Product product){
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() ==null || product.getAlias().isEmpty()){
            product.setAlias(product.getName().toLowerCase().replace(" ","_"));
        } else {
            product.setAlias(product.getAlias().replace(" ","_"));
        }
        product.setUpdateTime(new Date());
        return repo.save(product);
    }

    public void updateStatus(boolean status, Integer id) {
        repo.updateEnabledStatus(id,status);
    }
    public void deleteProduct(Integer id) throws ProductNotFoundException {
        Long countById = repo.countById(id);
        if(countById==0 || countById==null){
            throw new ProductNotFoundException("Could not find any Product with Id: " + id);
        }
        repo.deleteById(id);
    }
    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return repo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
    }

}
