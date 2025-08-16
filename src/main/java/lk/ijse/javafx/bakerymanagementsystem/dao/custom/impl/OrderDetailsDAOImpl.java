package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDetailsDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.OrderDetails;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public List<OrderDetails> getAll() {
        return List.of();
    }

    @Override
    public Optional<String> getLastId() {
        return Optional.empty();
    }

    @Override
    public boolean save(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute(
                "insert into order_details values (?,?,?,?)",
                orderDetails.getOrderId(),
                orderDetails.getProductId(),
                orderDetails.getQuantity(),
                orderDetails.getPrice()
        );
    }

    @Override
    public boolean update(OrderDetails orderDetails) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<String> getAllIds() {
        return List.of();
    }

    @Override
    public Optional<OrderDetails> findById(String id) {
        return Optional.empty();
    }
}