package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.IngredientDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientDAOImpl implements IngredientDAO {
    @Override
    public List<Ingredient> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Ingredient");
        List<Ingredient> list = new ArrayList<>();
        while (resultSet.next()){
            Ingredient ingredient = new Ingredient(
                    resultSet.getString("ingredient_id"),
                    resultSet.getString("name"),
                    resultSet.getString("expire_date"),
                    resultSet.getDouble("quantity_available"),
                    resultSet.getString("supplier_id")
            );
            list.add(ingredient);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT ingredient_id FROM ingredient ORDER BY ingredient_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Ingredient ingredient) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Ingredient VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql, ingredient.getIngredientId(), ingredient.getName(), ingredient.getExpireDate(), ingredient.getQuantityAvailable(), ingredient.getSupplierId());
    }

    @Override
    public boolean update(Ingredient ingredient) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Ingredient SET name=?, expire_date=?, quantity_available=?, supplier_id=? WHERE ingredient_id=?";
        return SQLUtil.execute(sql, ingredient.getName(), ingredient.getExpireDate(), ingredient.getQuantityAvailable(), ingredient.getSupplierId(), ingredient.getIngredientId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Ingredient WHERE ingredient_id=?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        List<String> supplierIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT supplier_id FROM Supplier");
        while (rs.next()) {
            supplierIds.add(rs.getString("supplier_id"));
        }
        return supplierIds;
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        return null;
    }
}