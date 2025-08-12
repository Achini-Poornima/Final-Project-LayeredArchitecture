package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Customer;

import java.sql.SQLException;
import java.util.Optional;

public interface CustomerDAO extends CrudDAO<Customer> {
    Optional<Customer> findCustomerByNic(String nic) throws SQLException, ClassNotFoundException;
    boolean existsCustomerByPhoneNumber(String phoneNumber) throws SQLException, ClassNotFoundException;
}
