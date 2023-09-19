-- Connect to the database
-- You need to connect to your database using appropriate credentials before running these commands.

-- Create the SCOTT schema if it doesn't exist

-- Use the SCOTT schema
SET search_path TO SCOTT;

-- Create the Client table
CREATE TABLE Cerebros_Client (
    clientId VARCHAR(50) PRIMARY KEY,
    person_id VARCHAR(50),
    preference_id VARCHAR(50)
);

-- Create the Person table
CREATE TABLE Cerebros_Person (
    person_id INT PRIMARY KEY,
    client_id VARCHAR(50),
    name VARCHAR(255),
    email VARCHAR(255),
    dob DATE,
    postalCode VARCHAR(10),
    country VARCHAR(50)
);

-- Create the ClientPasswords table
CREATE TABLE Cerebros_ClientPasswords (
    clientId VARCHAR(50) PRIMARY KEY,
    passwordHash VARCHAR(255)
);

-- Create the ClientIdentifications table
CREATE TABLE Cerebros_ClientIdentifications (
    clientId VARCHAR(50),
    idType VARCHAR(50),
    idNumber VARCHAR(255),
    PRIMARY KEY (clientId, idType)
);

-- Create the ClientPreferences table
CREATE TABLE Cerebros_ClientPreferences (
    clientId VARCHAR(50) PRIMARY KEY,
    purpose ENUM('Investment', 'Savings', 'Retirement', 'Other'),
    riskTolerance ENUM('Low', 'Moderate', 'High'),
    timeHorizon ENUM('Short-term', 'Medium-term', 'Long-term'),
    incomeBracket ENUM('Low', 'Middle', 'High')
);

-- Create the Instruments table
CREATE TABLE Cerebros_Instruments (
    instrumentId VARCHAR(50) PRIMARY KEY,
    description VARCHAR(255),
    externalIdType VARCHAR(50),
    externalId VARCHAR(255),
    categoryId INT,
    minQuantity DECIMAL(10, 2),
    maxQuantity DECIMAL(10, 2)
);

-- Create the Portfolio table
CREATE TABLE Cerebros_Portfolio (
    portfolio_id INT PRIMARY KEY,
    clientId VARCHAR(50),
    instrumentId VARCHAR(50),
    holdings DECIMAL(10, 2)
);

-- Create the Trades table
CREATE TABLE Cerebros_Trades (
    tradeId INT PRIMARY KEY,
    clientId VARCHAR(50),
    instrument_id VARCHAR(50),
    orderId VARCHAR(50),
    executionPrice DECIMAL(10, 2),
    cashValue DECIMAL(10, 2),
    executedTimestamp TIMESTAMP
);

-- Create the Orders table
CREATE TABLE Cerebros_Orders (
    orderId INT PRIMARY KEY,
    clientId VARCHAR(50),
    instrumentId VARCHAR(50),
    direction ENUM('B', 'S'),
    quantity DECIMAL(10, 2),
    targetPrice DECIMAL(10, 2),
    placedTimestamp TIMESTAMP
);

-- Create the Prices table
CREATE TABLE Cerebros_Prices (
    pricesId INT PRIMARY KEY,
    instrumentId VARCHAR(50),
    bidPrice DECIMAL(10, 2),
    askPrice DECIMAL(10, 2),
    timestamp TIMESTAMP
);

INSERT INTO Cerebros_Instruments (instrumentId, description, externalIdType, externalId, categoryId, minQuantity, maxQuantity)
VALUES
    ('Q123', 'Alphabet Inc. Class C Capital Stock', 'CUSIP', '02079K107', 'STOCK', 1, 1000),
    ('Q456', 'Tesla, Inc. Common Stock', 'CUSIP', '88160R101', 'STOCK', 1, 1000),
    ('N123456', 'JPMorgan Chase & Co. Capital Stock', 'CUSIP', '46625H100', 'STOCK', 1, 1000),
    ('N123789', 'Berkshire Hathaway Inc. Class A', 'ISIN', 'US0846707026', 'STOCK', 1, 10),
    ('C100', 'JPMorgan Chase Bank, National Association 01/19', 'CUSIP', '48123Y5A0', 'CD', 100, 1000),
    ('T67890', 'USA, Note 3.125 15nov2028 10Y', 'CUSIP', '9128285M8', 'GOVT', 100, 10000),
    ('T67894', 'USA, Note 2.5 31jan2024 5Y', 'CUSIP', '9128285Z9', 'GOVT', 100, 10000),
    ('T67895', 'USA, Note 2.625 31jan2026 7Y', 'CUSIP', '9128286A3', 'GOVT', 100, 10000),
    ('T67897', 'USA, Note 2.5 31jan2021 2Y', 'CUSIP', '9128285X4', 'GOVT', 100, 10000),
    ('T67899', 'USA, Notes 2.5% 15jan2022 3Y', 'CUSIP', '9128285V8', 'GOVT', 100, 10000),
    ('T67880', 'USA, Note 1.5 31dec2023 5Y', 'CUSIP', '9128285U0', 'GOVT', 100, 10000),
    ('T67883', 'USA, Bond 3.375 15nov2048 30Y', 'CUSIP', '912810SE9', 'GOVT', 100, 10000),
    ('T67878', 'USA, Bond 3 15aug2048 30Y', 'CUSIP', '912810SD1', 'GOVT', 100, 10000);

