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
        property = "id")
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String note;
    private String imageUrl;
    private Boolean isDeleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @OneToMany(mappedBy = "template")
    private List<Item> items = new ArrayList<Item>();
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Template(String name, String note, String imageUrl, Category category, User user){
        this.name = name;
        this.note = note;
        this.imageUrl = imageUrl;
        this.category = category;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", note='" + note + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                ", items=" + items +
                '}';
    }
}

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class Template {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(name = "name", unique = true)
//    private String name;
//
//    private String note;
//
//    private String imageUrl;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Category category;
//
//    @OneToMany(mappedBy = "template")
//    private List<Item> items = new ArrayList<Item>();
//
//    @Override
//    public String toString() {
//        return "Template{" +
//                "name='" + name + '\'' +
//                ", note='" + note + '\'' +
//                ", imageUrl='" + imageUrl + '\'' +
//                ", category=" + category +
//                ", items=" + items +
//                '}';
//    }
//}
