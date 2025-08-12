package lk.ijse.javafx.bakerymanagementsystem.dao.custom;

import lk.ijse.javafx.bakerymanagementsystem.dao.CrudDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.User;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {
    boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException;
}
