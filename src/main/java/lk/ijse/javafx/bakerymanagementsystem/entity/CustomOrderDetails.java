package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomOrderDetails {
    public String productId;
    public String productName;
    public String productPrice;
    public String productStockQuantity;
    public String supplierId;
}
