package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    private String attendanceId;
    private String employeeId;
    private String inTime;
    private String outTime;
    private String date;

}


