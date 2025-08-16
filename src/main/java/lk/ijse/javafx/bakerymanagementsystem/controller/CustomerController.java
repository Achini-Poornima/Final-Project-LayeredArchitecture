package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CustomerTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.CustomerBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {
    public TextField txtNic;
    public TextField txtEmail;
    public Label lblId;
    public TextField txtAddress;
    public TextField txtContact;
    public TextField txtName;
    public AnchorPane ancCustomer;

    public TableColumn<CustomerTM, String> colNic;
    public TableColumn<CustomerTM, String> colEmail;
    public TableColumn<CustomerTM,String> colName;
    public TableColumn<CustomerTM,String> colAddress;
    public TableColumn<CustomerTM,String> colContact;
    public TableColumn<CustomerTM,String> colId;
    public TableView<CustomerTM> tblCustomer;

    private final CustomerBO customerBO = BOFactory.getInstance().getBo(BOTypes.CUSTOMER);
    private final String nicPattern =  "^[0-9]{9}[vVxX]$|^[0-9]{12}$";
    private final String phonePattern = "^[0-9]{10}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CustomerTM selectedUser = tblCustomer.getSelectionModel().getSelectedItem();
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
                String id = lblId.getText();
                boolean isDeleted = customerBO.deleteCustomer(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Customer!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer..!").show();
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
            customerBO.saveCustomer(customerDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save customer..!").show();
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
            customerBO.updateCustomer(customerDto);
            new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update customer..!").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblCustomer.setItems(FXCollections.observableArrayList(
                customerBO.getAllCustomer().stream().map(customerDTO ->
                        new CustomerTM(
                                customerDTO.getCustomerId(),
                                customerDTO.getName(),
                                customerDTO.getAddress(),
                                customerDTO.getNic(),
                                customerDTO.getContact(),
                                customerDTO.getEmail()
                        )).toList()
        ));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        txtName.clear();
        txtAddress.clear();
        txtNic.clear();
        txtContact.clear();
        txtEmail.clear();
        tblCustomer.getSelectionModel().clearSelection();
        loadNextId();
        loadTable();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = customerBO.getNextId();
        lblId.setText(nextId);
    }

    @FXML
    public void setData(MouseEvent mouseEvent) {
        CustomerTM selectedCustomer = tblCustomer.getSelectionModel().getSelectedItem();
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
        CustomerTM selectedItem = tblCustomer.getSelectionModel().getSelectedItem();

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
