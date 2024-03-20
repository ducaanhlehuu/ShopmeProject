package com.shopme.admin.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "email",length = 128, nullable = false,unique = true)
    private String Email;
    @Column(name ="enabled",length = 4,nullable = false)
    private boolean Enabled;
    @Column(name = "first_name",length = 45,nullable = false)
    private String FirstName;
    @Column(name = "last_name",length = 45,nullable = false)
    private String LastName;
    @Column(name = "password",length = 64, nullable = false)
    private String Password;
    @Column(name = "photos",length = 64)
    private String Photos;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

    public User(String email, boolean enabled, String firstName, String lastName, String password, String photos, Set<Role> roles) {
        Email = email;
        Enabled = enabled;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        Photos = photos;
        this.roles = roles;
    }

    public User() {
    }

    public Integer getId() {
        return Id;
    }

    public User(String email, String firstName, String lastName, String password) {
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", Email='" + Email + '\'' +
                ", Enabled=" + Enabled +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Password='" + Password + '\'' +
                ", Photos='" + Photos + '\'' +
                ", roles=" + roles +
                '}';
    }
    @Transient
    public String getPhotosImagePath(){
        if(this.Id ==null || this.Photos==null)
            return "/images/default-user.png";
        return "/user-photos/" + this.Id +"/"+ this.Photos;
    }
}
