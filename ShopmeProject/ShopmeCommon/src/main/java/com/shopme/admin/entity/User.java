package com.shopme.admin.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "email",length = 128, nullable = false,unique = true)
    private String email;
    @Column(name ="enabled",length = 4,nullable = false)
    private boolean enabled;
    @Column(name = "first_name",length = 45,nullable = false)
    private String firstName;
    @Column(name = "last_name",length = 45,nullable = false)
    private String lastName;
    @Column(name = "password",length = 64, nullable = false)
    private String password;
    @Column(name = "photos",length = 64)
    private String photos;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

    public User(Integer id, String email, boolean enabled, String firstName, String lastName, String password, String photos, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.enabled = enabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photos = photos;
        this.roles = roles;
    }

    public User() {
    }

    public User(String email, boolean enabled, String firstName, String lastName, String password, String photos, Set<Role> roles) {
        this.email = email;
        this.enabled = enabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.photos = photos;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Transient
    public String getPhotosImagePath(){
        if(this.id ==null || this.photos==null)
            return "/images/default-user.png";
        return "/user-photos/" + this.id +"/"+ this.photos;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", photos='" + photos + '\'' +
                ", roles=" + roles +
                '}';
    }
}
