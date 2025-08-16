package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.DBConnection.DbConnection;
import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDetailsDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.PlaceOrderBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.CustomerDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDetailsDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.ProductDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Customer;
import lk.ijse.javafx.bakerymanagementsystem.entity.Order;
import lk.ijse.javafx.bakerymanagementsystem.entity.OrderDetails;
import lk.ijse.javafx.bakerymanagementsystem.entity.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
    private OrderDetailsDAO orderDetailsDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER_DETAILS);
    private ProductDAO productDAO = DAOFactory.getInstance().getDAO(DAOTypes.PRODUCT);
    private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);

    @Override
    public boolean placeOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            Optional<Customer> optionalCustomer = customerDAO.findById(orderDto.getCustomerId());
            if (optionalCustomer.isEmpty()) {
                throw new NotFoundException("Customer not found");
            }

            Order order = new Order();
            order.setOrderId(orderDto.getOrderId());
            order.setCustomerId(orderDto.getCustomerId());
            order.setOrderDate(orderDto.getOrderDate());

            boolean isOrderSaved = orderDAO.save(order);
            if (isOrderSaved) {
                boolean isSuccess = saveDetailsAndUpdateProduct(orderDto.getCartList());
                if (isSuccess) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }    }

    private boolean saveDetailsAndUpdateProduct(ArrayList<OrderDetailsDto> cartList) throws SQLException, ClassNotFoundException {
        for (OrderDetailsDto detailsDTO : cartList) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrderId(detailsDTO.getOrderId());
            orderDetail.setProductId(detailsDTO.getProductId());
            orderDetail.setQuantity(detailsDTO.getQuantity());
            orderDetail.setPrice(detailsDTO.getPrice());

            if (!orderDetailsDAO.save(orderDetail)) {
                return false;
            }

            Optional<Product> optionalItem = productDAO.findById(detailsDTO.getProductId());
            if (optionalItem.isEmpty()) {
                throw new NotFoundException("Item not found");
            }
            boolean isItemUpdated = productDAO.reduceQuantity(
                    detailsDTO.getProductId(),
                    detailsDTO.getQuantity()
            );
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(orderDAO.getLastId());
        char tableChar = 'O';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

}
