package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
@Setter
public class OrderDetailsTM {
    private String orderId;
    private String productId;
    private int quantity;
    private double price;
}
