package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

@Data
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private String attendanceId;
    private String employeeId;
    private String inTime;
    private String outTime;
    private String date;

}