INSERT INTO Cerebros_Prices (pricesId, instrumentId, bidPrice, askPrice, timestamp)
VALUES
    (1, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '46625H100'), 104.25, 104.75, '21-AUG-19 10.00.01.042000000 AM GMT'),
    (2, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = 'US0846707026'), 312000, 312500, '21-AUG-19 05.00.00.040000000 AM -05:00'),
    (3, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '48123Y5A0'), 95.42, 95.92, '21-AUG-19 10.00.02.042000000 AM GMT'),
    (4, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285M8'), 1.03390625, 1.03375, '21-AUG-19 10.00.02.000000000 AM GMT'),
    (5, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285Z9'), 0.99828125, 0.998125, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (6, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128286A3'), 1.00015625, 1.0, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (7, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285X4'), 0.999375, 0.999375, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (8, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285V8'), 0.999375, 0.999375, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (9, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '9128285U0'), 1.00375, 1.00375, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (10, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '912810SE9'), 1.0596875, 1.0596875, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (11, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '912810SD1'), 0.98546875, 0.9853125, '21-AUG-19 10.00.02.002000000 AM GMT'),
    (12, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '02079K107'), 1161.42, 1162.42, '21-AUG-19 06.52.20.350000000 PM AMERICA/NEW_YORK'),
    (13, (SELECT instrumentId FROM Cerebros_Instruments WHERE externalId = '88160R101'), 322.89, 323.39, '21-AUG-19 06.52.20.356000000 PM AMERICA/NEW_YORK');



INSERT INTO Cerebros_Portfolio (portfolio_id, clientId, instrumentId, holdings)
VALUES
    (1, 'YOUR_CLIENT_ID', 'Q123', 100),   -- Example data, replace with actual values
    (2, 'YOUR_CLIENT_ID', 'Q456', 50),    -- Example data, replace with actual values
    (3, 'YOUR_CLIENT_ID', 'N123456', 200), -- Example data, replace with actual values
    (4, 'YOUR_CLIENT_ID', 'N123789', 10),  -- Example data, replace with actual values
    (5, 'YOUR_CLIENT_ID', 'C100', 500),   -- Example data, replace with actual values
    (6, 'YOUR_CLIENT_ID', 'T67890', 1000),-- Example data, replace with actual values
    (7, 'YOUR_CLIENT_ID', 'T67894', 300),  -- Example data, replace with actual values
    (8, 'YOUR_CLIENT_ID', 'T67895', 700),  -- Example data, replace with actual values
    (9, 'YOUR_CLIENT_ID', 'T67897', 200),  -- Example data, replace with actual values
    (10, 'YOUR_CLIENT_ID', 'T67899', 400), -- Example data, replace with actual values
    (11, 'YOUR_CLIENT_ID', 'T67880', 800), -- Example data, replace with actual values
    (12, 'YOUR_CLIENT_ID', 'T67883', 150), -- Example data, replace with actual values
    (13, 'YOUR_CLIENT_ID', 'T67878', 250); -- Example data, replace with actual values



