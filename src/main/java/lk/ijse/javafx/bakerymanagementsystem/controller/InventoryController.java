package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.InventoryDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CustomerTM;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.InventoryTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.InventoryBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.InventoryDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.InventoryDAOImpl;
import lk.ijse.javafx.bakerymanagementsystem.model.InventoryModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    public ComboBox<String> txtProductId;
    public ComboBox<String> txtIngredientId;
    public TableColumn<InventoryTM, String> colIngredientId;
    public TableColumn<InventoryTM, String> colProductId;
    public TableColumn<InventoryTM, Integer> colStockQuantity;
    public TableColumn<InventoryTM, String> colInventoryId;
    public Label lblId;
    public TableView<InventoryTM> tblInventory;
    public TextField txtStockQuantity;

    private final InventoryDAO inventoryDAO = new InventoryDAOImpl();
    private final InventoryBO inventoryBO = BOFactory.getInstance().getBo(BOTypes.INVENTORY);
    private final InventoryModel inventoryModel = new InventoryModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        InventoryTM selectedInventory = tblInventory.getSelectionModel().getSelectedItem();
        if (selectedInventory == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an inventory record to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this inventory record?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String id = lblId.getText();
                boolean isDeleted = inventoryBO.deleteInventory(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Inventory deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Inventory!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete inventory..!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            resetPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to reset page.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validInputFields()) return;

        InventoryDto inventoryDto = createInventoryDtoFromInputs();

        try {
            inventoryBO.saveInventory(inventoryDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Inventory saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save inventory..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validInputFields()) return;

        InventoryDto inventoryDto = createInventoryDtoFromInputs();

        try {
            inventoryBO.updateInventory(inventoryDto);
            new Alert(Alert.AlertType.INFORMATION, "Inventory updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update inventory..!").show();
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
        InventoryTM inventoryDto = tblInventory.getSelectionModel().getSelectedItem();
        if (inventoryDto != null) {
            lblId.setText(inventoryDto.getInventoryId());
            txtStockQuantity.setText(String.valueOf(inventoryDto.getStockQuantity()));
            txtProductId.setValue(inventoryDto.getProductId());
            txtIngredientId.setValue(inventoryDto.getIngredientId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colStockQuantity.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));

        try {
            loadTable();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load initial data.").show();
        }
    }

    private boolean validInputFields() {
        boolean isValid = true;

        txtStockQuantity.setStyle(null);
        txtProductId.setStyle(null);
        txtIngredientId.setStyle(null);

        if (txtStockQuantity.getText().isEmpty()) {
            txtStockQuantity.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else {
            try {
                Integer.parseInt(txtStockQuantity.getText());
            } catch (NumberFormatException e) {
                txtStockQuantity.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                new Alert(Alert.AlertType.WARNING, "Stock quantity must be a valid number.").show();
                return false;
            }
        }

        if (txtProductId.getValue() == null) {
            txtProductId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (txtIngredientId.getValue() == null) {
            txtIngredientId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (!isValid) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields.").show();
        }

        return isValid;
    }

    private InventoryDto createInventoryDtoFromInputs() {
        return new InventoryDto(
                lblId.getText(),
                Integer.parseInt(txtStockQuantity.getText()),
                txtProductId.getValue(),
                txtIngredientId.getValue()
        );
    }

    private void loadIngredientIds() {
        try {
            ArrayList<String> ingredientIds = inventoryModel.getIngredientIds();
            txtIngredientId.setItems(FXCollections.observableArrayList(ingredientIds));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load ingredient IDs.").show();
        }
    }

    private void loadProductIds() {
        try {
            ArrayList<String> productIds = inventoryModel.getProductIds();
            txtProductId.setItems(FXCollections.observableArrayList(productIds));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load product IDs.").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblInventory.setItems(FXCollections.observableArrayList(
                inventoryBO.getAllInventory().stream().map(inventoryDTO ->
                        new InventoryTM(
                                inventoryDTO.getInventoryId(),
                                inventoryDTO.getStockQuantity(),
                                inventoryDTO.getProductId(),
                                inventoryDTO.getIngredientId()
                        )).toList()
        ));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTable();
        loadIngredientIds();
        loadProductIds();
        txtStockQuantity.clear();
        txtProductId.setValue(null);
        txtIngredientId.setValue(null);
        tblInventory.getSelectionModel().clearSelection();
        clearStyles();
    }

    private void clearStyles() {
        txtStockQuantity.setStyle(null);
        txtProductId.setStyle(null);
        txtIngredientId.setStyle(null);
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = inventoryBO.getNextId();
        lblId.setText(nextId);
    }
}
