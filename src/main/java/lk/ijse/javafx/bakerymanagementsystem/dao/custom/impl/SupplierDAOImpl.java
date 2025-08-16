package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SupplierDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDAOImpl implements SupplierDAO {
    @Override
    public List<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Supplier");
        List<Supplier> list = new ArrayList<>();
        while (resultSet.next()){
            Supplier supplier = new Supplier(
                    resultSet.getString("supplier_id"),
                    resultSet.getString("name"),
                    resultSet.getString("supplied_ingredient"),
                    resultSet.getString("address"),
                    resultSet.getString("email")
            );
            list.add(supplier);
        }
        return list;
    }

    @Override
    public Optional<String> getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString(1));
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Supplier supplier) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Supplier(supplier_id,name,supplied_Ingredient,address,email) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql,
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getSuppliedIngredient(),
                supplier.getAddress(),
                supplier.getEmail()
        );
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Supplier SET name=?, supplied_Ingredient=?, address=?, email=? WHERE supplier_id=?";
        return SQLUtil.execute(sql,
                supplier.getName(),
                supplier.getSuppliedIngredient(),
                supplier.getAddress(),
                supplier.getEmail(),
                supplier.getSupplierId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Supplier WHERE supplier_id=?";
        return SQLUtil.execute(sql,id);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT supplier_id FROM Supplier");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()){
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    @Override
    public Optional<Supplier> findById(String id) {
        return Optional.empty();
    }
}
