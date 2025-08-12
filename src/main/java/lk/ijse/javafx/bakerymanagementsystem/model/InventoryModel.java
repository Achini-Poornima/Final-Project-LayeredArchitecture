package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.InventoryDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryModel {
    public ArrayList<InventoryDto> getAllInventory() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Inventory");
        ArrayList<InventoryDto> inventoryDtos = new ArrayList<>();
        while (resultSet.next()){
            inventoryDtos.add(new InventoryDto(
                    resultSet.getString("inventory_id"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getString("product_id"),
                    resultSet.getString("ingredient_id")
            ));
        }
        return inventoryDtos;
    }

    public boolean deleteInventory(String inventoryId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Inventory WHERE inventory_id = ?";
        return SQLUtil.execute(sql,inventoryId);
    }

    public boolean updateInventory(InventoryDto inventoryDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Inventory SET  stock_quantity = ?, product_id = ?,  ingredient_id=? WHERE inventory_id = ?";
        return SQLUtil.execute(sql,  inventoryDto.getStockQuantity(), inventoryDto.getProductId(),inventoryDto.getIngredientId(), inventoryDto.getInventoryId());
    }

    public boolean saveInventory(InventoryDto inventoryDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Inventory VALUES (?,?,?, ?)";
        return SQLUtil.execute(sql, inventoryDto.getInventoryId(), inventoryDto.getStockQuantity(), inventoryDto.getProductId(),inventoryDto.getIngredientId());
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT inventory_id FROM Inventory ORDER BY inventory_id DESC LIMIT 1");
        String prefix = "IN";

        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String numericPart = lastId.substring(prefix.length()); // e.g., "005"

            int lastIdNumber = Integer.parseInt(numericPart);
            int nextIdNumber = lastIdNumber + 1;
            return String.format("%s%03d", prefix, nextIdNumber);
        }

        return prefix + "001";
    }


    public ArrayList<String> getProductIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> productIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT product_id FROM Product");
        while (rs.next()) {
            productIds.add(rs.getString("product_id"));
        }
        return productIds;
    }

    public ArrayList<String> getIngredientIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> ingredientIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT ingredient_id FROM Ingredient");
        while (rs.next()) {
            ingredientIds.add(rs.getString("ingredient_id"));
        }
        return ingredientIds;
    }
}
