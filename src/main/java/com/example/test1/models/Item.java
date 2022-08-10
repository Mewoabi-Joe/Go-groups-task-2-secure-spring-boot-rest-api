package com.example.test1.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity = 1;
    private String state = "normal";
    @ManyToOne(fetch = FetchType.LAZY)
    private Template template;
    @ManyToOne(fetch = FetchType.LAZY)
    private List list;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Item(Template template, List list, User user){
        this.template = template;
        this.list = list;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", template/item_name=" + template.getName() +
                ", category_name" + template.getCategory().getName() +
                ", list_name=" + list.getName() +
                '}';
    }
}
