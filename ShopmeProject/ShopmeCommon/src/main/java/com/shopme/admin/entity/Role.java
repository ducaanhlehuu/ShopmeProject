package com.shopme.admin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "name",length = 40, nullable = false,unique = true)
    private String Name;
    @Column(name = "description",length = 150,nullable = false)
    private String Description;

    public Role() {
    }

    public Role(Integer id, String name, String description) {
        Id = id;
        Name = name;
        Description = description;
    }

    public Role(String name, String description) {
        Name = name;
        Description = description;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return this.Name;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
