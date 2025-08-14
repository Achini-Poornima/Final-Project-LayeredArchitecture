package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.CustomerBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;

import java.sql.SQLException;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    @Override
    public List<CustomerDto> getAllCustomer() throws SQLException {
        return List.of();
    }

    @Override
    public void saveCustomer(CustomerDto dto) throws DuplicateException, Exception {

    }

    @Override
    public void updateCustomer(CustomerDto dto) throws SQLException {

    }

    @Override
    public boolean deleteCustomer(String id) throws InUseException, Exception {
        return false;
    }

    @Override
    public String getNextId() throws SQLException {
        return "";
    }
}
