package com.shopme.admin.category;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateNewCategory(){
        Category category = new Category("Electronics");
        Category saved = repo.save(category);
        System.out.println(saved);
        assertThat(saved.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateSubCategory(){
        Category parent = new Category(2);
        Category subCate = new Category("pc gaming",parent);
        Category saved = repo.save(subCate);
        assertThat(saved.getId()).isGreaterThan(0);
    }
    @Test
    public void testGetCate(){
        Category category = repo.findById(2).get();
        Set<Category> children = category.getChildren();

        for(Category c : children){
            System.out.println(c.getName() );
        }
    }

    @Test void testPrintSameLevel(){
        Iterable<Category> categories = repo.findAll();

        for (Category c : categories) {
            if (c.getParent() == null) {
                System.out.println(c.getName());

                Set<Category> children = c.getChildren();

                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }
    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            for (int i = 0; i < newSubLevel; i++) {
                System.out.print("--");
            }

            System.out.println(subCategory.getName());

            printChildren(subCategory, newSubLevel);
        }
    }

}
