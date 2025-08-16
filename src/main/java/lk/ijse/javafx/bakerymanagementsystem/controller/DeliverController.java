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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.DeliverTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.DeliverBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.DeliverDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.DeliverDAOImpl;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeliverController implements Initializable {

    public DatePicker txtDeliverDate;
    public AnchorPane ancDeliver;

    public TableColumn<DeliverTM,String> colDeliverAddress;

    public TableColumn<DeliverTM,Integer> colDeliverCharge;

    public TableColumn<DeliverTM,String> colDeliverDate;

    public TableColumn<DeliverTM,String> colOrderId;

    public TableColumn<DeliverTM,String> colId;

    public Label lblId;

    public TableView<DeliverTM> tblDeliver;

    public TextField txtDeliverAddress;

    public TextField txtDeliverCharge;

    public ComboBox<String> txtOrderId;

    private final DeliverDAO deliverDAO = new DeliverDAOImpl();
    private final DeliverBO deliverBO = BOFactory.getInstance().getBo(BOTypes.DELIVER);

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        DeliverTM selectedDeliver = tblDeliver.getSelectionModel().getSelectedItem();
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
                String deliverId = lblId.getText();
                boolean isDeleted = deliverBO.deleteDeliver(deliverId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Deliver deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Deliver!").show();
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
         }catch (Exception e){
             new Alert(Alert.AlertType.ERROR,"Failed to load next Deliver ID.").show();
         }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        if (!validDataInputs()) return;

        DeliverDto deliverDto = createDeliverDtoFromInputs();
            try {
                deliverBO.saveDeliver(deliverDto);
                new  Alert(Alert.AlertType.INFORMATION,"Deliver Added Successfully.").show();
                resetPage();
            } catch (DuplicateException e) {
                System.out.println(e.getMessage());
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save Deliver..!").show();
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
                deliverBO.updateDeliver(deliverDto);
                new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").show();
                    resetPage();
            } catch (NotFoundException | DuplicateException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to update customer..!").show();
            }
    }

    @FXML
    void setData(MouseEvent event) {
         DeliverTM deliverDto = tblDeliver.getSelectionModel().getSelectedItem();
         if (deliverDto != null){
             lblId.setText(deliverDto.getDeliverId());
             txtDeliverAddress.setText(deliverDto.getDeliverAddress());
             txtDeliverCharge.setText(String.valueOf(deliverDto.getDeliverCharge()));
             txtOrderId.setValue(deliverDto.getOrderId());
         }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("deliverId"));
        colDeliverAddress.setCellValueFactory(new PropertyValueFactory<>("deliverAddress"));
        colDeliverCharge.setCellValueFactory(new PropertyValueFactory<>("deliverCharge"));
        colDeliverDate.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        try {
              resetPage();
          }catch (Exception e){
              e.printStackTrace();
              new Alert(Alert.AlertType.ERROR,"Failed To load Table..").show();
          }
    }

    /*private void loadTodayOrderIds() {
        try {
            ArrayList<String> orderIds = new ArrayList<>(deliverDAO.getTodayOrderIds());
            ObservableList<String> observableOrderIds = FXCollections.observableArrayList(orderIds);
            txtOrderId.setItems(observableOrderIds);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load today's order IDs.").show();
        }
    }*/


    private void loadTable() throws SQLException, ClassNotFoundException {
        tblDeliver.setItems(FXCollections.observableArrayList(
                deliverBO.getAllDeliver().stream().map(deliverDto ->
                        new DeliverTM(
                                deliverDto.getDeliverId(),
                                deliverDto.getDeliverAddress(),
                                deliverDto.getDeliverCharge(),
                                deliverDto.getDeliverDate(),
                                deliverDto.getOrderId()
                        )).toList()
        ));
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
       loadNextId();
       loadTable();
       /*loadTodayOrderIds();*/
       txtDeliverAddress.clear();
       txtDeliverCharge.clear();
       txtOrderId.setValue(null);
       tblDeliver.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = deliverBO.getNextId();
        lblId.setText(nextId);
    }
}


