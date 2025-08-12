package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.QueryDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.CustomOrder;
import lk.ijse.javafx.bakerymanagementsystem.entity.CustomOrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<CustomOrder> findFullOrderDataByCustomerId(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT o.order_id, " +
                "       o.order_date, " +
                "       o.payment_status, " +
                "       c.customer_id, " +
                "       c.name AS customer_name, " +
                "       c.address AS customer_address, " +
                "       c.nic, " +
                "       c.email, " +
                "       c.phone, " +
                "       od.item_id, " +
                "       i.name AS item_name, " +
                "       od.quantity, " +
                "       od.price " +
                "FROM orders o " +
                "         JOIN customer c ON o.customer_id = c.customer_id " +
                "         JOIN order_details od ON o.order_id = od.order_id " +
                "         JOIN item i ON od.item_id = i.item_id " +
                "WHERE o.customer_id = ? " +
                "ORDER BY o.order_id, od.item_id");

        Map<String, CustomOrder> orderMap = new HashMap<>();
        while (rst.next()) {
            String orderId = rst.getString("order_id");
            CustomOrder customOrder = orderMap.get(orderId);
            if (customOrder == null) {
                customOrder = new CustomOrder(
                        orderId,
                        rst.getDate("order_date"),
                        rst.getString("payment_status"),
                        rst.getString("customer_id"),
                        rst.getString("customer_name"),
                        rst.getString("address"),
                        rst.getString("nic"),
                        rst.getString("email"),
                        rst.getString("phone"),
                        new ArrayList<>()
                );
                orderMap.put(orderId, customOrder);
            }
            customOrder.orderDetailsList.add(
                    new CustomOrderDetails(

                    )
            );
        }
        return new ArrayList<>(orderMap.values());

    }
}
