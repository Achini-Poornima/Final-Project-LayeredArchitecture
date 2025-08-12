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
import lk.ijse.javafx.bakerymanagementsystem.Dto.SupplierDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {

    @FXML
    private AnchorPane ancSupplier;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<SupplierDto, String> colEmail;

    @FXML
    private TableColumn<SupplierDto, String> colName;

    @FXML
    private TableColumn<SupplierDto, String> colSuppliedIngredient;

    @FXML
    private TableColumn<SupplierDto, String> colSupplierId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<SupplierDto> tblSupplier;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtSuppliedIngredient;

    @FXML
    private TextField txtaddress;

    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplierDto selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Supplier?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = supplierModel.deleteUser(selectedSupplier.getSupplierId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Supplier!").show();
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
            new Alert(Alert.AlertType.ERROR, "Failed to load next Supplier ID.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDatainputs()) return;

        SupplierDto supplierDto = createSupplierDtoFromInputs();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to save this Supplier.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isSaved = supplierModel.saveSupplier(supplierDto);
                if (isSaved) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier saved successfully.").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save supplier.").show();
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while adding the user!").show();
            }
        }
    }

    private SupplierDto createSupplierDtoFromInputs() {
        return new SupplierDto(
                lblId.getText(),
                txtName.getText(),
                txtSuppliedIngredient.getText(),
                txtaddress.getText(),
                txtEmail.getText()
        );
    }

    private boolean validDatainputs() {
        String name = txtName.getText().trim();
        String supIngre = txtSuppliedIngredient.getText().trim();
        String address = txtaddress.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || supIngre.isEmpty() || address.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields.").show();
            return false;
        }
        return true;
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDatainputs()) return;

        SupplierDto supplierDto = createSupplierDtoFromInputs();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to update this User?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isUpdated = supplierModel.updateUser(supplierDto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to update Supplier!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
        SupplierDto supplierDto = tblSupplier.getSelectionModel().getSelectedItem();
        if (supplierDto != null) {
            lblId.setText(supplierDto.getSupplierId());
            txtName.setText(supplierDto.getName());
            txtSuppliedIngredient.setText(supplierDto.getSuppliedIngredient());
            txtaddress.setText(supplierDto.getAddress());
            txtEmail.setText(supplierDto.getEmail());
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
            new Alert(Alert.AlertType.ERROR, "Failed Load Data..").show();
        }
    }

    private void loadTable() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSuppliedIngredient.setCellValueFactory(new PropertyValueFactory<>("suppliedIngredient"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            ArrayList<SupplierDto> suppliers = supplierModel.getAllSuppliers();
            if (suppliers != null && !suppliers.isEmpty()) {
                ObservableList<SupplierDto> userList = FXCollections.observableArrayList(suppliers);
                tblSupplier.setItems(userList);
            } else {
                tblSupplier.setItems(FXCollections.observableArrayList());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load suppliers.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtName.clear();
        txtSuppliedIngredient.clear();
        txtaddress.clear();
        txtEmail.clear();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(supplierModel.getNextId());
    }

}
