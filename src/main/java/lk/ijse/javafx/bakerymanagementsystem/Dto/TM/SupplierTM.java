package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierTM {
    private String supplierId;
    private String name;
    private String suppliedIngredient;
    private String address;
    private String email;
}
