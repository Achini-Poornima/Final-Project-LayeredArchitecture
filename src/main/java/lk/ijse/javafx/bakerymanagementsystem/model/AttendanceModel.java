package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AttendanceModel {
    public ArrayList<AttendanceDto> getAllAttendance() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Attendance");
        ArrayList<AttendanceDto> attendanceDtos = new ArrayList<>();
        while (resultSet.next()){
            attendanceDtos.add(new AttendanceDto(
                    resultSet.getString("attendance_id"),
                    resultSet.getString("employee_id"),
                    resultSet.getString("in_time"),
                    resultSet.getString("out_time"),
                    resultSet.getString("date")
            ));
        }
        return attendanceDtos;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT attendance_id FROM Attendance ORDER BY attendance_id DESC LIMIT 1");
        char tableChar = 'A';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean updateAttendance(AttendanceDto attendanceDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Attendance SET employee_id = ?,in_time = ?,out_time = ?,date = ? WHERE attendance_id = ?";
        return  SQLUtil.execute(sql,attendanceDto.getEmployeeId(),attendanceDto.getInTime(),attendanceDto.getOutTime(),attendanceDto.getDate(),attendanceDto.getAttendanceId());
    }

    public boolean saveAttendance(AttendanceDto attendanceDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Attendance(attendance_id, employee_id, in_time, out_time, date) VALUES (?,?,?,?,?);";
        return SQLUtil.execute(sql,attendanceDto.getAttendanceId(),attendanceDto.getEmployeeId(),attendanceDto.getInTime(),attendanceDto.getOutTime(),attendanceDto.getDate());
    }

    public boolean deleteAttendance(String attendanceId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Attendance WHERE attendance_id = ?";
        return SQLUtil.execute(sql,attendanceId);
    }
}
