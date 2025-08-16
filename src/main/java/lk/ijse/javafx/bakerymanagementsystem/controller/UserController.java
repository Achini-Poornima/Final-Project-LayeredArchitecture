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
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.CustomerTM;
import lk.ijse.javafx.bakerymanagementsystem.Dto.TM.UserTM;
import lk.ijse.javafx.bakerymanagementsystem.Dto.UserDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOFactory;
import lk.ijse.javafx.bakerymanagementsystem.bo.BOTypes;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.UserBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.UserDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.UserDAOImpl;
import lk.ijse.javafx.bakerymanagementsystem.model.UserModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public AnchorPane ancUser;
    public TableColumn<UserTM, String> coUserName;
    public TableColumn<UserTM, String> colPassword;
    public TableColumn<UserTM, String> colRole;
    public TableColumn<UserTM, String> colId;
    public Label lblId;
    public TableView<UserTM> tblUser;
    public TextField txtPassword;
    public TextField txtRole;
    public TextField txtUserName;

    private final UserDAO userDAO = new UserDAOImpl();
    private final UserModel userModel = new UserModel();
    private final String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{4,19}$";
    private final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+])[A-Za-z\\d!@#$%^&*()\\-+]{8,}$";
    private final UserBO userBO = BOFactory.getInstance().getBo(BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        coUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        try {
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
            userBO.saveUser(userDto);
            resetPage();
            new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully.").show();
        } catch (DuplicateException e) {
            System.out.println(e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save user..!").show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validDateInputs()) return;

        UserDto userDto = createUserDtoFromInputs();

        try {
            userBO.updateUser(userDto);
            new Alert(Alert.AlertType.INFORMATION, "User updated successfully!").show();
            resetPage();
        } catch (NotFoundException | DuplicateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update user..!").show();
        }

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        UserTM selectedUser = tblUser.getSelectionModel().getSelectedItem();
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
                String id = selectedUser.getUserId();
                boolean isDeleted = userBO.deleteUser(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!").show();
                    resetPage();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Failed to delete User!").show();
                }
            } catch (InUseException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete user..!").show();
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
            UserTM userDto = tblUser.getSelectionModel().getSelectedItem();
            if (userDto != null) {
                lblId.setText(userDto.getUserId());
                txtUserName.setText(userDto.getUserName());
                txtPassword.setText(userDto.getPassword());
                txtRole.setText(userDto.getRole());
            }
    }

    private void resetPage() throws SQLException, ClassNotFoundException {
        loadNextId();
        loadTable();
        txtUserName.clear();
        txtPassword.clear();
        txtRole.clear();
        tblUser.getSelectionModel().clearSelection();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextId = userBO.getNextId();
        lblId.setText(nextId);
    }

    private void loadTable() throws SQLException, ClassNotFoundException {
        tblUser.setItems(FXCollections.observableArrayList(
                userBO.getAllUser().stream().map(UserDTO ->
                        new UserTM(
                                UserDTO.getUserId(),
                                UserDTO.getUserName(),
                                UserDTO.getPassword(),
                                UserDTO.getRole()
                        )).toList()
        ));
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