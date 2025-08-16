package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.AttendanceDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Attendance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceDAOImpl implements AttendanceDAO {

    @Override
    public List<Attendance> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Attendance");
        List<Attendance> list = new ArrayList<>();
        while(resultSet.next()){
            Attendance attendance = new Attendance(
                    resultSet.getString("attendance_id"),
                    resultSet.getString("employee_id"),
                    resultSet.getString("in_time"),
                    resultSet.getString("out_time"),
                    resultSet.getString("date")
            );
            list.add(attendance);
        }
       return  list;
    }

    @Override
    public Optional<String> getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT attendance_id FROM attendance ORDER BY attendance_id DESC LIMIT 1");
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString(1));
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Attendance attendance) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Attendance(attendance_id, employee_id, in_time, out_time, date) VALUES (?,?,?,?,?);";
        return SQLUtil.execute(sql,attendance.getAttendanceId(),attendance.getEmployeeId(),attendance.getInTime(),attendance.getOutTime(),attendance.getDate());

    }

    @Override
    public boolean update(Attendance attendance) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Attendance SET employee_id = ?,in_time = ?,out_time = ?,date = ? WHERE attendance_id = ?";
        return  SQLUtil.execute(sql,attendance.getEmployeeId(),attendance.getInTime(),attendance.getOutTime(),attendance.getDate(),attendance.getAttendanceId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Attendance WHERE attendance_id = ?";
        return SQLUtil.execute(sql,id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Attendance> findById(String id) {
        return null;
    }

    @Override
    public List<String> getAllEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute(
                "select employee_id from Employee"
        );

        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
}
