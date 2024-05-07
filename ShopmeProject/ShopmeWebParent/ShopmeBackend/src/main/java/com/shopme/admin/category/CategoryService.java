package com.shopme.admin.category;

import com.shopme.admin.entity.Category;
import com.shopme.admin.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CategoryService {
    public static int CATEGORY_PER_PAGE = 4;
    @Autowired
    private CategoryRepository repo;
    public Category save(Category category){
        Category parent = category.getParent();
        if(parent!=null){
            String allParentIds = parent.getAllParentIDs() == null ? "-": parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            category.setAllParentIDs(allParentIds);
        }
        return repo.save(category);
    }
    public List<Category> listAll() {
        List<Category> listRootCategories = repo.listRootCategories(Sort.by("name").ascending());
        return listHierarchicalCategories(listRootCategories);
    }
    public List<Category> listByPage(Integer pageNum,CategoryPageInfo pageInfo,String keyword) {
        Pageable pageable = PageRequest.of(pageNum-1,CATEGORY_PER_PAGE,Sort.by("name").ascending());
        Page<Category> pageRootCategories = null;

        if(keyword!=null && !keyword.isEmpty()){
            pageRootCategories = repo.searchCategoriesByKeyword(pageable,keyword);
        } else {
            pageRootCategories = repo.listRootCategories(pageable);
        }
        List<Category> listRootCategories = pageRootCategories.getContent();
        pageInfo.setTotalPages(pageRootCategories.getTotalPages());
        pageInfo.setNumberOfElements(pageRootCategories.getNumberOfElements());
        pageInfo.setTotalElements(pageRootCategories.getTotalElements());
        return listHierarchicalCategories(listRootCategories);
    }
    public Category getCategory(Integer id) throws CategoryNotFoundException {
        try{
            return repo.findById(id).get();
        }
        catch (Exception e){
            throw new CategoryNotFoundException("Can not find category with id: ");
        }
    }

    public List<Category> listHierarchicalCategories(List<Category> listRootCategories){
        List<Category> hierarchicalCategories = new ArrayList<Category>();

        for (Category rootCate : listRootCategories){
            hierarchicalCategories.add(Category.copyFull(rootCate));
            Set<Category> children = rootCate.getChildren();

            for (Category subCategory : children){
                String name = "--" + subCategory.getName();
                hierarchicalCategories.add(Category.copyFull(subCategory,name));
                listSubHierarchicalCategories(hierarchicalCategories,1,subCategory);
            }
        }
        return hierarchicalCategories;
    }
    public void listSubHierarchicalCategories(List<Category> hierarchicalCategories, int subLevel, Category parent){
        Set<Category> children = parent.getChildren();
        int newSubLevel = subLevel + 1;
        for(Category child : children){
            String name = "";
            for(int i =0;i< newSubLevel;i++){
                name +="--";
            }
            name += child.getName();
            hierarchicalCategories.add(Category.copyFull(child,name));
            listSubHierarchicalCategories(hierarchicalCategories,newSubLevel,child);
        }
    }

    public String checkDuplicate(Integer id, String name, String alias){
        boolean isCreateNew = (id==null) || (id==-1);

        Category categoryByName = repo.findByName(name);
        if(isCreateNew) {
            if(categoryByName!=null){
                return "DuplicateName";
            }
            else{
                Category categoryByAlias = repo.findByAlias(alias);
                if(categoryByAlias!=null){
                    return "DuplicateAlias";
                }
            }
        }
        else{
            if(categoryByName!=null && categoryByName.getId()!=id){
                return "DuplicateName";
            }
            Category categoryByAlias = repo.findByAlias(alias);
            if(categoryByAlias!=null && categoryByAlias.getId()!=id){
                return "DuplicateAlias";
            }
        }
        return "OK";
    }

    public void updateStatus(boolean status, Integer id){
        repo.updateEnabledStatus(status,id);
    }
    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        Long countById = repo.countById(id);
        if(countById==0 || countById==null){
            throw new CategoryNotFoundException("Could not find any category with Id: " + id);
        }
        repo.deleteById(id);
    }

}
