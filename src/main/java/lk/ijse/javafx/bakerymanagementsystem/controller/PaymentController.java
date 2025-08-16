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
import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CustomerTM;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.PaymentTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.PaymentBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.PaymentDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.javafx.bakerymanagementsystem.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {
    public AnchorPane ancPayment;
    public TableColumn<PaymentTM,Double> colAmount;
    public TableColumn<PaymentTM,String> colOrderId;
    public TableColumn<PaymentTM,String> colPaymentId;
    public TableColumn<PaymentTM,String> colPaymentDate;
    public TableColumn<PaymentTM,String> colPaymentMethod;
    public Label lblId;
    public TableView<PaymentTM> tblPayment;
    public TextField txtAmount;
    public ComboBox<String> txtOrderId;
    public DatePicker txtPaymentDate;
    public TextField txtPaymentMethod;

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final PaymentBO paymentBO = BOFactory.getInstance().getBo(BOTypes.PAYMENT);
    PaymentModel paymentModel = new PaymentModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        PaymentTM selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a payment to delete.").show();
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Payment?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String id = lblId.getText();
                boolean isDeleted = paymentBO.deletePayment(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Payment!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete payment..!").show();
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

        PaymentDto paymentDto = createDeliverDtoFromInputs();
        try {
            paymentBO.savePayment(paymentDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Payment saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save payment..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        PaymentDto paymentDto = createDeliverDtoFromInputs();
        try {
            paymentBO.updatePayment(paymentDto);
            new Alert(Alert.AlertType.INFORMATION, "Payment updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update payment..!").show();
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
        PaymentTM selectedPayment = tblPayment.getSelectionModel().getSelectedItem();
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
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblPayment.setItems(FXCollections.observableArrayList(
                paymentBO.getAllPayment().stream().map(paymentDTO ->
                        new PaymentTM(
                                paymentDTO.getPaymentId(),
                                paymentDTO.getAmount(),
                                paymentDTO.getPaymentMethod(),
                                paymentDTO.getPaymentDate(),
                                paymentDTO.getOrderId()
                        )).toList()
        ));
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
        loadTable();
        loadTodayOrderIds();
        txtAmount.clear();
        txtPaymentMethod.clear();
        txtPaymentDate.setValue(null);
        txtOrderId.getSelectionModel().clearSelection();
        txtOrderId.setValue(null);
    }


    private void loadNextId() throws SQLException, ClassNotFoundException {
        Optional<String> nextIdOpt = paymentBO.getNextId().describeConstable();
        String nextId = nextIdOpt.orElse("P001");
        lblId.setText(nextId);
    }

}
