package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Payment {
    private String paymentId;
    private double amount;
    private String paymentMethod;
    private String paymentDate;
    private String orderId;

    public Payment(String paymentId, double amount, String paymentMethod, LocalDateTime paymentDate, String orderId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate.toString();
        this.orderId = orderId;
    }
}