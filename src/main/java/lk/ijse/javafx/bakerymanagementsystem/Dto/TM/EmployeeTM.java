package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmployeeTM {
    private String employeeId;
    private String name;
    private String contactNo;
    private String email;
    private String address;
    private LocalDate joinDate;
    private LocalDate dateOfBirth;
    private String role;
}
