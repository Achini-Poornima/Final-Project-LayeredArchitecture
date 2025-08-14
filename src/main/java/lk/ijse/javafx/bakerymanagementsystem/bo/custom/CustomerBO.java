package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO extends SuperBO {
    List<CustomerDto> getAllCustomer() throws SQLException;

    void saveCustomer(CustomerDto dto) throws DuplicateException, Exception;

    void updateCustomer(CustomerDto dto) throws SQLException;

    boolean deleteCustomer(String id) throws InUseException, Exception;

    String getNextId() throws SQLException;

}
