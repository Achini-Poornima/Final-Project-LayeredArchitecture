package lk.ijse.javafx.bakerymanagementsystem.bo;

import lk.ijse.javafx.bakerymanagementsystem.bo.custom.UserBO;
import lk.ijse.javafx.bakerymanagementsystem.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory bFactory;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return bFactory ==  null ? (bFactory = new BOFactory()) : bFactory;
    }

    @SuppressWarnings("unchecked")
    public <Hello extends SuperBO> Hello getBo(BOTypes type) {
        return switch (type){
            case ATTENDANCE -> (Hello) new AttendanceBOImpl();
            case CUSTOMER -> (Hello) new CustomerBOImpl();
            case DELIVER -> (Hello) new DeliverBOImpl();
            case EMPLOYEE -> (Hello) new EmployeeBOImpl();
            case EXPENSES -> (Hello) new EmployeeBOImpl();
            case INGREDIENT -> (Hello) new IngredientBOImpl();
            case INVENTORY -> (Hello) new InventoryBOImpl();
            case PAYMENT -> (Hello) new PaymentBOImpl();
            case PLACE_ORDER -> (Hello) new PlaceOrderBOImpl();
            case PRODUCT -> (Hello) new ProductBOImpl();
            case SALARY -> (Hello) new SalaryBOImpl();
            case SUPPLIER -> (Hello) new SupplierBOImpl();
            case USER -> (Hello) new UserBOImpl();
        };
    }
}
