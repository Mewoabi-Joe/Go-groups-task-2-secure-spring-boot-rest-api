package com.example.test1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Item> items = new ArrayList<Item>();
    @OneToMany(mappedBy = "user")
    private List<Template> templates = new ArrayList<Template>();
    @OneToMany(mappedBy = "user")
    private List<com.example.test1.models.List> lists = new ArrayList<com.example.test1.models.List>();
    @OneToMany(mappedBy = "user")
    private List<Category> categories = new ArrayList<Category>();

    public User(String name, String email, String password){
        this.username = name;
        this.email = email;
        this.password = password;
    }
}
