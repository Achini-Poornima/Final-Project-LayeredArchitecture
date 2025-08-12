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
import lk.ijse.javafx.bakerymanagementsystem.Dto.DeliverDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.model.DeliverModel;
import lk.ijse.javafx.bakerymanagementsystem.model.IngredientModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class IngredientController implements Initializable {

    @FXML
    private AnchorPane ancIngredient;

    @FXML
    private TableColumn<IngredientDto, String> colExpireDate;

    @FXML
    private TableColumn<IngredientDto, String> colIngredientId;

    @FXML
    private TableColumn<IngredientDto, String> colName;

    @FXML
    private TableColumn<IngredientDto, Double> colQuantityAvailable;

    @FXML
    private TableColumn<IngredientDto, String> colSupplierId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<IngredientDto> tblIngredient;

    @FXML
    private DatePicker txtExpierDate;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtQuantityAvailable;

    @FXML
    private ComboBox<String> txtSupplierId;

    private final IngredientModel ingredientModel = new IngredientModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        IngredientDto selectedIngredient = tblIngredient.getSelectionModel().getSelectedItem();
        if (selectedIngredient == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an ingredient to delete").show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Ingredient.?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            try {
                boolean isDeleted = ingredientModel.deleteIngredient(selectedIngredient.getIngredientId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Ingredient Deleted Successfully.").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Ingredient.").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while deleting the ingredient!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        IngredientDto ingredientDto = createDeliverDtoFromInputs();
        try {
            boolean isAdded = ingredientModel.saveIngredient(ingredientDto);
            if (isAdded){
                new  Alert(Alert.AlertType.INFORMATION,"Ingredient Added Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed save Ingredient.").show();
            }
        }catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while adding the user!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        IngredientDto ingredientDto = createDeliverDtoFromInputs();
        try {
            boolean isUpdated = ingredientModel.updateIngredient(ingredientDto);
            if (isUpdated){
                new  Alert(Alert.AlertType.INFORMATION,"Ingredient Updated Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed Update Ingredient.").show();
            }
        }catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while adding the user!").show();
        }
    }

    private IngredientDto createDeliverDtoFromInputs() {
        double quantity = 0.0;
        try {
            quantity = Double.parseDouble(txtQuantityAvailable.getText());
        } catch (NumberFormatException e) {
        }
        return new IngredientDto(
                lblId.getText(),
                txtName.getText(),
                String.valueOf(txtExpierDate.getValue()),
                quantity,
                txtSupplierId.getValue()
        );
    }


    private boolean validDataInputs() {
        boolean isValid = true;

        txtName.setStyle("");
//        txtExpierDate.setStyle("");
        txtQuantityAvailable.setStyle("");
        txtSupplierId.setStyle("");

        if (txtName.getText().isEmpty()) {
            txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

//        if (txtExpierDate.getValue() == null) {
//            txtExpierDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
//            isValid = false;
//        }

        if (txtQuantityAvailable.getText().isEmpty()) {
            txtQuantityAvailable.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        } else {
            try {
                Double.parseDouble(txtQuantityAvailable.getText());
            } catch (NumberFormatException e) {
                txtQuantityAvailable.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
        }

        if (txtSupplierId.getValue() == null) {
            txtSupplierId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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
    void onSetData(MouseEvent event) {
        IngredientDto selectedIngredient = tblIngredient.getSelectionModel().getSelectedItem();
        if (selectedIngredient != null) {
            lblId.setText(selectedIngredient.getIngredientId());
            txtName.setText(selectedIngredient.getName());
            txtExpierDate.setValue(LocalDate.parse(selectedIngredient.getExpireDate()));
            txtQuantityAvailable.setText(String.valueOf(selectedIngredient.getQuantityAvailable()));
            txtSupplierId.setValue(selectedIngredient.getSupplierId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            resetPage();
            loadSupplierIds();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
        }
    }

    private void loadTable() {
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExpireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        colQuantityAvailable.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        try {
            ArrayList<IngredientDto> deliver = IngredientModel.getAllDelivers();
            if (deliver != null && !deliver.isEmpty()) {
                ObservableList<IngredientDto> IngredientList = FXCollections.observableArrayList(deliver);
                tblIngredient.setItems(IngredientList);
            } else {
                tblIngredient.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Ingredient.").show();
        }
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        txtSupplierId.setItems(FXCollections.observableArrayList(ingredientModel.getAllSupplierIds()));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtExpierDate.setValue(null);
        txtName.clear();
        txtQuantityAvailable.clear();
        txtSupplierId.setValue(null);
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(ingredientModel.getNextId());
    }
}
