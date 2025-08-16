package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.CustomerDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Customer");

        List<Customer> list = new ArrayList<>();
        while (rst.next()) {
            Customer customer = new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            list.add(customer);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Customer customer) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("insert into Customer values (?,?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getName(),
                customer.getAddress(),
                customer.getNic(),
                customer.getContact(),
                customer.getEmail()
        );
    }

    @Override
    public boolean update(Customer customer) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("update Customer set name = ?, address = ?,nic = ?, contact = ?,email = ? where customer_id = ?",
                customer.getName(),
                customer.getAddress(),
                customer.getNic(),
                customer.getContact(),
                customer.getEmail(),
                customer.getCustomerId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("delete from Customer where customer_id = ?",id);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT customer_id FROM Customer");
        List<String> ids = new ArrayList<>();
        while (resultSet.next()) {
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    @Override
    public Optional<Customer> findById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE customer_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findCustomerByNic(String nic) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE nic = ?", nic);
        if (resultSet.next()) {
            return Optional.of(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsCustomerByContactNumber(String contact) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE contact = ?", contact);
        return resultSet.next();
    }
}
