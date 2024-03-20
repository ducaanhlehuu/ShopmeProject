package com.shopme.admin.user;

import com.shopme.admin.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<User,Integer>,CrudRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.Email = :email")
    public User getUserByEmail(@Param("email") String email);
    @Query("UPDATE User u SET u.Enabled=?2 Where u.Id =?1")
    @Modifying
    public void updateEnabledStatus(Integer id,boolean enabled);
}
