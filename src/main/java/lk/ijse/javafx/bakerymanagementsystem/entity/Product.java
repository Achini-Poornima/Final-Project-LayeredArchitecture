package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class Product {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String supplierId;
}