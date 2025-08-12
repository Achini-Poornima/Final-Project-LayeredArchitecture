package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class ProductDto {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String supplierId;
}