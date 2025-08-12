package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

@AllArgsConstructor
@Setter
@ToString
@Getter
@NoArgsConstructor
public class SalaryDto {
    private String salaryId;
    private double basicSalary;
    private double bonus;
    private double netSalary;
    private String paymentDate;
    private String employeeId;
}
