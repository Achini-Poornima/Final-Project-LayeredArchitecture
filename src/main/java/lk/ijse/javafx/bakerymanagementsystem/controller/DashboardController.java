package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class DashboardController {

    public AnchorPane ancLogin;
    public Button btnUser;
    public Button btnCustomer;
    public Button btnOrder;
    public Button btnDelivery;
    public Button btnEmployee;
    public Button btnSupplier;
    public Button btnProduct;
    public Button btnPayment;
    public Button btnInventory;
    public Button btnSalary;
    public Button btnIngrediant;
    public Button btnExpenses;
    public Button btnAttendance;
    public Button btnLogout;
    @FXML
    private AnchorPane ancMainContainer;

private static final String DEFAULT_STYLE ="-fx-border-color: #fff; -fx-text-fill: #000000; -fx-border-width: 1; -fx-background-color: #a29bfe; -fx-background-radius: 3; -fx-border-radius: 3;";
private static final String ACTIVE_STYLE ="-fx-border-color: #5f27cd; -fx-text-fill: #5f27cd; -fx-border-width: 1; -fx-background-color: #a29bfe; -fx-background-radius: 3; -fx-border-radius: 3;";

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        changeColor();
        btnCustomer.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Customer.fxml");
    }

    @FXML
    void btnDeliverOnAction(ActionEvent event) {
        changeColor();
        btnDelivery.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Deliver.fxml");}

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        changeColor();
        btnEmployee.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Employee.fxml");
    }

    @FXML
    void btnExpensesOnAction(ActionEvent event) {
        changeColor();
        btnExpenses.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Expenses.fxml");
    }

    @FXML
    void btnIngrediantOnAction(ActionEvent event) {
        changeColor();
        btnIngrediant.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Ingredient.fxml");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        changeColor();
        btnInventory.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Inventory.fxml");
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {
        changeColor();
        btnOrder.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Order.fxml");
    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {
        changeColor();
        btnPayment.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Payment.fxml");
    }

    @FXML
    void btnProductOnAction(ActionEvent event) {
        changeColor();
        btnProduct.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Product.fxml");
    }

    @FXML
    void btnSalaryOnAction(ActionEvent event) {
        changeColor();
        btnSalary.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Salary.fxml");
    }

    @FXML
    void btnSuppliersOnAction(ActionEvent event) {
        changeColor();
        btnSupplier.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Supplier.fxml");}

    @FXML
    void btnUserOnAction(ActionEvent event) {
        changeColor();
        btnUser.setStyle(ACTIVE_STYLE);
        navigateTo("/view/User.fxml");
    }
    public void btnAttendanceOnAction(ActionEvent actionEvent) {
        changeColor();
        btnAttendance.setStyle(ACTIVE_STYLE);
        navigateTo("/view/Attendance.fxml");
    }

    private void navigateTo(String path) {
        try{

            ancMainContainer.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());
            ancMainContainer.getChildren().add(anchorPane);
        }catch (IOException e){
            new Alert(Alert.AlertType.ERROR,"Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }


    public void btnLogoutOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/ALogin.fxml"));
                ancMainContainer.getScene().setRoot(root);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Error loading login page", ButtonType.OK).show();
                e.printStackTrace();
            }
        }
    }

    private void changeColor(){
        btnUser.setStyle(DEFAULT_STYLE);
        btnCustomer.setStyle(DEFAULT_STYLE);
        btnOrder.setStyle(DEFAULT_STYLE);
        btnDelivery.setStyle(DEFAULT_STYLE);
        btnEmployee.setStyle(DEFAULT_STYLE);
        btnSupplier.setStyle(DEFAULT_STYLE);
        btnProduct.setStyle(DEFAULT_STYLE);
        btnPayment.setStyle(DEFAULT_STYLE);
        btnInventory.setStyle(DEFAULT_STYLE);
        btnSalary.setStyle(DEFAULT_STYLE);
        btnIngrediant.setStyle(DEFAULT_STYLE);
        btnExpenses.setStyle(DEFAULT_STYLE);
        btnAttendance.setStyle(DEFAULT_STYLE);
        btnLogout.setStyle(DEFAULT_STYLE);
    }

}

