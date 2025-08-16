package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.InventoryDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface InventoryBO extends SuperBO {
    List<InventoryDto> getAllInventory() throws SQLException, ClassNotFoundException;
    void saveInventory(InventoryDto dto) throws SQLException, ClassNotFoundException;
    void updateInventory(InventoryDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteInventory(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
