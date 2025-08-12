package lk.ijse.javafx.bakerymanagementsystem.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Setter
public class ExpensesDto {
    private String expensesId;
    private String category;
    private double amount;
    private String date;

    public ExpensesDto(String trim, String trim1, double amount, LocalDate now) {
        this.expensesId = trim;
        this.category = trim1;
        this.amount = amount;
        this.date = now.toString();
    }
}