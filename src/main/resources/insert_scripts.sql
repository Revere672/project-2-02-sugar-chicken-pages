CREATE TABLE Employees (
    Employee_ID INT PRIMARY KEY NOT NULL,
    Employee_Name VARCHAR(50) NOT NULL,
    Employee_Email VARCHAR(50) NOT NULL,
    Employee_Role VARCHAR(15) NOT NULL,
    Last_Login TIMESTAMP NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Passwords (
    Employee_ID INT PRIMARY KEY REFERENCES Employees(Employee_ID) NOT NULL,
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE Order_History (
    Order_ID SERIAL PRIMARY KEY,
    Order_Time TIMESTAMP NOT NULL,
    Total_Price NUMERIC(10, 2) NOT NULL,
    Employee_ID INT NOT NULL REFERENCES Employees(Employee_ID)
);

CREATE TABLE Overarching_Items (
    Item_Name VARCHAR(50) PRIMARY KEY,
    Side_Count INT NOT NULL,
    Protein_Count INT NOT NULL,
    Has_Misc BOOLEAN NOT NULL,
    Item_Cost NUMERIC(10, 2) NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Order_Items (
    Order_ID INT NOT NULL,
    Item_Number INT NOT NULL,
    Item_Name VARCHAR(50) REFERENCES Overarching_Items(Item_Name) NOT NULL, 
    Side_1 VARCHAR(3) REFERENCES Menu(Menu_ID),    
    Side_2 VARCHAR(3) REFERENCES Menu(Menu_ID),   
    Protein_1 VARCHAR(3) REFERENCES Menu(Menu_ID), 
    Protein_2 VARCHAR(3) REFERENCES Menu(Menu_ID), 
    Protein_3 VARCHAR(3) REFERENCES Menu(Menu_ID), 
    Misc_Item VARCHAR(3) REFERENCES Menu(Menu_ID), 
    Item_Cost NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (Order_ID, Item_Number),
    CONSTRAINT fk_order
        FOREIGN KEY (Order_ID) REFERENCES Order_History(Order_ID)
);

CREATE TABLE Overarching_Items (
    Item_Name VARCHAR(50) PRIMARY KEY,
    Side_Count INT NOT NULL,
    Protein_Count INT NOT NULL,
    Has_Misc BOOLEAN NOT NULL,
    Item_Cost NUMERIC(10, 2) NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Menu(
    Menu_ID VARCHAR(3) PRIMARY KEY,
    Menu_Name VARCHAR(30) NOT NULL,
    Extra_Cost NUMERIC(10, 2) NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Inventory(
    Inventory_ID INT PRIMARY KEY,
    Product_Name VARCHAR(30) NOT NULL,
    Supplier VARCHAR(50),
    Cost FLOAT NOT NULL,
    Quantity FLOAT NOT NULL,
    Restock_Quantity FLOAT NOT NULL,
    Actions VARCHAR(30)
);

CREATE TABLE Ingredients_Needed (
    Menu_ID CHAR(10) NOT NULL REFERENCES Menu(Menu_ID),
    Inventory_ID INT NOT NULL REFERENCES Inventory(Inventory_ID),
    Quantity_Needed FLOAT NOT NULL,
    PRIMARY KEY (Menu_ID, Inventory_ID)
);



