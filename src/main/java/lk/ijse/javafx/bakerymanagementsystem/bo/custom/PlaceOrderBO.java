package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;
import java.sql.SQLException;

public interface PlaceOrderBO extends SuperBO {
    boolean placeOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException;
    String getNextId() throws SQLException, ClassNotFoundException;
}
