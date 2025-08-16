package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface PaymentBO extends SuperBO {
    List<PaymentDto> getAllPayment() throws SQLException, ClassNotFoundException;
    void savePayment(PaymentDto dto) throws SQLException, ClassNotFoundException;
    void updatePayment(PaymentDto dto) throws SQLException, ClassNotFoundException;
    boolean deletePayment(String id) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
