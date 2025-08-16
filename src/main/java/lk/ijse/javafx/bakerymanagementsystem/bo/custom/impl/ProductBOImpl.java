package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.ProductBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ProductDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SupplierDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Product;
import lk.ijse.javafx.bakerymanagementsystem.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductBOImpl implements ProductBO {
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final ProductDAO productDAO = DAOFactory.getInstance().getDAO(DAOTypes.PRODUCT);
    @Override
    public List<ProductDto> getAllProduct() throws SQLException, ClassNotFoundException {
        List<Product> products = productDAO.getAll();
        List<ProductDto> productDto = new ArrayList<>();
        for (Product product : products) {
            productDto.add(converter.getProductDto(product));
        }
        return productDto;
    }

    @Override
    public void saveProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        Optional<Product> optionalProduct = productDAO.findById(dto.getProductId());
        if (optionalProduct.isPresent()) {
            throw new DuplicateException("Duplicate Product ID");
        }
        Product product = converter.getProduct(dto);
        boolean save = productDAO.save(product);

    }

    @Override
    public void updateProduct(ProductDto dto) throws SQLException, ClassNotFoundException {
        Optional<Product> optionalProduct = productDAO.findById(dto.getProductId());
        if (optionalProduct.isPresent()) {
            throw new NotFoundException("Product Not Found");
        }

        Product product = converter.getProduct(dto);
        productDAO.update(product);

    }

    @Override
    public boolean deleteProduct(String id) throws SQLException, ClassNotFoundException {
        Optional<Product> optionalProduct = productDAO.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException("Product not found..!");
        }

        try {
            boolean delete = productDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(productDAO.getLastId());
        String tableChar = "PR";
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
