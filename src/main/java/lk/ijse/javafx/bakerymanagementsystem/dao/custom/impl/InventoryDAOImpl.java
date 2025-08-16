package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.InventoryDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public List<Inventory> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Inventory");
        List<Inventory> list = new ArrayList<>();
        while (resultSet.next()){
            Inventory inventory = new Inventory(
                    resultSet.getString("inventory_id"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getString("product_id"),
                    resultSet.getString("ingredient_id")
            );
            list.add(inventory);
        }
        return list;
    }

    @Override
    public Optional<String> getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT inventory_id FROM inventory ORDER BY inventory_id DESC LIMIT 1");
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString(1));
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Inventory inventory) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Inventory VALUES (?,?,?, ?)";
        return SQLUtil.execute(sql, inventory.getInventoryId(), inventory.getStockQuantity(), inventory.getProductId(),inventory.getIngredientId());
    }

    @Override
    public boolean update(Inventory inventory) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Inventory SET  stock_quantity = ?, product_id = ?,  ingredient_id=? WHERE inventory_id = ?";
        return SQLUtil.execute(sql,  inventory.getStockQuantity(), inventory.getProductId(),inventory.getIngredientId(), inventory.getInventoryId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Inventory WHERE inventory_id = ?";
        return SQLUtil.execute(sql,id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Inventory> findById(String id) {
        return null;
    }

    @Override
    public List<Inventory> getProductIds(String productId) throws SQLException, ClassNotFoundException {
        ArrayList<String> productIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT product_id FROM Product");
        while (rs.next()) {
            productIds.add(rs.getString("product_id"));
        }
        return List.of();
    }

    @Override
    public List<Inventory> getIngredientIds(String ingredientId) throws SQLException, ClassNotFoundException {
        ArrayList<String> ingredientIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT ingredient_id FROM Ingredient");
        while (rs.next()) {
            ingredientIds.add(rs.getString("ingredient_id"));
        }
        return List.of();
    }
}
