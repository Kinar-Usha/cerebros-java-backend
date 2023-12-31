DROP TABLE Cerebros_Trades;
DROP TABLE Cerebros_Orders;
--DROP TABLE Cerebros_Prices;
DROP TABLE Cerebros_Portfolio;
DROP TABLE Cerebros_ClientIdentifications;
DROP TABLE Cerebros_ClientPreferences;
DROP TABLE Cerebros_ClientPasswords;
DROP TABLE Cerebros_Cash;
--DROP TABLE Cerebros_Person;
--DROP TABLE Cerebros_INSTRUMENTS;
DROP TABLE Cerebros_Client;

-- Create the Client table
CREATE TABLE Cerebros_Client (
    clientId VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    dob DATE,
    postalCode VARCHAR(10),
    country VARCHAR(2) CHECK (country IN ('US', 'IE', 'IN'))
) ;

-- Create the ClientPasswords table
CREATE TABLE Cerebros_ClientPasswords (
    clientId VARCHAR(50),
    passwordHash VARCHAR(255),
    CONSTRAINT "Cerebros_ClientPasswords_PK" PRIMARY KEY (CLIENTID) ENABLE,
    CONSTRAINT "Cerebros_ClientPasswords_FK" FOREIGN KEY (CLIENTID) REFERENCES Cerebros_Client (CLIENTID) ENABLE
);

-- Create the ClientIdentifications table
CREATE TABLE Cerebros_ClientIdentifications (
    clientId VARCHAR(50),
    idType VARCHAR(50) CHECK (idType IN ('SSN', 'ADH', 'PSP')),
    idNumber VARCHAR(255) UNIQUE,
    CONSTRAINT "Cerebros_ClientIdentifications_PK" PRIMARY KEY (CLIENTID, IDTYPE) ENABLE,
    CONSTRAINT "Cerebros_ClientIdentifications_FK" FOREIGN KEY (CLIENTID) REFERENCES Cerebros_Client (CLIENTID) ENABLE
);

-- Create the ClientPreferences table
CREATE TABLE Cerebros_ClientPreferences (
    clientId VARCHAR2(50),
    purpose VARCHAR2(20) CHECK (purpose IN ('Investment', 'Savings', 'Retirement', 'Other')),
    riskTolerance VARCHAR2(10) CHECK (riskTolerance IN ('Low', 'Moderate', 'High')),
    timeHorizon VARCHAR2(20) CHECK (timeHorizon IN ('Short-term', 'Medium-term', 'Long-term')),
    incomeBracket VARCHAR2(10) CHECK (incomeBracket IN ('Low', 'Middle', 'High')),
    CONSTRAINT "Cerebros_ClientPreferences_PK" PRIMARY KEY (CLIENTID) ENABLE,
    CONSTRAINT "Cerebros_ClientPreferences_FK" FOREIGN KEY (CLIENTID) REFERENCES Cerebros_Client (CLIENTID) ENABLE
);

-- Create the Instruments table
--CREATE TABLE Cerebros_Instruments (
--    instrumentId VARCHAR(50) PRIMARY KEY,
--    description VARCHAR(255),
--    externalIdType VARCHAR(50),
--    externalId VARCHAR(255),
--    categoryId VARCHAR(100),
--    minQuantity DECIMAL(10, 2),
--    maxQuantity DECIMAL(10, 2)
--);

-- Create the Portfolio table
CREATE TABLE Cerebros_Portfolio (
    portfolioId NUMBER GENERATED as IDENTITY(START with 1 INCREMENT by 1) PRIMARY KEY  ,
    clientId VARCHAR(50) REFERENCES Cerebros_Client(clientId),
    instrumentId VARCHAR(50),
    description VARCHAR(50),
    categoryId VARCHAR(50),
    holdings DECIMAL(10, 2),
    price DECIMAL(10,2)
);

-- Create the Trades table
CREATE TABLE Cerebros_Trades (
    tradeId NUMBER GENERATED as IDENTITY(START with 1 INCREMENT by 1) PRIMARY KEY,
    clientId VARCHAR(50) REFERENCES Cerebros_Client(clientId),
    instrumentId VARCHAR(50),
    t_description VARCHAR(100),
    orderId VARCHAR(50),
    direction varchar(10),
    quantity DECIMAL(10,2),
    executionPrice DECIMAL(10, 2),
    cashValue DECIMAL(10, 2),
    executedTimestamp TIMESTAMP
);

-- Create the Orders table
CREATE TABLE Cerebros_Orders (
    orderId VARCHAR(50) PRIMARY KEY,
    clientId VARCHAR(50) REFERENCES Cerebros_Client(clientId),
    instrumentId VARCHAR(50),
    direction VARCHAR(10),
    quantity DECIMAL(10, 2),
    targetPrice DECIMAL(10, 2),
    placedTimestamp TIMESTAMP
);

