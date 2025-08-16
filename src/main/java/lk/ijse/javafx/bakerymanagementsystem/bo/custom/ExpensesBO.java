package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface ExpensesBO extends SuperBO {
    List<ExpensesDto> getAllExpenses() throws SQLException, ClassNotFoundException;
    void saveExpenses(ExpensesDto dto) throws SQLException, ClassNotFoundException;

    void updateExpenses(ExpensesDto dto) throws SQLException, ClassNotFoundException;

    boolean deleteExpenses(String id) throws SQLException, ClassNotFoundException;

    String getNextId() throws SQLException, ClassNotFoundException;

}
