package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class SupplierDto {
    private String supplierId;
    private String name;
    private String suppliedIngredient;
    private String address;
    private String email;
}
