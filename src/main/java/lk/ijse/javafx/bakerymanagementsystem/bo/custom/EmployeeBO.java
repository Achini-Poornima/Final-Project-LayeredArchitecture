package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.EmployeeDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeBO extends SuperBO {
    List<EmployeeDto> getAllEmployee() throws SQLException, ClassNotFoundException;
    void saveEmployee(EmployeeDto dto) throws DuplicateException, Exception;

    void updateEmployee(EmployeeDto dto) throws SQLException, ClassNotFoundException;

    boolean deleteEmployee(String id) throws InUseException, Exception;

    String getNextId() throws SQLException, ClassNotFoundException;
}
