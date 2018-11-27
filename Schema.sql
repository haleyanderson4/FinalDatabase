CREATE TABLE Job(jobId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, jobTitle VARCHAR(50), industry VARCHAR(25), description VARCHAR(100), companyId INTEGER FOREIGN KEY companyId(companyId) REFERENCES Company(companyId), managerId INTEGER FOREIGN KEY managerId(managerId) REFERENCES Manager(managerId), type BOOL);

CREATE TABLE Company(companyId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, companyName VARCHAR(50), numEmployees INTEGER, yearlyRevenue FLOAT(20,2), stockPrice FLOAT(20,2));

CREATE TABLE Manager(managerId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, name VARCHAR(50), technicalExperience BOOL, yearsAtCompany INTEGER);

CREATE TABLE RelatedJobs(jobId INTEGER PRIMARY KEY FOREIGN KEY jobId(jobId) REFERENCES Job(jobId) ON DELETE CASCADE, related1 INTEGER, related2 INTEGER, related3 INTEGER, related4 INTEGER, related5 INTEGER);

CREATE TABLE Competition(jobId INTEGER PRIMARY KEY AUTO_INCREMENT FOREIGN KEY jobId(jobId) REFERENCES Job(jobId) ON DELETE CASCADE, numOpenSpots INTEGER, numApplicants INTEGER);

CREATE TABLE Location(companyId INTEGER PRIMARY KEY AUTO_INCREMENT, locationArea VARCHAR (25), street VARCHAR(100), city VARCHAR(25), state VARCHAR(2));

CREATE TABLE FullTime(jobId INTEGER PRIMARY KEY FOREIGN KEY jobId(jobId) REFERENCES Job(jobId) ON DELETE CASCADE, numStockOptions INTEGER, signingBonus FLOAT(20,2), salary FLOAT(20,2));

CREATE TABLE Internship(jobId INTEGER PRIMARY KEY FOREIGN KEY jobId(jobId) REFERENCES Job(jobId) ON DELETE CASCADE, payPeriod VARCHAR(25), salary FLOAT(20,2), season VARCHAR(25));
