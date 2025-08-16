package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.InventoryDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.InventoryDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.InventoryBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.InventoryDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Attendance;
import lk.ijse.javafx.bakerymanagementsystem.entity.Inventory;
import lk.ijse.javafx.bakerymanagementsystem.entity.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryBOImpl implements InventoryBO {
    private final InventoryDAO inventoryDAO = DAOFactory.getInstance().getDAO(DAOTypes.INVENTORY);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<InventoryDto> getAllInventory() throws SQLException, ClassNotFoundException {
        List<Inventory> inventory = inventoryDAO.getAll();
        List<InventoryDto> inventoryDto = new ArrayList<>();
        for (Inventory inventorys : inventory) {
            inventoryDto.add(converter.getInventoryDto(inventorys));
        }
        return inventoryDto;
    }

    @Override
    public void saveInventory(InventoryDto dto) throws SQLException, ClassNotFoundException {
        Optional<Inventory> optionalInventory = inventoryDAO.findById(dto.getInventoryId());
        if (optionalInventory.isEmpty()) {
            throw new DuplicateException("Duplicate Inventory ID");
        }
        Inventory inventory = converter.getInventory(dto);
        boolean save = inventoryDAO.save(inventory);
    }

    @Override
    public void updateInventory(InventoryDto dto) throws SQLException, ClassNotFoundException {
        Optional<Inventory> optionalInventory = inventoryDAO.findById(dto.getInventoryId());
        if (optionalInventory.isPresent()) {
            throw new NotFoundException("Inventory Not Found");
        }

        Inventory inventory = converter.getInventory(dto);
        inventoryDAO.update(inventory);
    }

    @Override
    public boolean deleteInventory(String id) throws SQLException, ClassNotFoundException {
        Optional<Inventory> optionalInventory = inventoryDAO.findById(id);
        if (optionalInventory.isEmpty()) {
            throw new NotFoundException("Inventory not found..!");
        }

        try {
            boolean delete = inventoryDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(inventoryDAO.getLastId());
        String tableChar = "IN";
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
