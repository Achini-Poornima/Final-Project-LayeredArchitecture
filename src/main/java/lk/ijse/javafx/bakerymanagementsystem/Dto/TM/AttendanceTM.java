package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AttendanceTM {
    private String id;
    private String employeeId;
    private String inTime;
    private String outTime;
    private String date;

    public String getAttendanceId() {
        return id;
    }
}
