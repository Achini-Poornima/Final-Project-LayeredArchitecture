package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String userId;
    private String userName;
    private String password;
    private String role;
}