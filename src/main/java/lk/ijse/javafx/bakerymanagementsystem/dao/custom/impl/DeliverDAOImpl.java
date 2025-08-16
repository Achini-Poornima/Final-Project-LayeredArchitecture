package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.DeliverDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Deliver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliverDAOImpl implements DeliverDAO {

    @Override
    public List<Deliver> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Deliver");
        List<Deliver> list = new ArrayList<>();
        while (resultSet.next()){
            Deliver deliver = new Deliver(
                    resultSet.getString("deliver_id"),
                    resultSet.getString("deliver_address"),
                    resultSet.getDouble("deliver_charge"),
                    resultSet.getDate("deliver_date"),
                    resultSet.getString("order_id")
            );
            list.add(deliver);
        }
        return list;
    }

    @Override
    public Optional<String> getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT deliver_id FROM deliver ORDER BY deliver_id DESC LIMIT 1");
        if (resultSet.next()) {
            return Optional.ofNullable(resultSet.getString(1));
        }
        return Optional.empty();
    }

    @Override
    public boolean save(Deliver deliver) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Deliver(deliver_id, deliver_address, deliver_charge, deliver_date, order_id) VALUES (?,?,?,?,?)";
        return SQLUtil.execute(sql,
                deliver.getDeliverId(),
                deliver.getDeliverAddress(),
                deliver.getDeliverCharge(),
                deliver.getDeliverDate(),
                deliver.getOrderId()
        );
    }

    @Override
    public boolean update(Deliver deliver) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Deliver SET deliver_address = ? , deliver_charge = ? , deliver_date = ? , order_id = ? WHERE deliver_id = ?";
        return SQLUtil.execute(sql,
                deliver.getDeliverAddress(),
                deliver.getDeliverCharge(),
                deliver.getDeliverDate(),
                deliver.getOrderId(),
                deliver.getDeliverId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Deliver WHERE deliver_id = ?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Deliver> findById(String id) {
        return null;
    }

    @Override
    public List<Deliver> getTodayOrderIds() throws SQLException, ClassNotFoundException {
        List<String> orderIds = new ArrayList<>();
        ResultSet rs = SQLUtil.execute("SELECT order_id FROM Orders WHERE DATE(order_date) = CURATE()");
        while (rs.next()) {
            orderIds.add(rs.getString("order_id"));
        }
        return List.of();
    }
}
