package com.example.test1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleItemResponse {
    private Long   item_id;
    private String name;
    private int quantity;
    private String state;

    @Override
    public String toString() {
        return "SimpleItemResponse{" +
                "item_id=" + item_id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

