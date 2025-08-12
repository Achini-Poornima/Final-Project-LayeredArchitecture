package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentModel {
    public static ArrayList<PaymentDto> getAllPayments() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Payment");
        ArrayList<PaymentDto> paymentList = new ArrayList<>();
        while (resultSet.next()) {
            paymentList.add(new PaymentDto(
                    resultSet.getString("payment_id"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("payment_method"),
                    resultSet.getString("payment_date"),
                    resultSet.getString("order_id")
            ));
        }
        return paymentList;
    }

    public ArrayList<String> getTodayOrderIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> orderIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT order_id FROM Orders WHERE DATE(order_date) = CURDATE()");
        while (rs.next()) {
            orderIds.add(rs.getString("order_id"));
        }
        return orderIds;
    }

    public String getNextPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT payment_id FROM Payment ORDER BY payment_id desc limit 1");
        char tableChar = 'P';
        if (resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNUmberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
            return nextIdString;
        }
        return tableChar + "001";
    }

    public boolean updatePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Payment SET amount = ?, payment_method = ?, payment_date = ?, order_id = ? WHERE payment_id = ?";
        return SQLUtil.execute(sql, paymentDto.getAmount(), paymentDto.getPaymentMethod(), paymentDto.getPaymentDate(), paymentDto.getOrderId(), paymentDto.getPaymentId());
    }

    public boolean savePayment(PaymentDto paymentDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Payment(payment_id, amount, payment_method, payment_date, order_id) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql, paymentDto.getPaymentId(), paymentDto.getAmount(), paymentDto.getPaymentMethod(), paymentDto.getPaymentDate(), paymentDto.getOrderId());
    }

    public boolean deletePayment(PaymentDto selectedPayment) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        return SQLUtil.execute(sql, selectedPayment.getPaymentId());
    }
}
