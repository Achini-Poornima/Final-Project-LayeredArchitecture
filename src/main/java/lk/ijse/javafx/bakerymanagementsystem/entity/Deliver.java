package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Deliver {
    private String deliverId;
    private String deliverAddress;
    private double deliverCharge;
    private Date deliverDate;
    private String orderId;

    public Deliver(String deliverId, String deliverAddress, double deliverCharge, LocalDate deliverDate, String orderId) {
        this.deliverId = deliverId;
        this.deliverAddress = deliverAddress;
        this.deliverCharge = deliverCharge;
        this.deliverDate = Date.valueOf(deliverDate);
        this.orderId = orderId;
    }
}
