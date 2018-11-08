CREATE TABLE Job(jobId INTEGER PRIMARY KEY, jobTitle VARCHAR(25), industry VARCHAR(25), description VARCHAR(100), companyId INTEGER, managerId INTEGER, type CHAR(1));

CREATE TABLE Company(companyId INTEGER PRIMARY KEY, companyName VARCHAR(100), numEmployees INTEGER, yearlyRevenue FLOAT, stockPrice FLOAT);

CREATE TABLE Manager(managerId INTEGER PRIMARY KEY, name VARCHAR(100), technicalExperience BOOL, yearsAtCompany INTEGER); 

CREATE TABLE RelatedJobs(jobId INTEGER PRIMARY KEY, related1 INTEGER, related2 INTEGER, related3 INTEGER, related4 INTEGER, related5 INTEGER);

CREATE TABLE Competition(jobId INTEGER PRIMARY KEY, numOpenSpots INTEGER, numApplicants INTEGER);

CREATE TABLE Location(companyId INTEGER PRIMARY KEY, locationArea VARCHAR (25), street VARCHAR(100), city VARCHAR(25), state VARCHAR(25));

CREATE TABLE Internship(jobId INTEGER PRIMARY KEY, payPeriod VARCHAR(10), salary FLOAT, season VARCHAR(10));

CREATE TABLE FullTime(jobId INTEGER PRIMARY KEY, numStockOptions INTEGER, signingBonus FLOAT); 
