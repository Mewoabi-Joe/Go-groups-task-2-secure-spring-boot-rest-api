package com.example.test1.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "name")
public class Category {
    @Id
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Template> templates = new ArrayList<Template>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Category(String name, User user){
this.name = name;
this.user = user;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", templates=" + templates +
                '}';
    }
}
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Category {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "name", unique = true)
//    private String name;
//
//    @OneToMany(mappedBy = "category")
//    private List<Template> templates = new ArrayList<Template>();
//
//    public Category(String name){
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "Category{" +
//                "name='" + name + '\'' +
//                ", templates=" + templates +
//                '}';
//    }
//}