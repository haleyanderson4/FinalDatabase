CREATE TABLE Company(companyId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, companyName VARCHAR(50) not null, numEmployees INTEGER not null, yearlyRevenue FLOAT(20,2) not null, stockPrice FLOAT(20,2) not null);

CREATE TABLE Job(jobId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, jobTitle VARCHAR(50) not null, industry VARCHAR(25) not null, description VARCHAR(100) not null, companyId INTEGER not null, FOREIGN KEY companyId(companyId) REFERENCES Company(companyId), isInternship BOOL not null);

CREATE TABLE Manager(managerId INTEGER PRIMARY KEY NOT NULL AUTO_INCREMENT, companyId INTEGER not null, FOREIGN KEY (companyId) REFERENCES Company(companyId), name VARCHAR(50) not null, technicalExperience BOOL not null, yearsAtCompany INTEGER not null);

CREATE TABLE RelatedJobs(jobId INTEGER PRIMARY KEY, FOREIGN KEY jobId(jobId) REFERENCES Job(jobId) ON DELETE CASCADE, related1 INTEGER, FOREIGN KEY (related1) REFERENCES Job(jobId) ON DELETE SET NULL, related2 INTEGER, FOREIGN KEY (related2) REFERENCES Job(jobId) ON DELETE SET NULL, related3 INTEGER, FOREIGN KEY (related3) REFERENCES Job(jobId) ON DELETE SET NULL, related4 INTEGER, FOREIGN KEY (related4) REFERENCES Job(jobId) ON DELETE SET NULL, 
related5 INTEGER, FOREIGN KEY (related5) REFERENCES Job(jobId) ON DELETE SET NULL);

CREATE TABLE Competition(jobId INTEGER PRIMARY KEY AUTO_INCREMENT, FOREIGN KEY (jobId) REFERENCES Job(jobId) ON DELETE CASCADE, numOpenSpots INTEGER, numApplicants INTEGER);

CREATE TABLE Location(companyId INTEGER PRIMARY KEY AUTO_INCREMENT, FOREIGN KEY (companyId) REFERENCES Company(companyId) ON DELETE CASCADE, locationArea VARCHAR (25) not null, street VARCHAR(100) not null, city VARCHAR(25) not null, state VARCHAR(2) not null);

CREATE TABLE FullTime(jobId INTEGER PRIMARY KEY, FOREIGN KEY (jobId) REFERENCES Job(jobId) ON DELETE CASCADE, numStockOptions INTEGER not null, signingBonus FLOAT(20,2) not null, salary FLOAT(20,2) not null);

CREATE TABLE Internship(jobId INTEGER PRIMARY KEY, FOREIGN KEY (jobId) REFERENCES Job(jobId) ON DELETE CASCADE, payPeriod VARCHAR(25) not null, rate FLOAT(20,2) not null, season VARCHAR(25) not null);
