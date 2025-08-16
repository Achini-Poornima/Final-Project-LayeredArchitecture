package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface UserBO extends SuperBO {
    List<UserDto> getAllUser() throws SQLException, ClassNotFoundException;
    void saveUser(UserDto dto) throws SQLException, ClassNotFoundException;
    void updateUser(UserDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteUser(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
