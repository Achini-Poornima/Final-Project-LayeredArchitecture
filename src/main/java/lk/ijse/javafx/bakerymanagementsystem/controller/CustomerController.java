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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import lk.ijse.javafx.bakerymanagementsystem.model.CustomerModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    @FXML
    public TextField txtNic;
    @FXML
    public TextField txtEmail;
    @FXML
    private TableColumn<CustomerDto, String> colNic;
    @FXML
    private TableColumn<CustomerDto, String> colEmail;
    @FXML
    private AnchorPane ancCustomer;
    @FXML
    private TableColumn<CustomerDto,String> colName;
    @FXML
    private TableColumn<CustomerDto,String> colAddress;
    @FXML
    private TableColumn<CustomerDto,String> colContact;
    @FXML
    private TableColumn<CustomerDto,String> colId;
    @FXML
    private Label lblId;
    @FXML
    private TableView<CustomerDto> tblCustomer;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtName;

    private final String nicPattern =  "^[0-9]{9}[vVxX]$|^[0-9]{12}$";
    private final String phonePattern = "^[0-9]{10}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final CustomerModel customerModel = new CustomerModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerDto selectedUser = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Customer to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Customer?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = customerModel.deleteCustomer(selectedUser.getCustomerId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                    loadTable();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Customer!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        CustomerDto customerDto = createCustomerDtoFromInputs();

        try {
            boolean isSaved = customerModel.saveCustomer(customerDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
                resetPage();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save customer.").show();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving customer.").show();
        }
    }

    private CustomerDto createCustomerDtoFromInputs() {
        return new CustomerDto(
                lblId.getText().trim(),
                txtName.getText().trim(),
                txtAddress.getText().trim(),
                txtNic.getText().trim(),
                txtContact.getText().trim(),
                txtEmail.getText().trim()
        );
    }

    private boolean validDataInputs() {
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String nic = txtNic.getText().trim();
        String contact = txtContact.getText().trim();
        String email = txtEmail.getText().trim();

        boolean isValidNic = nic.matches(nicPattern);
        boolean isValidPhone = contact.matches(phonePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValid = true;

        txtName.setStyle("");
        txtAddress.setStyle("");
        txtNic.setStyle("");
        txtContact.setStyle("");
        txtEmail.setStyle("");

        if (name.isEmpty()) {
            txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (address.isEmpty()) {
            txtAddress.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValidNic) {
            txtNic.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValidPhone) {
            txtContact.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValidEmail) {
            txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValid) {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Invalid Input");
            warningAlert.setHeaderText("Please correct the highlighted fields.");
            warningAlert.setContentText("One or more inputs are invalid. Fields in red need to be corrected.");
            warningAlert.show();
        }

        return isValid;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        CustomerDto customerDto = createCustomerDtoFromInputs();

        try {
            boolean isUpdated = customerModel.updateCustomer(customerDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
                loadTable();
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update Customer!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Class not found error: " + e.getMessage()).show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        ArrayList<CustomerDto> customers = (ArrayList<CustomerDto>) CustomerModel.getAllCustomers();
        if (customers != null) {
            ObservableList<CustomerDto> customerList = FXCollections.observableArrayList(customers);
            tblCustomer.setItems(customerList);
        } else {
            tblCustomer.setItems(FXCollections.observableArrayList());
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        txtName.clear();
        txtAddress.clear();
        txtNic.clear();
        txtContact.clear();
        txtEmail.clear();
        tblCustomer.getSelectionModel().clearSelection();
        loadNextId();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(customerModel.getNextId());
    }

    @FXML
    public void setData(MouseEvent mouseEvent) {
        CustomerDto selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            lblId.setText(selectedCustomer.getCustomerId());
            txtName.setText(selectedCustomer.getName());
            txtAddress.setText(selectedCustomer.getAddress());
            txtNic.setText(selectedCustomer.getNic());
            txtContact.setText(selectedCustomer.getContact());
            txtEmail.setText(selectedCustomer.getEmail());
        }
    }

    @FXML
    public void btnSendMailOnAction(ActionEvent actionEvent) {
        CustomerDto selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a customer to send an email.").show();
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SendMail.fxml"));
            Parent load = fxmlLoader.load();

            SendMailPageController controller = fxmlLoader.getController();
            controller.setCustomerEmail(selectedItem.getEmail());

            Stage stage = new Stage();
            stage.setScene(new Scene(load));
            stage.setTitle("Bakery");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(txtContact.getScene().getWindow());
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load the Send Mail window.").show();
        }
    }
}
