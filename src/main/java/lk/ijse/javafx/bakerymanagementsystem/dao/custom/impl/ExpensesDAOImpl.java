package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ExpensesDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Expenses;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpensesDAOImpl implements ExpensesDAO {
    @Override
    public List<Expenses> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Expenses");
        List<Expenses> list = new ArrayList<>();
        while (resultSet.next()) {
            Expenses expenses = new Expenses(
                    resultSet.getString("expenses_id"),
                    resultSet.getString("category"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("date")
            );
            list.add(expenses);
        }
        return list;

    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT expenses_id FROM employee ORDER BY expenses_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Expenses expenses) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Expenses(expenses_id, category, amount, date) VALUES (?,?,?,?)";
        return SQLUtil.execute(sql, expenses.getExpensesId(), expenses.getCategory(), expenses.getAmount(), expenses.getDate());

    }

    @Override
    public boolean update(Expenses expenses) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Expenses SET category = ?, amount = ?, date = ? WHERE expenses_id = ?";
        return SQLUtil.execute(sql,  expenses.getCategory(),expenses.getAmount(),expenses.getDate(), expenses.getExpensesId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Expenses WHERE expenses_id = ?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Expenses> findById(String id) {
        return null;
    }
}