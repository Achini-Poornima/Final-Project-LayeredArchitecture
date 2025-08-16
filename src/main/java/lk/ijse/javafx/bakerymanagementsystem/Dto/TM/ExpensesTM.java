package lk.ijse.javafx.bakerymanagementsystem.Dto.TM;

import lombok.*;

@ToString
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Getter
public class ExpensesTM {
    private String expensesId;
    private String category;
    private double amount;
    private String date;
}
