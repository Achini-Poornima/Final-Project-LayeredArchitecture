package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class IngredientTM {
    private String ingredientId;
    private String name;
    private String expireDate;
    private double quantityAvailable;
    private String supplierId;
}
