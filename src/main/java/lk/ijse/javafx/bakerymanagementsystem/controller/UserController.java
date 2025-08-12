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
import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private AnchorPane ancUser;

    @FXML
    private TableColumn<UserDto, String> coUserName;

    @FXML
    private TableColumn<UserDto, String> colPassword;

    @FXML
    private TableColumn<UserDto, String> colRole;

    @FXML
    private TableColumn<UserDto, String> colId;

    @FXML
    private Label lblId;

    @FXML
    private TableView<UserDto> tblUser;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRole;

    @FXML
    private TextField txtUserName;

    private final UserModel userModel = new UserModel();
    private final String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{4,19}$";
    private final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+])[A-Za-z\\d!@#$%^&*()\\-+]{8,}$";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadTable();
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data.").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        UserDto userDto = createUserDtoFromInputs();

        try {
            boolean isAdded = userModel.saveUser(userDto);
            if (isAdded) {
                new Alert(Alert.AlertType.INFORMATION, "User added successfully!").show();
                loadTable();
                resetPage();
                loadNextId();
            } else {
                new Alert(Alert.AlertType.WARNING, "Failed to add User!").show();
            }
        } catch (SQLIntegrityConstraintViolationException e) {
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
        if (!validDateInputs()) return;

        UserDto userDto = createUserDtoFromInputs();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to update this User?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isUpdated = userModel.updateUser(userDto);
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
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        UserDto selectedUser = tblUser.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete.").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initStyle(StageStyle.UNDECORATED);
        confirmationAlert.setContentText("Are you sure you want to delete this User?");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = userModel.deleteUser(selectedUser.getUserId());
                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
                    loadTable();
                    resetPage();
                    loadNextId();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete User!").show();
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
            new Alert(Alert.AlertType.ERROR, "Failed to load next user ID.").show();
        }
    }

    public void setDataOnMouseClicked(MouseEvent mouseEvent) {
            UserDto userDto = tblUser.getSelectionModel().getSelectedItem();
            if (userDto != null) {
                lblId.setText(userDto.getUserId());
                txtUserName.setText(userDto.getUserName());
                txtPassword.setText(userDto.getPassword());
                txtRole.setText(userDto.getRole());
            }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        txtUserName.clear();
        txtPassword.clear();
        txtRole.clear();
        tblUser.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblId.setText(userModel.getNextId());
    }

    private void loadTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        coUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
            ArrayList<UserDto> users = userModel.getAllUsers();
            if (users != null && !users.isEmpty()) {
                ObservableList<UserDto> userList = FXCollections.observableArrayList(users);
                tblUser.setItems(userList);
            } else {
                tblUser.setItems(FXCollections.observableArrayList());
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load users.").show();
        }
    }

    private boolean validDateInputs() {
        String username = txtUserName.getText().trim();
        String password = txtPassword.getText().trim();
        String role = txtRole.getText().trim();
        boolean isValidUsername = username.matches(usernamePattern);
        boolean isValidPassword = password.matches(passwordPattern);
        boolean isValid = true;

        txtUserName.setStyle("");
        txtPassword.setStyle("");

        if(!isValidUsername){
            txtUserName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if(role.isEmpty()){
            txtRole.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if(!isValidPassword){
            txtPassword.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
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

    private UserDto createUserDtoFromInputs() {
        return new UserDto(
                lblId.getText(),
                txtUserName.getText(),
                txtPassword.getText(),
                txtRole.getText()
        );
    }
}