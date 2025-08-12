package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDetailsDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.OrderDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CartTM;
import lk.ijse.javafx.bakerymanagementsystem.model.CustomerModel;
import lk.ijse.javafx.bakerymanagementsystem.model.OrderModel;
import lk.ijse.javafx.bakerymanagementsystem.model.ProductModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

        @FXML
        private AnchorPane ancOrder;

        @FXML
        private ComboBox<String> cmbCustomerId;

        @FXML
        private ComboBox<String> cmbProductId;

        @FXML
        private ComboBox<String> cmbPay;

        @FXML
        private TableColumn<?, ?> colAction;

        @FXML
        private TableColumn<CartTM, String> colItemId;

        @FXML
        private TableColumn<CartTM, String> colName;

        @FXML
        private TableColumn<CartTM, Double> colPrice;

        @FXML
        private TableColumn<CartTM, Integer> colQuantity;

        @FXML
        private TableColumn<CartTM, Double> colTotal;

        @FXML
        private Label lblCustomerName;

        @FXML
        private Label lblItemName;

        @FXML
        private Label lblItemPrice;

        @FXML
        private Label lblItemQty;

        @FXML
        private Label lblOrderId;

        @FXML
        private Label orderDate;

        @FXML
        private TableView<CartTM> tblCart;

        @FXML
        private TextField txtAddToCartQty;

        private final OrderModel orderModel =new OrderModel();
        private final ProductModel productModel = new ProductModel();

        private final CustomerModel customerModel = new CustomerModel();

        private final ObservableList<CartTM> cartData = FXCollections.observableArrayList();

        @FXML
          void btnAddToCartOnAction(ActionEvent event) {
                String productId = cmbProductId.getValue();
                String cartQtyString = txtAddToCartQty.getText();

                if (productId == null) {
                        new Alert(Alert.AlertType.WARNING, "Please select item..!").show();
                        return;
                }

                if (!cartQtyString.matches("^[0-9]+$")) {
                        new Alert(Alert.AlertType.WARNING, "Please enter valid quantity..!").show();
                        return;
                }

                int cartQty = Integer.parseInt(cartQtyString);
                int itemStockQty = Integer.parseInt(lblItemQty.getText());

                if (itemStockQty < cartQty) {
                        new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
                        return;
                }

                String productName = lblItemName.getText();
                double price = Double.parseDouble(lblItemPrice.getText());
                double total = price * cartQty;

                for (CartTM cartTM : cartData) {
                        if (cartTM.getProductId().equals(productId)) {
                                int newQty = cartTM.getQuantity() + cartQty;

                                if (itemStockQty < newQty) {
                                        new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
                                        return;
                                }
                                cartTM.setQuantity(newQty);
                                cartTM.setTotal(newQty * price);

                                txtAddToCartQty.setText("");
                                tblCart.refresh();

                                return;
                        }
                }

                Button removeBtn = new Button("Remove");
                CartTM cartTM = new CartTM(
                        productId,
                        productName,
                        cartQty,
                        price,
                        total,
                        removeBtn
                );

                removeBtn.setOnAction(action -> {
                        cartData.remove(cartTM);
                        tblCart.refresh();
                });

                txtAddToCartQty.setText("");
                cartData.add(cartTM);

                }

        @FXML
        void btnPlaceOrderOnAction(ActionEvent event) {
            if (tblCart.getItems().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please add items to cart..!").show();
                return;
            }

            if (cmbCustomerId.getValue().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select customer for place order..!").show();
                return;
            }

            String selectedCustomerId = cmbCustomerId.getValue();
            String orderId = lblOrderId.getText();
            LocalDateTime date = LocalDateTime.now();
            String paymentStatus=cmbPay.getValue();

            ArrayList<OrderDetailsDto> cartList = new ArrayList<>();
            for (CartTM cartTM : cartData) {
                OrderDetailsDto orderDetailsDTO = new OrderDetailsDto(
                        orderId,
                        cartTM.getProductId(),
                        cartTM.getQuantity(),
                        cartTM.getPrice()
                );
                cartList.add(orderDetailsDTO);
            }
            OrderDto orderDTO = new OrderDto(
                    orderId,
                    selectedCustomerId,
                    date,
                    paymentStatus,
                    cartList
            );

            try {
                boolean isPlaced = orderModel.placeOrder(orderDTO);

                if (isPlaced) {
                    loadOrderPayment();
                     new Alert(Alert.AlertType.INFORMATION, "Place order successful!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to place order..!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to place order..!").show();
            }

        }

    private void loadOrderPayment() throws IOException {
        Parent rootNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/OrderPayment.fxml")));
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(new Scene(rootNode));
        stage.show();
    }

    @FXML
        void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
             resetPage();
        }


        @FXML
        void cmbItemOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
                String selectedId = cmbProductId.getSelectionModel().getSelectedItem();
                ProductDto productDto = productModel.findById(selectedId);

                if (productDto != null) {
                        lblItemName.setText(productDto.getName());
                        lblItemQty.setText(String.valueOf(productDto.getStockQuantity()));
                        lblItemPrice.setText(String.valueOf(productDto.getPrice()));
                } else {
                        lblItemName.setText("");
                        lblItemQty.setText("");
                        lblItemPrice.setText("");
                }
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                colItemId.setCellValueFactory(new PropertyValueFactory<>("productId"));
                colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
                colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
                colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
                colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

                tblCart.setItems(cartData);

                cmbPay.setItems(FXCollections.observableArrayList("Cash","Card"));

                try {
                        resetPage();
                } catch (Exception e) {
                        e.printStackTrace();
                        new Alert(
                                Alert.AlertType.ERROR, "Fail to load data..!"
                        ).show();
                }        }

        private void resetPage() throws SQLException, ClassNotFoundException {
            lblItemName.setText("");
            lblItemPrice.setText("");
            lblItemQty.setText("");
            lblCustomerName.setText("");
            cmbPay.setValue(null);
                lblOrderId.setText(orderModel.getNextOrderId());
                orderDate.setText(LocalDate.now().toString());
                loadCustomerIds();
                loadItemIds();
        }

        private void loadItemIds() throws SQLException, ClassNotFoundException {
                cmbProductId.setItems(
                        FXCollections.observableArrayList(
                                productModel.getAllProductIds()
                        )
                );
        }

        private void loadCustomerIds() throws SQLException, ClassNotFoundException {
                cmbCustomerId.setItems(
                FXCollections.observableArrayList(
                        customerModel.getAllCustomerIds()
               )
      );
        }

        @FXML
        void cmbCustomerOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
                String selectedId = cmbCustomerId.getSelectionModel().getSelectedItem();
                String name = customerModel.findNameById(selectedId);
                lblCustomerName.setText(name);
        }
}
