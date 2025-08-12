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
import lk.ijse.javafx.bakerymanagementsystem.Dto.ProductDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.model.ProductModel;
import lk.ijse.javafx.bakerymanagementsystem.model.SupplierModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private AnchorPane ancProduct;

    @FXML
    private ComboBox<String> cmbSupplierId;

    @FXML
    private TableColumn<ProductDto,String> colId;

    @FXML
    private TableColumn<ProductDto,String> colName;

    @FXML
    private TableColumn<ProductDto,Double> colPrice;

    @FXML
    private TableColumn<ProductDto,Integer> colStockQuantity;

    @FXML
    private TableColumn<ProductDto,String> colSupplierId;

    @FXML
    private Label lblId;

    @FXML
    private Label lblId1;

    @FXML
    private TableView<ProductDto> tblProduct;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtStockQuantity;

    private final String pricePattern = "^\\d+(\\.\\d{1,2})?$";

    private final ProductModel productModel = new ProductModel();
    private final SupplierModel supplierModel = new SupplierModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ProductDto selectedProduct = tblProduct.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Product Details to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Product?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = productModel.deleteProduct(selectedProduct.getProductId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Product Details deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Product Details!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Cannot delete: This product is associated with existing orders.").show();
            }
        }

    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        ProductDto productDto = createProductDtoFromInputs();

        try {
            boolean isAdded = productModel.saveProduct(productDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Product added successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to add Product!").show();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while adding the Salary!").show();
        }
    }

    private ProductDto createProductDtoFromInputs() {
        return new ProductDto(
                lblId.getText(),
                txtName.getText(),
                Double.parseDouble(txtPrice.getText()),
                Integer.parseInt(txtStockQuantity.getText()),
                cmbSupplierId.getValue()
        );
    }

    private boolean validDateInputs() {
        boolean isValid = true;

        String name = txtName.getText().trim();
        String priceText = txtPrice.getText().trim();
        String quantityText = txtStockQuantity.getText().trim();
        String supplierId = cmbSupplierId.getValue() != null ? cmbSupplierId.getValue().trim() : "";

        txtName.setStyle(null);
        txtPrice.setStyle(null);
        txtStockQuantity.setStyle(null);
        cmbSupplierId.setStyle(null);

        if (name.isEmpty()) {
            txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        double price = 0;
        if (priceText.isEmpty() || !priceText.matches(pricePattern)) {
            txtPrice.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else {
            price = Double.parseDouble(priceText);
        }

        int stockQuantity = 0;
        try {
            stockQuantity = Integer.parseInt(quantityText);
            if (stockQuantity < 0) {
                txtStockQuantity.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            txtStockQuantity.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (supplierId.isEmpty()) {
            cmbSupplierId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        return isValid;
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        ProductDto productDto = createProductDtoFromInputs();
        try {
            boolean isUpdated = productModel.updateProduct(productDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Product Details updated successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update Product Details!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
        ProductDto productDto = tblProduct.getSelectionModel().getSelectedItem();
        if (productDto != null){
            lblId.setText(productDto.getProductId());
            txtName.setText(productDto.getName());
            txtPrice.setText(String.valueOf(productDto.getPrice()));
            txtStockQuantity.setText(String.valueOf(productDto.getStockQuantity()));
            cmbSupplierId.setValue(productDto.getSupplierId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            loadNextId();
            resetPage();
            loadSupplierIds();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to Load Data..").show();
        }
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> supplierIds = supplierModel.getAllSupplierIds();
        ObservableList<String> observableListIds = FXCollections.observableArrayList(supplierIds);
        cmbSupplierId.setItems(observableListIds);
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtName.clear();
        txtPrice.clear();
        txtStockQuantity.clear();
        cmbSupplierId.setValue(null);
        tblProduct.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(productModel.getNextId());
    }

    private void loadTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStockQuantity.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        try {
            ArrayList<ProductDto> product = productModel.getAllProducts();
            if (product != null && !product.isEmpty()){
                ObservableList<ProductDto> productList = FXCollections.observableArrayList(product);
                tblProduct.setItems(productList);
            }else {
                tblProduct.setItems(FXCollections.observableArrayList());
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Product Details.").show();
        }
    }
}
