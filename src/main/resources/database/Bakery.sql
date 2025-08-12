DROP DATABASE Bakery;
CREATE DATABASE Bakery;
USE Bakery;

CREATE TABLE Customer (
                          customer_id VARCHAR(100) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          address VARCHAR(100),
                          nic VARCHAR(100),
                          contact VARCHAR(100) NOT NULL,
                          email VARCHAR(100)
);

CREATE TABLE Users (
                       user_id VARCHAR(100) PRIMARY KEY,
                       user_name VARCHAR(100) NOT NULL,
                       password VARCHAR(10) NOT NULL,
                       role VARCHAR(100) NOT NULL
);

CREATE TABLE Orders (
                        order_id VARCHAR(100) PRIMARY KEY,
                        customer_id VARCHAR(100) REFERENCES Customer(customer_id),
                        order_date DATETIME,
                        payment_status VARCHAR(10) NOT NULL
);

CREATE TABLE Deliver (
                         deliver_id VARCHAR(100) PRIMARY KEY,
                         deliver_address VARCHAR(100),
                         deliver_charge DECIMAL(10,2),
                         deliver_date DATETIME,
                         order_id VARCHAR(100) REFERENCES Orders(order_id)
);

CREATE TABLE Employee (
                          employee_id VARCHAR(100) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          contact_no CHAR(10) NOT NULL,
                          email VARCHAR(100),
                          address VARBINARY(100),
                          join_date DATE,
                          date_of_birth Date,
                          role VARCHAR(40)
);

CREATE TABLE Salary (
                        salary_id VARCHAR(100) PRIMARY KEY,
                        basic_salary DECIMAL(10,2),
                        bonus DECIMAL(10,2),
                        net_salary DECIMAL(10,2),
                        payment_date DATE,
                        employee_id VARCHAR(100) REFERENCES Employee(employee_id)
);

CREATE TABLE Expenses (
                          expenses_id VARCHAR(100) PRIMARY KEY,
                          category VARCHAR(100),
                          amount DECIMAL(10,2),
                          date DATE
);

CREATE TABLE Payment (
                         payment_id VARCHAR(100) PRIMARY KEY,
                         amount DECIMAL(10,2),
                         payment_method VARCHAR(50),
                         payment_date DATE,
                         order_id VARCHAR(100) REFERENCES Orders(order_id)
);

CREATE TABLE Supplier (
                          supplier_id VARCHAR(100) PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          supplied_ingredient VARCHAR(255),
                          address VARCHAR(100),
                          email VARCHAR(100)
);

CREATE TABLE Product (
                         product_id VARCHAR(100) PRIMARY KEY,
                         name VARCHAR(100),
                         price DECIMAL(10,2),
                         stock_quantity INT,
                         supplier_id VARCHAR(100) REFERENCES Supplier(supplier_id)
);

CREATE TABLE Ingredient (
                            ingredient_id VARCHAR(100) PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            expire_date DATE,
                            quantity_available DECIMAL(10,2),
                            supplier_id VARCHAR(100) REFERENCES Supplier(supplier_id)
);

CREATE TABLE Inventory (
                           inventory_id VARCHAR(100) PRIMARY KEY,
                           stock_quantity INT,
                           product_id VARCHAR(100) REFERENCES Product(product_id),
                           ingredient_id VARCHAR(100) REFERENCES Ingredient(ingredient_id)
);

CREATE TABLE Order_Details (
                               order_id VARCHAR(100) REFERENCES Orders(order_id),
                               product_id VARCHAR(100) REFERENCES Product(product_id),
                               quantity INT,
                               price DECIMAL(10,2)
);

CREATE TABLE Attendance(
    attendance_id VARCHAR(100) PRIMARY KEY ,
    employee_id VARCHAR(100) REFERENCES Employee(employee_id),
    in_time VARCHAR(100),
    out_time VARCHAR(100),
    date VARCHAR(100)
);