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
    Password VARCHAR(255) NOT NULL -- will likely be stored as a SHA256 hash
);

CREATE TABLE Order_History (
    Order_ID SERIAL PRIMARY KEY,
    Order_Time TIMESTAMP NOT NULL,
    Total_Price NUMERIC(10, 2) NOT NULL, -- Will eventually add from Overarching_Items + ExtraCosts in Menu_Item
    Employee_ID INT NOT NULL REFERENCES Employees(Employee_ID)
);

CREATE TABLE Overarching_Items ( -- This table represents overarching itens being 'Bowl', 'Big Plate', 'A la Carte small/med/large' et cetera.
    Item_Name VARCHAR(50) PRIMARY KEY,
    Side_Count INT NOT NULL,
    Protein_Count INT NOT NULL,
    Has_Misc BOOLEAN NOT NULL,
    Item_Cost NUMERIC(10, 2) NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Menu( -- This table represents individual items like fried rice, super greens. Normal size for the entrees is actually small, so if I order a bowl I will get 'Fried Rice S' menu key. If I order A la Carte medium fried rice I will get 'Fried Rice M'
    Menu_Name VARCHAR(50) PRIMARY KEY, -- Fried Rice S, Orange Chicken S, Drink M, etc.
    Menu_ID VARCHAR(6) NOT NULL, -- Same as the GUI stuff, R2, R1, V4, etc.
    Extra_Cost NUMERIC(10, 2) NOT NULL,
    Active BOOLEAN NOT NULL
);

CREATE TABLE Order_Items ( -- This table represents the comprehensive list of orderes and all their items inside. It will carry an orderiD, an item sub-number, a dish that is in overarching items, and the various parts 
    Order_ID INT NOT NULL, 
    Item_Number INT NOT NULL,
    Item_Name VARCHAR(50) REFERENCES Overarching_Items(Item_Name) NOT NULL, -- Reference the main menu items
    Side_1 VARCHAR(50) REFERENCES Menu(Menu_Name), -- All referencing the names in Menu, like 'Fried Rice S'
    Side_2 VARCHAR(50) REFERENCES Menu(Menu_Name),   
    Protein_1 VARCHAR(50) REFERENCES Menu(Menu_Name), 
    Protein_2 VARCHAR(50) REFERENCES Menu(Menu_Name), 
    Protein_3 VARCHAR(50) REFERENCES Menu(Menu_Name), 
    Misc_Item VARCHAR(50) REFERENCES Menu(Menu_Name), 
    Item_Cost NUMERIC(10, 2) NOT NULL,
    PRIMARY KEY (Order_ID, Item_Number),
    CONSTRAINT fk_order
        FOREIGN KEY (Order_ID) REFERENCES Order_History(Order_ID)
);

CREATE TABLE Inventory( -- This table represents ingredients that are used for 
    Inventory_ID INT PRIMARY KEY,
    Product_Name VARCHAR(50) NOT NULL,
    Supplier VARCHAR(50),
    Cost FLOAT NOT NULL, -- per pound
    Quantity FLOAT NOT NULL,
    Restock_Quantity FLOAT NOT NULL,
    Actions VARCHAR(30)
);

CREATE TABLE Ingredients_Needed ( -- This is a junction table between Menu and Inventory that keeps track of the mount of ingredients that need to be used.
    Menu_Name VARCHAR(50) NOT NULL REFERENCES Menu(Menu_Name),
    Inventory_ID INT NOT NULL REFERENCES Inventory(Inventory_ID),
    Quantity_Needed FLOAT NOT NULL,
    PRIMARY KEY (Menu_Name, Inventory_ID)
);



