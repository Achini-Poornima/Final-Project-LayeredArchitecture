package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
public class OrderDetails {
    private String orderId;
    private String productId;
    private int quantity;
    private double price;
}