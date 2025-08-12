package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Order;

import java.sql.SQLException;

public interface OrderDAO extends CrudDAO<Order> {
    boolean existOrdersByCustomerId(String customerId) throws SQLException, ClassNotFoundException;
}
