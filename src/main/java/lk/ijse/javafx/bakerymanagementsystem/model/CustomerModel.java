package lk.ijse.javafx.bakerymanagementsystem.model;

import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import lk.ijse.javafx.bakerymanagementsystem.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static List<CustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM Customer");

        ArrayList<CustomerDto> list = new ArrayList<>();
        while (rst.next()) {
            CustomerDto customerDto = new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            list.add(customerDto);
        }
        return list;
    }
    public String getNextId() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select customer_id from Customer order by customer_id desc limit 1");

        char tableChr = 'C';

        if (rst.next()){
            String lastId = rst.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNum = lastIdNumber + 1;
            String nextIdString = String.format(tableChr + "%03d", nextIdNum);
            return nextIdString;

         }
         return "C001";
    }

//    public boolean checkLogin(String username, String password) throws Exception {
//        String sql = "SELECT * FROM Users WHERE user_name = ? AND password = ?";
//        ResultSet resultSet = CrudUtil.execute(sql, username, password);
//        return resultSet.next();
//    }

     public boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("insert into Customer values (?,?,?,?,?,?)",
              customerDto.getCustomerId(),
              customerDto.getName(),
              customerDto.getAddress(),
              customerDto.getNic(),
              customerDto.getContact(),
              customerDto.getEmail()
      );

    }

    public boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("delete from Customer where customer_id = ?",customerId);
    }

    public boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("update Customer set name = ?, address = ?,nic = ?, contact = ?,email = ? where customer_id = ?",
                customerDto.getName(),
                customerDto.getAddress(),
                customerDto.getNic(),
                customerDto.getContact(),
                customerDto.getEmail(),
                customerDto.getCustomerId());
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select customer_id from Customer");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("select name from Customer where customer_id=?", customerId);
        if (rst.next()) {
            return rst.getString(1);
        }
        return "";
    }
}
