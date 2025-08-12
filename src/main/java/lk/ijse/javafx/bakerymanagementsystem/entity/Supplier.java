package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Supplier {
    private String supplierId;
    private String name;
    private String suppliedIngredient;
    private String address;
    private String email;
}
