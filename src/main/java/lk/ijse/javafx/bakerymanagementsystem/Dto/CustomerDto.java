package lk.ijse.javafx.bakerymanagementsystem.Dto;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDto {
    private String customerId;
    private String name;
    private String address;
    private String nic;
    private String contact;
    private String email;

    public CustomerDto(String customerId, String name, String address,String nic, String contact,String email) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.nic = nic;
        this.contact = contact;
        this.email = email;
    }

}