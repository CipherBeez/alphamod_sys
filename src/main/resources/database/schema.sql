DROP DATABASE IF EXISTS alpha;
CREATE DATABASE alpha;
USE alpha;

-- Item Table
CREATE TABLE Item (
                      item_id VARCHAR(500) NOT NULL,
                      item_name VARCHAR(300) NOT NULL,
                      quantity INT NOT NULL,
                      buying_price DECIMAL(10,2) NOT NULL,
                      selling_price DECIMAL(10,2) NOT NULL,
                      PRIMARY KEY (item_id)
);

-- Orders Table
CREATE TABLE Orders (
                        order_id VARCHAR(10) NOT NULL,
                        order_date DATE NOT NULL,
                        PRIMARY KEY (order_id)
    -- ❌ Removed ON DELETE/UPDATE here: this is not valid on the primary key directly
);

-- order_details Table
CREATE TABLE order_details (
                               order_id VARCHAR(20),
                               item_id VARCHAR(20),
                               qty INT NOT NULL,
                               unit_price DECIMAL(10,2) NOT NULL,
                               PRIMARY KEY (order_id, item_id),
                               FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
                               FOREIGN KEY (item_id) REFERENCES Item(item_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Supplier Table
CREATE TABLE Supplier (
                          supplier_id VARCHAR(10) NOT NULL,
                          supplier_name VARCHAR(150) NOT NULL,
                          supplier_contact VARCHAR(10),
                          supplier_address VARCHAR(100),
                          PRIMARY KEY (supplier_id)
);

-- Supplier_Orders Table
CREATE TABLE Supplier_Orders (
                                 sup_order_id VARCHAR(10) NOT NULL,
                                 supplier_id VARCHAR(10) NOT NULL,
                                 order_date DATE NOT NULL,
                                 PRIMARY KEY (sup_order_id),
                                 FOREIGN KEY (supplier_id) REFERENCES Supplier(supplier_id)
                                     ON UPDATE CASCADE
                                     ON DELETE CASCADE
);

-- sup_order_details Table
CREATE TABLE sup_order_details (
                                   sup_order_id VARCHAR(20),
                                   item_id VARCHAR(20),
                                   qty INT NOT NULL,
                                   unit_price DECIMAL(10,2) NOT NULL,
                                   PRIMARY KEY (sup_order_id, item_id),
                                   FOREIGN KEY (sup_order_id) REFERENCES Supplier_Orders(sup_order_id)
                                       ON DELETE CASCADE ON UPDATE CASCADE,
                                   FOREIGN KEY (item_id) REFERENCES Item(item_id)
                                       ON DELETE CASCADE ON UPDATE CASCADE
);


-- Dummy Data

-- Insert dummy suppliers
INSERT INTO Supplier (supplier_id, supplier_name, supplier_contact, supplier_address) VALUES
                                                                                          ('S001', 'ABC Traders', '0771234567', 'Colombo 01'),
                                                                                          ('S002', 'Global Supplies', '0779876543', 'Kandy'),
                                                                                          ('S003', 'Quick Mart', '0714567890', 'Galle');

-- Insert dummy items
INSERT INTO Item (item_id, item_name, quantity, buying_price, selling_price) VALUES
                                                                                 ('I001', 'Pen', 100, 20.00, 35.00),
                                                                                 ('I002', 'Notebook', 200, 50.00, 80.00),
                                                                                 ('I003', 'Pencil', 150, 10.00, 20.00),
                                                                                 ('I004', 'Eraser', 300, 5.00, 10.00);