CREATE TABLE Cerebros_Cash (
    clientId VARCHAR(50),
    cashRemaining DECIMAL(20,2),
    CONSTRAINT "Cerebros_Cash_PK" PRIMARY KEY (CLIENTID) ENABLE,
    CONSTRAINT "Cerebros_Cash_FK" FOREIGN KEY (CLIENTID) REFERENCES Cerebros_Client (CLIENTID) ENABLE
);

-- Create the Prices table
--CREATE TABLE Cerebros_Prices (
--    pricesId INT PRIMARY KEY,
--    instrumentId VARCHAR(50),
--    bidPrice DECIMAL(10, 2),
--    askPrice DECIMAL(10, 2),
--    timestamp TIMESTAMP
--);

--ALTER TABLE Cerebros_Portfolio
--ADD FOREIGN KEY (instrumentId)
--REFERENCES Cerebros_Instruments (instrumentId);
--
--ALTER TABLE Cerebros_Trades
--ADD FOREIGN KEY (instrumentId)
--REFERENCES Cerebros_Instruments (instrumentId);
--
--ALTER TABLE Cerebros_Orders
--ADD FOREIGN KEY (instrumentId)
--REFERENCES Cerebros_Instruments (instrumentId);
--
--ALTER TABLE Cerebros_Prices
--ADD FOREIGN KEY (instrumentId)
--REFERENCES Cerebros_Instruments (instrumentId);

--ALTER TABLE Cerebros_Trades
--ADD FOREIGN KEY (orderId)
--REFERENCES Cerebros_Orders (orderId);




-- SET SCAN OFF;

------------------------------------------------------------

INSERT INTO CEREBROS_CLIENT (CLIENTID, NAME, EMAIL, DOB, POSTALCODE, COUNTRY) VALUES ('YOUR_CLIENTID', 'John Doe', 'john.doe@gmail.com', TO_DATE('2001-09-11', 'YYYY-MM-DD'), '600097', 'IN');
INSERT INTO CEREBROS_CLIENTPASSWORDS (CLIENTID, PASSWORDHASH) VALUES ('YOUR_CLIENTID', '1234567890');
INSERT INTO CEREBROS_CLIENTPREFERENCES (CLIENTID, PURPOSE, RISKTOLERANCE, TIMEHORIZON, INCOMEBRACKET) VALUES ('YOUR_CLIENTID', 'Investment', 'High', 'Long-term', 'High');
INSERT INTO CEREBROS_CLIENTIDENTIFICATIONS (CLIENTID, IDTYPE, IDNUMBER) VALUES ('YOUR_CLIENTID', 'PSP', 'B7654321');
INSERT INTO CEREBROS_CLIENT (CLIENTID, NAME, EMAIL, DOB, POSTALCODE, COUNTRY) VALUES ('YOUR_CLIENTID1', 'Alice Doe', 'alice.doe@gmail.com', TO_DATE('2001-09-11', 'YYYY-MM-DD'), '600097', 'IN');
INSERT INTO CEREBROS_CLIENTPASSWORDS (CLIENTID, PASSWORDHASH) VALUES ('YOUR_CLIENTID1', '1234567890');
INSERT INTO CEREBROS_CLIENTIDENTIFICATIONS (CLIENTID, IDTYPE, IDNUMBER) VALUES ('YOUR_CLIENTID', 'SSN', '333-12-4445');

INSERT INTO cerebros_cash VALUES('YOUR_CLIENTID',1000000.00);
--INSERT INTO cerebros_cash VALUES('YOUR_CLIENTID1',1000000.00);


