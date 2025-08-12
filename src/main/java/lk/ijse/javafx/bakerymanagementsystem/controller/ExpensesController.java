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
import lk.ijse.javafx.bakerymanagementsystem.Dto.ExpensesDto;
import lk.ijse.javafx.bakerymanagementsystem.model.ExpensesModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExpensesController implements Initializable {

    @FXML
    private AnchorPane ancExpenses;

    @FXML
    private TableColumn<ExpensesDto, Double> colAmount;

    @FXML
    private TableColumn<ExpensesDto, String> colCategory;

    @FXML
    private TableColumn<ExpensesDto, LocalDate> colDate;

    @FXML
    private TableColumn<ExpensesDto, String> colExpensesId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<ExpensesDto> tblExpenses;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtCategory;

    @FXML
    private DatePicker txtDate;

    ExpensesModel expensesModel = new ExpensesModel();

    private final String amountPattern = "^\\\\d+(\\\\.\\\\d{1,2})?$\n";

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ExpensesDto expensesDto = tblExpenses.getSelectionModel().getSelectedItem();
        if (expensesDto == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Expenses to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Expenses?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = expensesModel.deleteExpenses(expensesDto.getExpensesId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Expenses deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Expenses!").show();
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to reset page.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        ExpensesDto expensesDto = createExpensesDtoFromInputs();
        try {
            boolean isAdded = expensesModel.saveExpenses(expensesDto);
            if (isAdded){
                new  Alert(Alert.AlertType.INFORMATION,"Expenses Added Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed save Expenses.").show();
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

        ExpensesDto expensesDto = createExpensesDtoFromInputs();
        try {
            boolean isUpdated = expensesModel.updateExpenses(expensesDto);
            if (isUpdated){
                new  Alert(Alert.AlertType.INFORMATION,"Expenses Updated Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed Update Expenses.").show();
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

    private ExpensesDto createExpensesDtoFromInputs() {
        return new ExpensesDto(
                lblId.getText().trim(),
                txtCategory.getText().trim(),
                Double.parseDouble(txtAmount.getText().trim()),
                 txtDate.getValue()
        );
    }

    private boolean validDataInputs() {
        boolean isValid = true;

        txtCategory.setStyle("");
        txtAmount.setStyle("");
        txtDate.setStyle("");

        if (txtCategory.getText().trim().isEmpty()) {
            txtCategory.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtAmount.getText().trim().isEmpty()) {
            txtAmount.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtDate.getValue() == null) {
            txtDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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
        ExpensesDto expensesDto = tblExpenses.getSelectionModel().getSelectedItem();
        if (expensesDto != null) {
            lblId.setText(expensesDto.getExpensesId());
            txtCategory.setText(expensesDto.getCategory());
            txtAmount.setText(String.valueOf(expensesDto.getAmount()));
            txtDate.setValue(LocalDate.parse(expensesDto.getDate()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        colExpensesId.setCellValueFactory(new PropertyValueFactory<>("expensesId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            ArrayList<ExpensesDto> expenses = expensesModel.getAllExpenses();
            if (expenses != null && !expenses.isEmpty()) {
                ObservableList<ExpensesDto> expensesDtos = FXCollections.observableArrayList(expenses);
                tblExpenses.setItems(expensesDtos);
            } else {
                tblExpenses.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load deliver.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtAmount.clear();
        txtCategory.clear();
        txtDate.setValue(null);
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(expensesModel.getNextId());
    }
}