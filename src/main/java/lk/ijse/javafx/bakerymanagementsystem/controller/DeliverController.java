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
import lk.ijse.javafx.bakerymanagementsystem.model.DeliverModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeliverController implements Initializable {

    @FXML
    public DatePicker txtDeliverDate;
    @FXML
    private AnchorPane ancDeliver;

    @FXML
    private TableColumn<DeliverDto,String> colDeliverAddress;

    @FXML
    private TableColumn<DeliverDto,Integer> colDeliverCharge;

    @FXML
    private TableColumn<DeliverDto,String> colDeliverDate;

    @FXML
    private TableColumn<DeliverDto,String> colOrderId;

    @FXML
    private TableColumn<DeliverDto,String> colId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<DeliverDto> tblDeliver;

    @FXML
    private TextField txtDeliverAddress;



    @FXML
    private TextField txtDeliverCharge;

    @FXML
    private ComboBox<String> txtOrderId;



    private final DeliverModel deliverModel = new DeliverModel();
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        DeliverDto selectedDeliver = tblDeliver.getSelectionModel().getSelectedItem();
        if (selectedDeliver == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a deliver to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Deliver?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = deliverModel.deleteUser(selectedDeliver.getDeliverId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Deliver deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Deliver!").show();
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
         }catch (Exception e){
             new Alert(Alert.AlertType.ERROR,"Failed to load next Deliver ID.").show();
         }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validDataInputs()) return;

        DeliverDto deliverDto = createDeliverDtoFromInputs();
            try {
                boolean isAdded = deliverModel.saveDeliver(deliverDto);
                if (isAdded){
                    new  Alert(Alert.AlertType.INFORMATION,"Deliver Added Successfully.").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                }else {
                    new Alert(Alert.AlertType.WARNING,"Failed save Deliver.").show();
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

    private DeliverDto createDeliverDtoFromInputs() {
        return new DeliverDto(
                lblId.getText(),
                txtDeliverAddress.getText(),
                Double.parseDouble(txtDeliverCharge.getText()),
                txtDeliverDate.getValue(),
                txtOrderId.getValue()
        );
    }


    private boolean validDataInputs() {
        String address = txtDeliverAddress.getText().trim();
        String charge = txtDeliverCharge.getText().trim();
        String date = String.valueOf(txtDeliverDate.getValue());
        boolean isValid = true;

        txtDeliverAddress.setStyle("");
        txtDeliverCharge.setStyle("");
        txtDeliverDate.setStyle("");
        txtOrderId.setStyle("");

        if (address.isEmpty()){
            txtDeliverAddress.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (charge.isEmpty()){
            txtDeliverCharge.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (date.isEmpty()){
            txtDeliverDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (txtOrderId.getValue() == null || txtOrderId.getValue().trim().isEmpty()) {
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
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDataInputs()) return;

        DeliverDto deliverDto = createDeliverDtoFromInputs();

            try {
                boolean isUpdated = deliverModel.updateDeliver(deliverDto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to update User!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    @FXML
    void setData(MouseEvent event) {
         DeliverDto deliverDto = tblDeliver.getSelectionModel().getSelectedItem();
         if (deliverDto != null){
             lblId.setText(deliverDto.getDeliverId());
             txtDeliverAddress.setText(deliverDto.getDeliverAddress());
             txtDeliverCharge.setText(String.valueOf(deliverDto.getDeliverCharge()));
             txtOrderId.setValue(deliverDto.getOrderId());
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

    private void loadTodayOrderIds() {
        try {
            ArrayList<String> orderIds = deliverModel.getTodayOrderIds();
            ObservableList<String> observableOrderIds = FXCollections.observableArrayList(orderIds);
            txtOrderId.setItems(observableOrderIds);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load today's order IDs.").show();
        }
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("deliverId"));
        colDeliverAddress.setCellValueFactory(new PropertyValueFactory<>("deliverAddress"));
        colDeliverCharge.setCellValueFactory(new PropertyValueFactory<>("deliverCharge"));
        colDeliverDate.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        try {
            ArrayList<DeliverDto> deliver = DeliverModel.getAllDelivers();
            if (deliver != null && !deliver.isEmpty()) {
                ObservableList<DeliverDto> deliverList = FXCollections.observableArrayList(deliver);
                tblDeliver.setItems(deliverList);
            } else {
                tblDeliver.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load deliver.").show();
        }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
       loadNextId();
       txtDeliverAddress.clear();
       txtDeliverCharge.clear();
       txtOrderId.setValue(null);
       tblDeliver.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(deliverModel.getNextId());
    }
}


