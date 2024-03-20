package com.shopme.admin.user;

import com.shopme.admin.entity.Role;
import com.shopme.admin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    public static int USER_PER_PAGE = 5;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public List<User> listAll(){
        return (List<User>) userRepository.findAll();
    }
    public Page<User> listPageUser(int pageNum){
        Pageable pageable = PageRequest.of(pageNum - 1,USER_PER_PAGE);
        return userRepository.findAll(pageable);
    }

    public User saveUser(User user){
        boolean isUpdatingUser = (user.getId() !=null);
        if(isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            if(user.getPassword().isEmpty() || user.getPassword().equals(existingUser.getPassword())){
                user.setPassword(existingUser.getPassword());
            }
            else{
                encodePassword(user);
            }
        }
        else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }
    public List<Role> listRoleAll(){
        return (List<Role>) roleRepository.findAll();
    }

    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepository.getUserByEmail(email);
        if (userByEmail==null)
            return true;
        // Not existed yet
        else{
            if(userByEmail.getId()!=id)
                return false;
            if(id==null){
                return false;
            }
            // Another existing user own the email
        }
        return true;
        // True while no change on email or no duplicated
    }
    public User getUser(Integer id) throws UserNotFoundException{
        try{
            return userRepository.findById(id).get();
        }
        catch(Exception ex){
            throw new UserNotFoundException("Could not find user with id: "+id);
        }
    }
    public void delete(Integer id) throws UserNotFoundException{
        boolean isExisting = userRepository.existsById(id);
        if(isExisting){
            userRepository.deleteById(id);
        }
        else {
            throw new UserNotFoundException("Could not find user with id: "+id);
        }
    }
    public void updateEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id,enabled);
    }
}
