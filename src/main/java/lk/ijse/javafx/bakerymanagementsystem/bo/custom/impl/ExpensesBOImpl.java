package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.ExpensesBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ExpensesDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Expenses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpensesBOImpl implements ExpensesBO {
    private final ExpensesDAO expensesDAO = DAOFactory.getInstance().getDAO(DAOTypes.EXPENSES);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<ExpensesDto> getAllExpenses() throws SQLException, ClassNotFoundException {
        List<Expenses> expenses = expensesDAO.getAll();
        List<ExpensesDto> expensesDtos = new ArrayList<>();
        for (Expenses expense : expenses) {
            expensesDtos.add(converter.getExpensesDto(expense));
        }
        return expensesDtos;
    }

    @Override
    public void saveExpenses(ExpensesDto dto) throws SQLException, ClassNotFoundException {
        Optional<Expenses> optionalExpenses = expensesDAO.findById(dto.getExpensesId());
        if (optionalExpenses.isPresent()) {
            throw new DuplicateException("Duplicate expenses id");
        }
        Expenses expenses = converter.getExpenses(dto);
        boolean save = expensesDAO.save(expenses);
    }

    @Override
    public void updateExpenses(ExpensesDto dto) throws SQLException, ClassNotFoundException {
        Optional<Expenses> optionalExpenses = expensesDAO.findById(dto.getExpensesId());
        if (optionalExpenses.isEmpty()) {
            throw new NotFoundException("Expenses not found");
        }
        Expenses expenses = converter.getExpenses(dto);
        boolean save = expensesDAO.save(expenses);
    }

    @Override
    public boolean deleteExpenses(String id) throws SQLException, ClassNotFoundException {
        Optional<Expenses> optionalExpenses = expensesDAO.findById(id);
        if (optionalExpenses.isEmpty()) {
            throw new NotFoundException("Expenses not found");
        }
        try {
            boolean delete = expensesDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(expensesDAO.getLastId());
        String tableChar = "Ex";
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
