package lk.ijse.javafx.bakerymanagementsystem.bo.custom;

import lk.ijse.javafx.bakerymanagementsystem.Dto.DeliverDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.SuperBO;

import java.sql.SQLException;
import java.util.List;

public interface DeliverBO extends SuperBO {
    List<DeliverDto> getAllDeliver() throws SQLException, ClassNotFoundException;
    void saveDeliver(DeliverDto dto) throws SQLException, ClassNotFoundException;

    void updateDeliver(DeliverDto dto) throws SQLException, ClassNotFoundException;

    boolean deleteDeliver(String id) throws SQLException, ClassNotFoundException;

    String getNextId() throws SQLException, ClassNotFoundException;

    List<String> getTodayOrderIds(String deliverId);
}
