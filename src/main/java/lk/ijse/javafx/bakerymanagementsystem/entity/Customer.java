package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    private String customerId;
    private String name;
    private String address;
    private String nic;
    private String contact;
    private String email;

    public Customer(String customerId, String name, String address, String nic, String contact, String email) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.nic = nic;
        this.contact = contact;
        this.email = email;
    }

}