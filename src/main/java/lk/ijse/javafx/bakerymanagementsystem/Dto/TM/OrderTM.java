package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDetailsDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class OrderTM {
    private String orderId;
    private String customerId;
    private LocalDateTime orderDate;
    private String paymentStatus;
    private ArrayList<OrderDetailsDto> cartList;
}
