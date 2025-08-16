package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomerTM {
    private String customerId;
    private String name;
    private String address;
    private String nic;
    private String contact;
    private String email;

//    public CustomerTM(String customerId, String name, String address, String nic, String contact, String email) {
//        this.id = customerId;
//        this.name = name;
//        this.address = address;
//        this.nic = nic;
//        this.contact = contact;
//        this.email = email;
//    }
}
