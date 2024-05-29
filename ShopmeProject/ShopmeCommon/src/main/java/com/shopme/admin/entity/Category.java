package com.shopme.admin.entity;

import jakarta.persistence.*;
import org.springframework.core.annotation.Order;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false,unique = true)
    private String name;
    @Column(length = 64, nullable = false,unique = true)
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;
    @Column(length = 5)
    private boolean enabled;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    @Column(name = "all_parent_ids")
    private String allParentIDs;

    public String getAllParentIDs() {
        return allParentIDs;
    }

    public void setAllParentIDs(String allParentIDs) {
        this.allParentIDs = allParentIDs;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "abc.png";
    }

    public Category(String name, String alias, String image, boolean enabled, Category parent, Set<Category> children) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.enabled = enabled;
        this.parent = parent;
        this.children = children;
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Category(String name, Category parent) {
        this(name);
        this.parent = parent;
    }

    public Category() {
    }
    public static Category copyFull(Category category){
        Category copy = new Category();
        copy.setId(category.getId());
        copy.setName(category.getName());
        copy.setAlias(category.getAlias());
        copy.setImage(category.getImage());
        copy.setEnabled(category.isEnabled());
        copy.setHasChildren(category.getChildren().size()>0);
        return copy;
    }
    public static Category copyFull(Category category,String name){
        Category copy = new Category();
        copy.setId(category.getId());
        copy.setName(name);
        copy.setAlias(category.getAlias());
        copy.setImage(category.getImage());
        copy.setEnabled(category.isEnabled());
        copy.setHasChildren(category.getChildren().size()>0);
        return copy;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }
    @Transient
    public String getImagePath(){
        return "/category-images/"+this.id+"/"+this.image;
    }

    @Transient
    private boolean hasChildren;

    public void setHasChildren(boolean hasChildren){
        this.hasChildren = hasChildren;
    }

    public boolean isHasChildren(){
        return this.hasChildren;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", image='" + image + '\'' +
                ", enabled=" + enabled +
                ", hasChildren=" + hasChildren +
                '}';
    }
}

