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
import lk.ijse.javafx.bakerymanagementsystem.Dto.DeliverDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.model.DeliverModel;
import lk.ijse.javafx.bakerymanagementsystem.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML
    private AnchorPane ancPayment;

    @FXML
    private TableColumn<PaymentDto,Double> colAmount;

    @FXML
    private TableColumn<PaymentDto,String> colOrderId;

    @FXML
    private TableColumn<PaymentDto,String> colPaymentId;

    @FXML
    private TableColumn<PaymentDto,String> colPaymentDate;

    @FXML
    private TableColumn<PaymentDto,String> colPaymentMethod;

    @FXML
    private Label lblId;

    @FXML
    private TableView<PaymentDto> tblPayment;

    @FXML
    private TextField txtAmount;

    @FXML
    private ComboBox<String> txtOrderId;

    @FXML
    private DatePicker txtPaymentDate;

    @FXML
    private TextField txtPaymentMethod;

    PaymentModel paymentModel = new PaymentModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        PaymentDto selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a payment to delete.").show();
            return;
        }

        boolean isDeleted = paymentModel.deletePayment(selectedPayment);
        if (isDeleted) {
            new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully.").show();
            loadTable();
            resetPage();
        } else {
            new Alert(Alert.AlertType.WARNING, "Failed to delete payment.").show();
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        PaymentDto paymentDto = createDeliverDtoFromInputs();
        try {
            boolean isAdded = paymentModel.savePayment(paymentDto);
            if (isAdded){
                new  Alert(Alert.AlertType.INFORMATION,"Payment Added Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed save Payment.").show();
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

        PaymentDto paymentDto = createDeliverDtoFromInputs();
        try {
            boolean isUpdate = paymentModel.updatePayment(paymentDto);
            if (isUpdate){
                new  Alert(Alert.AlertType.INFORMATION,"Payment update Successfully.").show();
                loadTable();
                resetPage();
                loadNextId();
            }else {
                new Alert(Alert.AlertType.WARNING,"Failed update Payment.").show();
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

    private PaymentDto createDeliverDtoFromInputs() {
        return new PaymentDto(
                lblId.getText().trim(),
                Double.parseDouble(txtAmount.getText().trim()),
                txtPaymentMethod.getText().trim(),
                LocalDateTime.now(),
                txtOrderId.getValue().trim()
        );
    }

    private boolean validDataInputs() {
        boolean isValid = true;

        txtAmount.setStyle("");
        txtPaymentMethod.setStyle("");
        txtPaymentMethod.setStyle("");
        txtPaymentDate.setStyle("");

        if (txtAmount.getText().trim().isEmpty()) {
            txtAmount.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtPaymentMethod.getText().trim().isEmpty()) {
            txtPaymentMethod.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtPaymentDate.getValue() == null) {
            txtPaymentDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtOrderId.getValue() == null) {
            txtOrderId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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
        PaymentDto selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            lblId.setText(selectedPayment.getPaymentId());
            txtAmount.setText(String.valueOf(selectedPayment.getAmount()));
            txtPaymentMethod.setText(selectedPayment.getPaymentMethod());
            txtPaymentDate.setValue(LocalDate.parse(selectedPayment.getPaymentDate()));
            txtOrderId.setValue(selectedPayment.getOrderId());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            resetPage();
            loadTodayOrderIds();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
        }
    }

    private void loadTable() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        try {
            ArrayList<PaymentDto> payment = PaymentModel.getAllPayments();
            if (payment != null && !payment.isEmpty()) {
                ObservableList<PaymentDto> paymentList = FXCollections.observableArrayList(payment);
                tblPayment.setItems(paymentList);
            } else {
                tblPayment.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load payment.").show();
        }
    }


    private void loadTodayOrderIds() {
        try {
            ArrayList<String> orderIds = paymentModel.getTodayOrderIds();
            ObservableList<String> observableOrderIds = FXCollections.observableArrayList(orderIds);
            txtOrderId.setItems(observableOrderIds);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load today's order IDs.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtAmount.clear();
        txtPaymentMethod.clear();
        txtPaymentDate.setValue(null);
        txtOrderId.getSelectionModel().clearSelection();
        txtOrderId.setValue(null);
    }


    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(paymentModel.getNextPaymentId());
    }
}
