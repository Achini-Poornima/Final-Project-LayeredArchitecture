package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentTM {
    private String paymentId;
    private double amount;
    private String paymentMethod;
    private String paymentDate;
    private String orderId;
}
