package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDetailsDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsModel {
  private final   ProductModel productModel=new ProductModel();
    public boolean saveOrderDetailsList(ArrayList<OrderDetailsDto> cartList) throws SQLException, ClassNotFoundException {
        for (OrderDetailsDto orderDetailsDTO:cartList){

            boolean isDetailsSaved = saveOrderDetailsList(orderDetailsDTO);
            if(!isDetailsSaved){
                return false;
            }
            boolean isUpdate = productModel.reduceqty(orderDetailsDTO);
            if(!isUpdate){
                return false;
            }

        }
        return true;
    }
    private boolean saveOrderDetailsList(OrderDetailsDto orderDetailsDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Order_Details VALUES (?,?,?,?)";
        return SQLUtil.execute(sql,orderDetailsDTO.getOrderId(),orderDetailsDTO.getProductId(),orderDetailsDTO.getQuantity(),orderDetailsDTO.getPrice());

    }}
