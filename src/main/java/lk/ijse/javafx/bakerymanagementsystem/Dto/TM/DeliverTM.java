package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeliverTM {
    private String deliverId;
    private String deliverAddress;
    private double deliverCharge;
    private Date deliverDate;
    private String orderId;
}
