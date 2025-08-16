package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface ExpensesBO extends SuperBO {
    List<ExpensesDto> getAllExpenses() ;
    void saveExpenses(ExpensesDto dto) ;

    void updateExpenses(ExpensesDto dto) ;

    boolean deleteExpenses(String id) ;

    String getNextId() throws SQLException, ClassNotFoundException;

}
