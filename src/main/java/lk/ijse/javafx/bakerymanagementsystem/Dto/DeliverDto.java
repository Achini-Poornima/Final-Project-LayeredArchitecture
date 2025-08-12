package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class DeliverDto {
    private String deliverId;
    private String deliverAddress;
    private double deliverCharge;
    private Date deliverDate;
    private String orderId;

    public DeliverDto(String deliverId, String deliverAddress, double deliverCharge,LocalDate deliverDate, String orderId) {
        this.deliverId = deliverId;
        this.deliverAddress = deliverAddress;
        this.deliverCharge = deliverCharge;
        this.deliverDate = Date.valueOf(deliverDate);
        this.orderId = orderId;
    }
}
