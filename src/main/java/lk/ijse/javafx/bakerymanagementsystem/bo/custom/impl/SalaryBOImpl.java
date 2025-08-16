package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.SalaryDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.SalaryBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.SalaryDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Salary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalaryBOImpl implements SalaryBO {
    private final SalaryDAO salaryDAO = DAOFactory.getInstance().getDAO(DAOTypes.SALARY);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<SalaryDto> getAllSalary() throws SQLException, ClassNotFoundException {
        List<Salary> salarys = salaryDAO.getAll();
        List<SalaryDto> salaryDto = new ArrayList<>();
        for (Salary salary : salarys) {
            salaryDto.add(converter.getSalaryDto(salary));
        }
        return salaryDto;
    }

    @Override
    public List<SalaryDto> saveSalary(SalaryDto dto) throws SQLException, ClassNotFoundException {
        List<Salary> salarys = salaryDAO.getAll();
        List<SalaryDto> salaryDto = new ArrayList<>();
        for (Salary salary : salarys) {
            salaryDto.add(converter.getSalaryDto(salary));
        }
        return salaryDto;
    }

    @Override
    public List<SalaryDto> updateSalary(SalaryDto dto) throws SQLException, ClassNotFoundException {
        List<Salary> salarys = salaryDAO.getAll();
        List<SalaryDto> salaryDto = new ArrayList<>();
        for (Salary salary : salarys) {
            salaryDto.add(converter.getSalaryDto(salary));
        }
        return salaryDto;
    }

    @Override
    public boolean deleteSalary(String id) throws SQLException, ClassNotFoundException {
        Optional<Salary> optionalSalary = salaryDAO.findById(id);
        if (optionalSalary.isEmpty()) {
            throw new NotFoundException("Salary Details not found..!");
        }

        try {
            boolean delete = salaryDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        Optional<String> lastIdOptional = salaryDAO.getLastId(); // make sure your DAO returns Optional<String>
        String tableChar = "SA";

        if (lastIdOptional.isPresent()) {
            String lastId = lastIdOptional.get();          // safely get the value
            String lastIdNumberString = lastId.substring(2); // skip "SA" prefix
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

}
