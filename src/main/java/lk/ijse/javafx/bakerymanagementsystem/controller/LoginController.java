package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.javafx.bakerymanagementsystem.model.UserModel;

public class LoginController {

    @FXML
    private AnchorPane ancLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    private final String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{2,19}$\n";
    private final String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\n";

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        boolean isValidUsername = username.matches(usernamePattern);
        boolean isValidPassword = password.matches(passwordPattern);
        boolean isValid = true;

        txtUsername.setStyle("");
        txtPassword.setStyle("");

        if(!isValidUsername){
            txtUsername.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if(!isValidPassword){
            txtPassword.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (isValid) {
            new Alert(Alert.AlertType.WARNING, "Please enter both username and password.", ButtonType.OK).show();
            return;
        }

        try {
            UserModel userModel = new UserModel();
            boolean isValidInput = userModel.checkLogin(username, password);

            if (isValidInput) {
                navigateTo("/view/BDashboard.fxml");
            } else {
                new Alert(Alert.AlertType.WARNING, "Invalid username or password!", ButtonType.OK).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Login failed. Error: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }

    public void navigateTo(String path) {
        try {
            ancLogin.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancLogin.widthProperty());
            anchorPane.prefHeightProperty().bind(ancLogin.heightProperty());
            ancLogin.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Page not found", ButtonType.OK).show();
            e.printStackTrace();
        }
    }
}