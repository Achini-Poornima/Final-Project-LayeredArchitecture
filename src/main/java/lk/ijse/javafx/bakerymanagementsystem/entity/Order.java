package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Order {
    private String orderId;
    private String customerId;
    private LocalDateTime orderDate;
    private String paymentStatus;
    private ArrayList<OrderDetails> cartList;
}