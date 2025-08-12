package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.DBConnection.DbConnection;
import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModel {
    private final OrderDetailsModel orderDetailsModel=new OrderDetailsModel();
    public String getNextOrderId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT order_id FROM Orders ORDER BY order_id DESC limit 1");
        char tableChar = 'O';
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

    public boolean placeOrder(OrderDto orderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            boolean isSaved = SQLUtil.execute("INSERT INTO Orders(order_id, customer_id, order_date, payment_status) VALUES (?,?,?,?)",orderDTO.getOrderId(),orderDTO.getCustomerId(),orderDTO.getOrderDate(),orderDTO.getPaymentStatus());
            if(isSaved){
                boolean isOderSaved =  orderDetailsModel.saveOrderDetailsList(orderDTO.getCartList());
                if(isOderSaved){
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        }catch (Exception e){
            connection.rollback();
            e.printStackTrace();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
