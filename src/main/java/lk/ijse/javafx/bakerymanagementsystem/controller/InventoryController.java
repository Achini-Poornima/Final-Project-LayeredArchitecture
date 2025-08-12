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
import lk.ijse.javafx.bakerymanagementsystem.model.InventoryModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    @FXML
    private ComboBox<String> txtProductId;

    @FXML
    private ComboBox<String> txtIngredientId;

    @FXML
    private TableColumn<InventoryDto, String> colIngredientId;

    @FXML
    private TableColumn<InventoryDto, String> colProductId;

    @FXML
    private TableColumn<InventoryDto, Integer> colStockQuantity;

    @FXML
    private TableColumn<InventoryDto, String> colInventoryId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<InventoryDto> tblInventory;

    @FXML
    private TextField txtStockQuantity;

    private final InventoryModel inventoryModel = new InventoryModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        InventoryDto selectedInventory = tblInventory.getSelectionModel().getSelectedItem();
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
                boolean isDeleted = inventoryModel.deleteInventory(selectedInventory.getInventoryId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Inventory record deleted successfully!").show();
                    loadTable();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete inventory record!").show();
                }
            } catch (Exception e) {
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
            new Alert(Alert.AlertType.ERROR, "Failed to reset page.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validInputFields()) return;

        InventoryDto inventoryDto = createInventoryDtoFromInputs();

        try {
            boolean isAdded = inventoryModel.saveInventory(inventoryDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Inventory record added successfully!").show();
                loadTable();
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to add inventory record!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validInputFields()) return;

        InventoryDto inventoryDto = createInventoryDtoFromInputs();

        try {
            boolean isUpdated = inventoryModel.updateInventory(inventoryDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Inventory record updated successfully!").show();
                loadTable();
                resetPage();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update inventory record!").show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
        InventoryDto inventoryDto = tblInventory.getSelectionModel().getSelectedItem();
        if (inventoryDto != null) {
            lblId.setText(inventoryDto.getInventoryId());
            txtStockQuantity.setText(String.valueOf(inventoryDto.getStockQuantity()));
            txtProductId.setValue(inventoryDto.getProductId());
            txtIngredientId.setValue(inventoryDto.getIngredientId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            loadProductIds();
            loadIngredientIds();
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

    private void loadTable() {
        colInventoryId.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        colStockQuantity.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));

        try {
            ArrayList<InventoryDto> inventoryList = inventoryModel.getAllInventory();
            tblInventory.setItems(FXCollections.observableArrayList(inventoryList));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory data.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
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
        lblId.setText(inventoryModel.getNextId());
    }
}
