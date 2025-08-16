package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.IngredientBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.IngredientDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Attendance;
import lk.ijse.javafx.bakerymanagementsystem.entity.Ingredient;
import lk.ijse.javafx.bakerymanagementsystem.entity.Ingredient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientBOImpl implements IngredientBO {
    private final IngredientDAO ingredientDAO = DAOFactory.getInstance().getDAO(DAOTypes.INGREDIENT);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<IngredientDto> getAllIngredient() throws SQLException, ClassNotFoundException {
        List<Ingredient> ingredient = ingredientDAO.getAll();
        List<IngredientDto> ingredientDto = new ArrayList<>();
        for (Ingredient ingredients : ingredient) {
            ingredientDto.add(converter.getIngredientDto(ingredients));
        }
        return ingredientDto;
    }

    @Override
    public void saveIngredient(IngredientDto dto) throws SQLException, ClassNotFoundException {
        Optional<Ingredient> optionalIngredient = ingredientDAO.findById(dto.getIngredientId());

        if (optionalIngredient != null && optionalIngredient.isPresent()) {
            throw new DuplicateException("Duplicate Ingredient ID");
        }

        Ingredient ingredient = converter.getIngredient(dto);
        boolean save = ingredientDAO.save(ingredient);

        if (!save) {
            throw new SQLException("Failed to save ingredient");
        }
    }


    @Override
    public void updateIngredient(IngredientDto dto) throws SQLException, ClassNotFoundException {
        Optional<Ingredient> optionalIngredient = ingredientDAO.findById(dto.getIngredientId());
        if (optionalIngredient.isPresent()) {
            throw new NotFoundException("Ingredient Not Found");
        }

        Ingredient ingredient = converter.getIngredient(dto);
        ingredientDAO.update(ingredient);

    }

    @Override
    public boolean deleteIngredient(String id) throws SQLException, ClassNotFoundException {
        Optional<Ingredient> optionalIngredient = ingredientDAO.findById(id);
        if (optionalIngredient.isEmpty()) {
            throw new NotFoundException("Ingredient not found..!");
        }

        try {
            boolean delete = ingredientDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        Optional<String> lastIdOptional = ingredientDAO.getLastId(); // DAO returns Optional<String>
        String tableChar = "I";

        if (lastIdOptional.isPresent()) {
            String lastId = lastIdOptional.get();          // safely get the value, e.g., "I001"
            String lastIdNumberString = lastId.substring(1); // remove prefix "I"
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

}
