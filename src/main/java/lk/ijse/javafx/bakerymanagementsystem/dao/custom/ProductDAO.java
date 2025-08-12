package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Product;

import java.sql.SQLException;

public interface ProductDAO extends CrudDAO<Product> {
    boolean reduceQuantity(String id, int qty) throws SQLException, ClassNotFoundException;
}