------------------------------------------------------------
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('Q123', 'Alphabet Inc. Class C Capital Stock', 'CUSIP', '02079K107', 'STOCK', 1, 1000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('Q456', 'Tesla, Inc. Common Stock', 'CUSIP', '88160R101', 'STOCK', 1, 1000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('N123456', 'JPMorgan Chase  Co. Capital Stock', 'CUSIP', '46625H100', 'STOCK', 1, 1000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('N123789', 'Berkshire Hathaway Inc. Class A', 'ISIN', 'US0846707026', 'STOCK', 1, 10);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('C100', 'JPMorgan Chase Bank, National Association 01/19', 'CUSIP', '48123Y5A0', 'CD', 100, 1000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67890', 'USA, Note 3.125 15nov2028 10Y', 'CUSIP', '9128285M8', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67894', 'USA, Note 2.5 31jan2024 5Y', 'CUSIP', '9128285Z9', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67895', 'USA, Note 2.625 31jan2026 7Y', 'CUSIP', '9128286A3', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67897', 'USA, Note 2.5 31jan2021 2Y', 'CUSIP', '9128285X4', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67899', 'USA, Notes 2.5% 15jan2022 3Y', 'CUSIP', '9128285V8', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67880', 'USA, Note 1.5 31dec2023 5Y', 'CUSIP', '9128285U0', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67883', 'USA, Bond 3.375 15nov2048 30Y', 'CUSIP', '912810SE9', 'GOVT', 100, 10000);
--
--INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
--VALUES ('T67878', 'USA, Bond 3 15aug2048 30Y', 'CUSIP', '912810SD1', 'GOVT', 100, 10000);
--
--------------------------------------------------------------------
----
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (1, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '46625H100'), 104.25, 104.75, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (2, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = 'US0846707026'), 312000, 312500, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (3, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '48123Y5A0'), 95.42, 95.92, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (4, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285M8'), 1.03390625, 1.03375, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (5, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285Z9'), 0.99828125, 0.998125, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (6, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128286A3'), 1.00015625, 1.0, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (7, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285X4'), 0.999375, 0.999375, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (8, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285V8'), 0.999375, 0.999375, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (9, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285U0'), 1.00375, 1.00375, '21-AUG-19 ');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (10, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '912810SE9'), 1.0596875, 1.0596875, '21-AUG-19');
--INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
--VALUES
--    (11, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '912810SD1'), 0.98546875, 0.9853125, '21-AUG-19');

-----------------------------------------------------------------
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId,description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'Q123', 'Alphabet Inc. Class C Capital Stock','CUSIP',100,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId,description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'Q456','Tesla, Inc. Common Stock', 'CUSIP', 50,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId,description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'N123456','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 200,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'N123789','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 10,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'C100','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 500,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'T67890','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 1000,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'T67894','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 300,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'T67895','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 700,100);
INSERT INTO Cerebros_Portfolio ( clientId, instrumentId, description,categoryId, holdings, price)
VALUES
    ( 'YOUR_CLIENTID', 'T67897','JPMorgan Chase  Co. Capital Stock', 'CUSIP', 200,100);  -- Example data, replace with actual values
--    (10, 'YOUR_CLIENTID', 'T67899', 400), -- Example data, replace with actual values
--    (11, 'YOUR_CLIENTID', 'T67880', 800), -- Example data, replace with actual values
--    (12, 'YOUR_CLIENTID', 'T67883', 150), -- Example data, replace with actual values
--    (13, 'YOUR_CLIENTID', 'T67878', 250); -- Example data, replace with actual values




-----------------------------------------------------------------
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_Q123_1', 'YOUR_CLIENTID', 'Q123', 'B', 100, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_Q456_1', 'YOUR_CLIENTID', 'Q456', 'B', 50, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_N123456_1', 'YOUR_CLIENTID', 'N123456', 'B', 200, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_N123789_1', 'YOUR_CLIENTID', 'N123789', 'B', 10, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_C100_1', 'YOUR_CLIENTID', 'C100', 'B', 500, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_T67890_1', 'YOUR_CLIENTID', 'T67890', 'B', 1000,100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_T67894_1', 'YOUR_CLIENTID', 'T67894', 'B', 300, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_T67895_1', 'YOUR_CLIENTID', 'T67895', 'B', 700, 100, '21-AUG-19');
INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_T67897_1', 'YOUR_CLIENTID', 'T67897', 'B', 200, 100, '21-AUG-19');
-------------------------------------------------------------------

---- add quantity and direction values
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description,orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'Q123', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_Q123_1','B',100, 100, -10475.00, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'Q456','JPMorgan Chase  Co. Capital Stock', 'BUY_ORDER_Q456_1','B',50, 100, -16169.50, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId, t_description,orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'N123456', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_N123456_1','B',200, 100, -20850.00, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'N123789', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_N123789_1','B',10, 100, -9592.00, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'C100','JPMorgan Chase  Co. Capital Stock', 'BUY_ORDER_C100_1','B',500, 100, -1033.75, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'T67890', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_T67890_1','B',1000, 100, -9981.25, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'T67894', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_T67894_1','B',300, 100, -10000.00, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId,t_description, orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'T67895', 'JPMorgan Chase  Co. Capital Stock','BUY_ORDER_T67895_1','B',700, 100, -9993.75, '21-AUG-19');
INSERT INTO Cerebros_Trades ( clientId, instrumentId, t_description,orderId,direction,quantity, executionPrice, cashValue, executedTimestamp)
VALUES
    ( 'YOUR_CLIENTID', 'T67897','JPMorgan Chase  Co. Capital Stock', 'BUY_ORDER_T67897_1','B',200, 100, -9993.75, '21-AUG-19');
--    (10, 'YOUR_CLIENTID', 'T67899', 'BUY_ORDER_T67899_1', 1.00375, -10037.50, '2023-09-19 13:30:00'),
--    (11, 'YOUR_CLIENTID', 'T67880', 'BUY_ORDER_T67880_1', 1.0596875, -10596.88, '2023-09-19 14:00:00'),
--    (12, 'YOUR_CLIENTID', 'T67883', 'BUY_ORDER_T67883_1', 0.9853125, -9853.13, '2023-09-19 14:30:00'),
--    (13, 'YOUR_CLIENTID', 'T67878', 'BUY_ORDER_T67878_1', 1162.42, -290605.00, '2023-09-19 15:00:00');
--
--------------------------------------------------------------------



COMMIT;

