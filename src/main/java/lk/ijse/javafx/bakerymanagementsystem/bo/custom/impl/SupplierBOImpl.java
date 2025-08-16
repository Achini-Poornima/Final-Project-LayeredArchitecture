package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.SupplierDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.SupplierDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.SupplierBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SupplierDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Supplier;
import lk.ijse.javafx.bakerymanagementsystem.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierBOImpl implements SupplierBO {
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final SupplierDAO supplierDAO = DAOFactory.getInstance().getDAO(DAOTypes.SUPPLIER);

    @Override
    public List<SupplierDto> getAllSupplier() throws SQLException, ClassNotFoundException {
        List<Supplier> suppliers = supplierDAO.getAll();
        List<SupplierDto> supplierDto = new ArrayList<>();
        for (Supplier supplier : suppliers) {
            supplierDto.add(converter.getSupplierDto(supplier));
        }
        return supplierDto;
    }

    @Override
    public void saveSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(dto.getSupplierId());
        if (optionalSupplier.isPresent()) {
            throw new DuplicateException("Duplicate Supplier ID");
        }
        Supplier supplier = converter.getSupplier(dto);
        boolean save = supplierDAO.save(supplier);
    }

    @Override
    public void updateSupplier(SupplierDto dto) throws SQLException, ClassNotFoundException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(dto.getSupplierId());
        if (optionalSupplier.isPresent()) {
            throw new NotFoundException("Supplier Not Found");
        }

        Supplier supplier = converter.getSupplier(dto);
        supplierDAO.update(supplier);

    }

    @Override
    public boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException {
        Optional<Supplier> optionalSupplier = supplierDAO.findById(id);
        if (optionalSupplier.isEmpty()) {
            throw new NotFoundException("Supplier not found..!");
        }

        try {
            boolean delete = supplierDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(supplierDAO.getLastId());
        char tableChar = 'S';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
