/**
 * Currently functioning:
 * Essentially the whole crud lab, updated for our schema (still needs testing). Nice!
 * Sorry @haleyanderson4, I'm having trouble testing. Will keep working.
 *
 * @TODO
 * Since we want a different format, should have minimal functionality in main method - most work done in helper methods
 * Maybe even different classes?
 * Front-end stuff
 * Populate our db with actual data (low priority)
 * Testing!!!
 * ??? More, what else
 */

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.sql.*;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        Connection con; //database connection
        Scanner scan = new Scanner(System.in);
        int editOption = 0;

        try {
            con = Config.getMySqlConnection(); //connect to database
            boolean loop = true;
            System.out.println("Options on what to do: \n1. Display all Jobs \n2. Add a new Job Posting \n3. Update a Job Posting \n4. Remove a Job \n5. Search by Location, Company, or Type "
                    + "\n6. Find All Info for a Job \n7. Get Select Info for a Job \n8. Job Statistics \n9. Undo \n10. Generate Database Report \n11. Quit");
            while(loop)
            {
                try
                {
                    System.out.println("What would you like to do: ");
                    editOption = scan.nextInt();
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
                    continue;
                }

                if(editOption == 1) //prelim completed
                {
                    boolean success = queryMethod(con, scan);
                    if(!success)
                    {
                        System.out.println("The Query failed. Please try again.");
                    }
                }

                if(editOption == 2) //prelim completed
                {
                    boolean success = createNewPosting(con, scan);
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
                    boolean success = updateField(con, scan);
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
                    boolean success = deleteCall(con, scan);
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
                    boolean success = jobInfo(con, scan);
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
                    boolean success = statistics(con, scan);
                    if(!success)
                    {
                        System.out.println("The look up failed. Please try again.");
                    }
                }

                if(editOption == 9)
                {
                    boolean success = undo(con);
                    if(success)
                    {
                        System.out.println("Undo Successful.");
                    }
                    if(!success)
                    {
                        System.out.println("Undo Failed. Please try again.");
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

    /**
     * Used to check if a given string str is less than a given length characters.
     * @return true if it is less than or equal to, false if it's more
     */
    public static boolean inputCheck(String str, int length)
    {
        if (str.length() <= length) return true;
        else {
            System.out.println("Must be less than " + length + " characters. Try again.");
            return false;
        }
    }

    /**
     * Can be called to delete an entry from a specific table.
     * @param pst the prepared statement
     * @param tableName the name of the table we're deleting from
     * @param idName the primary key associated with this table
     * @param id the id of the entry we're deleting
     * @return true if the delete is successful, false otherwise
     */
    public static boolean deleteEntry(PreparedStatement pst, String tableName, String idName, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setString(1, tableName);
            pst.setString(2, idName);
            pst.setInt(3, id);
            pst.executeUpdate();
            System.out.println("The record has been deleted from " + tableName + ".");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("There was an error deleting. Error: " + e);
            return false;
        }
    }

    /**
     * Updates an entry, where the field we are updating wants a STRING.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateStringField(PreparedStatement pst, String field, String answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setString(1, field);
            pst.setString(2, answer);
            pst.setInt(3, id);
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
    public static boolean updateIntField(PreparedStatement pst, String field, int answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setString(1, field);
            pst.setInt(2, answer);
            pst.setInt(3, id);
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
    public static boolean updateFloatField(PreparedStatement pst, String field, float answer, int id)
    {
        try
        {
            pst.clearParameters();
            pst.setString(1, field);
            pst.setFloat(2, answer);
            pst.setInt(3, id);
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

    //prelim completed
    /**
     * Returns all entries in the JOB table.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if the call was successful, false otherwise
     */
    public static boolean queryMethod(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("The Job Database:");
            PreparedStatement pst1 = con.prepareStatement("select * from Job;");
            ResultSet rs = pst1.executeQuery();
            String type = "Full Time";
            if(rs.getBoolean(7))
            {
                type = "Internship";
            }

            while (rs.next())
            {
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                        + " Type: " + type);
            }
            System.out.println("");
            return true;
        }
        catch(Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
        }
        return false;
    }

    /**
     * Creates a new posting for a newly created job!
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createNewPosting(Connection con, Scanner scan)
    {
        try
        {
            boolean validInput = true;
            boolean success = true;
            int jobId = 0;
            int companyId = 0;
            int managerId = 0;

            PreparedStatement pstId = con.prepareStatement("SELECT MAX(?) FROM ?;");

            PreparedStatement pstC = con.prepareStatement("INSERT INTO Company(companyName, numEmployees, yearlyRevenue, stockPrice) VALUES(?,?,?,?);");
            PreparedStatement pstL = con.prepareStatement("INSERT INTO Location(companyId, locationArea, street, city, state) VALUES(?,?,?,?,?);");
            System.out.println("Do you need to create a new Company? Enter 'y' for yes.");
            String company = scan.nextLine();
            boolean createCompany = false;
            if(company.toLowerCase().equals("y"))
            {
                createCompany = true;
                success = createCompany(validInput, pstC, pstL, scan);
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

                PreparedStatement companyExist = con.prepareStatement("SELECT COUNT(*) FROM Company WHERE companyId=?;");
                companyExist.clearParameters();
                companyExist.setInt(1, companyId);
                ResultSet setC = companyExist.executeQuery();

                int existingCompany = setC.getInt(1);

                if (existingCompany == 0)
                {
                    System.out.println("That Company does not exsist. Please try again.");
                    return false;
                }
            }

            PreparedStatement pstM = con.prepareStatement("INSERT INTO Manager(name, technicalExperience, yearsAtCompany) VALUES(?,?,?);");
            System.out.println("Do you need to create a new Manager? Enter 'y' for yes.");
            String manager = scan.nextLine();
            boolean createManager = false;
            if(manager.toLowerCase().equals("y"))
            {
                createManager = true;
                success = createManager(validInput, pstM, scan);
                if(!success)
                {
                    System.out.println("The Manager creation failed. Please try again.");
                    return false;
                }
            }
            else
            {
                System.out.println("Enter the Job's Manager Id");
                managerId = scan.nextInt();
                scan.nextLine();

                PreparedStatement managerExist = con.prepareStatement("SELECT COUNT(*) FROM Manager WHERE managerId=?;");
                managerExist.clearParameters();
                managerExist.setInt(1, managerId);
                ResultSet setM = managerExist.executeQuery();

                int existingManager = setM.getInt(1);

                if (existingManager == 0)
                {
                    System.out.println("That Manager does not exsist. Please try again.");
                    return false;
                }
            }

            PreparedStatement pstJ = con.prepareStatement("INSERT INTO Job(jobTitle, industry, description, companyId, managerId, type) VALUES(?,?,?,?,?,?);");
            PreparedStatement pstComp = con.prepareStatement("INSERT INTO Competition(jobId, numOpenSpots, numApplicants) VALUES(?,?,?);");
            boolean type = true;
            success = createJob(validInput, pstJ, pstComp, type, scan, con);
            if(!success)
            {
                System.out.println("The Job creation failed. Please try again.");
                return false;
            }

            PreparedStatement pstF = con.prepareStatement("INSERT INTO FullTime(jobId, numStockOptions, signingBonus, salary) VALUES(?,?,?,?);");
            PreparedStatement pstI = con.prepareStatement("INSERT INTO Internship(jobId, payPeriod, salary, season) VALUES(?,?,?,?);");
            success = createType(validInput, pstF, pstI, type, scan);
            if(!success)
            {
                System.out.println("The Full Time or Internship creation failed. Please try again.");
                return false;
            }

            if(!validInput)
            {
                System.out.println("Please enter this information again.");
                return false;
            }

            PreparedStatement pstR = con.prepareStatement("INSERT INTO RelatedJobs(jobId, related1, related2, related3, related4, related5) VALUES(?,?,?,?,?,?);");
            boolean related = true;
            success = createRelated(validInput, pstR, related, scan);
            if(!success)
            {
                System.out.println("The Related Jobs creation failed. Please try again.");
                return false;
            }

            if(validInput)
            {
                PreparedStatement pstStart = con.prepareStatement("START TRANSACTION");
                pstStart.execute();

                if(createCompany)
                {
                    pstC.executeUpdate();
                    System.out.println("The Company has been created.");

                    pstId.clearParameters();
                    pstId.setString(1, "companyId");
                    pstId.setString(2, "Company");
                    ResultSet rsId = pstId.executeQuery();
                    while(rsId.next())
                    {
                        companyId = rsId.getInt(1);
                    }

                    pstL.setInt(1, companyId);
                    pstL.executeUpdate();
                    System.out.println("The Location has been created.");
                }

                if(createManager)
                {
                    pstM.executeUpdate();
                    System.out.println("The Manager has been created.");

                    pstId.clearParameters();
                    pstId.setString(1, "managerId");
                    pstId.setString(2, "Manager");
                    ResultSet rsId = pstId.executeQuery();
                    while(rsId.next())
                    {
                        managerId = rsId.getInt(1);
                    }
                }

                pstJ.setInt(5, companyId);
                pstJ.setInt(6, managerId);
                pstJ.executeUpdate();
                System.out.println("The Job has been created.");

                pstId.clearParameters();
                pstId.setString(1, "jobId");
                pstId.setString(2, "Job");
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
                    pstF.clearParameters();
                    pstF.setInt(1, jobId);

                    pstF.executeUpdate();
                    System.out.println("The Full Time Position has been created.");
                }
                if(type)
                {
                    pstI.clearParameters();
                    pstI.setInt(1, jobId);

                    pstI.executeUpdate();
                    System.out.println("The Internship has been created.");
                }

                if(related)
                {
                    pstR.setInt(1, jobId);
                    pstR.executeUpdate();
                    System.out.println("The Related Job Posting has been created.");
                }

                PreparedStatement pstEnd = con.prepareStatement("COMMIT");
                pstEnd.execute();
                return true;
            }
        }
        catch (Exception e)
        {
            System.out.println("Please enter this information again, and be sure that the ID number chosen is available.");
        }
        return false;
    }

    //prelim completed
    /**
     * Parent method to update database tables, where the user selected UPDATE INFORMATION.
     * Method calls updateTable methods to execute the requested updates.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateField(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What would you like to update?");
            System.out.println("1. Company Information \n2. Competition Information \n3. Full Time Information \n4. Internship Information \n5. Job Information \n6. Location Information \n7. Manager Information \n8. Related Job Information");
            int selectOption = scan.nextInt();
            scan.nextLine();

            if(selectOption == 1)
            {
                boolean success = updateCompany(con, scan);
                return success;
            }
            else if(selectOption == 2)
            {
                boolean success = updateCompetition(con, scan);
                return success;
            }
            else if(selectOption == 3)
            {
                boolean success = updateFullTime(con, scan);
                return success;
            }
            else if(selectOption == 4) //@TODO update answer to int/float
            {
                boolean success = updateInternship(con, scan);
                return success;
            }
            else if(selectOption == 5)
            {
                boolean success = updateJob(con, scan);
                return success;
            }
            else if(selectOption == 6)
            {
                boolean success = updateLocation(con, scan);
                return success;
            }
            else if(selectOption == 7)
            {
                boolean success = updateManager(con, scan);
                return success;
            }
            else if(selectOption == 8)
            {
                boolean success = updateRelatedJobs(con, scan);
                return success;
            }
            else
            {
                System.out.println("Please enter a number between 1 and 8.");
                return false;
            }
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }

    //prelim completed
    /**
     * Searchs for information from certain tables. Also gives several statistics for the group searched.
     * Parent method to search tables.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
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

    //prelim completed
    /**
     * Selects all relevant job information across all tables in the database.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean jobInfo(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID number of the job you are looking for?");
            int jobId = scan.nextInt();
            scan.nextLine();

            PreparedStatement pstType = con.prepareStatement("SELECT type FROM Job WHERE jobId=?;");
            pstType.setInt(1, jobId);
            ResultSet rsType = pstType.executeQuery();
            Boolean type = rsType.getBoolean(1);

            if (!type)
            {
                PreparedStatement pst8F = con.prepareStatement("SELECT * FROM Job j, Company c, Competition co, Location l, Manager m, FullTime f, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?;");
                pst8F.clearParameters();
                pst8F.setInt(1, jobId);
                ResultSet rs = pst8F.executeQuery();
                while (rs.next()) //update
                {
                    String techBool = "No";
                    if (rs.getBoolean(19))
                    {
                        techBool = "Yes";
                    }

                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: Full Time"
                            + "\nCompany Name: " + rs.getString(9) + " Number of Employees: " + rs.getInt(10) + " Yearly Revenue: " + rs.getFloat(11) + " Stock Price: " + rs.getFloat(12)
                            + "\nLocation Area: " + rs.getString(14) + " Address: " + rs.getString(15) + " " + rs.getString(16)
                            + "\nManager's Name: " + rs.getString(18) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(20)
                            + "\n Full Time Salary" + rs.getFloat(24) + "\nNumber of Stock Options: " + rs.getInt(22) + " Signing Bonus: " + rs.getFloat(23)
                            + "\nRelated Job 1: " + rs.getInt(26) + "Related Job 2: " + rs.getInt(27) + "Related Job 3: " + rs.getInt(28) + "Related Job 4: " + rs.getInt(29) + "Related Job 5: " + rs.getInt(30));
                }
            }
            if (type)
            {
                PreparedStatement pst8I = con.prepareStatement("SELECT * FROM Job j, Company c, Competition co, Location l, Manager m, Internship i, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?;");
                pst8I.clearParameters();
                pst8I.setInt(1, jobId);
                ResultSet rs = pst8I.executeQuery();
                while (rs.next())
                {
                    String techBool = "No";
                    if (rs.getBoolean(19))
                    {
                        techBool = "Yes";
                    }

                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: Internship"
                            + "\nCompany Name: " + rs.getString(9) + " Number of Employees: " + rs.getInt(10) + " Yearly Revenue: " + rs.getFloat(11) + " Stock Price: " + rs.getFloat(12)
                            + "\nLocation Area: " + rs.getString(14) + " Address: " + rs.getString(15) + " " + rs.getString(16)
                            + "\nManager's Name: " + rs.getString(18) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(20)
                            + "\nInternship Pay Period: " + rs.getString(22) + " Salary: " + rs.getFloat(23) + " Season: " + rs.getString(24)
                            + "\nRelated Job 1: " + rs.getInt(26) + "Related Job 2: " + rs.getInt(27) + "Related Job 3: " + rs.getInt(28) + "Related Job 4: " + rs.getInt(29) + "Related Job 5: " + rs.getInt(30));
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

    //doesn't need to be split, prelim completed
    /**
     * Selects all relevant job information across certain tables in the database.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean lookup(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID number of the job you are looking for?");
            int jobId = scan.nextInt();
            scan.nextLine();

            System.out.println("What Information are you looking for?");
            System.out.println("1. Company Information \n2. Competition \n3. Type (Full Time / Internship) \n4. Core Job Info \n5. Location Information \n6. Manager Information \n7. Related Jobs");
            int selectOption = scan.nextInt();
            scan.nextLine();

            if(selectOption == 1)
            {
                PreparedStatement pst1 = con.prepareStatement("SELECT * FROM Job j, Company c WHERE j.companyId=c.companyId AND j.jobId=?;");
                pst1.clearParameters();
                pst1.setInt(1, jobId);
                ResultSet rs = pst1.executeQuery();
                while(rs.next())
                {
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\nCompany Name: " + rs.getString(9) + " Number of Employees: " + rs.getInt(10) + " Yearly Revenue: " + rs.getFloat(11) + " Stock Price: " + rs.getFloat(12) + "\n");
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
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\nNumber of Open Spots: " + rs.getInt(9) + " Number of Applicants: " + rs.getInt(10) + "\n");
                }
            }

            if(selectOption == 3)
            {
                PreparedStatement pstType = con.prepareStatement("SELECT type FROM Job WHERE jobId=?;");
                pstType.setInt(1, jobId);
                ResultSet rsType = pstType.executeQuery();
                String type = rsType.getString(1);

                if(type == "F") //add salary
                {
                    PreparedStatement pstFullTime = con.prepareStatement("SELECT * FROM Job j, FullTime f WHERE f.jobId=j.jobId AND j.jobId=?;");
                    pstFullTime.setInt(1, jobId);

                    ResultSet rs = pstFullTime.executeQuery();
                    while(rs.next())
                    {
                        System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                + " Type: " + rs.getString(7) + "\n Full Time Salary: N/A" + "\nNumber of Stock Options: " + rs.getInt(9) + " Signing Bonus: " + rs.getFloat(10) + "\n");
                    }
                }
                if(type == "I")
                {
                    PreparedStatement pstIntern = con.prepareStatement("SELECT * FROM Job j, Internship i WHERE i.jobId=j.jobId AND j.jobId=?;");
                    pstIntern.setInt(1, jobId);

                    ResultSet rs = pstIntern.executeQuery();
                    while(rs.next())
                    {
                        System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                + " Type: " + rs.getString(7) + "\nInternship Pay Period: " + rs.getString(9) + " Salary: " + rs.getFloat(10) + " Season: " + rs.getString(11) + "\n");
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
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\n");
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
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\nLocation Area: " + rs.getString(9) + " Address: " + rs.getString(10) + " " + rs.getString(11) + "\n");
                }
            }

            if(selectOption == 6)
            {
                PreparedStatement pst6 = con.prepareStatement("SELECT * FROM Job j, Manager m WHERE j.managerId=m.managerId AND j.jobId=?;");
                pst6.clearParameters();
                pst6.setInt(1, jobId);
                ResultSet rs = pst6.executeQuery();
                while(rs.next())
                {
                    String techBool = "No";
                    if(rs.getBoolean(10))
                    {
                        techBool = "Yes";
                    }
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\nManager's Name: " + rs.getString(9) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(11) + "\n");
                }
            }

            if(selectOption == 7)
            {
                PreparedStatement pst7 = con.prepareStatement("SELECT * FROM Job j, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?;");
                pst7.clearParameters();
                pst7.setInt(1, jobId);
                ResultSet rs = pst7.executeQuery();
                while(rs.next())
                {
                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + rs.getString(7) + "\nRelated Job 1: " + rs.getInt(9) + "Related Job 2: " + rs.getInt(10) + "Related Job 3: " + rs.getInt(11) + "Related Job 4: " + rs.getInt(12) + "Related Job 5: " + rs.getInt(13) + "\n");
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

    /**
     * Calls the deleteEntry method with the associated tables.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean deleteCall(Connection con, Scanner scan)
    {
        try
        {
            int jobId = 0;

            System.out.println("Please enter the Job's Id to be deleted:");
            jobId = scan.nextInt();
            scan.nextLine();

            PreparedStatement pstCall = con.prepareStatement("SELECT ? FROM Job WHERE jobId=?;");
            pstCall.clearParameters();
            pstCall.setString(1, "companyId");
            pstCall.setInt(2, jobId);
            ResultSet rs = pstCall.executeQuery();
            int companyId = rs.getInt(1);

            pstCall.clearParameters();
            pstCall.setString(1, "managerId");
            pstCall.setInt(2, jobId);
            rs = pstCall.executeQuery();
            int managerId = rs.getInt(1);

            pstCall.clearParameters();
            pstCall.setString(1, "type");
            pstCall.setInt(2, jobId);
            rs = pstCall.executeQuery();
            boolean type = rs.getBoolean(1);

            boolean deleteCompany = false;
            boolean deleteManager = false;
            PreparedStatement pstDeleteMaybe = con.prepareStatement("SELECT COUNT(*) FROM Job WHERE jobId=? AND ?=?;");
            pstDeleteMaybe.clearParameters();
            pstDeleteMaybe.setInt(1, jobId);
            pstDeleteMaybe.setString(2, "companyId");
            pstDeleteMaybe.setInt(3, companyId);
            rs = pstDeleteMaybe.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1) == 0)
                {
                    deleteCompany = true;
                }
            }

            pstDeleteMaybe.clearParameters();
            pstDeleteMaybe.setInt(1, jobId);
            pstDeleteMaybe.setString(2, "managerId");
            pstDeleteMaybe.setInt(3, managerId);
            rs = pstDeleteMaybe.executeQuery();
            while(rs.next())
            {
                if(rs.getInt(1) == 0)
                {
                    deleteManager = true;
                }
            }

            PreparedStatement pst5 = con.prepareStatement("DELETE FROM ? WHERE ?=?;");

            if(deleteCompany)
            {
                deleteEntry(pst5, "Company", "companyId", companyId);
                deleteEntry(pst5, "Location", "companyId", companyId);
            }
            if(deleteManager)
            {
                deleteEntry(pst5, "Manager", "managerId", managerId);
            }
            deleteEntry(pst5, "Competition", "jobId", jobId);
            deleteEntry(pst5, "Job", "jobId", jobId);
            deleteEntry(pst5, "RelatedJobs", "jobId", jobId);

            if(type)
            {
                deleteEntry(pst5, "Internship", "jobId", jobId);
            }
            else if(!type)
            {
                deleteEntry(pst5, "FullTime", "jobId", jobId);
            }

            System.out.println("The record has been completely deleted.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }


    //update Table methods
    /**
     * Updates a table, where the user selected COMPANY.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateCompany(Connection con, Scanner scan)
    {
        try {
            System.out.println("What is the ID of the Company you would like to update?");
            int companyId = scan.nextInt();
            scan.nextLine();

            System.out.println("The current information for this Company is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Company WHERE companyId=?;");
            pstM.clearParameters();
            pstM.setInt(1, companyId);
            ResultSet rs = pstM.executeQuery();
            while (rs.next()) {
                System.out.println("Company ID: " + rs.getInt(1) + " Company Name: " + rs.getString(2) + " Number of Employees: " + rs.getInt(3) + " Yearly Revenue: " + rs.getFloat(4) + " Stock Price: " + rs.getFloat(5));
            }

            System.out.println("Which field would you like to update? \n1. Company Name \n2. Number of Employees \n3. Yearly Revenue \n4. Stock Price");
            int updateId = scan.nextInt();
            scan.nextLine();

            if (updateId < 1 || updateId > 4) {
                System.out.println("Please enter a number between 1 and 4.");
                return false;
            }

            String field = "";
            String answer = "";
            int intAnswer = -1;
            float floatAnswer = -1;

            System.out.println("What would you like this field to up updated to?");
            if (updateId == 1) {
                field = "companyName";
                answer = scan.nextLine();
                if (!inputCheck(answer, 50)) {
                    return false;
                }
            } else if (updateId == 2) {
                field = "numEmployees";
                intAnswer = scan.nextInt();
            } else if (updateId == 3) {
                field = "yearlyRevenue";
                floatAnswer = scan.nextFloat();
            } else if (updateId == 4) {
                field = "stockPrice";
                floatAnswer = scan.nextFloat();
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Company SET ?=? WHERE companyId=?;");
            if (!answer.equals("")) {
                updateStringField(pstJ, field, answer, companyId);
            } else if (intAnswer != -1) {
                updateIntField(pstJ, field, intAnswer, companyId);
            } else if (floatAnswer != -1) {
                updateFloatField(pstJ, field, floatAnswer, companyId);
            }
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }

    /**
     * Updates a table, where the user selected COMPETITION.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateCompetition(Connection con, Scanner scan)
    {
        try {
            System.out.println("What is the ID of the Job you would like to update the Competition to?");
            int jobId = scan.nextInt();
            scan.nextLine();

            System.out.println("The current information for this Job's Competition is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Competition WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while (rs.next()) {
                System.out.println("Job ID: " + rs.getInt(1) + " Number of Stock Options: " + rs.getInt(2) + " Singing Bonus: " + rs.getFloat(3));
            }

            System.out.println("Which field would you like to update? \n1. Number of Open Spots \n2. Number of Applicants ");
            int updateId = scan.nextInt();
            scan.nextLine();

            if (updateId < 1 || updateId > 2) {
                System.out.println("Please enter a number between 1 and 2.");
                return false;
            }

            String field = "";
            int answer = -1;

            System.out.println("What would you like this field to up updated to?");
            if (updateId == 1) {
                field = "numOpenSpots";
                answer = scan.nextInt();
            } else if (updateId == 2) {
                field = "numApplicants";
                answer = scan.nextInt();
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Competition SET ?=? WHERE jobId=?;");
            updateIntField(pstJ, field, answer, jobId);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }

    /**
     * Updates a table, where the user selected FULL TIME.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateFullTime(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID of the Full Time Job you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();

            System.out.println("The current information for this Job is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM FullTime WHERE jobId=?;");
            pstM.clearParameters();
            pstM.setInt(1, jobId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                System.out.println("Job ID: " + rs.getInt(1)+ " Full Time Number of Stock Options: " + rs.getInt(2) + " Singing Bonus: " + rs.getFloat(3) + " Salary: " + rs.getFloat(4));
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

            PreparedStatement pstJ = con.prepareStatement("UPDATE FullTime SET ?=? WHERE jobId=?;");
            if (intAnswer != -1)
            {
                updateIntField(pstJ, field, intAnswer, jobId);
            }
            if (floatAnswer != -1)
            {
                updateFloatField(pstJ, field, floatAnswer, jobId);
            }
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
    public static boolean updateInternship(Connection con, Scanner scan)
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
                System.out.println("Job ID: " + rs.getInt(1)+ "Internship Pay Period: " + rs.getString(2) + " Salary: " + rs.getFloat(3) + " Season: " + rs.getString(4));

            }

            System.out.println("Which field would you like to update? \n1. Pay Period \n2. Salary \n3. Season");
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
                field = "salary";
                floatAnswer = scan.nextFloat();
            }
            else if(updateId == 3)
            {
                field = "season";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Internship SET ?=? WHERE jobId=?;");
            if (!answer.equals(""))
            {
                updateStringField(pstJ, field, answer, jobId);
            }
            else if (floatAnswer != -1)
            {
                updateFloatField(pstJ, field, floatAnswer, jobId);
            }
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
    public static boolean updateJob(Connection con, Scanner scan)
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
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                        + " Type: " + rs.getString(7));
            }

            System.out.println("Which field would you like to update? \n1. Job Title \n2. Industry \n3. Description");
            System.out.println("Company ID, Manager ID, and Type are not an allowed to be updated.");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 3)
            {
                System.out.println("Please enter a number between 1 and 3.");
                return false;
            }

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
                field = "description";
                answer = scan.nextLine();

                if(!inputCheck(answer, 100)) return false;
            }
            else if(updateId == 3)
            {
                field = "industry";
                answer = scan.nextLine();

                if(!inputCheck(answer, 25)) return false;
            }

            PreparedStatement pstJ = con.prepareStatement("UPDATE Job SET ?=? WHERE jobId=?;");
            updateStringField(pstJ, field, answer, jobId);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected LOCATION.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateLocation(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID of the Company you would like to update the Location of?");
            int companyId = scan.nextInt();
            scan.nextLine();

            System.out.println("The current information for this Company's Location is: ");
            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Company WHERE companyId=?;");
            pstM.clearParameters();
            pstM.setInt(1, companyId);
            ResultSet rs = pstM.executeQuery();
            while(rs.next())
            {
                System.out.println("Company ID: " + rs.getInt(1)+ "Location Area: " + rs.getString(2) + " Address: " + rs.getString(3) + " " + rs.getString(4) + "\n");
            }

            System.out.println("Which field would you like to update? \n1. Location Area \n2. Street Address \n3. City \n4. State");
            int updateId = scan.nextInt();
            scan.nextLine();

            if(updateId < 1 || updateId > 4)
            {
                System.out.println("Please enter a number between 1 and 4.");
                return false;
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

            PreparedStatement pstL = con.prepareStatement("UPDATE Location SET ?=? WHERE companyId=?;");
            updateStringField(pstL, field, answer, companyId);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Updates a table, where the user selected MANAGER.
     * @return true if the update was successful, false otherwise
     */
    public static boolean updateManager(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID of the Manager you would like to update?");
            int managerId = scan.nextInt();
            scan.nextLine();

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
                System.out.println("Manager ID:" + rs.getInt(1) + " Manager's Name: " + rs.getString(2) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(4));
            }

            System.out.println("Which field would you like to update? \n1.Manager's Name \n2. Technical Experience \n3. Years at Company");
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

            PreparedStatement pstRJ = con.prepareStatement("UPDATE Manager SET ?=? WHERE managerId=?;");
            updateStringField(pstRJ, field, answer, managerId);
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
    public static boolean updateRelatedJobs(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("What is the ID of the Job's Related Jobs you would like to update?");
            int jobId = scan.nextInt();
            scan.nextLine();

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

            PreparedStatement pstRJ = con.prepareStatement("UPDATE RelatedJobs SET ?=? WHERE jobId=?;");
            updateIntField(pstRJ, updateString, relatedJob, jobId);
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
            return false;
        }
        return true;
    }


    //search tables
    /**
     * Searchs a table, where the user selected LOCATION.
     * @return true if there were no issues, false otherwise
     */
    public static boolean searchLocation(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("Please enter the Location Area you want to search for");
            String location = scan.nextLine();

            if (location.length() > 25)
            {
                System.out.println("Location Area needs to be 25 characters or less. Please try again.");
                return false;
            }
            else
            {
                System.out.println("The Job Database in that Location Area:\n");
                PreparedStatement pst8 = con.prepareStatement("select j.jobTitle, j.industry, j.description, j.companyId, j.managerId, j.type from Job j, Location l WHERE j.companyId = l.companyId AND l.locationArea=?;");
                pst8.clearParameters();
                pst8.setString(1, location);
                ResultSet rs = pst8.executeQuery();

                boolean whichType = true;
                float averageSalary = 0;
                while (rs.next())
                {
                    String type = "Full Time";
                    whichType = rs.getBoolean(7);
                    if(whichType)
                    {
                        type = "Internship";
                    }
                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: "
                            + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6) + " Type: " + type);
                    averageSalary = rs.getFloat(8);
                }

                System.out.println("\nSome Statistics for that Location Area:\n");
                PreparedStatement pst8Stat = con.prepareStatement("select COUNT(*) from Location l;");
                ResultSet rs8Stat = pst8Stat.executeQuery();
                while (rs8Stat.next())
                {
                    System.out.println("Number of Companies in the Area: " + rs8Stat.getInt(1) + " Avergae Salary: " + averageSalary);
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
                if(rs.getBoolean(7))
                {
                    type = "Internship";
                }
                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                        + " Type: " + type);
            }

            System.out.println("\nSome Statistics from " + name + ":");
            PreparedStatement pst6Stat = con.prepareStatement("SELECT COUNT(*), AVG(c.numApplicants) FROM Job j, Competition c WHERE j.companyId =? AND c.jobId = j.jobId;");
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

            if (type.length() > 1 || (!type.toLowerCase().equals("i") && !type.toLowerCase().equals("f")))
            {
                System.out.println("Type needs to be of length 1 (I or F). Please try again.");
                return false;
            }
            else
            {
                if(type.equals("f"))
                {
                    search = false;
                }

                System.out.println("The Job Database of that Type:\n");
                PreparedStatement pst7 = con.prepareStatement("SELECT * FROM Job WHERE type=?;");
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
                    System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                            + " Type: " + type);
                }

                if(!search)
                {
                    System.out.println("\nSome Statistics on Full Time Jobs:");
                    PreparedStatement pstFTypeStat = con.prepareStatement("SELECT AVG (salary), Max (salary), AVG (signingBonus) FROM FullTime;");
                    ResultSet rsFTypeStat = pstFTypeStat.executeQuery();
                    while (rsFTypeStat.next())
                    {
                        System.out.println("Average Salary: " + rsFTypeStat.getFloat(1) + " Highest Salary: " + rsFTypeStat.getFloat(2) + " Average SigningBonus: " + rsFTypeStat.getFloat(3));
                    }
                }
                else if(search)
                {
                    System.out.println("\nSome Statistics on Internships:");
                    PreparedStatement pstI1TypeStat = con.prepareStatement("SELECT AVG (salary), MAX (salary) FROM Internship;");
                    ResultSet rsI1TypeStat = pstI1TypeStat.executeQuery();
                    PreparedStatement pstI2TypeStat = con.prepareStatement("SELECT COUNT(*) FROM Internship WHERE salary = 0;");
                    ResultSet rsI2TypeStat = pstI2TypeStat.executeQuery();
                    while (rsI1TypeStat.next() || rsI2TypeStat.next())
                    {
                        System.out.println("Average Salary: " + rsI1TypeStat.getFloat(1) + " Highest Salary: " + rsI1TypeStat.getFloat(2) + " Number of Unpaid Internships: " + rsI2TypeStat.getInt(1));
                    }

                    System.out.println("\nSummer Internships: ");
                    PreparedStatement pstSum = con.prepareStatement("SELECT jobId, jobTitle FROM Job WHERE jobId IN (SELECT jobId FROM Internship WHERE season='summer');");
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

    /**
     * Returns statistics for certain records in the database.
     * @param Takes Connection and Scanner as input to assist in executing the SQL commands.
     * @return true if there were no issues, false otherwise
     */
    public static boolean statistics(Connection con, Scanner scan)
    {
        try
        {
            System.out.println("Statistic Categories: \n");

            System.out.println("\nAverage Internship Salaries by Location: ");
            PreparedStatement pstLI = con.prepareStatement("SELECT l.locationArea, COUNT(*), AVG(i.salary)FROM Location l, Internship i, Job j WHERE j.companyId = l.companyId AND i.jobId = j.jobId GROUP BY l.locationArea;");
            ResultSet rsLocI = pstLI.executeQuery();
            while(rsLocI.next())
            {
                System.out.println("Location Area: " + rsLocI.getString(1) + " Number of Jobs: " + rsLocI.getInt(2) + " Average Salary: " + rsLocI.getFloat(3));
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

            System.out.println("\nAverage Competition by Company: ");
            PreparedStatement pstComp = con.prepareStatement("SELECT j.companyId, AVG(c.numOpenSpots), AVG(c.numApplicants) FROM Competition c, Job j WHERE j.jobId = c.jobId GROUP BY j.companyId;");
            ResultSet rsComp = pstComp.executeQuery();
            while(rsComp.next())
            {
                System.out.println("Company: " + rsComp.getString(1) + " Average Open Spots: " + rsComp.getFloat(2) + " Average Applicants: " + rsComp.getFloat(3));
            }

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong. Please enter this again.");
        }
        return false;
    }

    //this method is the old ugly delete in case we still need it
    public static boolean deleteCallOLD(Connection con, Scanner scan)
    {
        //@TODO should be updated to use deleteEntry method
        int jobId;

        try
        {
            System.out.println("Please enter the Job's Id");
            jobId = scan.nextInt();
            scan.nextLine();

            PreparedStatement pstCall = con.prepareStatement("SELECT ? FROM ? WHERE ?=?");
            pstCall.clearParameters();
            pstCall.setString(1, "companyId");
            pstCall.setString(2, "Job");
            pstCall.setString(3, "jobId");
            pstCall.setInt(4, jobId);
            ResultSet rs = pstCall.executeQuery();
            int companyId = rs.getInt(1);

            pstCall.clearParameters();
            pstCall.setString(1, "managerId");
            pstCall.setString(2, "Job");
            pstCall.setString(3, "jobId");
            pstCall.setInt(4, jobId);
            rs = pstCall.executeQuery();
            int managerId = rs.getInt(1);

            pstCall.clearParameters();
            pstCall.setString(1, "type");
            pstCall.setString(2, "Job");
            pstCall.setString(3, "jobId");
            pstCall.setInt(4, jobId);
            rs = pstCall.executeQuery();
            boolean type = rs.getBoolean(1);


            PreparedStatement pst5 = con.prepareStatement("DELETE FROM ? WHERE ?=?");
            //@TODO should be modified to use deleteEntry method
            deleteEntry(pst5, "Company", "companyId", companyId);
            // pst5.clearParameters();
            // pst5.setString(1, "Company");
            // pst5.setString(2, "companyId");
            // pst5.setInt(3, companyId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Company.");

            deleteEntry(pst5, "Competition", "jobId", jobId);
            // pst5.clearParameters();
            // pst5.setString(1, "Competition");
            // pst5.setString(2, "jobId");
            // pst5.setInt(3, jobId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Competition.");

            deleteEntry(pst5, "Job", "jobId", jobId);
            // pst5.clearParameters();
            // pst5.setString(1, "Job");
            // pst5.setString(2, "jobId");
            // pst5.setInt(3, jobId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Job.");

            deleteEntry(pst5, "Location", "companyId", companyId);
            // pst5.clearParameters();
            // pst5.setString(1, "Location");
            // pst5.setString(2, "companyId");
            // pst5.setInt(3, companyId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Location.");

            deleteEntry(pst5, "Manager", "managerId", managerId);
            // pst5.clearParameters();
            // pst5.setString(1, "Manager");
            // pst5.setString(2, "managerId");
            // pst5.setInt(3, managerId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Manager.");

            deleteEntry(pst5, "RelatedJobs", "jobId", jobId);
            // pst5.clearParameters();
            // pst5.setString(1, "RelatedJobs");
            // pst5.setString(2, "jobId");
            // pst5.setInt(3, jobId);
            // pst5.executeUpdate();
            // System.out.println("The record has been deleted from Related Jobs.");

            if(type)
            {
                deleteEntry(pst5, "Internship", "jobId", jobId);
                // pst5.clearParameters();
                // pst5.setString(1, "Internship");
                // pst5.setString(2, "jobId");
                // pst5.setInt(3, jobId);
                // pst5.executeUpdate();
                // System.out.println("The record has been deleted from Internship.");
            }
            else if(!type)
            {
                deleteEntry(pst5, "FullTime", "jobId", jobId);
                // pst5.clearParameters();
                // pst5.setString(1, "FullTime");
                // pst5.setString(2, "jobId");
                // pst5.setInt(3, jobId);
                // pst5.executeUpdate();
                // System.out.println("The record has been deleted from Full Time.");
            }

            System.out.println("The record has been completely deleted.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input.");
        }
        return false;
    }


    //create new entry methods
    /**
     * Gathers information for the Company table.
     * @param validInput to account for all input issues.
     * @param pstC is the Prepared Statement for the Company table.
     * @param pstL is the Prepared Statement for the Location table.
     * @param pstId is the Prepared Statement for getting the Id number.
     * @param companyId is the to get companyId not local to this method.
     * @return true if the creation was successful, false otherwise.
     */
    public static boolean createCompany(boolean validInput, PreparedStatement pstC, PreparedStatement pstL, Scanner scan)
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

            if (companyName.equals("") || inputCheck(companyName, 100))
            {
                validInput = false;
            }

            if(!validInput)
            {
                System.out.println("Please enter this information again.");
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

            if(!inputCheck(street,100) || !inputCheck(locationArea, 25) || !inputCheck(city, 25))
            {
                validInput = false;
            }
            if(state.length() != 2)
            {
                System.out.println("The State must be 2 characters. Please try again.");
                validInput = false;
            }

            if(validInput == false)
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
     * Gathers information for the Manager table.
     * @param validInput to account for all input issues.
     * @param pstM is the Prepared Statement for the Manager table.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createManager(boolean validInput, PreparedStatement pstM, Scanner scan)
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

            pstM.clearParameters();
            pstM.setString(1, name);
            pstM.setBoolean(2, technicalExperience);
            pstM.setInt(3, yearsAtCompany);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Please enter a valid input. Try again.");
            return false;
        }
    }

    /**
     * Gathers information for the company field.
     * @param validInput to account for all input issues.
     * @param pstJ is the Prepared Statement for the Job table.
     * @param pstComp is the Prepared Statement for the Competition table.
     * @param createCompany is the boolean whether to ask for companyId or not.
     * @param createManager is the boolean whether to ask for managerId or not.
     * @param type is the boolean that needs to not be a local variable.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createJob(boolean validInput, PreparedStatement pstJ, PreparedStatement pstComp, boolean type, Scanner scan, Connection con)
    {
        try
        {
            String jobTitle = "";
            String industry = "";
            String description = "";
            String typeS = "";
            int companyId = 0;
            int managerId = 0;

            System.out.println("Enter the Job's Title (length 25)");
            jobTitle = scan.nextLine();
            System.out.println("Enter the Job's Industry (length 25)");
            industry = scan.nextLine();
            System.out.println("Enter the Job's Description (length 100)");
            description = scan.nextLine();

            System.out.println("Enter the Job's Type (I/F)");
            typeS = scan.nextLine();

            if(typeS.toLowerCase().equals("f"))
            {
                type = false;
            }
            else if(typeS.toLowerCase().equals("i"))
            {
                type = true;
            }
            else
            {
                System.out.println("Type needs to be 1 character only, I or F. Please try again.");
                validInput = false;
            }

            if(!inputCheck(description, 100) || !inputCheck(jobTitle, 25) || !inputCheck(industry, 25))
            {
                validInput = false;
            }
            if(jobTitle.equals("") || industry.equals("") || description.equals("") || typeS.equals(""))
            {
                System.out.println("All fields must be entered.");
                validInput = false;
            }

            if(!validInput)
            {
                return false;
            }

            pstJ.clearParameters();
            pstJ.setString(2, jobTitle);
            pstJ.setString(3, industry);
            pstJ.setString(4, description);
            pstJ.setBoolean(7, type);

            int numOpenSpots;
            int numApplicants;

            System.out.println("Enter the Job's Number of Open Spots");
            numOpenSpots = scan.nextInt();
            scan.nextLine();
            System.out.println("Enter the Job's Number of Applicants");
            numApplicants = scan.nextInt();
            scan.nextLine();

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
     * Gathers information for the company field.
     * @param validInput to account for all input issues.
     * @param pstR is the Prepared Statement for the Related Jobs table.
     * @param related is the boolean for whether to add to the Related Jobs table or not.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createRelated(boolean validInput, PreparedStatement pstR, boolean related, Scanner scan)
    {
        try
        {
            System.out.println("Does the job you are creating have any related jobs? Enter 'Y' for yes.");
            String relatedMaybe = scan.nextLine();

            int related1 = 0;
            int related2 = 0;
            int related3 = 0;
            int related4 = 0;
            int related5 = 0;

            if(relatedMaybe.toLowerCase().equals("y"))
            {
                System.out.println("How many? Enter a number between 1 and 5.");
                int numOfRelated = scan.nextInt();

                if(numOfRelated < 1 || numOfRelated > 5)
                {
                    System.out.println("Please enter a please between 1 and 5. Try again.");
                    validInput = false;
                    return false;
                }

                for(int i = 0; i < numOfRelated; i++)
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
                } // fix this
            }
            else
            {
                related = false;
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
     * @param validInput to account for all input issues.
     * @param pstF is the Prepared Statement for the Full Time table.
     * @param pstI is the Prepared Statement for the Internship table.
     * @param type is the boolean set in the Job field on whether it is full time or internship.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean createType(boolean validInput, PreparedStatement pstF, PreparedStatement pstI, boolean type, Scanner scan)
    {
        try
        {
            int stockOptions = 0;
            float signingBonus = 0;
            String payPeriod = "";
            float salary = 0;
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
                System.out.println("Enter the Job's salary");
                salary = scan.nextFloat();
                scan.nextLine();
                System.out.println("Enter the Job's Season (length 10)");
                season = scan.nextLine();

                if(!inputCheck(payPeriod, 10) || !inputCheck(season, 10))
                {
                    return false;
                }

                pstI.setString(2, payPeriod);
                pstI.setFloat(3, salary);
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
     * Creates an external file of all entries in the database
     * @param type is the boolean set in the Job field on whether it is full time or internship.
     * @return true if the creation was successful, false otherwise
     */
    public static boolean generateReport(Connection con)
    {
        try
        {
            //generate a report
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong.");
        }
        return false;
    }
    /**
     * Undos the last user change, restores previous verion.
     * @return true if there were no issues, false otherwise
     */
    public static boolean undo(Connection con)
    {
        try
        {
            //pstUndo.execute();
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong.");
        }
        return false;
    }

    /**
    * Gets info from the GUI about job components.
    * @TODO figure out what we're doing with said data.
    */
    public static void getJobInfo(String jobTitle, String industry, String description, int companyId, int managerId, String type)
    {

    }
}
