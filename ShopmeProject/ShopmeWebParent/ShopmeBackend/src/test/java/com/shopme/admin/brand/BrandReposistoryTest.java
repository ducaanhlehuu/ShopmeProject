package com.shopme.admin.brand;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.entity.Brand;
import com.shopme.admin.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashSet;

@DataJpaTest(showSql = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandReposistoryTest {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Test
    public void testCreateBrand(){
        Brand acer = new Brand("acer","abc.png");
        Category laptop = categoryRepository.findByName("Laptops");
        acer.addCategory(laptop);
        Brand saved = brandRepository.save(acer);
        assertThat(saved.getId()).isGreaterThan(0);
    }
}
