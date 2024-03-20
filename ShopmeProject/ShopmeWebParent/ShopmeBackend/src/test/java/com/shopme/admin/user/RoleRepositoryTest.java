package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import com.shopme.admin.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
    private RoleRepository roleRepository;
    @Autowired
    public RoleRepositoryTest(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Test
    public void testCreateFirstRole(){
        Role roleUser = new Role("User","basic user, only can view");
        Role savedRole = roleRepository.save(roleUser);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateRoles(){
        Role SalePerson = new Role("Salesperson",
                "Manage products, customers, shipping,orders and sales reports");
        Role Editor = new Role("Editor",
                "Manage categories, brands, products, articles and menus");
        Role Shipper = new Role("Shipper",
                "View orders, products, and update orders status");
        Role Admin = new Role("Admin",
                "Manage everything");
        Role Assistant = new Role("Assistant",
                "Manage questions and reviews");
        roleRepository.saveAll(List.of(Admin,Assistant,Shipper,Editor,SalePerson));
    }
}
