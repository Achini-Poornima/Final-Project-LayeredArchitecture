package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class InventoryDto {
    private String inventoryId;
    private int stockQuantity;
    private String productId;
    private String ingredientId;

}
