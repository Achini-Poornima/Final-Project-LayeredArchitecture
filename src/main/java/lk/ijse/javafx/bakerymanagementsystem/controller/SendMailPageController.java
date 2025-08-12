package lk.ijse.javafx.bakerymanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailPageController {
    @FXML
    public Button btnSendMailOnAction;

    @FXML
    private TextField txtSubject;

    @FXML
    private TextArea txtDescription;

    @Setter
    private String customerEmail;
    @FXML
    void btnSendMailOnAction(ActionEvent event) {
        String subject = txtSubject.getText() != null ? txtSubject.getText().trim() : "";
        String description = txtDescription.getText() != null ? txtDescription.getText().trim() : "";

        if (customerEmail == null || customerEmail.isEmpty() || subject.isEmpty() || description.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required!").show();
            return;
        }
        String from = "achinipoornima2004@gmail.com";
        String password = "gkiv jhhr crwq qmve"; //

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customerEmail));
            message.setSubject(subject);
            message.setText(description);

            Transport.send(message);
            new Alert(Alert.AlertType.INFORMATION, "Email sent successfully").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to send email!").show();
        }
    }

}
