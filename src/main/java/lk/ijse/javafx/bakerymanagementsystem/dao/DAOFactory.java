package lk.ijse.javafx.bakerymanagementsystem.dao;

import lk.ijse.javafx.bakerymanagementsystem.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        return daoFactory == null ? (daoFactory = new DAOFactory()) : daoFactory;
    }
    public <T extends SuperDAO> T getDAO(DAOTypes daoType) {
        return (T) switch (daoType){
            case ATTENDANCE  -> new AttendanceDAOImpl();
            case CUSTOMER -> new CustomerDAOImpl();
            case DELIVER -> new DeliverDAOImpl();
            case EMPLOYEE  -> new EmployeeDAOImpl();
            case EXPENSES  -> new ExpensesDAOImpl();
            case INGREDIENT  -> new IngredientDAOImpl();
            case INVENTORY  -> new InventoryDAOImpl();
            case ORDER   -> new OrderDAOImpl();
            case ORDER_DETAILS  -> new OrderDetailsDAOImpl();
            case PAYMENT   -> new PaymentDAOImpl();
            case PRODUCT   -> new ProductDAOImpl();
            case QUERY -> new QueryDAOImpl();
            case SALARY   -> new SalaryDAOImpl();
            case SUPPLIER   -> new SupplierDAOImpl();
            case USER -> new UserDAOImpl();
        };
    }
}
