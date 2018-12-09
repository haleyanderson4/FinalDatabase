/**
 * @TODO
 * Front-end stuff
 * Testing!!!
 *      1 - 9 are tested and work
 * Report Generation
 * Rollback / Undo
 */

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

public class Main
{
    private static GUI gui;

    public static void main(String[] args)
    {
        Connection con; //database connection
        Scanner scan = new Scanner(System.in);
        int editOption = 0;

        Logger logger = Logger.getLogger("com.mysql.cj.jdbc.Driver");
        logger.setLevel(Level.FINER);
        FileHandler fh;
        try
        {
            fh = new FileHandler("JDBCLog.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }
        catch (Exception e)
        {
            System.out.println("Error logging: " + e);
        }



        try {
            con = Config.getMySqlConnection(); //connect to database
            gui = new GUI(con, scan, logger); //GUI TEST
            boolean loop = true;
            boolean isNotFirst = false;
            while(loop)
            {
                try
                {
                    if(isNotFirst)
                    {
                        System.out.println("Press enter to continue");
                        scan.nextLine();
                    }
                    isNotFirst = true;

                    System.out.println("Options on what to do: \n1. Display all Jobs \n2. Add a new Job Posting \n3. Update a Job Posting \n4. Remove a Job \n5. Search by Location, Company, or Type "
                            + "\n6. Find All Info for a Job \n7. Get Select Info for a Job \n8. Job Statistics \n9. Add a new Manager \n11. Generate Database Report \n11. Quit");

                    System.out.println("What would you like to do: ");
                    editOption = scan.nextInt();
                    scan.nextLine();
                    System.out.println(" ");
                    if (editOption < 1 || editOption > 11)
                    {
                        System.out.println("Please enter a number between 1 and 11\n");
                        continue;
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Please enter a number between 1 and 11\n");
                    scan.nextLine();
                    editOption = 0;
                    continue;
                }

                if(editOption == 1) //prelim completed
                {
                    boolean success = queryMethod(con, scan, false);
                    if(!success)
                    {
                        System.out.println("The Query failed. Please try again.");
                    }
                }

                if(editOption == 2) //prelim completed
                {
                    boolean success = createNewPosting(con, scan, logger);
                    if(success)
                    {
                        System.out.println("The new Posting was successfully added to the Job Board.");
                    }
                    else
                    {
                        System.out.println("Process failed. The Posting was not added. Please try again.");
                    }
                }

                if(editOption == 3) //need to add input checks
                {
                    boolean success = updateField(con, scan, logger);
                    if(success)
                    {
                        System.out.println("The Update was successfully added to the Job Board.");
                    }
                    else
                    {
                        System.out.println("Process failed. The Update was not added. Please try again.");
                    }
                }

                if(editOption == 4) //prelim completed
                {
                    boolean success = deleteCall(con, scan, false, 0, logger);
                    if(success)
                    {
                        System.out.println("The entry was successfully deleted.");
                    }
                    else
                    {
                        System.out.println("Process failed. The entry was not deleted. Please try again.");
                    }
                }

                if(editOption == 5) //prelim done
                {
                    boolean success = searchBy(con, scan);
                    if(!success)
                    {
                        System.out.println("The search failed. Please try again.");
                    }
                }

                if(editOption == 6) //prelim completed
                {
                    boolean success = jobInfo(con, scan, 0, false);
                    if(!success)
                    {
                        System.out.println("The look up failed. Please try again.");
                    }
                }

                if(editOption == 7) //prelim completed
                {
                    boolean success = lookup(con, scan);
                    if(!success)
                    {
                        System.out.println("The look up failed. Please try again.");
                    }
                }

                if(editOption == 8)
                {
                    boolean success = statistics(con);
                    if(!success)
                    {
                        System.out.println("The look up failed. Please try again.");
                    }
                }

                if (editOption == 9)
                {
                    boolean success = createNewManager(con, scan, logger);
                    if (success)
                    {
                        System.out.println("Manager successfully added.");
                    }
                    else
                    {
                        System.out.println("Process failed. The manager was not added. Try agan.");
                    }
                }

                if(editOption == 10)
                {
                    boolean success = generateReport(con);
                    if(success)
                    {
                        System.out.println("Report Generation Successful.");
                    }
                    if(!success)
                    {
                        System.out.println("Report Generation Failed. Please try again.");
                    }
                }
                if(editOption == 11) //done lol
                {
                    loop = false;
                }

                System.out.println("");
            }

            System.out.println("Thank you for using this database.");
            con.close();
        }
        catch(Exception e) { System.out.println(e); }
    }


    //OPTION 1 METHODS
    /**
     * Returns all entries in the JOB table.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if the call was successful, false otherwise
     */
    public static boolean queryMethod(Connection con, Scanner scan, boolean fromGUI)
    {
        try
        {
            System.out.println("The Job Database:");
            PreparedStatement pst1 = con.prepareStatement("select * from Job;");
            ResultSet rs = pst1.executeQuery();
            String type = "Full Time";

            if (fromGUI)
            {
              String[] columnNames = {"Job ID", "Title", "Industry", "Description", "Company ID", "Type"};
              rs.last();
              gui.add(gui.showTable(rs, rs.getRow()+1, 6, columnNames));
            }
            else
            {
              while (rs.next())
              {
                  if(rs.getBoolean(6))
                  {
                      type = "Internship";
                  }
                  System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                          + " Type: " + type);
              }
              System.out.println("");
            }
            return true;
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return false;
    }


    //OPTION 2 METHODS
    /**
     * Creates a new posting for a newly created job!
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createNewPosting(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            boolean success = true;
            int jobId = 0;
            int companyId = 0;

            PreparedStatement pstC = con.prepareStatement("INSERT INTO Company(companyName, numEmployees, yearlyRevenue, stockPrice) VALUES(?,?,?,?);");
            PreparedStatement pstL = con.prepareStatement("INSERT INTO Location(companyId, locationArea, street, city, state) VALUES(?,?,?,?,?);");
            System.out.println("Do you need to create a new Company? Enter 'y' for yes.");
            String company = scan.nextLine();
            boolean createCompany = false;
            if(company.toLowerCase().equals("y"))
            {
                createCompany = true;
                success = createCompany(pstC, pstL, scan);
                if(!success)
                {
                    System.out.println("The Company creation failed. Please try again.");
                    return false;
                }
            }
            else
            {
                System.out.println("Enter the Job's Company Id");
                companyId = scan.nextInt();
                scan.nextLine();

                if(!checkCompanyID(con, companyId))
                {
                    return false;
                }
            }

            PreparedStatement pstJ = con.prepareStatement("INSERT INTO Job(jobTitle, industry, description, companyId, isInternship) VALUES(?,?,?,?,?);");
            PreparedStatement pstComp = con.prepareStatement("INSERT INTO Competition(jobId, numOpenSpots, numApplicants) VALUES(?,?,?);");
            boolean type = true;
            success = createJob(pstJ, pstComp, scan, new Job(), false);
            String typeS = getType(scan, pstJ);
            if(!success || typeS.equals("nope"))
            {
                System.out.println("The Job creation failed. Please try again.");
                return false;
            }
            if(typeS.equals("false"))
            {
                type = false;
            }

            PreparedStatement pstF = con.prepareStatement("INSERT INTO FullTime(jobId, numStockOptions, signingBonus, salary) VALUES(?,?,?,?);");
            PreparedStatement pstI = con.prepareStatement("INSERT INTO Internship(jobId, payPeriod, rate, season) VALUES(?,?,?,?);");
            success = createType(pstF, pstI, type, scan);
            if(!success)
            {
                System.out.println("The Full Time or Internship creation failed. Please try again.");
                return false;
            }

            PreparedStatement pstR = con.prepareStatement("INSERT INTO RelatedJobs(jobId, related1, related2, related3, related4, related5) VALUES(?,?,?,?,?,?);");
            System.out.println("Does the job you are creating have any related jobs? Enter 'Y' for yes.");
            String relatedMaybe = scan.nextLine();
            boolean createRelated = false;
            if(relatedMaybe.toLowerCase().equals("y"))
            {
                createRelated = true;
                success = createRelated(pstR, scan);
                if (!success)
                {
                    System.out.println("The Related Jobs creation failed. Please try again.");
                    return false;
                }
            }

            PreparedStatement pstStart = con.prepareStatement("START TRANSACTION;");
            pstStart.execute();

            if(createCompany)
            {
                pstC.executeUpdate();
                System.out.println("The Company has been created.");

                PreparedStatement pstId = con.prepareStatement("SELECT MAX(companyId) FROM Company;");
                ResultSet rsId = pstId.executeQuery();
                while(rsId.next())
                {
                    companyId = rsId.getInt(1);
                }

                pstL.setInt(1, companyId);
                pstL.executeUpdate();
                System.out.println("The Location has been created.");
            }

            pstJ.setInt(4, companyId);
            pstJ.executeUpdate();
            System.out.println("The Job has been created.");

            PreparedStatement pstId = con.prepareStatement("SELECT MAX(jobId) FROM Job;");
            ResultSet rsId = pstId.executeQuery();
            while(rsId.next())
            {
                jobId = rsId.getInt(1);
            }

            pstComp.setInt(1, jobId);
            pstComp.executeUpdate();
            System.out.println("The Competition has been created.");

            if(!type)
            {
                pstF.setInt(1, jobId);

                pstF.executeUpdate();
                System.out.println("The Full Time Position has been created.");
            }
            if(type)
            {
                pstI.setInt(1, jobId);

                pstI.executeUpdate();
                System.out.println("The Internship has been created.");
            }

            if(createRelated)
            {
                pstR.setInt(1, jobId);
                pstR.executeUpdate();
                System.out.println("The Related Job Posting has been created.");
            }

            PreparedStatement pstEnd = con.prepareStatement("COMMIT;");
            if(createCompany)
            {
                logger.info("" + pstC);
                logger.info("" + pstL);
            }
            logger.info("" + pstJ);
            logger.info("" + pstComp);
            if(type)
            {
                logger.info("" + pstI);
            }
            else
            {
                logger.info("" + pstF);
            }

            if(createRelated)
            {
                logger.info("" + pstR);
            }

            pstEnd.execute();
            return true;

            //PreparedStatement pstRoll = con.prepareStatement("ROLLBACK");
            //            pstRoll.execute();
        }
        catch (Exception e)
        {
            System.out.println("Please enter this information again, and be sure that all information is correct.");
        }
        return false;
    }

    /**
     * Gathers information for the Company table.
     * @param pstC is the Prepared Statement for the Company table.
     * @param pstL is the Prepared Statement for the Location table.
     * @return true if the creation was successful, false otherwise.
     */
    public static boolean createCompany(PreparedStatement pstC, PreparedStatement pstL, Scanner scan)
    {
        try
        {
            String companyName = "";
            int numEmployees = 0;
            float yearlyRevenue = 0;
            float stockPrice = 0;

            String locationArea = "";
            String street = "";
            String city = "";
            String state = "";

            //company creation
            System.out.println("Enter the Company's Name (length 100)");
            companyName = scan.nextLine();
            System.out.println("Enter the Company's Number of Employees");
            numEmployees = scan.nextInt();
            scan.nextLine();
            System.out.println("Enter the Company's Yearly Revenue");
            yearlyRevenue = scan.nextFloat();
            scan.nextLine();
            System.out.println("Enter the Company's Stock Price");
            stockPrice = scan.nextFloat();
            scan.nextLine();

            if (!inputCheck(companyName, 100))
            {
                System.out.println("Please enter this information again");
                return false;
            }

            //Location creation
            System.out.println("Enter the Company's Location Area (length 25)");
            locationArea = scan.nextLine();
            System.out.println("Enter the Company's Street Address (length 100)");
            street = scan.nextLine();
            System.out.println("Enter the Company's City (length 25)");
            city = scan.nextLine();
            System.out.println("Enter the Company's State (length 2)");
            state = scan.nextLine();

            if(!inputCheck(street,100) || !inputCheck(locationArea, 25) || !inputCheck(city, 25) || !inputCheck(state, 2))
            {
                return false;
            }

            pstC.clearParameters();
            pstC.setString(1, companyName);
            pstC.setInt(2, numEmployees);
            pstC.setFloat(3, yearlyRevenue);
            pstC.setFloat(4, stockPrice);

            pstL.clearParameters();
            pstL.setString(2, locationArea);
            pstL.setString(3, street);
            pstL.setString(4, city);
            pstL.setString(5, state);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
        }
        return false;
    }

    /**
     * Gathers information for the company field.
     * @param pstJ is the Prepared Statement for the Job table.
     * @param pstComp is the Prepared Statement for the Competition table.
     * @param job is a Job object that contains the appropriate data, if gotten from GUI.
     * @param fromGUI is set to true if called from the GUI, false if called from command line.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createJob(PreparedStatement pstJ, PreparedStatement pstComp, Scanner scan, Job job, boolean fromGUI)
    {
        try
        {
            String jobTitle = "";
            String industry = "";
            String description = "";

            if (!fromGUI)
            {
              System.out.println("Enter the Job's Title (length 25)");
              jobTitle = scan.nextLine();
              System.out.println("Enter the Job's Industry (length 25)");
              industry = scan.nextLine();
              System.out.println("Enter the Job's Description (length 100)");
              description = scan.nextLine();
            }

            if (fromGUI)
            {
              jobTitle = job.jobTitle;
              industry = job.industry;
              description = job.description;
            }

            if(!inputCheck(description, 100) || !inputCheck(jobTitle, 25) || !inputCheck(industry, 25))
            {
                return false;
            }

            pstJ.clearParameters();
            pstJ.setString(1, jobTitle);
            pstJ.setString(2, industry);
            pstJ.setString(3, description);

            int numOpenSpots;
            int numApplicants;

            if (!fromGUI)
            {
              System.out.println("Enter the Job's Number of Open Spots");
              numOpenSpots = scan.nextInt();
              scan.nextLine();
              System.out.println("Enter the Job's Number of Applicants");
              numApplicants = scan.nextInt();
              scan.nextLine();
            }

            if (fromGUI)
            {
              numOpenSpots = job.numOpenSpots;
              numApplicants = job.numApplicants;
            }

            pstComp.clearParameters();
            pstComp.setInt(2, numOpenSpots);
            pstComp.setInt(3, numApplicants);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }

    /**
     * Gets user input on the type of the job.
     * @param scan because it needs user input
     * @return true if the job is an internship, false if full time, nope otherwise
     */
    public static String getType(Scanner scan, PreparedStatement pstJ)
    {
        String typeS = "";
        try
        {
            System.out.println("Enter the Job's Type (I/F)");
            typeS = scan.nextLine();
            boolean type = true;

            if(typeS.toLowerCase().equals("f"))
            {
                typeS = "false";
                type = false;
            }
            else if(typeS.toLowerCase().equals("i"))
            {
                typeS = "true";
                type = true;
            }
            else
            {
                System.out.println("Type needs to be 1 character only, I or F. Please try again.");
                return "nope";
            }

            pstJ.setBoolean(5, type);
        }
        catch (Exception e)
        {
            System.out.println("Somehting went wrong. Please try again.");
        }
        return typeS;
    }

    /**
     * Gathers information for the company field.
     * @param pstR is the Prepared Statement for the Related Jobs table.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createRelated(PreparedStatement pstR, Scanner scan)
    {
        try
        {
            int related1 = 0;
            int related2 = 0;
            int related3 = 0;
            int related4 = 0;
            int related5 = 0;

            System.out.println("How many? Enter a number between 1 and 5.");
            int numOfRelated = scan.nextInt();

            if(numOfRelated < 1 || numOfRelated > 5)
            {
                System.out.println("Please enter a please between 1 and 5. Try again.");
                return false;
            }

            for(int i = 1; i <= numOfRelated; i++)
            {
                System.out.println("What is the ID number of the related job");
                int tempRJ = scan.nextInt();
                scan.nextLine();

                if(i == 1)
                {
                    related1 = tempRJ;
                }
                else if(i == 2)
                {
                    related2 = tempRJ;
                }
                else if(i == 3)
                {
                    related3 = tempRJ;
                }
                else if(i == 4)
                {
                    related4 = tempRJ;
                }
                else if(i == 5)
                {
                    related5 = tempRJ;
                }
            }

            pstR.clearParameters();
            pstR.setInt(2, related1);
            pstR.setInt(3, related2);
            pstR.setInt(4, related3);
            pstR.setInt(5, related4);
            pstR.setInt(6, related5);

            return true;
        }
        catch(Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }

    /**
     * Gathers information for the company field.
     * @param pstF is the Prepared Statement for the Full Time table.
     * @param pstI is the Prepared Statement for the Internship table.
     * @param type is the boolean set in the Job field on whether it is full time or internship.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createType(PreparedStatement pstF, PreparedStatement pstI, boolean type, Scanner scan)
    {
        try
        {
            int stockOptions = 0;
            float signingBonus = 0;
            String payPeriod = "";
            float salary = 0;
            float rate = 0;
            String season = "";

            if(!type)
            {
                System.out.println("Enter the Job's Number of Stock Options");
                stockOptions = scan.nextInt();
                scan.nextLine();
                System.out.println("Enter the Job's Signing Bonus");
                signingBonus = scan.nextFloat();
                scan.nextLine();
                System.out.println("Enter the Job's Salary");
                salary = scan.nextFloat();
                scan.nextLine();

                pstF.setInt(2, stockOptions);
                pstF.setFloat(3, signingBonus);
                pstF.setFloat(4, salary);
            }
            else if(type)
            {
                System.out.println("Enter the Job's Pay Period (length 10)");
                payPeriod = scan.nextLine();
                System.out.println("Enter the Job's Rate");
                rate = scan.nextFloat();
                scan.nextLine();
                System.out.println("Enter the Job's Season (length 10)");
                season = scan.nextLine();

                if(!inputCheck(payPeriod, 10) || !inputCheck(season, 10))
                {
                    return false;
                }

                pstI.setString(2, payPeriod);
                pstI.setFloat(3, rate);
                pstI.setString(4, season);
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
        }
        return false;
    }

    /**
     * Gathers information for the Manager table.
     * @param pstM is the Prepared Statement for the Manager table.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createManager(PreparedStatement pstM, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("Enter the Manager's name (length 100)");
            String name = scan.nextLine();
            System.out.println("Enter 'Y' if the Manager has technical experience");
            String temp = scan.nextLine();
            boolean technicalExperience = false;
            if (temp.toLowerCase().equals("y"))
            {
                technicalExperience = true;
            }
            System.out.println("Enter the Manager's Years at the Company");
            int yearsAtCompany = scan.nextInt();
            scan.nextLine();

            pstM.setString(2, name);
            pstM.setBoolean(3, technicalExperience);
            pstM.setInt(4, yearsAtCompany);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }


    //OPTION 3 METHODS
    /**
     * Parent method to update database tables, where the user selected UPDATE INFORMATION.
     * Method calls updateTable methods to execute the requested updates.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateField(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What would you like to update?");
            System.out.println("1. Company Information \n2. Full Time Information \n3. Internship Information \n4. Job Information \n5. Manager Information \n6. Related Job Information");
            int selectOption = scan.nextInt();
            scan.nextLine();

            if(selectOption == 1)
            {
                boolean success = updateCompany(con, scan, logger);
                return success;
            }
            else if(selectOption == 2)
            {
                boolean success = updateFullTime(con, scan, logger);
                return success;
            }
            else if(selectOption == 3)
            {
                boolean success = updateInternship(con, scan, logger);
                return success;
            }
            else if(selectOption == 4)
            {
                boolean success = updateJob(con, scan, logger);
                return success;
            }
            else if(selectOption == 5)
            {
                boolean success = updateManager(con, scan, logger);
                return success;
            }
            else if(selectOption == 6)
            {
                boolean success = updateRelatedJobs(con, scan, logger);
                return success;
            }
            else
            {
                System.out.println("Please enter a number between 1 and 6.");
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }

    /**
     * Updates a table, where the user selected COMPANY.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateCompany(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Company you would like to update?");
            int companyId = scan.nextInt();
            scan.nextLine();

            if(!checkCompanyID(con, companyId))
            {
                return false;
            }

            System.out.println("The current information for this Company is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Company WHERE companyId=?;");
            pstM.clearParameters();
            pstM.setInt(1, companyId);
            ResultSet rs = pstM.executeQuery();
            while (rs.next()) {
                System.out.println("Company ID: " + rs.getInt(1) + " Company Name: " + rs.getString(2) + " Number of Employees: " + rs.getInt(3) + " Yearly Revenue: " + rs.getFloat(4) + " Stock Price: " + rs.getFloat(5));
            }

            System.out.println("Which field would you like to update? \n1. Company Name \n2. Number of Employees \n3. Yearly Revenue \n4. Stock Price \n5. None");
            int updateId = scan.nextInt();
            scan.nextLine();

            if (updateId < 1 || updateId > 5)
            {
                System.out.println("Please enter a number between 1 and 5.");
                return false;
            }
            if(updateId != 5)
            {
                String field = "";
                String answer = "";
                int intAnswer = -1;
                float floatAnswer = -1;

                System.out.println("What would you like this field to up updated to?");
                if (updateId == 1)
                {
                    field = "companyName";
                    answer = scan.nextLine();
                    if (!inputCheck(answer, 50))
                    {
                        return false;
                    }
                }
                else if (updateId == 2)
                {
                    field = "numEmployees";
                    intAnswer = scan.nextInt();
                }
                else if (updateId == 3)
                {
                    field = "yearlyRevenue";
                    floatAnswer = scan.nextFloat();
                }
                else if (updateId == 4)
                {
                    field = "stockPrice";
                    floatAnswer = scan.nextFloat();
                }

                PreparedStatement pstJ = con.prepareStatement("UPDATE Company SET " + field + "=? WHERE companyId=?;");
                if (!answer.equals(""))
                {
                    updateStringField(pstJ, answer, companyId);
                }
                else if (intAnswer != -1)
                {
                    updateIntField(pstJ, intAnswer, companyId);
                }
                else if (floatAnswer != -1)
                {
                    updateFloatField(pstJ, floatAnswer, companyId);
                }
                logger.info("" + pstJ);
            }

            System.out.println("Continue to Location Update.");
            return updateLocation(con, scan, companyId, logger);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected LOCATION.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateLocation(Connection con, Scanner scan, int companyId, Logger logger)
    {
        try
        {
            System.out.println("The current information for this Company's Location is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Location WHERE companyId=?;");
            pstM.clearParameters();
            pstM.setInt(1, companyId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                System.out.println("Company ID: " + rs.getInt(1)+ " Location Area: " + rs.getString(2) + " Address: " + rs.getString(3) + " " + rs.getString(4) + ", " + rs.getString(5) + "\n");
            }

            System.out.println("Which field would you like to update? \n1. Location Area \n2. Street Address \n3. City \n4. State \n5. None");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 5)
            {
                System.out.println("Please enter a number between 1 and 5.");
                return false;
            }
            if(updateId == 5)
            {
                System.out.println("Thank you for your Update.");
                return true;
            }

            String field = "";
            String answer = "";

            System.out.println("What would you like this field to up updated to?");
            if(updateId == 1)
            {
                field = "locationArea";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }
            else if(updateId == 2)
            {
                field = "street";
                answer = scan.nextLine();

                if(!inputCheck(answer, 100)) return false;
            }
            else if(updateId == 3)
            {
                field = "city";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }
            else if(updateId == 4)
            {
                field = "state";
                answer = scan.nextLine();

                if(!inputCheck(answer, 2)) return false;
            }

            PreparedStatement pstL = con.prepareStatement("UPDATE Location SET " + field + "=? WHERE companyId=?;");
            updateStringField(pstL, answer, companyId);
            logger.info("" + pstL);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected FULL TIME.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateFullTime(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Full Time Job you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();
            if(!checkID(con, jobId))
            {
                return false;
            }

            System.out.println("The current information for this Job is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM FullTime WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                System.out.println("Job ID: " + rs.getInt(1)+ " Full Time Number of Stock Options: " + rs.getInt(2) + " Signing Bonus: " + rs.getFloat(3) + " Salary: " + rs.getFloat(4));
            }

            System.out.println("Which field would you like to update? \n1. Number of Stock Options \n2. Signing Bonus \n3. Salary");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }

            String field = "";
            int intAnswer = -1;
            float floatAnswer = -1;

            System.out.println("What would you like this field to up updated to?");
            if(updateId == 1)
            {
                field = "numStockOptions";
                intAnswer = scan.nextInt();
            }
            else if(updateId == 2)
            {
                field = "signingBonus";
                floatAnswer = scan.nextFloat();
            }
            else if(updateId == 3)
            {
                field = "salary";
                floatAnswer = scan.nextFloat();
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE FullTime SET " + field + "=? WHERE jobId=?;");
            if (intAnswer != -1)
            {
                updateIntField(pstJ, intAnswer, jobId);
            }
            if (floatAnswer != -1)
            {
                updateFloatField(pstJ, floatAnswer, jobId);
            }
            logger.info("" + pstJ);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }

    /**
     * Updates a table, where the user selected INTERNSHIP.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateInternship(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Job you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();
            if(!checkID(con, jobId))
            {
                return false;
            }

            System.out.println("The current information for this Job is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Job WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                System.out.println("Job ID: " + rs.getInt(1)+ "Internship Pay Period: " + rs.getString(2) + " Rate: " + rs.getFloat(3) + " Season: " + rs.getString(4));

            }

            System.out.println("Which field would you like to update? \n1. Pay Period \n2. Rate \n3. Season");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }

            String field = "";
            String answer = "";
            float floatAnswer = -1;

            System.out.println("What would you like this field to up updated to?");
            if(updateId == 1)
            {
                field = "payPeriod";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }
            else if(updateId == 2)
            {
                field = "rate";
                floatAnswer = scan.nextFloat();
            }
            else if(updateId == 3)
            {
                field = "season";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Internship SET " + field + "=? WHERE jobId=?;");
            if (!answer.equals(""))
            {
                updateStringField(pstJ, answer, jobId);
            }
            else if (floatAnswer != -1)
            {
                updateFloatField(pstJ, floatAnswer, jobId);
            }
            logger.info("" + pstJ);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected JOB.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateJob(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Job you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();

            System.out.println("The current information for this Job is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Job WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                String type = "Full Time";
                if(rs.getBoolean(6))
                {
                    type = "Internship";
                }
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                        + " Type: " + type + "\n");
            }

            System.out.println("Which field would you like to update? \n1. Job Title \n2. Industry \n3. Description \n4. None");
            System.out.println("Company ID and Type are not an allowed to be updated.");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 4)
            {
                System.out.println("Please enter a number between 1 and 4.");
                return false;
            }
            if(updateId != 4)
            {
                String field = "";
                String answer = "";

                System.out.println("What would you like this field to up updated to?");
                if(updateId == 1)
                {
                    field = "jobTitle";
                    answer = scan.nextLine();

                    if(!inputCheck(answer, 50)) return false;
                }
                else if(updateId == 2)
                {
                    field = "industry";
                    answer = scan.nextLine();

                    if(!inputCheck(answer, 25)) return false;
                }
                else if(updateId == 3)
                {
                    field = "description";
                    answer = scan.nextLine();

                    if(!inputCheck(answer, 100)) return false;
                }

                PreparedStatement pstJ = con.prepareStatement("UPDATE Job SET " + field + " =? WHERE jobID=?;");
                if (!updateStringField(pstJ, answer, jobId))
                {
                    System.out.println("There was an error updating.");
                    return false;
                }
                logger.info("" + pstJ);
            }

            System.out.println("Continue to Competition Update.");
            return updateCompetition(con, scan, jobId, logger);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected COMPETITION.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateCompetition(Connection con, Scanner scan, int jobId, Logger logger)
    {
        try {
            System.out.println("The current information for this Job's Competition is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Competition WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while (rs.next()) {
                System.out.println("Job ID: " + rs.getInt(1) + " Number of Open Spots: " + rs.getInt(2) + " Number of Applicants: " + rs.getInt(3));
            }

            System.out.println("Which field would you like to update? \n1. Number of Open Spots \n2. Number of Applicants \n3. None");
            int updateId = scan.nextInt();
            scan.nextLine();

            if (updateId < 1 || updateId > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }
            if(updateId == 3)
            {
                System.out.println("Thank you for your Update.");
                return true;
            }

            String field = "";
            int answer = -1;

            System.out.println("What would you like this field to up updated to?");
            if (updateId == 1)
            {
                field = "numOpenSpots";
                answer = scan.nextInt();
            }
            else if (updateId == 2)
            {
                field = "numApplicants";
                answer = scan.nextInt();
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Competition SET " + field + "=? WHERE jobId=?;");
            updateIntField(pstJ, answer, jobId);
            logger.info("" + pstJ);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }

    /**
     * Updates a table, where the user selected MANAGER.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateManager(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Manager you would like to update?");
            int managerId = scan.nextInt();
            scan.nextLine();

            if(!checkManagerID(con, managerId))
            {
                return false;
            }

            System.out.println("The current information for this Manager is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Manager WHERE managerId=?;");
            pstM.clearParameters();
            pstM.setInt(1, managerId);
            ResultSet rs = pstM.executeQuery();
            while (rs.next())
            {
                String techBool = "No";
                if (rs.getBoolean(4))
                {
                    techBool = "Yes";
                }
                System.out.println("Manager ID: " + rs.getInt(1) + "Company ID: " + rs.getInt(2) + " Manager's Name: " + rs.getString(3) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(5));
            }

            System.out.println("Which field would you like to update? \n1.Manager's Name \n2. Technical Experience \n3. Years at Company");
            System.out.println("Company ID is not an updatable field.");
            int updateId = scan.nextInt();
            scan.nextLine();

            if (updateId < 1 || updateId > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }

            String field = "";
            String answer = "";

            System.out.println("What would you like this field to up updated to?");
            if (updateId == 1) {
                field = "name";
                answer = scan.nextLine();
                if(!inputCheck(answer, 50)) return false;
            }
            else if (updateId == 2)
            {
                answer = "false";
                System.out.println("Enter 'Y' for yes");
                if (scan.nextLine().toLowerCase().equals("y"))
                {
                    answer = "true";
                }
                field = "technicalExperience";
            }
            else if (updateId == 3)
            {
                field = "yearsAtCompany";
                answer = "" + scan.nextInt();
                scan.nextLine();
            }

            PreparedStatement pstRJ = con.prepareStatement("UPDATE Manager SET " + field + "=? WHERE managerId=?;");
            updateStringField(pstRJ, answer, managerId);
            logger.info("" + pstRJ);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected RELATED JOBS.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateRelatedJobs(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            System.out.println("What is the ID of the Job's Related Jobs you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();
            if(!checkID(con, jobId))
            {
                return false;
            }

            System.out.println("The current information for this Job is: ");
            PreparedStatement pst7 = con.prepareStatement("SELECT * FROM RelatedJobs WHERE jobId=?;");
            pst7.clearParameters();
            pst7.setInt(1, jobId);
            ResultSet rs = pst7.executeQuery();
            while (rs.next()) {
                System.out.println("Job ID: " + rs.getInt(1) + " Related Job 1: " + rs.getInt(2) + "Related Job 2: " + rs.getInt(3) + "Related Job 3: " + rs.getInt(4) + "Related Job 4: " + rs.getInt(5) + "Related Job 5: " + rs.getInt(6));
            }

            System.out.println("Which Field would you like to update? \n1. Related Job 1 \n2. Related Job 2 \n3. Related Job 3 \n4. Related Job 4 \n5. Related Job 5");
            int updateId = scan.nextInt();
            scan.nextLine();
            String updateString = "related" + updateId;

            if (updateId < 1 || updateId > 5) {
                System.out.println("Please enter a number between 1 and 5.");
                return false;
            }

            System.out.println("What would you like this field to up updated to?");
            int relatedJob = scan.nextInt();
            scan.nextLine();

            PreparedStatement pstRJ = con.prepareStatement("UPDATE RelatedJobs SET " + updateString + "=? WHERE jobId=?;");
            updateIntField(pstRJ, relatedJob, jobId);
            logger.info("" + pstRJ);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }


    //OPTION 3 SUPPLEMENTARY METHODS
    /**
     * Updates an entry, where the field we are updating wants a STRING.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateStringField(PreparedStatement pst, String answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setString(1, answer);
            pst.setInt(2, id);
            pst.executeUpdate();
            System.out.println("The record has been updated.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error updating: " + e);
            return false;
        }
    }

    /**
     * Updates an entry, where the field we are updating wants an INT.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateIntField(PreparedStatement pst, int answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setInt(1, answer);
            pst.setInt(2, id);
            pst.executeUpdate();
            System.out.println("The record has been updated.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error updating: " + e);
            return false;
        }
    }

    /**
     * Updates an entry, where the field we are updating wants a FLOAT.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateFloatField(PreparedStatement pst, float answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setFloat(1, answer);
            pst.setInt(2, id);
            pst.executeUpdate();
            System.out.println("The record has been updated.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Error updating: " + e);
            return false;
        }
    }


    //OPTION 4 METHODS
    /**
     * Deletes the entries from the associated tables.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if the delete is successful, false otherwise
     */
    public static boolean deleteCall(Connection con, Scanner scan, boolean fromGUI, int jobId, Logger logger)
    {
        try
        {
            if (!fromGUI)
            {
                System.out.println("Please enter the Job's Id");
                jobId = scan.nextInt();
                scan.nextLine();
                if(!checkID(con, jobId))
                {
                    return false;
                }
            }

            PreparedStatement pstCompID = con.prepareStatement("SELECT companyId FROM Job WHERE jobId=?");
            pstCompID.clearParameters();
            pstCompID.setInt(1, jobId);
            ResultSet rs = pstCompID.executeQuery();
            int companyId = 0;
            while(rs.next())
            {
                companyId = rs.getInt(1);
            }

            PreparedStatement pstType = con.prepareStatement("SELECT isInternship FROM Job WHERE jobId=?");
            pstType.clearParameters();
            pstType.setInt(1, jobId);
            rs = pstType.executeQuery();
            boolean type = true;
            while(rs.next())
            {
                type = rs.getBoolean(1);
            }

            PreparedStatement pstStart = con.prepareStatement("START TRANSACTION;");
            pstStart.execute();

            PreparedStatement pstJ = con.prepareStatement("DELETE FROM Job WHERE jobId=?;");
            pstJ.clearParameters();
            pstJ.setInt(1, jobId);
            pstJ.executeUpdate();
            System.out.println("The record has been deleted from Job.");

            PreparedStatement pstCom = con.prepareStatement("DELETE FROM Competition WHERE jobId=?;");
            pstCom.clearParameters();
            pstCom.setInt(1, jobId);
            pstCom.executeUpdate();
            System.out.println("The record has been deleted from Competition.");

            PreparedStatement pstRJCount = con.prepareStatement("SELECT COUNT(*) FROM RelatedJobs WHERE jobId=?;");
            PreparedStatement pstRJ = con.prepareStatement("DELETE FROM RelatedJobs WHERE jobId=?;");
            pstRJCount.clearParameters();
            pstRJCount.setInt(1, jobId);
            rs = pstRJCount.executeQuery();
            boolean rj = false;
            while(rs.next())
            {
                if (rs.getInt(1) != 0)
                {
                    pstRJ.clearParameters();
                    pstRJ.setInt(1, jobId);
                    pstRJ.executeUpdate();
                    System.out.println("The record has been deleted from Related Jobs.");
                    rj = true;
                }
            }

            PreparedStatement pstComNum = con.prepareStatement("SELECT COUNT(*) FROM Job WHERE companyId=?;");
            PreparedStatement pstComP = con.prepareStatement("DELETE FROM Company WHERE companyId=?;");
            PreparedStatement pstLoc = con.prepareStatement("DELETE FROM Location WHERE companyId=?;");
            PreparedStatement pstManager = con.prepareStatement("DELETE FROM Manager WHERE companyId=?;");

            pstComNum.clearParameters();
            pstComNum.setInt(1, companyId);
            rs = pstComNum.executeQuery();
            boolean company = false;

            while(rs.next())
            {
                if(rs.getInt(1) == 0)
                {
                    System.out.println("The Company no longer has any jobs and it and corresponding managers will be deleted.");

                    pstComP.clearParameters();
                    pstComP.setInt(1, companyId);
                    pstComP.executeUpdate();
                    System.out.println("The record has been deleted from Company.");

                    pstLoc.clearParameters();
                    pstLoc.setInt(1, companyId);
                    pstLoc.executeUpdate();
                    System.out.println("The record has been deleted from Location.");

                    pstManager.clearParameters();
                    pstManager.setInt(1, companyId);
                    pstManager.executeUpdate();
                    System.out.println("Corresponding Managers have been deleted.");
                    company = true;
                }
            }

            PreparedStatement pstIn = con.prepareStatement("DELETE FROM Internship WHERE jobId=?;");
            PreparedStatement pstF = con.prepareStatement("DELETE FROM FullTime WHERE jobId=?;");

            if(type)
            {
                pstIn.clearParameters();
                pstIn.setInt(1, jobId);
                pstIn.executeUpdate();
                System.out.println("The record has been deleted from Internship.");
            }
            else if(!type)
            {
                pstF.clearParameters();
                pstF.setInt(1, jobId);
                pstF.executeUpdate();
                System.out.println("The record has been deleted from Full Time.");
            }

            PreparedStatement pstEnd = con.prepareStatement("COMMIT;");
            pstEnd.execute();

            System.out.println("The record has been completely deleted.");

            logger.info("" + pstJ);
            logger.info("" + pstCom);
            logger.info("" + pstRJ);
            if(company)
            {
                logger.info("" + pstComP);
                logger.info("" + pstLoc);
                logger.info("" + pstManager);
            }
            if(type)
            {
                logger.info("" + pstIn);
            }
            else
            {
                logger.info("" + pstF);
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }


    //OPTION 5 METHODS
    /**
     * Searchs for information from certain tables. Also gives several statistics for the group searched.
     * Parent method to search tables.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean searchBy(Connection con, Scanner scan)
    {
        int searchOption;

        try
        {
            System.out.println("What would you like to search by? \n1. Location \n2. Company \n3. Type");

            searchOption = scan.nextInt();
            scan.nextLine();

            if(searchOption == 1)
            {
                boolean success = searchLocation(con,scan);
                return success;
            }
            else if(searchOption == 2)
            {
                boolean success = searchCompany(con,scan);
                return success;
            }
            else if(searchOption == 3)
            {
                boolean success = searchType(con,scan);
                return success;
            }
            else
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }
        }
        catch(Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }

    /**
     * Searchs a table, where the user selected LOCATION.
     * @return true if there were no issues, false otherwise
     */
    public static boolean searchLocation(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("Please enter the state you want to search for");
            String location = scan.nextLine();
            if(!inputCheck(location, 25))
            {
                System.out.println("Please enter a valid input.");
                return false;
            }
            else
            {
                System.out.println("The Job Database in that state:\n");
                PreparedStatement pst8 = con.prepareStatement("select j.jobId, j.jobTitle, j.industry, j.description, j.companyId, j.isInternship from Job j, Location l WHERE j.companyId = l.companyId AND l.state=?;");
                pst8.clearParameters();
                pst8.setString(1, location);
                ResultSet rs = pst8.executeQuery();

                while (rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: "
                            + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Type: " + type);
                }

                System.out.println("Press enter to see some statistics for your search");
                scan.nextLine();

                System.out.println("\nSome Statistics for that state:\n");
                PreparedStatement pst8Stat = con.prepareStatement("select COUNT(*) from Location l WHERE l.state=?;");
                pst8Stat.setString(1, location);
                ResultSet rs8Stat = pst8Stat.executeQuery();
                while (rs8Stat.next())
                {
                    System.out.println("Number of Companies in the Area: " + rs8Stat.getInt(1));
                }
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please Enter a valid Input.");
        }
        return false;
    }

    /**
     * Searchs a table, where the user selected COMPANY.
     * @return true if there were no issues, false otherwise
     */
    public static boolean searchCompany(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("Please enter the ID of the Company you want to search for");
            int companyId = scan.nextInt();
            scan.nextLine();

            if(!checkCompanyID(con, companyId))
            {
                return false;
            }

            PreparedStatement pst6N = con.prepareStatement("select companyName from Company WHERE companyId =?;");
            pst6N.clearParameters();
            pst6N.setInt(1, companyId);
            ResultSet rs6N = pst6N.executeQuery();
            String name = "";
            while(rs6N.next())
            {
                name = rs6N.getString(1);
            }

            System.out.println("The Job Database of " + name + ":\n");
            PreparedStatement pst6 = con.prepareStatement("select * from Job WHERE companyId =?;");
            pst6.clearParameters();
            pst6.setInt(1, companyId);
            ResultSet rs = pst6.executeQuery();

            while(rs.next())
            {
                String type = "Full Time";
                if(rs.getBoolean(6))
                {
                    type = "Internship";
                }
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                        + " Type: " + type);
            }

            System.out.println("Press enter to see some statistics for your search");
            scan.nextLine();

            System.out.println("\nSome Statistics from " + name + ":");
            PreparedStatement pst6Stat = con.prepareStatement("SELECT COUNT(c.numApplicants), AVG(c.numApplicants) FROM Job j, Competition c WHERE j.jobId=c.jobId AND j.companyId=?;");
            pst6Stat.clearParameters();
            pst6Stat.setInt(1, companyId);
            ResultSet rs6Stat = pst6Stat.executeQuery();
            while(rs6Stat.next())
            {
                System.out.println("Number of Open Jobs at " + name + ": " + rs6Stat.getInt(1) + " Average number of Applications to each Job: " + rs6Stat.getFloat(2));

            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please Enter a valid Input.");
        }
        return false;
    }

    /**
     * Searchs a table, where the user selected TYPE.
     * @return true if there were no issues, false otherwise
     */
    public static boolean searchType(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("Please enter the Type of Job you want to search for (I/F)");
            String type = scan.nextLine();
            boolean search = true;

            if (type.length() > 1 || !(type.toLowerCase().equals("i") || type.toLowerCase().equals("f")))
            {
                System.out.println("Type needs to be of length 1 (I or F). Please try again.");
                return false;
            }
            else
            {
                if(type.toLowerCase().equals("f"))
                {
                    search = false;
                }

                System.out.println("The Job Database of that Type:\n");
                PreparedStatement pst7 = con.prepareStatement("SELECT * FROM Job WHERE isInternship=?;");
                pst7.clearParameters();
                pst7.setBoolean(1, search);
                ResultSet rs = pst7.executeQuery();
                while(rs.next())
                {
                    String typeS = "Full Time";
                    if(search)
                    {
                        typeS = "Internship";
                    }
                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + typeS);
                }

                System.out.println("Press enter to see some statistics for your search");
                scan.nextLine();

                if(!search)
                {
                    System.out.println("\nSome Statistics on Full Time Jobs:");
                    PreparedStatement pstFTypeStat = con.prepareStatement("SELECT AVG(salary), Max(salary), AVG(signingBonus) FROM FullTime;");
                    ResultSet rsFTypeStat = pstFTypeStat.executeQuery();
                    while (rsFTypeStat.next())
                    {
                        System.out.println("Average Rate: " + rsFTypeStat.getFloat(1) + " Highest Salary: " + rsFTypeStat.getFloat(2) + " Average SigningBonus: " + rsFTypeStat.getFloat(3));
                    }
                }
                else if(search)
                {
                    System.out.println("\nSome Statistics on Internships:");
                    PreparedStatement pstI1TypeStat = con.prepareStatement("SELECT AVG(rate), MAX(rate) FROM Internship;");
                    ResultSet rsI1TypeStat = pstI1TypeStat.executeQuery();

                    float avgSal = 0;
                    float highSal = 0;
                    while (rsI1TypeStat.next())
                    {
                        avgSal = rsI1TypeStat.getFloat(1);
                        highSal = rsI1TypeStat.getFloat(2);
                    }


                    PreparedStatement pstI2TypeStat = con.prepareStatement("SELECT COUNT(*) FROM Internship WHERE rate = 0;");
                    ResultSet rsI2TypeStat = pstI2TypeStat.executeQuery();
                    int unpaid = 0;
                    while(rsI2TypeStat.next())
                    {
                        unpaid = rsI2TypeStat.getInt(1);
                    }
                    System.out.println("Average Rate: " + avgSal + " Highest Rate: " + highSal + " Number of Unpaid Internships: " + unpaid);


                    System.out.println("\nSummer Internships: ");
                    PreparedStatement pstSum = con.prepareStatement("SELECT jobId, jobTitle FROM Job WHERE jobId IN (SELECT jobId FROM Internship WHERE season='Summer');");
                    ResultSet rsSum = pstSum.executeQuery();
                    while(rsSum.next())
                    {
                        System.out.println("Job Title: " + rsSum.getString(2) + " Job ID: " + rsSum.getInt(1));
                    }
                }
                else
                {
                    return false;
                }
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please Enter a valid Input.");
        }
        return false;
    }


    //OPTION 6 METHODS
    /**
     * Selects all relevant job information across all tables in the database.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean jobInfo(Connection con, Scanner scan, int jobId, boolean fromGUI)
    {
        try
        {
            if(!fromGUI)
            {
                System.out.println("What is the ID number of the job you are looking for?");
                jobId = scan.nextInt();
                scan.nextLine();
                if(!checkID(con, jobId))
                {
                    return false;
                }
            }

            PreparedStatement pstType = con.prepareStatement("SELECT isInternship FROM Job WHERE jobId=?;");
            pstType.setInt(1, jobId);
            ResultSet rsType = pstType.executeQuery();
            boolean type = true;
            while(rsType.next())
            {
                type = rsType.getBoolean(1);
            }
            System.out.println(type);

            //@TODO deal with locationArea
            String pstString = "SELECT j.jobId, j.jobTitle, j.industry, j.description, j.companyId, c.companyName, c.numEmployees, c.yearlyRevenue, c.stockPrice, l.locationArea, l.street, l.city, l.state FROM Job j, Company c, Competition co, Location l WHERE j.jobId=co.jobId AND j.companyId=c.companyId AND j.companyId=l.companyId AND j.jobId=?;";
            PreparedStatement pst8F = con.prepareStatement(pstString);
            pst8F.clearParameters();
            pst8F.setInt(1, jobId);
            ResultSet rs = pst8F.executeQuery();
            if (fromGUI)
            {
              gui.setRSInfo(rs);
            }
            while (rs.next()) //update
            {
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                        + " Type: Full Time"
                        + "\nCompany Name: " + rs.getString(6) + " Number of Employees: " + rs.getInt(7) + " Yearly Revenue: " + rs.getFloat(8) + " Stock Price: " + rs.getFloat(9)
                        + "\nLocation Area: " + rs.getString(10) + " Address: " + rs.getString(11) + " " + rs.getString(12));
            }

            if (!type)
            {
                PreparedStatement pst8F2 = con.prepareStatement("SELECT salary, numStockOptions, signingBonus FROM FullTime WHERE jobId=?;");
                pst8F2.clearParameters();
                pst8F2.setInt(1, jobId);
                rs = pst8F2.executeQuery();
                while(rs.next())
                {
                    System.out.println("Full Time Salary" + rs.getFloat(1) + "\nNumber of Stock Options: " + rs.getInt(2) + " Signing Bonus: " + rs.getFloat(3));
                }
            }
            if (type)
            {
                PreparedStatement pst8I = con.prepareStatement("SELECT payPeriod, rate, season FROM Internship WHERE jobId=?;");
                pst8I.clearParameters();
                pst8I.setInt(1, jobId);
                rs = pst8I.executeQuery();
                while (rs.next())
                {
                    System.out.println("Internship Pay Period: " + rs.getString(1) + " Rate: " + rs.getFloat(2) + " Season: " + rs.getString(3));
                }
            }
            if (fromGUI)
            {
              System.out.println("set type");
              gui.setRSType(rs, type);
            }

            PreparedStatement pst8F3 = con.prepareStatement("SELECT COUNT(*), related1, related2, related3, related4, related5 FROM RelatedJobs WHERE jobId=?;");
            pst8F3.clearParameters();
            pst8F3.setInt(1, jobId);
            rs = pst8F3.executeQuery();
            if (fromGUI)
            {
                gui.setRSRelated(rs);
            }
            while(rs.next())
            {
                if(rs.getInt(1) == 0)
                {
                    System.out.println("No related Jobs have been added.");
                    return true;
                }
                else
                {
                    System.out.println("Related Job 1: " + rs.getInt(2) + " Related Job 2: " + rs.getInt(3) + " Related Job 3: " + rs.getInt(4) + " Related Job 4: " + rs.getInt(5) + " Related Job 5: " + rs.getInt(6));

                }
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid ID number. Try again.");
        }
        return false;
    }


    //OPTION 7 METHODS
    /**
     * Selects all relevant job information across certain tables in the database.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean lookup(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID number of the job you are looking for?");
            int jobId = scan.nextInt();
            scan.nextLine();
            if(!checkID(con, jobId))
            {
                return false;
            }

            System.out.println("What Information are you looking for?");
            System.out.println("1. Company Information \n2. Competition \n3. Type (Full Time / Internship) \n4. Core Job Info \n5. Location Information \n6. Related Jobs");
            int selectOption = scan.nextInt();
            scan.nextLine();

            if(selectOption < 1 || selectOption > 6)
            {
                System.out.println("Please enter a number between 1 and 6.");
                return false;
            }

            if(selectOption == 1)
            {
                PreparedStatement pst1 = con.prepareStatement("SELECT * FROM Job j, Company c WHERE j.companyId=c.companyId AND j.jobId=?;");
                pst1.clearParameters();
                pst1.setInt(1, jobId);
                ResultSet rs = pst1.executeQuery();
                while(rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + type + "\nCompany Name: " + rs.getString(8) + " Number of Employees: " + rs.getInt(9) + " Yearly Revenue: " + rs.getFloat(10) + " Stock Price: " + rs.getFloat(11) + "\n");
                }
            }

            if(selectOption == 2)
            {
                PreparedStatement pst2 = con.prepareStatement("SELECT * FROM Job j, Competition c WHERE j.jobId=c.jobId AND j.jobId=?;");
                pst2.clearParameters();
                pst2.setInt(1, jobId);
                ResultSet rs = pst2.executeQuery();
                while(rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + type + "\nNumber of Open Spots: " + rs.getInt(8) + " Number of Applicants: " + rs.getInt(9) + "\n");
                }
            }

            if(selectOption == 3)
            {
                PreparedStatement pstType = con.prepareStatement("SELECT isInternship FROM Job WHERE jobId=?;");
                pstType.setInt(1, jobId);
                ResultSet rsType = pstType.executeQuery();
                boolean type = true;
                while(rsType.next())
                {
                    type = rsType.getBoolean(1);
                }

                if(!type)
                {
                    PreparedStatement pstFullTime = con.prepareStatement("SELECT j.jobId, j.jobTitle, j.industry, j.description, j.companyId, f.salary, f.numStockOptions, f.signingBonus FROM Job j, FullTime f WHERE f.jobId=j.jobId AND j.jobId=?;");
                    pstFullTime.setInt(1, jobId);

                    ResultSet rs = pstFullTime.executeQuery();
                    while(rs.next())
                    {
                        System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                                + " Type: Full Time" + "\n Full Time Salary: " + rs.getFloat(6) + "\nNumber of Stock Options: " + rs.getInt(7) + " Signing Bonus: " + rs.getFloat(8) + "\n");
                    }
                }
                else if(type)
                {
                    PreparedStatement pstIntern = con.prepareStatement("SELECT j.jobId, j.jobTitle, j.industry, j.description, j.companyId, i.payPeriod, i.rate, i.season FROM Job j, Internship i WHERE i.jobId=j.jobId AND j.jobId=?;");
                    pstIntern.setInt(1, jobId);

                    ResultSet rs = pstIntern.executeQuery();
                    while(rs.next())
                    {
                        System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                                + " Type: Internship" + "\nInternship Pay Period: " + rs.getString(6) + " Rate: " + rs.getFloat(7) + " Season: " + rs.getString(8) + "\n");
                    }
                }
            }

            if(selectOption == 4)
            {
                PreparedStatement pst4 = con.prepareStatement("SELECT * FROM Job j WHERE j.jobId=?;");
                pst4.clearParameters();
                pst4.setInt(1, jobId);
                ResultSet rs = pst4.executeQuery();
                while(rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + type + "\n");
                }
            }

            if(selectOption == 5)
            {
                PreparedStatement pst5 = con.prepareStatement("SELECT * FROM Job j, Location l WHERE j.companyId=l.companyId AND j.jobId=?;");
                pst5.clearParameters();
                pst5.setInt(1, jobId);
                ResultSet rs = pst5.executeQuery();
                while(rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + type + "\nLocation Area: " + rs.getString(8) + " Address: " + rs.getString(9) + " " + rs.getString(10) + ", " + rs.getString(11) + "\n");
                }
            }

            if(selectOption == 6)
            {
                PreparedStatement pst7 = con.prepareStatement("SELECT * FROM Job j, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?;");
                pst7.clearParameters();
                pst7.setInt(1, jobId);
                ResultSet rs = pst7.executeQuery();
                while(rs.next())
                {
                    String type = "Full Time";
                    if(rs.getBoolean(6))
                    {
                        type = "Internship";
                    }

                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5)
                            + " Type: " + type + "\nRelated Job 1: " + rs.getInt(8) + " Related Job 2: " + rs.getInt(9) + " Related Job 3: " + rs.getInt(10) + " Related Job 4: " + rs.getInt(11) + " Related Job 5: " + rs.getInt(12) + "\n");
                }
            }
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }


    //OPTION 8 METHODS
    /**
     * Returns statistics for certain records in the database.
     * @param con and scan as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean statistics(Connection con)
    {
        try
        {
            System.out.println("Statistic Categories: \n");

            System.out.println("\nAverage Internship Rates by Location: ");
            PreparedStatement pstLI = con.prepareStatement("SELECT l.locationArea, COUNT(*), AVG(i.rate)FROM Location l, Internship i, Job j WHERE j.companyId = l.companyId AND i.jobId = j.jobId GROUP BY l.locationArea;");
            ResultSet rsLocI = pstLI.executeQuery();
            while(rsLocI.next())
            {
                System.out.println("Location Area: " + rsLocI.getString(1) + " Number of Jobs: " + rsLocI.getInt(2) + " Average Rate: " + rsLocI.getFloat(3));
            }

            System.out.println("\nAverage Full Time Salaries by Location: ");
            PreparedStatement pstLF = con.prepareStatement("SELECT l.locationArea, COUNT(*), AVG(i.salary)FROM Location l, FullTime i, Job j WHERE j.companyId = l.companyId AND i.jobId = j.jobId GROUP BY l.locationArea;");
            ResultSet rsLocF = pstLF.executeQuery();
            while(rsLocF.next())
            {
                System.out.println("Location Area: " + rsLocF.getString(1) + " Number of Jobs: " + rsLocF.getInt(2) + " Average Salary: " + rsLocF.getFloat(3));
            }

            System.out.println("\nCompany Statistics by Location: ");
            PreparedStatement pstComLoc = con.prepareStatement("SELECT l.locationArea, COUNT(*), AVG(c.yearlyRevenue), AVG(c.numEmployees), AVG(c.stockPrice) FROM Company c, Location l WHERE l.companyId = c.companyId GROUP BY l.locationArea;");
            ResultSet rsComLoc = pstComLoc.executeQuery();
            while(rsComLoc.next())
            {
                System.out.println("Location Area: " + rsComLoc.getString(1) + " Number of Companies: " + rsComLoc.getInt(2) + " Average Yearly Revenue: " + rsComLoc.getFloat(3) + " Average Number of Employees: " + rsComLoc.getFloat(4) + " Average Stock Price: " + rsComLoc.getFloat(5));
            }

            System.out.println("\nAverage Competition by Industry: ");
            PreparedStatement pstComp = con.prepareStatement("SELECT j.industry, AVG(c.numOpenSpots), AVG(c.numApplicants) FROM Competition c, Job j WHERE j.jobId = c.jobId GROUP BY j.industry;");
            ResultSet rsComp = pstComp.executeQuery();
            while(rsComp.next())
            {
                System.out.println("Industry: " + rsComp.getString(1) + " Average Open Spots: " + rsComp.getFloat(2) + " Average Applicants: " + rsComp.getFloat(3));
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong. Please enter this again.");
        }
        return false;
    }


    //OPTION 9 METHODS
    /**
     * Creates a new manager.
     * @param con Connection to the DB
     * @param scan Scanner object
     * @return true if creation was successful, false otherwise
     */
    public static boolean createNewManager(Connection con, Scanner scan, Logger logger)
    {
        try
        {
            PreparedStatement pstC = con.prepareStatement("INSERT INTO Company(companyName, numEmployees, yearlyRevenue, stockPrice) VALUES(?,?,?,?);");
            PreparedStatement pstL = con.prepareStatement("INSERT INTO Location(companyId, locationArea, street, city, state) VALUES(?,?,?,?,?);");
            System.out.println("Do you need to create a new Company? Enter 'y' for yes.");
            String company = scan.nextLine();
            boolean success = true;
            boolean companyCreate = false;
            int companyId = 0;

            if(company.toLowerCase().equals("y"))
            {
                companyCreate = true;

                success = createCompany(pstC, pstL, scan);
                if(!success)
                {
                    System.out.println("The Company creation failed. Please try again.");
                    return false;
                }

                PreparedStatement pstStart = con.prepareStatement("START TRANSACTION;");
                pstStart.execute();
                pstC.executeUpdate();
                System.out.println("The Company was successfully created.");

                PreparedStatement pstId = con.prepareStatement("SELECT MAX(companyId) FROM Company;");
                ResultSet rsId = pstId.executeQuery();
                while(rsId.next())
                {
                    companyId = rsId.getInt(1);
                }

                pstL.setInt(1, companyId);
                pstL.executeUpdate();
                System.out.println("The Location was successfully created.");
            }
            else
            {
                System.out.println("Enter the Manager's Company Id");
                companyId = scan.nextInt();
                scan.nextLine();

                if(!checkCompanyID(con, companyId))
                {
                    return false;
                }
            }

            PreparedStatement pstM = con.prepareStatement("INSERT INTO MANAGER(companyId, name, technicalExperience, yearsAtCompany) VALUES(?,?,?,?);");
            pstM.clearParameters();
            pstM.setInt(1, companyId);
            success = createManager(pstM, scan, logger);
            if (!success)
            {
                System.out.println("The manager creation failed. Please try again.");
                return false;
            }
            pstM.executeUpdate();
            PreparedStatement pstEnd = con.prepareStatement("COMMIT;");
            pstEnd.execute();

            if(companyCreate)
            {
                logger.info("" + pstC);
                logger.info("" + pstL);
            }
            logger.info("" + pstM);

            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.out.println("There was an error creating the manager. Try again.");
        }
        return false;
    }


    //OPTION 10 METHODS
    /**
     * Creates an external file of all entries in the database
     * @param con as input to assist in executing the SQL commands.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean generateReport(Connection con)
    {
        try
        {
            //generate a report
            csvGenerator csv = new csvGenerator();
            return csv.write(con);
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong.");
        }
        return false;
    }


    //OPTION 11 HAS NO METHODS


    //UNIVERSAL METHODS
    /**
     * Used to check if a given string str is less than a given length characters.
     * @return true if it is less than or equal to, false if it's more
     */
    public static boolean inputCheck(String str, int length)
    {
        if (str.length() <= length)
        {
            return true;
        }
        else {
            System.out.println("Must be less than " + length + " characters. Try again.");
            return false;
        }
    }

    /**
     * Checks to ensure the ID is in the database to avoid throwing errors.
     * @param companyId is the ID being checked.
     * @return true if the ID is in the database, false otherwise
     */
    public static boolean checkCompanyID(Connection con, int companyId)
    {
        try
        {
            PreparedStatement pstCheck = con.prepareStatement("SELECT COUNT(*) FROM Company WHERE companyId=?;");
            pstCheck.clearParameters();
            pstCheck.setInt(1, companyId);
            ResultSet rsCheck = pstCheck.executeQuery();
            while(rsCheck.next())
            {
                if(rsCheck.getInt(1) == 0)
                {
                    System.out.println("Please enter a Company ID that is in system.");
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong, please try again.");
            return false;
        }
        return true;
    }

    /**
     * Checks to ensure the ID is in the database to avoid throwing errors.
     * @param managerId is the ID being checked.
     * @return true if the ID is in the database, false otherwise
     */
    public static boolean checkManagerID(Connection con, int managerId)
    {
        try {
            PreparedStatement pstCheck = con.prepareStatement("SELECT COUNT(*) FROM Manager WHERE managerId=?;");
            pstCheck.clearParameters();
            pstCheck.setInt(1, managerId);
            ResultSet rsCheck = pstCheck.executeQuery();
            while (rsCheck.next())
            {
                if (rsCheck.getInt(1) == 0)
                {
                    System.out.println("Please enter a Manager ID that is in system.");
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong, please try again.");
            return false;
        }
        return true;
    }

    /**
     * Checks to ensure the ID is in the database to avoid throwing errors.
     * @param jobId is the ID being checked.
     * @return true if the ID is in the database, false otherwise
     */
    public static boolean checkID(Connection con, int jobId)
    {
        try
        {
            PreparedStatement pstCheck = con.prepareStatement("SELECT COUNT(*) FROM Job WHERE jobId=?;");
            pstCheck.clearParameters();
            pstCheck.setInt(1, jobId);
            ResultSet rsCheck = pstCheck.executeQuery();
            while(rsCheck.next())
            {
                if(rsCheck.getInt(1) == 0)
                {
                    System.out.println("Please enter a Job ID that is in system.");
                    return false;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Something went wrong, please try again.");
            return false;
        }
        return true;
    }

    public static void writeAllToCSV(Connection con)
    {
      FileWriter fw = new FileWriter("CompanyDatabase.csv");
      PreparedStatement pst = con.prepareStatement("SELECT * FROM Company");
      ResultSet rs = pst.executeQuery();
      while (rs.next())
      {
        fw.append();
      }
    }
}
