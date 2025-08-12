package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ProductDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Product");
        List<Product> list = new ArrayList<>();
        while (resultSet.next()){
            Product product = new Product(
                    resultSet.getString("product_id"),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("stock_quantity"),
                    resultSet.getString("supplier_id")
            );
            list.add(product);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT customer_id FROM customer ORDER BY customer_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Product product) throws SQLException, ClassNotFoundException {
        String sql  = "INSERT INTO Product(product_id,name,price,stock_quantity,supplier_id) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql,product.getProductId(),product.getName(),product.getPrice(),product.getStockQuantity(),product.getSupplierId());
    }

    @Override
    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        String sql  = "UPDATE Product SET name = ?,price = ?,stock_quantity = ?,supplier_id = ? WHERE product_id = ?";
        return SQLUtil.execute(sql,product.getName(),product.getPrice(),product.getStockQuantity(),product.getSupplierId(),product.getProductId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql  = "DELETE FROM Product WHERE Product_id = ?";
        return SQLUtil.execute(sql,id);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select product_id from Product");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    @Override
    public Optional<Product> findById(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM customer WHERE customer_id = ?", id);
        if (resultSet.next()) {
            return Optional.of(new Product(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getString(5)
            ));
        }
        return Optional.empty();
    }

    @Override
    public boolean reduceQuantity(String id, int qty) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Product SET stock_quantity=stock_quantity-? WHERE product_id=?";
        return SQLUtil.execute(sql, qty, id);
    }
}