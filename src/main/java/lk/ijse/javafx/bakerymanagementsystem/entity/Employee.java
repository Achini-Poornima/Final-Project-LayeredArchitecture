package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Employee {
    private String employeeId;
    private String name;
    private String contactNo;
    private String email;
    private String address;
    private LocalDate joinDate;
    private LocalDate dateOfBirth;
    private String role;


}