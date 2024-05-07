package com.shopme.admin.category;

import com.shopme.admin.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CategoryServiceTest {

    @MockBean
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;
    @Test
    public void testCheckDuplicateNewModel() {
        Integer id = null;
        String name = "Electronics";
        String alias = "Electronics";

        Category category = new Category(id,name,alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);

        String result = service.checkDuplicate(id,name,alias);
        assertThat(result).isEqualTo("DuplicateName");

    }
    @Test
    public void testCheckDuplicateAlias() {
        Integer id = null;
        String name = "ics";
        String alias = "Electronics";

        Category category = new Category(id,name,alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.checkDuplicate(id,name,alias);
        assertThat(result).isEqualTo("DuplicateAlias");

    }
    @Test
    public void testCheckDuplicateAliasExistedModel() {
        Integer id = 1;
        String name = "ics";
        String alias = "phone";

        Category category = new Category(id,name,alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.checkDuplicate(id,name,alias);
        assertThat(result).isEqualTo("OK");

    }
}
