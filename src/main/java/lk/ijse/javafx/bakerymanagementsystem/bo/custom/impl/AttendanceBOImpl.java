package lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl;

import lk.ijse.javafx.bakerymanagementsystem.Dto.AttendanceDto;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.AttendanceBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.DuplicateException;
import lk.ijse.javafx.bakerymanagementsystem.bo.exception.NotFoundException;
import lk.ijse.javafx.bakerymanagementsystem.bo.util.EntityDTOConverter;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOFactory;
import lk.ijse.javafx.bakerymanagementsystem.dao.DAOTypes;
import lk.ijse.javafx.bakerymanagementsystem.dao.custom.AttendanceDAO;
import lk.ijse.javafx.bakerymanagementsystem.entity.Attendance;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceBOImpl implements AttendanceBO {

    private final AttendanceDAO attendanceDAO = DAOFactory.getInstance().getDAO(DAOTypes.ATTENDANCE);
    private final EntityDTOConverter converter = new EntityDTOConverter();

    @Override
    public List<AttendanceDto> getAllAttendance() throws SQLException, ClassNotFoundException {
        List<Attendance> attendances = attendanceDAO.getAll();
        List<AttendanceDto> attendanceDto = new ArrayList<>();
        for (Attendance attendance : attendances) {
            attendanceDto.add(converter.getAttendanceDto(attendance));
        }
        return attendanceDto;
    }

    @Override
    public void saveAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(dto.getAttendanceId());
        if (optionalAttendance.isPresent()) {
            throw new DuplicateException("Duplicate Attendance ID");
        }
        Attendance attendance = converter.getAttendance(dto);
        boolean save = attendanceDAO.save(attendance);
    }

    @Override
    public void updateAttendance(AttendanceDto dto) throws SQLException, ClassNotFoundException {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(dto.getAttendanceId());
        if (optionalAttendance.isPresent()) {
            throw new NotFoundException("Attendance Not Found");
        }

        Attendance attendance = converter.getAttendance(dto);
        attendanceDAO.update(attendance);
    }

    @Override
    public boolean deleteAttendance(String id) throws SQLException, ClassNotFoundException {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(id);
        if (optionalAttendance.isEmpty()) {
            throw new NotFoundException("Attendance not found..!");
        }

        try {
            boolean delete = attendanceDAO.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getNextId() throws SQLException, ClassNotFoundException {
        String lastId = attendanceDAO.getLastId();
        char tableChar = 'A';
        if (lastId != null) {
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format(tableChar + "%03d", nextIdNumber);
        }
        return tableChar + "001";
    }
}
