package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.ExpensesBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.EmployeeDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ExpensesDAO;

import java.sql.SQLException;
import java.util.List;

public class ExpensesBOImpl implements ExpensesBO {
    private final ExpensesDAO expensesDAO = DAOFactory.getInstance().getDAO(DAOTypes.EXPENSES);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<ExpensesDto> getAllExpenses() {
        return List.of();
    }

    @Override
    public void saveExpenses(ExpensesDto dto) {

    }

    @Override
    public void updateExpenses(ExpensesDto dto) {

    }

    @Override
    public boolean deleteExpenses(String id) {
        return false;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = expensesDAO.getLastId();
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
