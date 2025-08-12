package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.EmployeeDto;
import lk.ijse.javafx.bakerymanagementsystem.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private AnchorPane ancEmployee;

    @FXML
    private TableColumn<EmployeeDto, String> colAddress;

    @FXML
    private TableColumn<EmployeeDto, String> colContactNo;

    @FXML
    private TableColumn<EmployeeDto, String> colDateOfBirth;

    @FXML
    private TableColumn<EmployeeDto, String> colEmail;

    @FXML
    private TableColumn<EmployeeDto, String> colHireDate;

    @FXML
    private TableColumn<EmployeeDto, String> colName;

    @FXML
    private TableColumn<EmployeeDto, String> colRole;

    @FXML
    private TableColumn<EmployeeDto, String> colid;

    @FXML
    private Label lblId;

    @FXML
    private TableView<EmployeeDto> tblEmployee;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtContactNo;

    @FXML
    private DatePicker txtDateOfBirth;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtJoinDate;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtRole;

    private final EmployeeModel employeeModel = new EmployeeModel();
    private final String contactPattern = "^[0-9]{10}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        EmployeeDto selectedUser = (EmployeeDto) tblEmployee.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Employee to delete.").show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Employee?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = employeeModel.deleteEmployee(selectedUser.getEmployeeId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Employee!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDateInpts()) return;
        EmployeeDto employeeDto = createEmployeeDtoFromInputs();

        try {
            boolean isSaved = employeeModel.saveEmployee(employeeDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to save Employee!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDateInpts()) return;
        EmployeeDto employeeDto = createEmployeeDtoFromInputs();
        try {
            boolean isUpdated = employeeModel.updateEmployee(employeeDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update Employee!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private EmployeeDto createEmployeeDtoFromInputs() {
        return new EmployeeDto(
                lblId.getText(),
                txtName.getText(),
                txtContactNo.getText(),
                txtEmail.getText(),
                txtAddress.getText(),
                txtJoinDate.getValue(),
                txtDateOfBirth.getValue(),
                txtRole.getText());

    }

    @FXML
    void onSetData(MouseEvent event) {
        EmployeeDto selectedEmployee = tblEmployee.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            lblId.setText(selectedEmployee.getEmployeeId());
            txtName.setText(selectedEmployee.getName());
            txtAddress.setText(selectedEmployee.getAddress());
            txtContactNo.setText(selectedEmployee.getContactNo());
            txtEmail.setText(selectedEmployee.getEmail());
            txtJoinDate.setValue(selectedEmployee.getJoinDate());
            txtDateOfBirth.setValue(selectedEmployee.getDateOfBirth());
            txtRole.setText(selectedEmployee.getRole());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            txtJoinDate.setValue(LocalDate.now());
            loadTable();
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtName.clear();
        txtAddress.clear();
        txtContactNo.clear();
        txtEmail.clear();
        txtRole.clear();
        txtDateOfBirth.setValue(null);
        txtJoinDate.setValue(LocalDate.now());
        tblEmployee.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(employeeModel.getNextId());

    }

    private void loadTable() {
        colid.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            ArrayList<EmployeeDto> employees = employeeModel.getAllEmployees();
            if (employees != null) {
                ObservableList<EmployeeDto> employeeList = FXCollections.observableArrayList(employees);
                tblEmployee.setItems(employeeList);
            } else {
                tblEmployee.setItems(FXCollections.observableArrayList());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to load employees.").show();
        }
    }

    private boolean validDateInpts() {
        String name = txtName.getText().trim();
        String contact = txtContactNo.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();
        String role = txtRole.getText().trim();

        boolean isValidContact = contact.matches(contactPattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValid = true;

        txtName.setStyle("");
        txtContactNo.setStyle("");
        txtEmail.setStyle("");
        txtAddress.setStyle("");
        txtJoinDate.setStyle("");
        txtDateOfBirth.setStyle("");
        txtRole.setStyle("");

        if (name.isEmpty()) {
            txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (!isValidContact) {
            txtContactNo.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (!isValidEmail) {
            txtEmail.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (address.isEmpty()) {
            txtAddress.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtJoinDate.getValue() == null) {
            txtJoinDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtDateOfBirth.getValue() == null) {
            txtDateOfBirth.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (role.isEmpty()) {
            txtRole.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        return isValid;
    }
}