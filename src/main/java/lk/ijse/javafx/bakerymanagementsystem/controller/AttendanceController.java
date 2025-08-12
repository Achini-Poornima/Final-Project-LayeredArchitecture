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
import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.model.AttendanceModel;
import lk.ijse.javafx.bakerymanagementsystem.model.EmployeeModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

public class AttendanceController implements Initializable {

    @FXML
    private AnchorPane ancAttendance;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private TableColumn<AttendanceDto, String> colAttendanceId;

    @FXML
    private TableColumn<AttendanceDto, String> colDate;

    @FXML
    private TableColumn<AttendanceDto, String> colEmployeeId;

    @FXML
    private TableColumn<AttendanceDto, String> colInTime;

    @FXML
    private TableColumn<AttendanceDto, String> colOutTime;

    @FXML
    private Label lblId;

    @FXML
    private Label lblInTime;

    @FXML
    private Label lblOutTime;

    @FXML
    private TableView<AttendanceDto> tblAttendance;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtInTimeHour;

    @FXML
    private TextField txtInTimeMin;

    @FXML
    private TextField txtOutTimeHour;

    @FXML
    private TextField txtOutTimeMin;
    
    private final AttendanceModel attendanceModel = new AttendanceModel();
    private final EmployeeModel employeeModel = new EmployeeModel();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        AttendanceDto selectedAttendance = tblAttendance.getSelectionModel().getSelectedItem();
        if (selectedAttendance == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a Attendance Details to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this Attendance Details?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = attendanceModel.deleteAttendance(selectedAttendance.getAttendanceId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Attendance Details deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Attendance Details!").show();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (validDateInputs()) return;

        AttendanceDto attendanceDto = createAttendanceDtoFromInputs();

        try {
            boolean isAdded = attendanceModel.saveAttendance(attendanceDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "Attendance added successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to add Attendance!").show();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred while adding the Attendance!").show();
        }
    }

    private boolean validDateInputs() {
        String employeeId = cmbEmployeeId.getValue().trim();
        String inTime = lblInTime.textProperty().getValue().trim();
        String outTime = lblOutTime.getText().trim();
        String date = String.valueOf(txtDate.getValue());

        if (employeeId.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Please fill all the fields correctly!").show();
            return true;
        }
        return false;
    }

    private AttendanceDto createAttendanceDtoFromInputs() {
        return new AttendanceDto(
                lblId.getText(),
                cmbEmployeeId.getValue(),
                lblInTime.getText(),
                lblOutTime.getText(),
                String.valueOf(txtDate.getValue())
        );
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (validDateInputs()) return;

        AttendanceDto attendanceDto = createAttendanceDtoFromInputs();
        try {
            boolean isUpdated = attendanceModel.updateAttendance(attendanceDto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Attendance Details updated successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to update Attendance Details!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void onInHour(KeyEvent keyEvent) {
        String hourText = txtInTimeHour.getText();

        if (hourText.length() == 2) {
            try {
                int hour = Integer.parseInt(hourText);
                if (hour >= 0 && hour <= 23) {
                    txtInTimeMin.requestFocus();
                } else {
                    lblInTime.setText("Invalid hour (00–23)");
                }
            } catch (NumberFormatException e) {
                lblInTime.setText("Invalid hour input");
            }
        }
    }

    public void onInMin(KeyEvent keyEvent) {
        String hourText = txtInTimeHour.getText();
        String minText = txtInTimeMin.getText();

        if (hourText.length() == 2 && minText.length() == 2) {
            try {
                int hour = Integer.parseInt(hourText);
                int minute = Integer.parseInt(minText);

                if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                    lblInTime.setText(String.format("%02d:%02d", hour, minute));
                } else {
                    lblInTime.setText("Invalid time");
                }
            } catch (NumberFormatException e) {
                lblInTime.setText("Invalid input");
            }
        }
    }

    public void onOutHour(KeyEvent keyEvent) {
        String hourText = txtOutTimeHour.getText();

        if (hourText.length() == 2) {
            try {
                int hour = Integer.parseInt(hourText);
                if (hour >= 0 && hour <= 23) {
                    txtOutTimeMin.requestFocus();
                } else {
                    lblOutTime.setText("Invalid hour (00–23)");
                }
            } catch (NumberFormatException e) {
                lblOutTime.setText("Invalid hour input");
            }
        }
    }

    public void onOutMin(KeyEvent keyEvent) {
        String hourText = txtOutTimeHour.getText();
        String minText = txtOutTimeMin.getText();

        if (hourText.length() == 2 && minText.length() == 2) {
            try {
                int hour = Integer.parseInt(hourText);
                int minute = Integer.parseInt(minText);

                if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
                    lblOutTime.setText(String.format("%02d:%02d", hour, minute));
                } else {
                    lblOutTime.setText("Invalid time");
                }
            } catch (NumberFormatException e) {
                lblOutTime.setText("Invalid input");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            loadNextId();
            resetPage();
            loadEmployeeIds();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to load table.");
        }
    }

    private void loadEmployeeIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> employeeIds = employeeModel.getAllEmployeeIds();
        ObservableList<String> observableOrderIds = FXCollections.observableArrayList(employeeIds);
        cmbEmployeeId.setItems(observableOrderIds);
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        cmbEmployeeId.setValue(null);
        txtInTimeHour.clear();
        txtInTimeMin.clear();
        txtOutTimeHour.clear();
        txtOutTimeMin.clear();
        txtDate.setValue(LocalDate.now());
        tblAttendance.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(attendanceModel.getNextId());
    }

    private void loadTable() {
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            ArrayList<AttendanceDto> attendance = attendanceModel.getAllAttendance();
            if (attendance != null && !attendance.isEmpty()) {
                ObservableList<AttendanceDto> attendanceList = FXCollections.observableArrayList(attendance);
                tblAttendance.setItems(attendanceList);
            } else {
                tblAttendance.setItems(FXCollections.observableArrayList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Attendance Details.").show();
        }
    }

    public void onSetData(MouseEvent mouseEvent) {
        AttendanceDto selectAttendance = tblAttendance.getSelectionModel().getSelectedItem();
        if (selectAttendance != null){
            lblId.setText(selectAttendance.getAttendanceId());
            cmbEmployeeId.setValue(selectAttendance.getEmployeeId());
            lblInTime.setText(selectAttendance.getInTime());
            lblOutTime.setText(selectAttendance.getOutTime());
            txtDate.setValue(LocalDate.parse(selectAttendance.getDate()));
        }
    }
}
