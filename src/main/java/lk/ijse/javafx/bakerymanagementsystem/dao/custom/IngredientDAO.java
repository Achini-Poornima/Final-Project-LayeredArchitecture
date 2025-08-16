package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientDAO extends CrudDAO<Ingredient> {
    List<String> getAllSupplierIds() throws SQLException, ClassNotFoundException;

}
