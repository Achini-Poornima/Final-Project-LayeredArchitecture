package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.Dto.PaymentDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.PaymentBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.PaymentDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Payment;
import lk.ijse.javafx.bakerymanagementsystem.entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentBOImpl implements PaymentBO {
    private final EntityDTOConverter converter = new EntityDTOConverter();
    private final PaymentDAO paymentDAO = DAOFactory.getInstance().getDAO(DAOTypes.PAYMENT);

    @Override
    public List<PaymentDto> getAllPayment() throws SQLException, ClassNotFoundException {
        List<Payment> payments = paymentDAO.getAll();
        List<PaymentDto> paymentDto = new ArrayList<>();
        for (Payment payment : payments) {
            paymentDto.add(converter.getPaymentDto(payment));
        }
        return paymentDto;
    }

    @Override
    public void savePayment(PaymentDto dto) throws SQLException, ClassNotFoundException {
        Optional<Payment> optionalPayment = paymentDAO.findById(dto.getPaymentId());
        if (optionalPayment.isPresent()) {
            throw new DuplicateException("Duplicate Payment ID");
        }
        Payment payment = converter.getPayment(dto);
        boolean save = paymentDAO.save(payment);

    }

    @Override
    public void updatePayment(PaymentDto dto) throws SQLException, ClassNotFoundException {
        Optional<Payment> optionalPayment = paymentDAO.findById(dto.getPaymentId());
        if (optionalPayment.isPresent()) {
            throw new NotFoundException("Payment Not Found");
        }

        Payment payment = converter.getPayment(dto);
        paymentDAO.update(payment);

    }

    @Override
    public boolean deletePayment(String id) throws SQLException, ClassNotFoundException {
        Optional<Payment> optionalPayment = paymentDAO.findById(id);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment not found..!");
        }

        try {
            boolean delete = paymentDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        Optional<String> lastIdOpt = paymentDAO.getLastId();

        if (lastIdOpt.isPresent()) {
            String lastId = lastIdOpt.get();
            int num = Integer.parseInt(lastId.substring(1)) + 1;
            return String.format("P%03d", num);
        } else {
            return "P001";
        }
    }

}
