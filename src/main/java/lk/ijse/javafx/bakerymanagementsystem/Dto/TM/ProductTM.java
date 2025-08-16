package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductTM {
    private String productId;
    private String name;
    private double price;
    private int stockQuantity;
    private String supplierId;
}
