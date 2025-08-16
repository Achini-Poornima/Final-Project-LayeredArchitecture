package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface ProductBO extends SuperBO {
    List<ProductDto> getAllProduct() throws SQLException, ClassNotFoundException;
    void saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException;
    void updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteProduct(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
