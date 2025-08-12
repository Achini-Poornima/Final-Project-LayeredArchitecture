package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpensesModel {
    public static ArrayList<ExpensesDto> getAllExpenses() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Expenses");
        ArrayList<ExpensesDto> expensesList = new ArrayList<>();
        while (resultSet.next()) {
            expensesList.add(new ExpensesDto(
                    resultSet.getString("expenses_id"),
                    resultSet.getString("category"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("date")
            ));
        }
        return expensesList;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT expenses_id FROM Expenses ORDER BY expenses_id desc limit 1");
        char tableChar = 'E';
        if (resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean deleteExpenses(String expensesId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Expenses WHERE expenses_id = ?";
        return SQLUtil.execute(sql, expensesId);
    }

    public boolean saveExpenses(ExpensesDto expensesDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Expenses(expenses_id, category, amount, date) VALUES (?,?,?,?)";
        return SQLUtil.execute(sql, expensesDto.getExpensesId(), expensesDto.getCategory(), expensesDto.getAmount(), expensesDto.getDate());
    }

    public boolean updateExpenses(ExpensesDto expensesDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Expenses SET category = ?, amount = ?, date = ? WHERE expenses_id = ?";
        return SQLUtil.execute(sql,  expensesDto.getCategory(),expensesDto.getAmount(),expensesDto.getDate(), expensesDto.getExpensesId());
    }
}