package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;

import java.sql.SQLException;
import java.util.List;

public interface AttendanceBO {
    List<AttendanceDto> getAllAttendance() throws SQLException, ClassNotFoundException;
    void saveAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException;
    void updateAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteAttendance(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
