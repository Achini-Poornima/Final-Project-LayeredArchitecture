package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.SupplierDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface SupplierBO extends SuperBO {
    List<SupplierDto> getAllSupplier() throws SQLException, ClassNotFoundException;
    void saveSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException;
    void updateSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
