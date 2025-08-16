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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.EmployeeTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.EmployeeBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl.EmployeeBOImpl;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {
    public AnchorPane ancEmployee;
    public TableColumn<EmployeeTM, String> colAddress;
    public TableColumn<EmployeeTM, String> colContactNo;
    public TableColumn<EmployeeTM, String> colDateOfBirth;
    public TableColumn<EmployeeTM, String> colEmail;
    public TableColumn<EmployeeTM, String> colHireDate;
    public TableColumn<EmployeeTM, String> colName;
    public TableColumn<EmployeeTM, String> colRole;
    public TableColumn<EmployeeTM, String> colId;
    public Label lblId;
    public TableView<EmployeeTM> tblEmployee;
    public TextField txtAddress;
    public TextField txtContactNo;
    public DatePicker txtDateOfBirth;
    public TextField txtEmail;
    public DatePicker txtJoinDate;
    public TextField txtName;
    public TextField txtRole;

    private final EmployeeBO employeeBO = BOFactory.getInstance().getBo(BOTypes.EMPLOYEE);
    private final String contactPattern = "^[0-9]{10}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        EmployeeTM selectedUser = (EmployeeTM) tblEmployee.getSelectionModel().getSelectedItem();
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
                String employeeId = lblId.getText();
                boolean isDeleted = employeeBO.deleteEmployee(employeeId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Employee!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete Employee..!").show();
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
            employeeBO.saveEmployee(employeeDto);
            new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
            resetPage();

        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save employee..!").show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDateInpts()) return;
        EmployeeDto employeeDto = createEmployeeDtoFromInputs();
        try {
            employeeBO.updateEmployee(employeeDto);
            new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
            resetPage();

        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update employee..!").show();
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
        EmployeeTM selectedEmployee = tblEmployee.getSelectionModel().getSelectedItem();
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
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

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
        loadTable();
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
        String nextId = lblId.getText();
        lblId.setText(nextId);

    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblEmployee.setItems(FXCollections.observableArrayList(
                employeeBO.getAllEmployee().stream().map(employeeDto ->
                        new EmployeeTM(
                                employeeDto.getEmployeeId(),
                                employeeDto.getName(),
                                employeeDto.getContactNo(),
                                employeeDto.getEmail(),
                                employeeDto.getAddress(),
                                employeeDto.getJoinDate(),
                                employeeDto.getDateOfBirth(),
                                employeeDto.getRole()
                        )).toList()
        ));
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