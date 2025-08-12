package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class OrderDto {
    private String orderId;
    private String customerId;
    private LocalDateTime orderDate;
    private String paymentStatus;
    private ArrayList<OrderDetailsDto> cartList;
}