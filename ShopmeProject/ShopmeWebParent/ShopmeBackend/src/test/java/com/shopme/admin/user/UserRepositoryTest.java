package com.shopme.admin.user;

import com.shopme.admin.entity.Role;
import com.shopme.admin.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    private UserRepository userRepository;
    private TestEntityManager testEntityManager;
    @Autowired
    public UserRepositoryTest(UserRepository userRepository, TestEntityManager testEntityManager) {
        this.userRepository = userRepository;
        this.testEntityManager = testEntityManager;
    }

//    @Test
//    public void CreateUserTest(){
//        Role roleAdmin = testEntityManager.find(Role.class,3);
//        User userDucAnh = new User("lehuuducanh1@gmail.com","Duc Anh", "Le Huu","ducanh123");
//        userDucAnh.addRole(roleAdmin);
//        userDucAnh.setEnabled(false);
//        User saveduser  = userRepository.save(userDucAnh);
//        assertThat(saveduser.getId()).isGreaterThan(0);
//    }
//    @Test
//    public void testCreateNewUserWithTwoRole(){
//        Role roleAdmin = testEntityManager.find(Role.class,3);
//        Role roleEditor = testEntityManager.find(Role.class,6);
//        User userAdmin = new User("admin@gmail.com","Admin","Admin","123456");
//        userAdmin.addRole(roleEditor);
//        userAdmin.addRole(roleAdmin);
//        User saveduser  = userRepository.save(userAdmin);
//        assertThat(saveduser.getId()).isGreaterThan(0);
//    }
    @Test
    public void testFindUser(){
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user ->{System.out.println(user+"/n");});

    }
    @Test
    public void testGetUserByEmail(){
        User user = userRepository.getUserByEmail("lehuuducanh1@gmail.com");
        assertThat(user).isNotNull();

    }

    @Test
    public void testDisableUser(){
        Integer id = 9;
        userRepository.updateEnabledStatus(id,false);
    }
    @Test
    public void testListPage() {
        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> pages = page.stream().toList();
        pages.forEach(p->{
            System.out.println(p);
        });
    }
    @Test
    public void testListPageWithKeyWord() {
        int pageNumber = 0;
        int pageSize = 4;
        String keyword = "Lee";
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<User> page = userRepository.listAll(keyword,pageable);
        List<User> pages = page.stream().toList();
        pages.forEach(p->{
            System.out.println(p.toString());
        });
    }
}
