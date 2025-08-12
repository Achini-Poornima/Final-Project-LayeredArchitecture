package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderPaymentController implements Initializable {

    @FXML
    private Label lblAddress;

    @FXML
    private Label lblContact;

    @FXML
    private Label lblCustomerName;

    private CustomerDto customer = new CustomerDto();

    public void setCustomerData(CustomerDto customer) {
        this.customer = customer;

        if (customer != null) {
            lblCustomerName.setText(customer.getName());
            lblContact.setText(customer.getContact());
            lblAddress.setText(customer.getAddress());
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Stage stage = (Stage) lblCustomerName.getScene().getWindow();
        stage.close();
    }

    @FXML
    void btnConfirmPaymentOnAction(ActionEvent event) {
        System.out.println("Payment confirmed for: " + customer.getName());

        Stage stage = (Stage) lblCustomerName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setCustomerData(customer);
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to load customer details.");
        }
    }
}
