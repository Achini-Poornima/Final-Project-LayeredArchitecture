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
import lk.ijse.javafx.bakerymanagementsystem.model.EmployeeModel;
import lk.ijse.javafx.bakerymanagementsystem.model.SalaryModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class SalaryController implements Initializable {

    @FXML
    private  DatePicker txtPaymentDate;

    @FXML
    private AnchorPane ancSalary;

    @FXML
    private TableColumn<SalaryDto,Integer> colBasicSalary;

    @FXML
    private TableColumn<SalaryDto,Integer> colBonus;

    @FXML
    private TableColumn<SalaryDto,String> colEmployeeId;

    @FXML
    private TableColumn<SalaryDto,Integer> colNetSalary;

    @FXML
    private TableColumn<SalaryDto,String> colPaymentDate;

    @FXML
    private TableColumn<SalaryDto,String> colSalaryId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<SalaryDto> tblSalary;

    @FXML
    private TextField txtBasicSalary;

    @FXML
    private TextField txtBonus;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private Label txtNetSalary;


    private final SalaryModel salaryModel = new SalaryModel();
    private final EmployeeModel employeeModel=new EmployeeModel();

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
                boolean isDeleted = salaryModel.deleteSalary(selectedSalary.getSalaryId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Salary Details deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Salary Details!").show();
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
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load next Salary ID.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        SalaryDto SalaryDto = createSalaryDtoFromInputs();

            try {
                boolean isAdded = salaryModel.saveSalary(SalaryDto);
                if (isAdded) {
                    new Alert(Alert.AlertType.INFORMATION, "Salary added successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to add Salary!").show();
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
                boolean isUpdated = salaryModel.updateSalary(salaryDto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Salary Details updated successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to update Salary Details!").show();
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
        try {
            loadTable();
            resetPage();
            loadNextId();
            loadEmployeeIds();

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
        ArrayList<String> employeeIds = employeeModel.getAllEmployeeIds();
        ObservableList<String> observableOrderIds = FXCollections.observableArrayList(employeeIds);
        cmbEmployeeId.setItems(observableOrderIds);
    }

    private void loadTable() {
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
        colBasicSalary.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        colBonus.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        colNetSalary.setCellValueFactory(new PropertyValueFactory<>("netSalary"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        try {
            ArrayList<SalaryDto> salary = salaryModel.getAllSalary();
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
        lblId.setText(salaryModel.getNextId());
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
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
