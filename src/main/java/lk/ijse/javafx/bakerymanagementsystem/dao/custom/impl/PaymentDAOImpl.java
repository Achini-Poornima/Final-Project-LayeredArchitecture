package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.PaymentDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public List<Payment> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Payment");
        List<Payment> list = new ArrayList<>();
        while (resultSet.next()) {
            Payment payment = new Payment(
                    resultSet.getString("payment_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("payment_method"),
                    resultSet.getString("payment_date"),
                    resultSet.getString("order_id")
            );
            list.add(payment);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Payment payment) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Payment(payment_id, amount, payment_method, payment_date, order_id) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql, payment.getPaymentId(), payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentDate(), payment.getOrderId());
    }

    @Override
    public boolean update(Payment payment) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Payment SET amount = ?, payment_method = ?, payment_date = ?, order_id = ? WHERE payment_id = ?";
        return SQLUtil.execute(sql, payment.getAmount(), payment.getPaymentMethod(), payment.getPaymentDate(), payment.getOrderId(), payment.getPaymentId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Payment> findById(String id) {
        return null;
    }

    @Override
    public List<Payment> getTodayOrderId(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<String> orderIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT order_id FROM Orders WHERE DATE(order_date) = CURDATE()");
        while (rs.next()) {
            orderIds.add(rs.getString("order_id"));
        }
        return List.of();
    }
}