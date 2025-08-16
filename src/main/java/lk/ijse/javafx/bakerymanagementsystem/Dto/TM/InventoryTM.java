package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryTM {
    private String inventoryId;
    private int stockQuantity;
    private String productId;
    private String ingredientId;
}
