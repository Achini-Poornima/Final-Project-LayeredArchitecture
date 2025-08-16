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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.AttendanceTM;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.AttendanceBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl.AttendanceBOImpl;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.AttendanceDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.AttendanceDAOImpl;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

public class AttendanceController implements Initializable {

    public AnchorPane ancAttendance;

    public ComboBox<String> cmbEmployeeId;
    public TableColumn<AttendanceTM, String> colAttendanceId;
    public TableColumn<AttendanceTM, String> colDate;
    public TableColumn<AttendanceTM, String> colEmployeeId;
    public TableColumn<AttendanceTM, String> colInTime;
    public TableColumn<AttendanceTM, String> colOutTime;
    public TableView<AttendanceTM> tblAttendance;

    public Label lblId;
    public Label lblInTime;
    public Label lblOutTime;
    public DatePicker txtDate;
    public TextField txtInTimeHour;
    public TextField txtInTimeMin;
    public TextField txtOutTimeHour;
    public TextField txtOutTimeMin;
    
    private final AttendanceBO attendanceBO = new AttendanceBOImpl();
    private final AttendanceDAO attendanceDAO = new AttendanceDAOImpl();

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        AttendanceTM selectedAttendance = tblAttendance.getSelectionModel().getSelectedItem();
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
                String attendanceId = lblId.getText();
                boolean isDeleted = attendanceBO.deleteAttendance(attendanceId);
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Attendance Details deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete Attendance Details!").show();
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
    void btnResetOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        resetPage();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (validDateInputs()) return;

        AttendanceDto attendanceDto = createAttendanceDtoFromInputs();

        try {
            attendanceBO.saveAttendance(attendanceDto);
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Attendance added successfully!").show();
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
            attendanceBO.updateAttendance(attendanceDto);
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(
                    Alert.AlertType.ERROR, "Fail to update customer..!"
            ).show();
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

        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colInTime.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        colOutTime.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        try {
            resetPage();
            loadEmployeeIds();
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR,"Fail to load table.");
        }
    }

    private void loadEmployeeIds() throws SQLException, ClassNotFoundException {
        ArrayList<String> employeeIds = (ArrayList<String>) attendanceDAO.getAllEmployeeIds();
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
        String nextId = attendanceBO.getNextId();
        lblId.setText(nextId);
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblAttendance.setItems(FXCollections.observableArrayList(
                attendanceBO.getAllAttendance().stream().map(attendanceDto ->
                        new AttendanceTM(
                                attendanceDto.getAttendanceId(),
                                attendanceDto.getEmployeeId(),
                                attendanceDto.getInTime(),
                                attendanceDto.getOutTime(),
                                attendanceDto.getDate()
                        )).toList()
        ));
    }

    public void onSetData(MouseEvent mouseEvent) {
        AttendanceTM selectAttendance = tblAttendance.getSelectionModel().getSelectedItem();
        if (selectAttendance != null){
            lblId.setText(selectAttendance.getAttendanceId());
            cmbEmployeeId.setValue(selectAttendance.getEmployeeId());
            lblInTime.setText(selectAttendance.getInTime());
            lblOutTime.setText(selectAttendance.getOutTime());
            txtDate.setValue(LocalDate.parse(selectAttendance.getDate()));
        }
    }
}
