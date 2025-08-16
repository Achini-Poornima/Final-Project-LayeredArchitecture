package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.CustomerDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.CustomerBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.InUseException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.CustomerDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOTypes.CUSTOMER);
    private final OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<CustomerDto> getAllCustomer() throws SQLException, ClassNotFoundException {
        List<Customer> customers = customerDAO.getAll();
        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer : customers) {
            dtos.add(converter.getCustomerDTO(customer));
        }
        return dtos;
    }

    @Override
    public void saveCustomer(CustomerDto dto) throws DuplicateException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(dto.getCustomerId());
        if (optionalCustomer.isPresent()) {
//            duplicate id
            throw new DuplicateException("Duplicate customer id");
        }

        Optional<Customer> customerByNicOptional = customerDAO.findCustomerByNic(dto.getNic());
        if (customerByNicOptional.isPresent()) {
            throw new DuplicateException("Duplicate customer nic");
        }

        if (customerDAO.existsCustomerByContactNumber(dto.getContact())) {
            throw new DuplicateException("Duplicate customer contact number");
        }

        Customer customer = converter.getCustomer(dto);
        boolean save = customerDAO.save(customer);
    }

    @Override
    public void updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        Optional<Customer> optionalCustomer = customerDAO.findById(dto.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Optional<Customer> customerByNicOptional = customerDAO.findCustomerByNic(dto.getNic());
        if (customerByNicOptional.isPresent()) {
            Customer customer = customerByNicOptional.get();

            if (customer.getCustomerId() != dto.getCustomerId()) {
                throw new DuplicateException("Duplicate nic");
            }
        }

        Customer customer = converter.getCustomer(dto);
        customerDAO.update(customer);
    }

    @Override
    public boolean deleteCustomer(String id) throws InUseException, Exception {
        Optional<Customer> optionalCustomer = customerDAO.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Customer not found..!");
        }

        if (orderDAO.existOrdersByCustomerId(id)) {
            throw new InUseException("Customer has orders");
        }

        try {
            boolean delete = customerDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = customerDAO.getLastId();
        char tableChar = 'C';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
