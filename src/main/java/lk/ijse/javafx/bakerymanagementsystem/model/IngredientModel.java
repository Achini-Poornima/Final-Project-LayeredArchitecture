package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredientModel {

    public static ArrayList<IngredientDto> getAllDelivers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Ingredient");
        ArrayList<IngredientDto> ingredientDtos = new ArrayList<>();
        while (resultSet.next()){
            ingredientDtos.add(new IngredientDto(
                    resultSet.getString("ingredient_id"),
                    resultSet.getString("name"),
                    resultSet.getString("expire_date"),
                    resultSet.getDouble("quantity_available"),
                    resultSet.getString("supplier_id")
            ));
        }
        return ingredientDtos;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT ingredient_id FROM Ingredient ORDER BY ingredient_id desc limit 1");
        char tableChar = 'I';
        if (resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> supplierIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT supplier_id FROM Supplier");
        while (rs.next()) {
            supplierIds.add(rs.getString("supplier_id"));
        }
        return supplierIds;
    }

    public boolean deleteIngredient(String ingredientId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Ingredient WHERE ingredient_id=?";
        return SQLUtil.execute(sql, ingredientId);
    }

    public boolean saveIngredient(IngredientDto ingredientDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Ingredient VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql, ingredientDto.getIngredientId(), ingredientDto.getName(), ingredientDto.getExpireDate(), ingredientDto.getQuantityAvailable(), ingredientDto.getSupplierId());
    }

    public boolean updateIngredient(IngredientDto ingredientDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Ingredient SET name=?, expire_date=?, quantity_available=?, supplier_id=? WHERE ingredient_id=?";
        return SQLUtil.execute(sql, ingredientDto.getName(), ingredientDto.getExpireDate(), ingredientDto.getQuantityAvailable(), ingredientDto.getSupplierId(), ingredientDto.getIngredientId());
    }
}
