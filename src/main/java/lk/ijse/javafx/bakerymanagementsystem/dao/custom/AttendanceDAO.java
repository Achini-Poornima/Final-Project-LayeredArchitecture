package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Attendance;

import java.sql.SQLException;
import java.util.List;

public interface AttendanceDAO extends CrudDAO<Attendance> {
    List<String> getAllEmployeeIds() throws SQLException, ClassNotFoundException;
}
