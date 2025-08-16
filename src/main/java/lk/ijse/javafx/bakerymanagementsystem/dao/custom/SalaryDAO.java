package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Salary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface SalaryDAO extends CrudDAO<Salary> {
    List<String> getAllEmployeeIds() throws SQLException, ClassNotFoundException;
    List<SalaryDto> getAllSalary() throws SQLException, ClassNotFoundException;
}
