package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import javafx.scene.control.Button;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartTM {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double total;
    private Button btnRemove;
}