package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserTM {
    private String userId;
    private String userName;
    private String password;
    private String role;
}
