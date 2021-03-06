#pip install Faker
#conda install -c conda-forge faker

from faker import Faker
import sqlite3
import random

def createJob(companyId, managerId):
    #create job
    jobTitle = faker.job();
    industry = faker.choice(['IT', 'SWE', 'SDE', 'WebDev', 'AppDev'])
    description = faker.text()
    type = random.choice([True, False])

    curr.execute("INSERT INTO Job VALUES(?, ?, ?, ?, ?, ?);", (jobTitle, industry, description, companyId, managerId, type))
    conn.commit()
    conn.close()

def createCompetition(jobId):
    #create competition
    numOpenSpots = random.randit(1, 3000)
    numApplicants = random.randit(0, 5000)

    curr.execute("INSERT INTO Competition VALUES(?,?,?);", (jobId, numOpenSpots, numApplicants))
    conn.commit()
    conn.close()

def createCompany():
    #create company
    companyName = faker.company()
    numEmployees = random.randit(1, 9999999)
    yearlyRevenue = round(random.uniform(0,2000000000), 2)
    stockPrice = round(random.uniform(0,2000), 2)

    curr.execute("INSERT INTO Company VALUES(?, ?, ?, ?);", (companyName, numEmployees, yearlyRevenue, stockPrice))

    #create location
    locationArea = faker.random_choices("Northwest", "Bay Area", "Mid West", "East Coast", "West Coast")
    street = faker.street()
    city = faker.city()
    state = faker.state_abbr()

    curr.execute("INSERT INTO Location VALUES(?, ?, ?, ?);", (locationArea, street, city, state))
    conn.commit()
    conn.close()

def createManager():
    #create manager
    name = faker.name()
    techincalExperience = random.choice([True, False])
    yearsAtCompany = random.randit(0, 40)

    curr.execute("INSERT INTO Manager VALUES(?, ?, ?);", (name, techincalExperience, yearsAtCompany))
    conn.commit()
    conn.close()

def createType(jobId, type):
    if type:
        #create internship
        payPeriod = faker.random_choices("Monthly", "Weekly", "Biweekly", "Lump Sum")
        salary = round(random.uniform(0,8000), 2)
        season = faker.random_choices("Summer", "Winter", "Fall", "Spring", "School Year")

        curr.execute("INSERT INTO Internship VALUES(?, ?, ?, ?);", (jobdId, payPeriod, salary, season))
    else:
        #create full time
        numStockOptions = random.randit(1, 5000)
        signingBonus = round(random.uniform(0,80000), 2)
        salary = round(random.uniform(0,4000000), 2)

        curr.execute("INSERT INTO FullTime VALUES(?, ?, ?, ?);", (jobId, numStockOptions, signingBonus, salary))
    conn.commit()
    conn.close()

def createRelatedJobs(jobId):
    #create related jobs
    curr.execute("SELECT COUNT(*) FROM Job;")
    ans = curr.fetchall()

    for i in ans:
        max = i - 1

    numRJ = random.randit(1, 5)
    if numRJ > max:
        numRJ = max

    relatedJob1 = 0
    relatedJob2 = 0
    relatedJob3 = 0
    relatedJob4 = 0
    relatedJob5 = 0

    for i in range(0,numRJ):
        curr.execute("SELECT jobId FROM Job ORDER BY RAND() LIMIT 1;")
        ans = curr.fetchall()

        for j in ans:
            if j == jobId:
                j = j+1
            id = j

        if i == 1:
            relatedJob1 = id
        if i == 2:
            relatedJob2 = id
        if i == 3:
            relatedJob3 = id
        if i == 4:
            relatedJob4 = id
        if i == 5:
            relatedJob5 = id

    curr.execute("INSERT INTO RelatedJobs VALUES(?, ?, ?, ?, ?, ?);", (jobId, relatedJob1, relatedJob2, relatedJob3, relatedJob4, relatedJob5))
    conn.commit()
    conn.close()



def main():
    managerId = 0
    companyId = 0
    jobId = 0

    company = random.choice([True, False])
    if company:
        createCompany()
        curr.execute("SELECT MAX(companyId) FROM Company;")
    else:
        curr.execute("SELECT companyId FROM Company ORDER BY RAND() LIMIT 1;")
    ans = curr.fetchall()

    for i in ans:
        companyId = i

    manager = random.choice([True, False])
    if manager:
        createManager()
    curr.execute("SELECT MAX(managerId) FROM Manager;")
    ans = curr.fetchall()

    for i in ans:
        managerId = i

    createJob(companyId, managerId)
    curr.execute("SELECT MAX(jobId) FROM Job;")
    ans = curr.fetchall()

    for i in ans:
        jobId = i

    curr.execute("SELECT type FROM Job WHERE jobId=?;", (jobId, ))
    ans = curr.fetchall()

    for i in ans:
        type = i

    createCompetition(jobId)
    createType(jobId, type)

    related = random.choice([True, False])
    if related:
        createRelatedJobs(jobId)


#method!!
conn = sqlite3.connect("jobDatabase")
curr = conn.cursor()

fake = Faker()

for i in range(0,10000):
    main()
