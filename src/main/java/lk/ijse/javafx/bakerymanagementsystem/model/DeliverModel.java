/*
//package lk.ijse.javafx.bakerymanagementsystem.model;
//
//import lk.ijse.javafx.bakerymanagementsystem.Dto.DeliverDto;
//import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//public class DeliverModel {
//
//    public static ArrayList<DeliverDto> getAllDelivers() throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Deliver");
//        ArrayList<DeliverDto> deliverDtos = new ArrayList<>();
//        while (resultSet.next()){
//            deliverDtos.add(new DeliverDto(
//                    resultSet.getString("deliver_id"),
//                    resultSet.getString("deliver_address"),
//                    resultSet.getDouble("deliver_charge"),
                    resultSet.getDate("deliver_date"),
//                    resultSet.getString("order_id")
//            ));
//        }
//        return deliverDtos;
//    }
//
//    public String getNextId() throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = SQLUtil.execute("SELECT deliver_Id FROM Deliver ORDER BY deliver_id desc limit 1");
//        char tableChar = 'D';
//        if (resultSet.next()){
//            String lastId = resultSet.getString(1);
//            String lastIdNUmberString = lastId.substring(1);
//            int lastIdNumber = Integer.parseInt(lastIdNUmberString);
//            int nextIdNumber = lastIdNumber + 1;
//            String nextIdString = String.format(tableChar + "%03d", nextIdNumber);
//            return nextIdString;
//        }
//        return tableChar + "001";
//    }
//
//    public boolean saveDeliver(DeliverDto deliverDto) throws SQLException, ClassNotFoundException {
//        String sql = "INSERT INTO Deliver(deliver_id, deliver_address, deliver_charge, deliver_date, order_id) VALUES (?,?,?,?,?)";
//        return SQLUtil.execute(sql,
//                deliverDto.getDeliverId(),
//                deliverDto.getDeliverAddress(),
//                deliverDto.getDeliverCharge(),
//                deliverDto.getDeliverDate(),
//                deliverDto.getOrderId()
//        );
//    }
//
//
//
//    public boolean updateDeliver(DeliverDto deliverDto) throws SQLException, ClassNotFoundException {
//        String sql = "UPDATE Deliver SET deliver_address = ? , deliver_charge = ? , deliver_date = ? , order_id = ? WHERE deliver_id = ?";
//        return SQLUtil.execute(sql,
//                deliverDto.getDeliverAddress(),
//                deliverDto.getDeliverCharge(),
//                deliverDto.getDeliverDate(),
//                deliverDto.getOrderId(),
//                deliverDto.getDeliverId()
//        );
//    }
//
//    public boolean deleteUser(String deliverId) throws SQLException, ClassNotFoundException {
//        String sql = "DELETE FROM Deliver WHERE deliver_id = ?";
//        return SQLUtil.execute(sql, deliverId);
//    }
//
//    public ArrayList<String> getTodayOrderIds() throws SQLException, ClassNotFoundException {
//        ArrayList<String> orderIds = new ArrayList<>();
//        ResultSet rs = SQLUtil.execute("SELECT order_id FROM Orders WHERE DATE(order_date) = CURATE()");
//        while (rs.next()) {
//            orderIds.add(rs.getString("order_id"));
//        }
//        return orderIds;
//    }
//
//}
*/
