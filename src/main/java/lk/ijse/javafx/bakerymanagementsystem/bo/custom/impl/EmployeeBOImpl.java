package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.EmployeeDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.EmployeeBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.EmployeeDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeBOImpl implements EmployeeBO {
    private final EmployeeDAO employeeDAO = DAOFactory.getInstance().getDAO(DAOTypes.EMPLOYEE);
    private final EntityDTOConverter entityDTOConverter = new EntityDTOConverter();

    @Override
    public List<EmployeeDto> getAllEmployee() throws SQLException, ClassNotFoundException {
        List<Employee> employees = employeeDAO.getAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDtos.add(entityDTOConverter.getEmployeeDto(employee));
        }
        return employeeDtos;
    }

    @Override
    public void saveEmployee(EmployeeDto dto) throws DuplicateException, Exception {
        Optional<Employee> optionalEmployee = employeeDAO.findById(dto.getEmployeeId());
        if (optionalEmployee.isPresent()) {
            throw new DuplicateException("Duplicate Employee id");
        }
        Employee employee = entityDTOConverter.getEmployee(dto);
        boolean save = employeeDAO.save(employee);
    }

    @Override
    public void updateEmployee(EmployeeDto dto) throws SQLException, ClassNotFoundException {
        Optional<Employee> optionalEmployee = employeeDAO.findById(dto.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("Employee not found");
        }
        Employee employee = entityDTOConverter.getEmployee(dto);
        boolean update = employeeDAO.update(employee);
    }

    @Override
    public boolean deleteEmployee(String id) throws InUseException, Exception {
        Optional<Employee> optionalEmployee = employeeDAO.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new NotFoundException("Employee not found..!");
        }
        try {
            boolean delete = employeeDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = employeeDAO.getLastId();
        char tableChar = 'E';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
