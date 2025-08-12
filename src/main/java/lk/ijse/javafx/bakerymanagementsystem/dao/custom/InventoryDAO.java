package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Inventory;

import java.sql.SQLException;
import java.util.List;

public interface InventoryDAO extends CrudDAO<Inventory> {
    List<Inventory> getProductIds(String productId) throws SQLException, ClassNotFoundException;
    List<Inventory> getIngredientIds(String ingredientId) throws SQLException, ClassNotFoundException;
}
