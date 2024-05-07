package com.shopme.admin.brand;

import com.shopme.admin.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class BrandService {
    public static int BRAND_PER_PAGE = 4;
    @Autowired
    private BrandRepository repo;

    public List<Brand> listAllBrand(){
        return (List<Brand>) repo.findAll();
    }
    public List<Brand> listAllNameID(){
        return repo.listAllIdName();
    }

    public Brand save(Brand brand) {
        return repo.save(brand);
    }

    public String checkDuplicate(Integer id, String name) {
        boolean isCreateNew = (id==null) || (id==-1);

        Brand brandByName = repo.findBrandByName(name);
        if(isCreateNew){
            if(brandByName!=null){
                return "Duplicate";
            }
        }
        else {
            if(brandByName!=null && id!= brandByName.getId()){
                return "Duplicate";
            }
        }
        return "OK";
    }
    public Brand getBrand(Integer id) throws BrandNotFoundException {
        try{
            return repo.findById(id).get();
        }
        catch (Exception e){
            throw new BrandNotFoundException("Can not find brand with id: ");
        }
    }
    public void deleteBrand(Integer id) throws BrandNotFoundException {
        Long countById = repo.countById(id);
        if(countById==0 || countById==null){
            throw new BrandNotFoundException("Could not find any Brand with Id: " + id);
        }
        repo.deleteById(id);
    }
}
