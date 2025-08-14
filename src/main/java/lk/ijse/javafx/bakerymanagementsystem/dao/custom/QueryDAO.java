package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.SuperDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.CustomOrder;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ArrayList<CustomOrder> findFullOrderDataByCustomerId(String customerId) throws SQLException, ClassNotFoundException;

}
