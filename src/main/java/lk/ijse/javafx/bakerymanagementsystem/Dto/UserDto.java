package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private String userId;
    private String userName;
    private String password;
    private String role;
}