INSERT INTO Cerebros_Trades (tradeId, clientId, instrument_id, orderId, executionPrice, cashValue, executedTimestamp)
VALUES
    (1, 'YOUR_CLIENT_ID', 'Q123', 'BUY_ORDER_Q123_1', 104.75, -10475.00, '2023-09-19 09:00:00'),
    (2, 'YOUR_CLIENT_ID', 'Q456', 'BUY_ORDER_Q456_1', 323.39, -16169.50, '2023-09-19 09:30:00'),
    (3, 'YOUR_CLIENT_ID', 'N123456', 'BUY_ORDER_N123456_1', 104.25, -20850.00, '2023-09-19 10:00:00'),
    (4, 'YOUR_CLIENT_ID', 'N123789', 'BUY_ORDER_N123789_1', 95.92, -9592.00, '2023-09-19 10:30:00'),
    (5, 'YOUR_CLIENT_ID', 'C100', 'BUY_ORDER_C100_1', 1.03375, -1033.75, '2023-09-19 11:00:00'),
    (6, 'YOUR_CLIENT_ID', 'T67890', 'BUY_ORDER_T67890_1', 0.998125, -9981.25, '2023-09-19 11:30:00'),
    (7, 'YOUR_CLIENT_ID', 'T67894', 'BUY_ORDER_T67894_1', 1.000, -10000.00, '2023-09-19 12:00:00'),
    (8, 'YOUR_CLIENT_ID', 'T67895', 'BUY_ORDER_T67895_1', 0.999375, -9993.75, '2023-09-19 12:30:00'),
    (9, 'YOUR_CLIENT_ID', 'T67897', 'BUY_ORDER_T67897_1', 0.999375, -9993.75, '2023-09-19 13:00:00'),
    (10, 'YOUR_CLIENT_ID', 'T67899', 'BUY_ORDER_T67899_1', 1.00375, -10037.50, '2023-09-19 13:30:00'),
    (11, 'YOUR_CLIENT_ID', 'T67880', 'BUY_ORDER_T67880_1', 1.0596875, -10596.88, '2023-09-19 14:00:00'),
    (12, 'YOUR_CLIENT_ID', 'T67883', 'BUY_ORDER_T67883_1', 0.9853125, -9853.13, '2023-09-19 14:30:00'),
    (13, 'YOUR_CLIENT_ID', 'T67878', 'BUY_ORDER_T67878_1', 1162.42, -290605.00, '2023-09-19 15:00:00');


INSERT INTO Cerebros_Orders (orderId, clientId, instrumentId, direction, quantity, targetPrice, placedTimestamp)
VALUES
    ('BUY_ORDER_Q123_1', 'YOUR_CLIENT_ID', 'Q123', 'B', 100, 104.75, '2023-09-19 09:00:00'),
    ('BUY_ORDER_Q456_1', 'YOUR_CLIENT_ID', 'Q456', 'B', 50, 323.39, '2023-09-19 09:30:00'),
    ('BUY_ORDER_N123456_1', 'YOUR_CLIENT_ID', 'N123456', 'B', 200, 104.25, '2023-09-19 10:00:00'),
    ('BUY_ORDER_N123789_1', 'YOUR_CLIENT_ID', 'N123789', 'B', 10, 95.92, '2023-09-19 10:30:00'),
    ('BUY_ORDER_C100_1', 'YOUR_CLIENT_ID', 'C100', 'B', 500, 1.03375, '2023-09-19 11:00:00'),
    ('BUY_ORDER_T67890_1', 'YOUR_CLIENT_ID', 'T67890', 'B', 1000, 0.998125, '2023-09-19 11:30:00'),
    ('BUY_ORDER_T67894_1', 'YOUR_CLIENT_ID', 'T67894', 'B', 300, 1.000, '2023-09-19 12:00:00'),
    ('BUY_ORDER_T67895_1', 'YOUR_CLIENT_ID', 'T67895', 'B', 700, 0.999375, '2023-09-19 12:30:00'),
    ('BUY_ORDER_T67897_1', 'YOUR_CLIENT_ID', 'T67897', 'B', 200, 0.999375, '2023-09-19 13:00:00'),
    ('BUY_ORDER_T67899_1', 'YOUR_CLIENT_ID', 'T67899', 'B', 400, 1.00375, '2023-09-19 13:30:00'),
    ('BUY_ORDER_T67880_1', 'YOUR_CLIENT_ID', 'T67880', 'B', 800, 1.0596875, '2023-09-19 14:00:00'),
    ('BUY_ORDER_T67883_1', 'YOUR_CLIENT_ID', 'T67883', 'B', 150, 0.9853125, '2023-09-19 14:30:00'),
    ('BUY_ORDER_T67878_1', 'YOUR_CLIENT_ID', 'T67878', 'B', 250, 1162.42, '2023-09-19 15:00:00');

