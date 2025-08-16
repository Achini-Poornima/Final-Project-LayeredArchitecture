package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.DeliverDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.DeliverBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.DeliverDAO;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.OrderDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Deliver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliverBOImpl implements DeliverBO {
    private final DeliverDAO deliverDAO= DAOFactory.getInstance().getDAO(DAOTypes.DELIVER);
    private final OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOTypes.ORDER);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<DeliverDto> getAllDeliver() throws SQLException, ClassNotFoundException {
        List<Deliver> delivers = deliverDAO.getAll();
        List<DeliverDto> deliverDtos = new ArrayList<>();
        for (Deliver deliver : delivers) {
            deliverDtos.add(converter.getDeliverDto(deliver));
        }
        return deliverDtos;
    }

    @Override
    public void saveDeliver(DeliverDto dto) throws SQLException, ClassNotFoundException {
        Optional<Deliver> optionalDeliver = deliverDAO.findById(dto.getDeliverId());
        if (optionalDeliver.isPresent()) {
            throw new DuplicateException("Duplicate deliver id");
        }
        Deliver deliver = converter.getDeliver(dto);
        boolean save = deliverDAO.save(deliver);
    }

    @Override
    public void updateDeliver(DeliverDto dto) throws SQLException, ClassNotFoundException {
        Optional<Deliver> optionalCustomer = deliverDAO.findById(dto.getDeliverId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException("Deliver not found");
        }
        Deliver deliver = converter.getDeliver(dto);
        deliverDAO.update(deliver);
    }

    @Override
    public boolean deleteDeliver(String id) throws SQLException, ClassNotFoundException {
        Optional<Deliver> optionalDeliver = deliverDAO.findById(id);
        if (optionalDeliver.isEmpty()) {
            throw new NotFoundException("Deliver not found..!");
        }
        try {
            boolean delete = deliverDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = String.valueOf(deliverDAO.getLastId());
        char tableChar = 'D';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }

}
