package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.UserBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.PaymentDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.UserDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.User;
import lk.ijse.javafx.bakerymanagementsystem.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBOImpl implements UserBO {
    private final UserDAO userDAO = DAOFactory.getInstance().getDAO(DAOTypes.USER);
    private final EntityDTOConverter converter = new EntityDTOConverter();
    @Override
    public List<UserDto> getAllUser() throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getAll();
        List<UserDto> userDto = new ArrayList<>();
        for (User user : users) {
            userDto.add(converter.getUserDto(user));
        }
        return userDto;
    }

    @Override
    public void saveUser(UserDto dto) throws SQLException, ClassNotFoundException {
        Optional<User> optionalUser = userDAO.findById(dto.getUserId());
        if (optionalUser.isPresent()) {
            throw new DuplicateException("Duplicate User ID");
        }
        User user = converter.getUser(dto);
        boolean save = userDAO.save(user);

    }

    @Override
    public void updateUser(UserDto dto) throws SQLException, ClassNotFoundException {
        Optional<User> optionalUser = userDAO.findById(dto.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User Not Found");
        }
        User user = converter.getUser(dto);
        userDAO.update(user);
    }

    @Override
    public boolean deleteUser(String id) throws SQLException, ClassNotFoundException {
        Optional<User> optionalUser = userDAO.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found..!");
        }

        return userDAO.delete(id);
    }


    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        Optional<String> lastIdOpt = userDAO.getLastId();
        char tableChar = 'U';

        if (lastIdOpt.isPresent()) {
            String lastId = lastIdOpt.get();
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        } else {
           return tableChar + "001";
        }
    }

}
