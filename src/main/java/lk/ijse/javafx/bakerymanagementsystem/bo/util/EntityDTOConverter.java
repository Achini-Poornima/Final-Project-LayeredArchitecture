package lk.ijse.javafx.bakerymanagementsystem.bo.util;

import lk.ijse.javafx.bakerymanagementsystem.Dto.*;
import lk.ijse.javafx.bakerymanagementsystem.entity.*;

public class EntityDTOConverter {
    public AttendanceDto getAttendanceDto(Attendance attendance) {
        AttendanceDto dto = new AttendanceDto();
        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setEmployeeId(attendance.getEmployeeId());
        dto.setInTime(attendance.getInTime());
        dto.setOutTime(attendance.getOutTime());
        dto.setDate(attendance.getDate());
        return dto;
    }

    public CustomerDto getCustomerDTO(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setAddress(customer.getAddress());
        dto.setNic(customer.getNic());
        dto.setContact(customer.getContact());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public DeliverDto getDeliverDto(Deliver deliver) {
        DeliverDto dto = new DeliverDto();
        dto.setDeliverId(deliver.getDeliverId());
        dto.setDeliverAddress(deliver.getDeliverAddress());
        dto.setDeliverCharge(deliver.getDeliverCharge());
        dto.setDeliverDate(deliver.getDeliverDate());
        dto.setOrderId(deliver.getOrderId());
        return dto;
    }

    public EmployeeDto getEmployeeDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setEmployeeId(employee.getEmployeeId());
        dto.setName(employee.getName());
        dto.setAddress(employee.getAddress());
        dto.setContactNo(employee.getContactNo());
        dto.setEmail(employee.getEmail());
        dto.setJoinDate(employee.getJoinDate());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setRole(employee.getRole());
        return dto;
    }

    public ExpensesDto getExpensesDto(Expenses expenses) {
        ExpensesDto dto = new ExpensesDto();
        dto.setExpensesId(expenses.getExpensesId());
        dto.setCategory(expenses.getCategory());
        dto.setAmount(expenses.getAmount());
        dto.setDate(expenses.getDate());
        return dto;
    }

    public IngredientDto getIngredientDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setIngredientId(ingredient.getIngredientId());
        dto.setName(ingredient.getName());
        dto.setExpireDate(ingredient.getExpireDate());
        dto.setQuantityAvailable(ingredient.getQuantityAvailable());
        dto.setSupplierId(ingredient.getSupplierId());
        return dto;
    }

    public InventoryDto getInventoryDto(Inventory inventory) {
        InventoryDto dto = new InventoryDto();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setStockQuantity(inventory.getStockQuantity());
        dto.setProductId(inventory.getProductId());
        dto.setIngredientId(inventory.getIngredientId());
        return dto;
    }

    public PaymentDto getPaymentDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setOrderId(payment.getOrderId());
        return dto;
    }

    public ProductDto getProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setSupplierId(product.getSupplierId());
        return dto;
    }

    public SalaryDto getSalaryDto(Salary salary) {
        SalaryDto dto = new SalaryDto();
        dto.setSalaryId(salary.getSalaryId());
        dto.setBasicSalary(salary.getBasicSalary());
        dto.setBonus(salary.getBonus());
        dto.setNetSalary(salary.getNetSalary());
        dto.setPaymentDate(salary.getPaymentDate());
        dto.setEmployeeId(salary.getEmployeeId());
        return dto;
    }

    public SupplierDto getSupplierDto(Supplier supplier) {
        SupplierDto dto = new SupplierDto();
        dto.setSupplierId(supplier.getSupplierId());
        dto.setName(supplier.getName());
        dto.setSuppliedIngredient(supplier.getSuppliedIngredient());
        dto.setAddress(supplier.getAddress());
        dto.setEmail(supplier.getEmail());
        return dto;
    }

    public UserDto getUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    public Attendance getAttendance(AttendanceDto dto) {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(dto.getAttendanceId());
        attendance.setEmployeeId(dto.getEmployeeId());
        attendance.setInTime(dto.getInTime());
        attendance.setOutTime(dto.getOutTime());
        attendance.setDate(dto.getDate());
        return attendance;
    }

    public Customer getCustomer(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setNic(dto.getNic());
        customer.setContact(dto.getContact());
        customer.setEmail(dto.getEmail());
        return customer;
    }

    public Deliver getDeliver(DeliverDto dto) {
        Deliver deliver = new Deliver();
        deliver.setDeliverId(dto.getDeliverId());
        deliver.setDeliverAddress(dto.getDeliverAddress());
        deliver.setDeliverCharge(dto.getDeliverCharge());
        deliver.setDeliverDate(dto.getDeliverDate());
        deliver.setOrderId(dto.getOrderId());
        return deliver;
    }

    public Employee getEmployee(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setName(dto.getName());
        employee.setAddress(dto.getAddress());
        employee.setContactNo(dto.getContactNo());
        employee.setEmail(dto.getEmail());
        employee.setJoinDate(dto.getJoinDate());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setRole(dto.getRole());
        return employee;
    }

    public Expenses getExpenses(ExpensesDto dto) {
        Expenses expenses = new Expenses();
        expenses.setExpensesId(dto.getExpensesId());
        expenses.setCategory(dto.getCategory());
        expenses.setAmount(dto.getAmount());
        expenses.setDate(dto.getDate());
        return expenses;
    }

    public Ingredient getIngredient(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(dto.getIngredientId());
        ingredient.setName(dto.getName());
        ingredient.setExpireDate(dto.getExpireDate());
        ingredient.setQuantityAvailable(dto.getQuantityAvailable());
        ingredient.setSupplierId(dto.getSupplierId());
        return ingredient;
    }

    public Inventory getInventory(InventoryDto dto) {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(dto.getInventoryId());
        inventory.setStockQuantity(dto.getStockQuantity());
        inventory.setProductId(dto.getProductId());
        inventory.setIngredientId(dto.getIngredientId());
        return inventory;
    }

    public Payment getPayment(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setOrderId(dto.getOrderId());
        return payment;
    }

    public Product getProduct(ProductDto dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSupplierId(dto.getSupplierId());
        return product;
    }

    public Salary getSalary(SalaryDto dto) {
        Salary salary = new Salary();
        salary.setSalaryId(dto.getSalaryId());
        salary.setBasicSalary(dto.getBasicSalary());
        salary.setBonus(dto.getBonus());
        salary.setNetSalary(dto.getNetSalary());
        salary.setPaymentDate(dto.getPaymentDate());
        salary.setEmployeeId(dto.getEmployeeId());
        return salary;
    }

    public Supplier getSupplier(SupplierDto dto) {
        Supplier supplier = new Supplier();
        supplier.setSupplierId(dto.getSupplierId());
        supplier.setName(dto.getName());
        supplier.setSuppliedIngredient(dto.getSuppliedIngredient());
        supplier.setAddress(dto.getAddress());
        supplier.setEmail(dto.getEmail());
        return supplier;
    }

    public User getUser(UserDto dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

}
