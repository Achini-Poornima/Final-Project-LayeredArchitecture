package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IngredientDto {
    private String ingredientId;
    private String name;
    private String expireDate;
    private double quantityAvailable;
    private String supplierId;
}