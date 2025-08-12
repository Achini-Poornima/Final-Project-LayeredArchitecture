package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ingredient {
    private String ingredientId;
    private String name;
    private String expireDate;
    private double quantityAvailable;
    private String supplierId;
}