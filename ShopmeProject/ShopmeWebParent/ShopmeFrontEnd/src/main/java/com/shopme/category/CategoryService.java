package com.shopme.category;

import com.shopme.admin.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listNoChildrenCategories(){
        final List<Category> listNoChildrenCategories = new ArrayList<Category>();
        List<Category> listAllEnabledCategories = repo.findAllCategoryEnabled();
        listAllEnabledCategories.forEach(category ->{
            Set<Category> children = category.getChildren();
            if(children==null || children.size()==0){
                listNoChildrenCategories.add(category);
            }
        });
        return listNoChildrenCategories;
    }

    public Category findCategoryByAlias(String alias){
        return repo.findByAliasEnabled(alias);
    }

    public List<Category> getCategoryParent(Category child){
        List<Category> parents = new ArrayList<>();
        Category parent = child.getParent();

        while(parent!=null){
            parents.add(0,parent);
            parent = parent.getParent();
        }
        parents.add(child);
        return parents;
    }
}
