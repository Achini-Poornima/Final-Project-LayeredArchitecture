package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.SalaryTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.SalaryBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SalaryDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.SalaryDAOImpl;
import lk.ijse.javafx.bakerymanagementsystem.model.SalaryModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class SalaryController implements Initializable {
    public DatePicker txtPaymentDate;
    public AnchorPane ancSalary;
    public TableColumn<SalaryTM,Integer> colBasicSalary;
    public TableColumn<SalaryTM,Integer> colBonus;
    public TableColumn<SalaryTM,String> colEmployeeId;
    public TableColumn<SalaryTM,Integer> colNetSalary;
    public TableColumn<SalaryTM,String> colPaymentDate;
    public TableColumn<SalaryTM,String> colSalaryId;
    public Label lblId;
    public TableView<SalaryDto> tblSalary;
    public TextField txtBasicSalary;
    public TextField txtBonus;
    public ComboBox<String> cmbEmployeeId;
    public Label txtNetSalary;

    private final SalaryDAO salaryDAO = new SalaryDAOImpl();
    private final SalaryBO salaryBO = BOFactory.getInstance().getBo(BOTypes.SALARY);
    private final SalaryModel salaryModel = new SalaryModel();

    @FXML
void btnDeleteOnAction(ActionEvent event) {
        SalaryDto selectedSalary = tblSalary.getSelectionModel().getSelectedItem();
        if (selectedSalary == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Salary Details to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Salary Details?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String id = lblId.getText();
                boolean isDeleted = salaryBO.deleteSalary(id);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Salary deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Salary!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete salary..!").show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        try {
            resetPage();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load next Salary ID.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        SalaryDto salaryDto = createSalaryDtoFromInputs();

        try {
            salaryBO.saveSalary(salaryDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Salary saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save salary..!").show();
        }
    }

    private SalaryDto createSalaryDtoFromInputs() {
        return new SalaryDto(
                lblId.getText(),
                Double.parseDouble(txtBasicSalary.getText()),
                Double.parseDouble(txtBonus.getText()),
                Double.parseDouble(txtNetSalary.getText()),
                String.valueOf(txtPaymentDate.getValue()),
                cmbEmployeeId.getValue()
        );
    }

    private boolean validDateInputs() {
        boolean isValid = true;

        txtBasicSalary.setStyle("");
        txtBonus.setStyle("");
        txtNetSalary.setStyle("");
        txtPaymentDate.setStyle("");
        cmbEmployeeId.setStyle("");

        if (txtBasicSalary.getText().trim().isEmpty()) {
            txtBasicSalary.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (txtBonus.getText().trim().isEmpty()) {
            txtBonus.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (txtNetSalary.getText().trim().isEmpty()) {
            txtNetSalary.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (txtPaymentDate.getValue() == null) {
            txtPaymentDate.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (cmbEmployeeId.getValue() == null || cmbEmployeeId.getValue().trim().isEmpty()) {
            cmbEmployeeId.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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
        if (!validDateInputs()) return;

        SalaryDto salaryDto = createSalaryDtoFromInputs();
        try {
            salaryBO.updateSalary(salaryDto);
            new Alert(Alert.AlertType.INFORMATION, "Salary updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update salary..!").show();
        }
    }

    @FXML
    void onSetData(MouseEvent event) {
         SalaryDto salaryDto = tblSalary.getSelectionModel().getSelectedItem();
         if (salaryDto != null ){
             lblId.setText(salaryDto.getSalaryId());
             txtBasicSalary.setText(String.valueOf(salaryDto.getBasicSalary()));
             txtBonus.setText(String.valueOf(salaryDto.getBonus()));
             txtNetSalary.setText(String.valueOf(salaryDto.getNetSalary()));
             txtPaymentDate.setValue(LocalDate.parse(salaryDto.getPaymentDate()));
             cmbEmployeeId.setValue(salaryDto.getEmployeeId());
         }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
        colBasicSalary.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        colBonus.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        colNetSalary.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        try {
            resetPage();
            txtBasicSalary.setOnKeyReleased(this::setSalary);
            txtBonus.setOnKeyReleased(this::setSalary);
            setNetSalary();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to Load Data..").show();
        }
    }


    private void setNetSalary() {
        String basicSalaryStr = txtBasicSalary.getText().trim();
        String bonusStr = txtBonus.getText().trim();

        if (basicSalaryStr.isEmpty() || !basicSalaryStr.matches("\\d+(\\.\\d+)?")) {
            txtNetSalary.setText("0.00");
            return;
        }

        try {
            double basicSalary = Double.parseDouble(basicSalaryStr);
            double bonus = bonusStr.isEmpty() || !bonusStr.matches("\\d+(\\.\\d+)?") ? 0.0 : Double.parseDouble(bonusStr);
            double netSalary = basicSalary + bonus;

            txtNetSalary.setText(String.format("%.2f", netSalary));
        } catch (NumberFormatException e) {
            txtNetSalary.setText("0.00");
        }
    }


    private void loadEmployeeIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> employeeIds = (ArrayList<String>) salaryDAO.getAllEmployeeIds();
        ObservableList<String> observableOrderIds = FXCollections.observableArrayList(employeeIds);
        cmbEmployeeId.setItems(observableOrderIds);
    }

    private void loadTable() {

        try {
            ArrayList<SalaryDto> salary = (ArrayList<SalaryDto>) salaryDAO.getAllSalary();
            if (salary != null && !salary.isEmpty()) {
                ObservableList<SalaryDto> salaryList = FXCollections.observableArrayList(salary);
                tblSalary.setItems(salaryList);
            } else {
                tblSalary.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Salary Details.").show();
        }
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = salaryBO.getNextId();
        lblId.setText(nextId);
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadEmployeeIds();
        loadTable();
        txtBasicSalary.clear();
        txtBonus.clear();
        setNetSalary();
        cmbEmployeeId.setValue(null);
        txtPaymentDate.setValue(LocalDate.now());
        tblSalary.getSelectionModel().clearSelection();
    }

    public void setSalary(KeyEvent keyEvent) {
        setNetSalary();
    }
}
