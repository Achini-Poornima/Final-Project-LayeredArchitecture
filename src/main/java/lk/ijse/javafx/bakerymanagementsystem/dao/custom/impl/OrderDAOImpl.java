package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public List<Order> getAll() throws SQLException, ClassNotFoundException {
        List<Order> list = new ArrayList<>();

        ResultSet rs = SQLUtil.execute("SELECT * FROM orders");
        while (rs.next()) {
            Order order = new Order(
                    rs.getString("order_id"),
                    rs.getString("customer_id"),
                    rs.getDate("order_date"),
                    rs.getString("payment_status")
                    );
            list.add(order);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("select order_id from orders order by order_id desc limit 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Order order) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("insert into orders values (?,?,?,?)",
                order.getOrderId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getPaymentStatus()
        );
    }

    @Override
    public boolean update(Order order) {
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
    public Optional<Order> findById(String id) {
        return null;
    }

    @Override
    public boolean existOrdersByCustomerId(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM orders WHERE customer_id = ?", customerId);
        return resultSet.next();
    }
}