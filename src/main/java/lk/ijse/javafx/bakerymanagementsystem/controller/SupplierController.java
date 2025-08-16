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
import lk.ijse.javafx.bakerymanagementsystem.Dto.SupplierDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CustomerTM;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.SupplierTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.SupplierBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.SuperDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SupplierDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.SupplierDAOImpl;
import lk.ijse.javafx.bakerymanagementsystem.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierController implements Initializable {
    public AnchorPane ancSupplier;
    public TableColumn<SupplierTM, String> colAddress;
    public TableColumn<SupplierTM, String> colEmail;
    public TableColumn<SupplierTM, String> colName;
    public TableColumn<SupplierTM, String> colSuppliedIngredient;
    public TableColumn<SupplierTM, String> colSupplierId;
    public Label lblId;
    public TableView<SupplierTM> tblSupplier;
    public TextField txtEmail;
    public TextField txtName;
    public TextField txtSuppliedIngredient;
    public TextField txtAddress;

    private final SupplierBO  supplierBO = BOFactory.getInstance().getBo(BOTypes.SUPPLIER);
    private final SupplierDAO supplierDAO = new SupplierDAOImpl();
    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        SupplierTM selectedSupplier = tblSupplier.getSelectionModel().getSelectedItem();
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
                String id = lblId.getText();
                boolean isDeleted = supplierBO.deleteSupplier(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Supplier deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Supplier!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete supplier..!").show();
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
                supplierBO.saveSupplier(supplierDto);
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplier saved successfully.").show();
            } catch (DuplicateException e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save supplier..!").show();
            }
        }
    }

    private SupplierDto createSupplierDtoFromInputs() {
        return new SupplierDto(
                lblId.getText(),
                txtName.getText(),
                txtSuppliedIngredient.getText(),
                txtAddress.getText(),
                txtEmail.getText()
        );
    }

    private boolean validDatainputs() {
        String name = txtName.getText().trim();
        String supIngre = txtSuppliedIngredient.getText().trim();
        String address = txtAddress.getText().trim();
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

        try {
            supplierBO.updateSupplier(supplierDto);
            new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update supplier..!").show();
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
        SupplierTM supplierDto = tblSupplier.getSelectionModel().getSelectedItem();
        if (supplierDto != null) {
            lblId.setText(supplierDto.getSupplierId());
            txtName.setText(supplierDto.getName());
            txtSuppliedIngredient.setText(supplierDto.getSuppliedIngredient());
            txtAddress.setText(supplierDto.getAddress());
            txtEmail.setText(supplierDto.getEmail());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSuppliedIngredient.setCellValueFactory(new PropertyValueFactory<>("suppliedIngredient"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed Load Data..").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblSupplier.setItems(FXCollections.observableArrayList(
                supplierBO.getAllSupplier().stream().map(supplierDTO ->
                        new SupplierTM(
                                supplierDTO.getSupplierId(),
                                supplierDTO.getName(),
                                supplierDTO.getSuppliedIngredient(),
                                supplierDTO.getAddress(),
                                supplierDTO.getEmail()
                        )).toList()
        ));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTable();
        txtName.clear();
        txtSuppliedIngredient.clear();
        txtAddress.clear();
        txtEmail.clear();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(supplierModel.getNextId());
    }

}
