package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Deliver;

import java.sql.SQLException;
import java.util.List;

public interface DeliverDAO extends CrudDAO<Deliver> {
    List<Deliver> getTodayOrderIds(String deliverId) throws SQLException, ClassNotFoundException;
}
