package lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.EmployeeDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.EmployeeDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public List<Employee> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Employee");

        List<Employee> list = new ArrayList<>();
        while (rst.next()) {
            Employee employee = new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    LocalDate.parse(rst.getString(6)),
                    LocalDate.parse(rst.getString(7)),
                    rst.getString(8)
            );
            list.add(employee);
        }
        return list;
    }

    @Override
    public String getLastId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = SQLUtil.execute("SELECT employee_id FROM employee ORDER BY employee_id DESC LIMIT 1");
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    @Override
    public boolean save(Employee employee) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("insert into Employee values (?,?,?,?,?,?,?,?)",
                employee.getEmployeeId(),
                employee.getName(),
                employee.getContactNo(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getJoinDate().toString(),
                employee.getDateOfBirth().toString(),
                employee.getRole()
        );
    }

    @Override
    public boolean update(Employee employee) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("update Employee set name = ?, contact_no = ?, email = ?, address = ?, join_date = ?, date_of_birth = ?, role = ? where employee_id = ?",
                employee.getName(),
                employee.getContactNo(),
                employee.getEmail(),
                employee.getAddress(),
                employee.getJoinDate().toString(),
                employee.getDateOfBirth().toString(),
                employee.getRole(),
                employee.getEmployeeId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("delete from Employee where employee_id = ?", id);
    }

    @Override
    public List<String> getAllIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select employee_id from Employee");
        List<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    @Override
    public Optional<Employee> findById(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Employee WHERE employee_id=?", id);
        if (rst.next()) {
            return Optional.of(new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    LocalDate.parse(rst.getString(6)),
                    LocalDate.parse(rst.getString(7)),
                    rst.getString(8)
            ));
        }
        return Optional.empty();
    }

}