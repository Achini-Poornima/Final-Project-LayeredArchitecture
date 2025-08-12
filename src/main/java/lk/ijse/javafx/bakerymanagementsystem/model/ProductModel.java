package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDetailsDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductModel {
    public ArrayList<String> getAllProductIds() throws SQLException, ClassNotFoundException {
            ResultSet rst = SQLUtil.execute("select product_id from Product");
            ArrayList<String> list = new ArrayList<>();
            while (rst.next()) {
                String id = rst.getString(1);
                list.add(id);
            }
            return list;


    }
    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT product_id FROM Product ORDER BY product_id DESC limit 1");
        char tableChar = 'P';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public ProductDto findById(String selectedId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Product WHERE product_id=?";
        ResultSet resultset = SQLUtil.execute(sql, selectedId);
        if (resultset.next()) {
            return new ProductDto(
                    resultset.getString("product_id"),
                    resultset.getString("name"),
                    resultset.getDouble("price"),
                    resultset.getInt("stock_quantity"),
                    resultset.getString("supplier_id")
            );
        }
        return null;
    }

    public ArrayList<ProductDto> getAllProducts() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Product");
        ArrayList<ProductDto> productDtos = new ArrayList<>();
        while (resultSet.next()){
            productDtos.add(new ProductDto(
                    resultSet.getString("product_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getString("supplier_id")
            ));
        }
        return productDtos;
    }

    public boolean reduceqty(OrderDetailsDto orderDetailsDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET stock_quantity=stock_quantity-? WHERE product_id=?";
        return SQLUtil.execute(sql, orderDetailsDTO.getQuantity(), orderDetailsDTO.getProductId());
    }

    public boolean deleteProduct(String productId) throws SQLException, ClassNotFoundException {
        String sql  = "DELETE FROM Product WHERE Product_id = ?";
        return SQLUtil.execute(sql,productId);
    }

    public boolean saveProduct(@NotNull ProductDto productDto) throws SQLException, ClassNotFoundException {
        String sql  = "INSERT INTO Product(product_id,name,price,stock_quantity,supplier_id) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql,productDto.getProductId(),productDto.getName(),productDto.getPrice(),productDto.getStockQuantity(),productDto.getSupplierId());
    }

    public boolean updateProduct(@NotNull ProductDto productDto) throws SQLException, ClassNotFoundException {
        String sql  = "UPDATE Product SET name = ?,price = ?,stock_quantity = ?,supplier_id = ? WHERE product_id = ?";
        return SQLUtil.execute(sql,productDto.getName(),productDto.getPrice(),productDto.getStockQuantity(),productDto.getSupplierId(),productDto.getProductId());
    }
}
