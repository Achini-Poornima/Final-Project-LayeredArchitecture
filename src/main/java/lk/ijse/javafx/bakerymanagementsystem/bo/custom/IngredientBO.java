package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface IngredientBO extends SuperBO {
    List<IngredientDto> getAllIngredient() throws SQLException, ClassNotFoundException;
    void saveIngredient(IngredientDto dto) throws SQLException, ClassNotFoundException;
    void updateIngredient(IngredientDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteIngredient(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
