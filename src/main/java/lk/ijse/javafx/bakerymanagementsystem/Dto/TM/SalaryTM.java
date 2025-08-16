package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalaryTM {
    private String salaryId;
    private double basicSalary;
    private double bonus;
    private double netSalary;
    private String paymentDate;
    private String employeeId;
}
