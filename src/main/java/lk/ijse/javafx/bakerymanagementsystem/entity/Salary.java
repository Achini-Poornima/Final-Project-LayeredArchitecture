package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@AllArgsConstructor
@Setter
@ToString
@Getter
@NoArgsConstructor
public class Salary {
    private String salaryId;
    private double basicSalary;
    private double bonus;
    private double netSalary;
    private String paymentDate;
    private String employeeId;
}
