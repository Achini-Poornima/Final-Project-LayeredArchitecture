package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO extends CrudDAO<Payment> {
    List<Payment> getTodayOrderId(String orderId) throws SQLException, ClassNotFoundException;
}
