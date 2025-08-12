package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDto {
    private String employeeId;
    private String name;
    private String contactNo;
    private String email;
    private String address;
    private LocalDate joinDate;
    private LocalDate dateOfBirth;
    private String role;


}