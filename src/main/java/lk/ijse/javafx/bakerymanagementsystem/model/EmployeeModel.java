package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.EmployeeDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeModel {

    public static ArrayList<EmployeeDto> getAllEmployees() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Employee");

        ArrayList<EmployeeDto> list = new ArrayList<>();
        while (rst.next()) {
            EmployeeDto employeeDto = new EmployeeDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    LocalDate.parse(rst.getString(6)),
                    LocalDate.parse(rst.getString(7)),
                    rst.getString(8)
            );
            list.add(employeeDto);
        }
        return list;
    }

    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select employee_id from Employee order by employee_id desc limit 1");

        char tableChr = 'E';

        if (rst.next()){
            String lastId = rst.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNum = lastIdNumber + 1;
            String nextIdString = String.format(tableChr + "%03d", nextIdNum);
            return nextIdString;
        }

        return "E001";
    }

    public boolean saveEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("insert into Employee values (?,?,?,?,?,?,?,?)",
                employeeDto.getEmployeeId(),
                employeeDto.getName(),
                employeeDto.getContactNo(),
                employeeDto.getEmail(),
                employeeDto.getAddress(),
                employeeDto.getJoinDate().toString(),
                employeeDto.getDateOfBirth().toString(),
                employeeDto.getRole()
        );
    }

    public boolean deleteEmployee(String employeeId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("delete from Employee where employee_id = ?", employeeId);
    }

    public boolean updateEmployee(EmployeeDto employeeDto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("update Employee set name = ?, contact_no = ?, email = ?, address = ?, join_date = ?, date_of_birth = ?, role = ? where employee_id = ?",
                employeeDto.getName(),
                employeeDto.getContactNo(),
                employeeDto.getEmail(),
                employeeDto.getAddress(),
                employeeDto.getJoinDate().toString(),
                employeeDto.getDateOfBirth().toString(),
                employeeDto.getRole(),
                employeeDto.getEmployeeId());
    }

    public ArrayList<String> getAllEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute(
                "select employee_id from Employee"
        );

        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String employeeId) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute(
                "select name from Employee where employee_id=?",
                employeeId
        );
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }
}