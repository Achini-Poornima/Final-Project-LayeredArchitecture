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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.ExpensesTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.ExpensesBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExpensesController implements Initializable {
    public AnchorPane ancExpenses;
    public TableColumn<ExpensesTM, Double> colAmount;
    public TableColumn<ExpensesTM, String> colCategory;
    public TableColumn<ExpensesTM, LocalDate> colDate;
    public TableColumn<ExpensesTM, String> colExpensesId;
    public Label lblId;
    public TableView<ExpensesTM> tblExpenses;
    public TextField txtAmount;
    public TextField txtCategory;
    public DatePicker txtDate;

    private final ExpensesBO expensesBO = BOFactory.getInstance().getBo(BOTypes.EXPENSES);

    private final String amountPattern = "^\\\\d+(\\\\.\\\\d{1,2})?$\n";

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ExpensesTM expensesDto = tblExpenses.getSelectionModel().getSelectedItem();
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
                String expensesId = lblId.getText();
                boolean isDeleted = expensesBO.deleteExpenses(expensesId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Expenses deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Expenses!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete customer..!").show();
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
            expensesBO.saveExpenses(expensesDto);
            new  Alert(Alert.AlertType.INFORMATION,"Expenses Added Successfully.").show();
            resetPage();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save customer..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        ExpensesDto expensesDto = createExpensesDtoFromInputs();
        try {
            expensesBO.updateExpenses(expensesDto);
            new  Alert(Alert.AlertType.INFORMATION,"Expenses Updated Successfully.").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update customer..!").show();
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
        ExpensesTM expensesDto = tblExpenses.getSelectionModel().getSelectedItem();
        if (expensesDto != null) {
            lblId.setText(expensesDto.getExpensesId());
            txtCategory.setText(expensesDto.getCategory());
            txtAmount.setText(String.valueOf(expensesDto.getAmount()));
            txtDate.setValue(LocalDate.parse(expensesDto.getDate()));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colExpensesId.setCellValueFactory(new PropertyValueFactory<>("expensesId"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

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
        tblExpenses.setItems(FXCollections.observableArrayList(
                expensesBO.getAllExpenses().stream().map(expensesDto ->
                        new ExpensesTM(
                                expensesDto.getExpensesId(),
                                expensesDto.getCategory(),
                                expensesDto.getAmount(),
                                expensesDto.getDate()
                        )).toList()
        ));

    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTable();
        txtAmount.clear();
        txtCategory.clear();
        txtDate.setValue(null);
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = expensesBO.getNextId();
        lblId.setText(nextId);
    }
}