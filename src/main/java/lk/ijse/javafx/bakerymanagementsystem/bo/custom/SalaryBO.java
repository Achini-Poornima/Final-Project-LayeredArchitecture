package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface SalaryBO extends SuperBO {
    List<SalaryDto> getAllSalary() throws SQLException, ClassNotFoundException;
    List<SalaryDto> saveSalary(SalaryDto dto) throws SQLException, ClassNotFoundException;
    List<SalaryDto> updateSalary(SalaryDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteSalary(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
