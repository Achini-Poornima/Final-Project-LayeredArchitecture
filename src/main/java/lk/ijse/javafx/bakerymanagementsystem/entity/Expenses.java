package lk.ijse.javafx.bakerymanagementsystem.entity;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Setter
public class Expenses {
    private String expensesId;
    private String category;
    private double amount;
    private String date;

    public Expenses(String trim, String trim1, double amount, LocalDate now) {
        this.expensesId = trim;
        this.category = trim1;
        this.amount = amount;
        this.date = now.toString();
    }
}