package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.IngredientDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.IngredientTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.IngredientBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.IngredientDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.IngredientDAOImpl;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class IngredientController implements Initializable {
    public AnchorPane ancIngredient;
    public TableColumn<IngredientTM, String> colExpireDate;
    public TableColumn<IngredientTM, String> colIngredientId;
    public TableColumn<IngredientTM, String> colName;
    public TableColumn<IngredientTM, Double> colQuantityAvailable;
    public TableColumn<IngredientTM, String> colSupplierId;
    public Label lblId;
    public TableView<IngredientTM> tblIngredient;
    public DatePicker txtExpierDate;
    public TextField txtName;
    public TextField txtQuantityAvailable;
    public ComboBox<String> txtSupplierId;

    private final IngredientDAO ingredientDAO = new IngredientDAOImpl();
    private final IngredientBO ingredientBO = BOFactory.getInstance().getBo(BOTypes.INGREDIENT);
//    private final IngredientModel ingredientModel = new IngredientModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        IngredientTM selectedIngredient = tblIngredient.getSelectionModel().getSelectedItem();
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
                    String id = lblId.getText();
                    boolean isDeleted = ingredientBO.deleteIngredient(id);
                    if (isDeleted) {
                        new Alert(Alert.AlertType.INFORMATION, "Ingredient deleted successfully!").show();
                        resetPage();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Failed to delete Ingredient!").show();
                    }
                } catch (InUseException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Fail to delete Ingredient..!").show();
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
            ingredientBO.saveIngredient(ingredientDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Ingredient saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save ingredient..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        IngredientDto ingredientDto = createDeliverDtoFromInputs();
        try {
            ingredientBO.updateIngredient(ingredientDto);
            new Alert(Alert.AlertType.INFORMATION, "Ingredient updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update ingredient..!").show();
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
        IngredientTM selectedIngredient = tblIngredient.getSelectionModel().getSelectedItem();
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
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("ingredientId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colExpireDate.setCellValueFactory(new PropertyValueFactory<>("expireDate"));
        colQuantityAvailable.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblIngredient.setItems(FXCollections.observableArrayList(
                ingredientBO.getAllIngredient().stream().map(ingredientDTO ->
                        new IngredientTM(
                                ingredientDTO.getIngredientId(),
                                ingredientDTO.getName(),
                                ingredientDTO.getExpireDate(),
                                ingredientDTO.getQuantityAvailable(),
                                ingredientDTO.getSupplierId()
                        )).toList()
        ));
    }

    private void loadSupplierIds() throws SQLException, ClassNotFoundException {
        txtSupplierId.setItems(FXCollections.observableArrayList(ingredientDAO.getAllSupplierIds()));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadNextId();
        loadSupplierIds();
        txtExpierDate.setValue(null);
        txtName.clear();
        txtQuantityAvailable.clear();
        txtSupplierId.setValue(null);
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = ingredientBO.getNextId();
        lblId.setText(new String(nextId));
    }
}
