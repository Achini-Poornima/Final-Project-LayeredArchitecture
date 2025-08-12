package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {
    public boolean saveUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Users(user_id,user_name, password, role) VALUES (?,?,?,?)";
        return SQLUtil.execute(sql,
                userDto.getUserId(),
                userDto.getUserName(),
                userDto.getPassword(),
                userDto.getRole());
    }

    public boolean updateUser(UserDto userDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Users SET user_name = ? , password = ? , role = ? WHERE user_id = ?";
        return SQLUtil.execute(sql,
                userDto.getUserName(),
                userDto.getPassword(),
                userDto.getRole(),
                userDto.getUserId());
    }

    public boolean deleteUser(String userId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        return SQLUtil.execute(sql, userId);
    }

    public boolean checkLogin(String username, String password) throws Exception {
        String sql = "SELECT * FROM Users WHERE user_name = ? AND password = ?";
        ResultSet resultSet = SQLUtil.execute(sql, username, password);
        return resultSet.next();
    }


    public ArrayList<UserDto> getAllUsers() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Users");
        ArrayList<UserDto> userDtos = new ArrayList<>();
        while (resultSet.next()) {
            userDtos.add(new UserDto(
                    resultSet.getString("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("password"),
                    resultSet.getString("role")
            ));
        }
        return userDtos;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("select user_id from Users order by user_id desc limit 1");
        char tableChar = 'U';
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
}
