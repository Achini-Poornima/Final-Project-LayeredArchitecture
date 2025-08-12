package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Inventory {
    private String inventoryId;
    private int stockQuantity;
    private String productId;
    private String ingredientId;

}
