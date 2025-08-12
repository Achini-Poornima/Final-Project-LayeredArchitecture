package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDetailsDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.OrderDetails;

import java.util.List;
import java.util.Optional;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public List<OrderDetails> getAll() {
        return null;
    }

    @Override
    public String getLastId() {
        return null;
    }

    @Override
    public boolean save(OrderDetails orderDetails) {
        return false;
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
        return null;
    }

    @Override
    public Optional<OrderDetails> findById(String id) {
        return null;
    }
}