package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.SuperDAO;

public interface QueryDAO extends SuperDAO {
    void findFullOrderDataByCustomerId(String customerId);

}
