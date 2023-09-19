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
    instrumentId INT PRIMARY KEY,
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
    instrumentId INT,
    holdings DECIMAL(10, 2)
);

-- Create the Trades table
CREATE TABLE Cerebros_Trades (
    tradeId INT PRIMARY KEY,
    clientId VARCHAR(50),
    instrument_id INT,
    orderId INT,
    executionPrice DECIMAL(10, 2),
    cashValue DECIMAL(10, 2),
    executedTimestamp TIMESTAMP
);

-- Create the Orders table
CREATE TABLE Cerebros_Orders (
    orderId INT PRIMARY KEY,
    clientId VARCHAR(50),
    instrumentId INT,
    direction ENUM('B', 'S'),
    quantity DECIMAL(10, 2),
    targetPrice DECIMAL(10, 2),
    placedTimestamp TIMESTAMP
);

-- Create the Prices table
CREATE TABLE Cerebros_Prices (
    pricesId INT PRIMARY KEY,
    instrumentId INT,
    bidPrice DECIMAL(10, 2),
    askPrice DECIMAL(10, 2),
    timestamp TIMESTAMP
);
