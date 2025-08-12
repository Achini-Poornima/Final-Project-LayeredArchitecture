package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.UserDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    @Override
    public List<User> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Users");
        List<User> list = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getString("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            );
            list.add(user);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(User user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Users(user_id,user_name, password, role) VALUES (?,?,?,?)";
        return SQLUtil.execute(sql,
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.getRole()
        );
    }

    @Override
    public boolean update(User user) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Users SET user_name = ? , password = ? , role = ? WHERE user_id = ?";
        return SQLUtil.execute(sql,
                user.getUserName(),
                user.getPassword(),
                user.getRole(),
                user.getUserId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return null;
    }

    @Override
    public boolean checkLogin(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Users WHERE user_name = ? AND password = ?";
        ResultSet resultSet = SQLUtil.execute(sql, username, password);
        return resultSet.next();
    }
}