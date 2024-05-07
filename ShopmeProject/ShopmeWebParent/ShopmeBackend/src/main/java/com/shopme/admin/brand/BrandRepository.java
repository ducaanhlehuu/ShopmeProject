package com.shopme.admin.brand;

import com.shopme.admin.entity.Brand;

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
public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer>, CrudRepository<Brand,Integer> {
    public Brand findBrandByName(String name);

    public Long countById(Integer id);
    @Query("SELECT NEW Brand(b.id,b.name) from Brand b order by b.name asc")
    public List<Brand> listAllIdName();
}
