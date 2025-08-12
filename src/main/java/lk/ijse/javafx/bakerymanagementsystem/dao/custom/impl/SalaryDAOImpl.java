package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SalaryDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Salary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalaryDAOImpl implements SalaryDAO {
    @Override
    public List<Salary> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM Salary");
        List<Salary> list = new ArrayList<>();
        while (resultSet.next()){
            Salary salary = new Salary(
                    resultSet.getString("salary_id"),
                    resultSet.getInt("basic_salary"),
                    resultSet.getInt("bonus"),
                    resultSet.getInt("net_salary"),
                    resultSet.getString("payment_date"),
                    resultSet.getString("employee_id")
            );
            list.add(salary);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT salary_id FROM salary ORDER BY salary_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Salary salary) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Salary(salary_id, basic_salary, bonus, net_salary, payment_date, employee_id) VALUES (?,?,?,?,?,?)";
        return SQLUtil.execute(sql, salary.getSalaryId(), salary.getBasicSalary(), salary.getBonus(), salary.getNetSalary(), salary.getPaymentDate(), salary.getEmployeeId());
    }

    @Override
    public boolean update(Salary salary) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Salary SET basic_salary = ? , bonus = ? , net_salary = ? , payment_date = ? , employee_id = ? WHERE salary_id = ?";
        return SQLUtil.execute(sql, salary.getBasicSalary(), salary.getBonus(), salary.getNetSalary(), salary.getPaymentDate(), salary.getEmployeeId(), salary.getSalaryId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Salary WHERE salary_id = ?";
        return SQLUtil.execute(sql, id);
    }

    @Override
    public List<String> getAllIds() {
        return null;
    }

    @Override
    public Optional<Salary> findById(String id) {
        return null;
    }
}
