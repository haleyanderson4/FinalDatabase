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

import java.sql.*;
import java.util.*;

public class main
{
    public static void main(String[] args)
    {
        Connection con; //database connection
        Scanner scan = new Scanner(System.in);
        int editOption = 0;

        try {
            con = Config.getMySqlConnection(); //connect to database
            boolean loop = true;
            System.out.println("Options on what to do: \n1. Display all Jobs \n2. Add a new Job Posting \n3. Update a Job Posting \n4. Remove a Job \n5. Search by Location, Company, or Type \n6. Find All Info for a Job \n7. Get Select Info for a Job \n8. Quit\n");

            while(loop)
            {
                try
                {
                    System.out.println("What would you like to do: \n");
                    editOption = scan.nextInt();

                    if (editOption < 1 || editOption > 8)
                    {
                        System.out.println("Please enter a number between 1 and 8\n");
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Please enter a number between 1 and 8\n");
                    scan.nextLine();
                }

                if(editOption == 1) //prelim completed
                {
                    System.out.println("The Job Database:");
                    PreparedStatement pst1 = con.prepareStatement("select * from Job");
                    ResultSet rs = pst1.executeQuery();
                    while(rs.next())
                    {
                        System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                + " Type: " + rs.getString(7));
                    }
                    System.out.println("");
                }

                if(editOption == 2) //prelim completed
                {
                    boolean result = createNewPosting(con, scan);
                    if(result)
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
                    try
                    {
                        System.out.println("What would you like to update?");
                        System.out.println("1.Company Information \n2. Competition Information \n3. Full Time Information \n4. Internship Information \n5. Job Information \n6. Location Information \n7. Manager Information \n8. Related Job Information");
                        int selectOption = scan.nextInt();
                        scan.nextLine();

                        if(selectOption == 1)
                        {
                            System.out.println("What is the ID of the Company you would like to update?");
                            int companyId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Company is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Company WHERE companyId=?");
                            pstM.clearParameters();
                            pstM.setInt(1, companyId);
                            ResultSet rs = pstM.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Company ID: " + rs.getInt(1) + " Company Name: " + rs.getString(2) + " Number of Employees: " + rs.getInt(3) + " Yearly Revenue: " + rs.getFloat(4) + " Stock Price: " + rs.getFloat(5));
                            }

                            System.out.println("Which field would you like to update? \n1. Company Name \n2. Number of Employees \n3. Yearly Revenue \n4. Stock Price");
                            int updateId = scan.nextInt();
                            scan.nextLine();

                            if(updateId < 1 || updateId > 4)
                            {
                                System.out.println("Please enter a number between 1 and 4.");
                                continue;
                            }

                            String field = "";
                            String answer = "";
                            int intAnswer = -1;
                            float floatAnswer = -1;

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "companyName";
                                answer = scan.nextLine();
                                if (!inputCheck(answer, 25)){ continue; }
                            }
                            else if(updateId == 2)
                            {
                                field = "numEmployees";
                                intAnswer = scan.nextInt();
                            }
                            else if(updateId == 3)
                            {
                                field = "yearlyRevenue";
                                floatAnswer = scan.nextFloat();
                            }
                            else if(updateId == 4)
                            {
                                field = "stockPrice";
                                floatAnswer = scan.nextFloat();
                            }

                            PreparedStatement pstJ = con.prepareStatement("UPDATE Company SET ?=? WHERE companyId=?");
                            if (!answer.equals(""))
                                updateStringField(pstJ, field, answer, companyId);
                            else if (intAnswer != -1)
                                updateIntField(pstJ, field, intAnswer, companyId);
                            else if (floatAnswer != -1)
                                updateFloatField(pstJ, field, floatAnswer, companyId);
                        }
                        else if(selectOption == 2)
                        {
                            System.out.println("What is the ID of the Job you would like to update the Competition to?");
                            int jobId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Job's Competition is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Competition WHERE jobId=?");
                            pstM.clearParameters();
                            pstM.setInt(1, jobId);
                            ResultSet rs = pstM.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1)+ " Number of Stock Options: " + rs.getInt(2) + " Singing Bonus: " + rs.getFloat(3));
                            }

                            System.out.println("Which field would you like to update? \n1. Number of Open Spots \n2. Number of Applicants ");
                            int updateId = scan.nextInt();
                            scan.nextLine();

                            if(updateId < 1 || updateId > 2)
                            {
                                System.out.println("Please enter a number between 1 and 2.");
                                continue;
                            }

                            String field = "";
                            int answer = -1;

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "numOpenSpots";
                                answer = scan.nextInt();
                            }
                            else if(updateId == 2)
                            {
                                field = "numApplicants";
                                answer = scan.nextInt();
                            }

                            PreparedStatement pstJ = con.prepareStatement("UPDATE Competition SET ?=? WHERE jobId=?");
                            updateIntField(pstJ, field, answer, jobId);
                        }
                        else if(selectOption == 3)
                        {
                            System.out.println("What is the ID of the Full Time Job you would like to update?");
                            int jobId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Job is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM FullTime WHERE jobId=?");
                            pstM.clearParameters();
                            pstM.setInt(1, jobId);
                            ResultSet rs = pstM.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1)+ " Full Time Number of Stock Options: " + rs.getInt(2) + " Singing Bonus: " + rs.getFloat(3));
                            }

                            System.out.println("Which field would you like to update? \n1. Number of Stock Options \n2. Signing Bonus ");
                            int updateId = scan.nextInt();
                            scan.nextLine();

                            if(updateId < 1 || updateId > 2)
                            {
                                System.out.println("Please enter a number between 1 and 2.");
                                continue;
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

                            PreparedStatement pstJ = con.prepareStatement("UPDATE FullTime SET ?=? WHERE jobId=?");
                            if (intAnswer != -1)
                                updateIntField(pstJ, field, intAnswer, jobId);
                            if (floatAnswer != -1)
                                updateFloatField(pstJ, field, floatAnswer, jobId);
                        }
                        else if(selectOption == 4) //@TODO update answer to int/float
                        {
                            System.out.println("What is the ID of the Job you would like to update?");
                            int jobId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Job is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Job WHERE jobId=?");
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
                                continue;
                            }

                            String field = "";
                            String answer = "";
                            float floatAnswer = -1;

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "payPeriod";
                                answer = scan.nextLine();
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
                            }

                            PreparedStatement pstJ = con.prepareStatement("UPDATE Internship SET ?=? WHERE jobId=?");
                            if (!answer.equals(""))
                                updateStringField(pstJ, field, answer, jobId);
                            else if (floatAnswer != -1)
                                updateFloatField(pstJ, field, floatAnswer, jobId);
                        }
                        else if(selectOption == 5)
                        {
                            System.out.println("What is the ID of the Job you would like to update?");
                            int jobId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Job is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Job WHERE jobId=?");
                            pstM.clearParameters();
                            pstM.setInt(1, jobId);
                            ResultSet rs = pstM.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                        + " Type: " + rs.getString(7));
                            }

                            System.out.println("Which field would you like to update? \n1. Job Title \n2. Industry \n3. Description \n4. Type");
                            System.out.println("Company ID and Manager ID are not an updatable field.");
                            int updateId = scan.nextInt();
                            scan.nextLine();

                            if(updateId < 1 || updateId > 4)
                            {
                                System.out.println("Please enter a number between 1 and 4.");
                                continue;
                            }

                            String field = "";
                            String answer = "";

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "jobTitle";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 2)
                            {
                                field = "description";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 3)
                            {
                                field = "industry";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 4)
                            {
                                field = "type";
                                answer = scan.nextLine();
                            }

                            PreparedStatement pstJ = con.prepareStatement("UPDATE Job SET ?=? WHERE jobId=?");
                            updateStringField(pstJ, field, answer, jobId);
                        }
                        else if(selectOption == 6)
                        {
                            System.out.println("What is the ID of the Company you would like to update the Location of?");
                            int companyId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Company's Location is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Company WHERE companyId=?");
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
                                continue;
                            }

                            String field = "";
                            String answer = "";

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "locationArea";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 2)
                            {
                                field = "street";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 3)
                            {
                                field = "city";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 4)
                            {
                                field = "state";
                                answer = scan.nextLine();
                            }

                            PreparedStatement pstL = con.prepareStatement("UPDATE Manager SET ?=? WHERE companyId=?");
                            updateStringField(pstL, field, answer, companyId);
                        }
                        else if(selectOption == 7)
                        {
                            System.out.println("What is the ID of the Manager you would like to update?");
                            int managerId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Manager is: ");
                            PreparedStatement pstM = con.prepareStatement("SELECT * FROM Manager WHERE managerId=?");
                            pstM.clearParameters();
                            pstM.setInt(1, managerId);
                            ResultSet rs = pstM.executeQuery();
                            while(rs.next())
                            {
                                String techBool = "No";
                                if(rs.getBoolean(4))
                                {
                                    techBool = "Yes";
                                }
                                System.out.println("Manager ID:" + rs.getInt(1) + " Manager's Name: " + rs.getString(2) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(4));
                            }

                            System.out.println("Which field would you like to update? \n1.Manager's Name \n2. Technical Experience \n3. Years at Company");
                            int updateId = scan.nextInt();
                            scan.nextLine();

                            if(updateId < 1 || updateId > 3)
                            {
                                System.out.println("Please enter a number between 1 and 3.");
                                continue;
                            }

                            String field = "";
                            String answer = "";

                            System.out.println("What would you like this field to up updated to?");
                            if(updateId == 1)
                            {
                                field = "name";
                                answer = scan.nextLine();
                            }
                            else if(updateId == 2)
                            {
                                answer = "false";
                                System.out.println("Enter 'Y' for yes");
                                if(scan.nextLine() == "Y" || scan.nextLine() == "y")
                                {
                                    answer = "true";
                                }
                                field = "technicalExperience";
                            }
                            else if(updateId == 3)
                            {
                                field = "yearsAtCompany";
                                answer = "" + scan.nextInt();
                                scan.nextLine();
                            }

                            PreparedStatement pstRJ = con.prepareStatement("UPDATE Manager SET ?=? WHERE managerId=?");
                            updateStringField(pstRJ, field, answer, managerId);
                        }
                        else if(selectOption == 8)
                        {
                            System.out.println("What is the ID of the Job's Related Jobs you would like to update?");
                            int jobId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The current information for this Job is: ");
                            PreparedStatement pst7 = con.prepareStatement("SELECT * FROM RelatedJobs WHERE jobId=?");
                            pst7.clearParameters();
                            pst7.setInt(1, jobId);
                            ResultSet rs = pst7.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1)+ " Related Job 1: " + rs.getInt(2) + "Related Job 2: " + rs.getInt(3) + "Related Job 3: " + rs.getInt(4) + "Related Job 4: " + rs.getInt(5) + "Related Job 5: " + rs.getInt(6));
                            }

                            System.out.println("Which Field would you like to update? \n1. Related Job 1 \n2. Related Job 2 \n3. Related Job 3 \n4. Related Job 4 \n5. Related Job 5");
                            int updateId = scan.nextInt();
                            scan.nextLine();
                            String updateString = "related" + updateId;

                            if(updateId < 1 || updateId > 5)
                            {
                                System.out.println("Please enter a number between 1 and 5.");
                                continue;
                            }

                            System.out.println("What would you like this field to up updated to?");
                            int relatedJob = scan.nextInt();
                            scan.nextLine();

                            PreparedStatement pstRJ = con.prepareStatement("UPDATE RelatedJobs SET ?=? WHERE jobId=?");
                            updateIntField(pstRJ, updateString, relatedJob, jobId);
                        }
                        else
                        {
                            System.out.println("Please enter a number between 1 and 8.");
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Please enter a valid input.");
                    }
                }

                if(editOption == 4) //prelim completed
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
                        String type = rs.getString(1);


                        PreparedStatement pst5 = con.prepareStatement("DELETE FROM ? WHERE ?=?");
                        //@TODO should be modified to use deleteEntry method
                        pst5.clearParameters();
                        pst5.setString(1, "Company");
                        pst5.setString(2, "companyId");
                        pst5.setInt(3, companyId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Company.");

                        pst5.clearParameters();
                        pst5.setString(1, "Competition");
                        pst5.setString(2, "jobId");
                        pst5.setInt(3, jobId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Competition.");

                        pst5.clearParameters();
                        pst5.setString(1, "Job");
                        pst5.setString(2, "jobId");
                        pst5.setInt(3, jobId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Job.");

                        pst5.clearParameters();
                        pst5.setString(1, "Location");
                        pst5.setString(2, "companyId");
                        pst5.setInt(3, companyId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Location.");

                        pst5.clearParameters();
                        pst5.setString(1, "Manager");
                        pst5.setString(2, "managerId");
                        pst5.setInt(3, managerId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Manager.");

                        pst5.clearParameters();
                        pst5.setString(1, "RelatedJobs");
                        pst5.setString(2, "jobId");
                        pst5.setInt(3, jobId);
                        pst5.executeUpdate();
                        System.out.println("The record has been deleted from Related Jobs.");

                        if(type == "I")
                        {
                            pst5.clearParameters();
                            pst5.setString(1, "Internship");
                            pst5.setString(2, "jobId");
                            pst5.setInt(3, jobId);
                            pst5.executeUpdate();
                            System.out.println("The record has been deleted from Internship.");
                        }
                        else if(type == "F")
                        {
                            pst5.clearParameters();
                            pst5.setString(1, "FullTime");
                            pst5.setString(2, "jobId");
                            pst5.setInt(3, jobId);
                            pst5.executeUpdate();
                            System.out.println("The record has been deleted from Full Time.");
                        }

                        System.out.println("The record has been completely deleted.");
                    }
                    catch (Exception e)
                    {
                        System.out.println("Please enter a valid input.");
                    }
                }

                if(editOption == 5) //prelim done
                {
                    int searchOption;

                    try
                    {
                        System.out.println("What would you like to search by? \n1. Location \n2. Company \n3. Type");

                        searchOption = scan.nextInt();
                        scan.nextLine();

                        if(searchOption == 1)
                        {
                            System.out.println("Please enter the Location Area you want to search for");
                            String location = scan.nextLine();

                            if (location.length() > 25)
                            {
                                System.out.println("Location Area needs to be 25 characters or less. Please try again.");
                            }
                            else
                            {
                                System.out.println("The Job Database in that Location Area:\n");
                                PreparedStatement pst8 = con.prepareStatement("select * from Job j, Company c, Location l WHERE j.companyId = l.companyId AND l.locationArea=?");
                                pst8.clearParameters();
                                pst8.setString(1, location);
                                ResultSet rs = pst8.executeQuery();
                                while (rs.next())
                                {
                                    System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getFloat(4) + " " + rs.getString(5) + " " + rs.getString(6));
                                }
                            }
                        }
                        else if(searchOption == 2)
                        {
                            System.out.println("Please enter the ID of the Company you want to search for");
                            int companyId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("The Job Database of that Company:\n");
                            PreparedStatement pst6 = con.prepareStatement("select * from Job WHERE companyId =?");
                            pst6.clearParameters();
                            pst6.setInt(1, companyId);
                            ResultSet rs = pst6.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                        + " Type: " + rs.getString(7));
                            }
                        }
                        else if(searchOption == 3)
                        {

                            System.out.println("Please enter the Type of Job you want to search for (I/F)");
                            String type = scan.nextLine();

                            if (type.length() > 1 || (type != "I" && type != "F"))
                            {
                                System.out.println("Type needs to be of length 1 (I or F). Please try again.");
                            }
                            else
                            {
                                System.out.println("The Job Database of that Type:\n");
                                PreparedStatement pst7 = con.prepareStatement("select * from Job WHERE type=?");
                                pst7.clearParameters();
                                pst7.setString(1, type);
                                ResultSet rs = pst7.executeQuery();
                                while(rs.next())
                                {
                                    System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: "+ rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                            + " Type: " + rs.getString(7));
                                }
                            }
                        }
                        else
                        {
                            System.out.println("Please enter a number between 1 and 3.");
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Please enter a valid input.");
                    }

                }

                if(editOption == 6) //prelim completed
                {
                    try
                    {
                        System.out.println("What is the ID number of the job you are looking for?");
                        int jobId = scan.nextInt();
                        scan.nextLine();

                        PreparedStatement pstType = con.prepareStatement("SELECT type FROM Job WHERE jobId=?");
                        pstType.setInt(1, jobId);
                        ResultSet rsType = pstType.executeQuery();
                        String type = rsType.getString(1);

                        if (type == "F")
                        {
                            PreparedStatement pst8F = con.prepareStatement("SELECT * FROM Job j, Company c, Competition co, Location l, Manager m, FullTime f, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?");
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
                                        + " Type: " + rs.getString(7)
                                        + "\nCompany Name: " + rs.getString(9) + " Number of Employees: " + rs.getInt(10) + " Yearly Revenue: " + rs.getFloat(11) + " Stock Price: " + rs.getFloat(12)
                                        + "\nLocation Area: " + rs.getString(14) + " Address: " + rs.getString(15) + " " + rs.getString(16)
                                        + "\nManager's Name: " + rs.getString(18) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(20)
                                        + "\n Full Time Salary: N/A" + "\nNumber of Stock Options: " + rs.getInt(22) + " Signing Bonus: " + rs.getFloat(23)
                                        + "\nRelated Job 1: " + rs.getInt(25) + "Related Job 2: " + rs.getInt(26) + "Related Job 3: " + rs.getInt(27) + "Related Job 4: " + rs.getInt(28) + "Related Job 5: " + rs.getInt(29));
                            }
                        }
                        if (type == "I") {
                            PreparedStatement pst8I = con.prepareStatement("SELECT * FROM Job j, Company c, Competition co, Location l, Manager m, Internship i, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?");
                            pst8I.clearParameters();
                            pst8I.setInt(1, jobId);
                            ResultSet rs = pst8I.executeQuery();
                            while (rs.next()) {
                                String techBool = "No";
                                if (rs.getBoolean(19)) {
                                    techBool = "Yes";
                                }

                                System.out.println("Job ID: " + rs.getInt(1) + " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                        + " Type: " + rs.getString(7)
                                        + "\nCompany Name: " + rs.getString(9) + " Number of Employees: " + rs.getInt(10) + " Yearly Revenue: " + rs.getFloat(11) + " Stock Price: " + rs.getFloat(12)
                                        + "\nLocation Area: " + rs.getString(14) + " Address: " + rs.getString(15) + " " + rs.getString(16)
                                        + "\nManager's Name: " + rs.getString(18) + " Technical Experience: " + techBool + " Years at Company: " + rs.getInt(20)
                                        + "\nInternship Pay Period: " + rs.getString(22) + " Salary: " + rs.getFloat(23) + " Season: " + rs.getString(24)
                                        + "\nRelated Job 1: " + rs.getInt(26) + "Related Job 2: " + rs.getInt(27) + "Related Job 3: " + rs.getInt(28) + "Related Job 4: " + rs.getInt(29) + "Related Job 5: " + rs.getInt(30));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Please enter a valid ID number. Try again.");
                    }
                }

                if(editOption == 7) //prelim completed
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
                            PreparedStatement pst1 = con.prepareStatement("SELECT * FROM Job j, Company c WHERE j.companyId=c.companyId AND j.jobId=?");
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
                            PreparedStatement pst2 = con.prepareStatement("SELECT * FROM Job j, Competition c WHERE j.jobId=c.jobId AND j.jobId=?");
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
                            PreparedStatement pstType = con.prepareStatement("SELECT type FROM Job WHERE jobId=?");
                            pstType.setInt(1, jobId);
                            ResultSet rsType = pstType.executeQuery();
                            String type = rsType.getString(1);

                            if(type == "F") //add salary
                            {
                                PreparedStatement pstFullTime = con.prepareStatement("SELECT * FROM Job j, FullTime f WHERE f.jobId=j.jobId AND j.jobId=?");
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
                                PreparedStatement pstIntern = con.prepareStatement("SELECT * FROM Job j, Internship i WHERE i.jobId=j.jobId AND j.jobId=?");
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
                            PreparedStatement pst4 = con.prepareStatement("SELECT * FROM Job j WHERE j.jobId=?");
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
                            PreparedStatement pst5 = con.prepareStatement("SELECT * FROM Job j, Location l WHERE j.companyId=l.companyId AND j.jobId=?");
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
                            PreparedStatement pst6 = con.prepareStatement("SELECT * FROM Job j, Manager m WHERE j.managerId=m.managerId AND j.jobId=?");
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
                            PreparedStatement pst7 = con.prepareStatement("SELECT * FROM Job j, RelatedJobs r WHERE j.jobId=r.jobId AND j.jobId=?");
                            pst7.clearParameters();
                            pst7.setInt(1, jobId);
                            ResultSet rs = pst7.executeQuery();
                            while(rs.next())
                            {
                                System.out.println("Job ID: " + rs.getInt(1)+ " Job Title: " + rs.getString(2) + " Industry: " + rs.getString(3) + " Description: " + rs.getString(4) + " Company ID: " + rs.getInt(5) + " Manager ID: " + rs.getInt(6)
                                        + " Type: " + rs.getString(7) + "\nRelated Job 1: " + rs.getInt(9) + "Related Job 2: " + rs.getInt(10) + "Related Job 3: " + rs.getInt(11) + "Related Job 4: " + rs.getInt(12) + "Related Job 5: " + rs.getInt(13) + "\n");
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Please enter a valid input.");
                    }
                }

                if(editOption == 8) //done lol
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


    public static boolean createNewPosting(Connection con, Scanner scan)
    {
        boolean validInput = true;

        try
        {
            int jobId;
            String jobTitle;
            String industry;
            String description;
            int companyId;
            int managerId;
            String type;

            System.out.println("Enter the Job's ID");
            jobId = scan.nextInt();
            scan.nextLine();

            System.out.println("Enter the Job's Title (length 25)");
            jobTitle = scan.nextLine();
            System.out.println("Enter the Job's Industry (length 25)");
            industry = scan.nextLine();
            System.out.println("Enter the Job's Description (length 100)");
            description = scan.nextLine();
            System.out.println("Enter the Job's Company Id");
            companyId = scan.nextInt();
            scan.nextLine();

            System.out.println("Enter the Job's Manager Id");
            managerId = scan.nextInt();
            scan.nextLine();

            System.out.println("Enter the Job's Type (I/F)");
            type = scan.nextLine();

            if (jobTitle.length() > 25 || industry.length() > 25)
            {
                System.out.println("Job Title and Industry need to be 25 characters or less. Please try again.");
                validInput = false;
            }
            if (description.length() > 100)
            {
                System.out.println("The Description needs to be 100 characters or less. Please try again.");
                validInput = false;
            }
            if (type.length() > 1 || (!type.equals("I") && !type.equals("F")))
            {
                System.out.println("Type needs to be 1 character only, I or F. Please try again.");
                validInput = false;
            }
            if(jobTitle.equals("") || industry.equals("") || description.equals("") || type.equals(""))
            {
                System.out.println("All fields must be entered.");
                validInput = false;
            }

            if(validInput == false)
            {
                System.out.println("Please enter this information again.");
                continue;
            }

            PreparedStatement companyExist = con.prepareStatement();

            String companyName;
            int numEmployees;
            float yearlyRevenue;
            float stockPrice;

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

            if (companyName.length() > 100 || companyName == "")
            {
                System.out.println("The Company Name needs to be 100 characters or less. Please try again.");
                validInput = false;
            }

            if(validInput == false)
            {
                System.out.println("Please enter this information again.");
                continue;
            }

            int numOpenSpots;
            int numApplicants;

            System.out.println("Enter the Job's Number of Open Spots");
            numOpenSpots = scan.nextInt();
            scan.nextLine();
            System.out.println("Enter the Job's Number of Applicants");
            numApplicants = scan.nextInt();
            scan.nextLine();

            int stockOptions = 0;
            float signingBonus = 0;
            String payPeriod = "";
            float salary = 0;
            String season = "";

            if(type.equals("F")) //ADD SALARY TO FULL TIME
            {
                System.out.println("Enter the Job's Number of Stock Options");
                stockOptions = scan.nextInt();
                scan.nextLine();
                System.out.println("Enter the Job's Signing Bonus");
                signingBonus = scan.nextFloat();
                scan.nextLine();
            }
            if(type.equals("I"))
            {
                System.out.println("Enter the Job's Pay Period (length 10)");
                payPeriod = scan.nextLine();
                System.out.println("Enter the Job's salary");
                salary = scan.nextFloat();
                scan.nextLine();
                System.out.println("Enter the Job's Season (length 10)");
                season = scan.nextLine();
            }

            if(payPeriod.length() > 10 || season.length() > 10)
            {
                System.out.println("Pay Period and Season must be 10 characters or less. Please try again.");
                continue;
            }

            String locationArea;
            String street;
            String city;
            String state; //change to 2 chars

            System.out.println("Enter the Company's Location Area (length 25)");
            locationArea = scan.nextLine();
            System.out.println("Enter the Company's Street Address (length 100)");
            street = scan.nextLine();
            System.out.println("Enter the Company's City (length 25)");
            city = scan.nextLine();
            System.out.println("Enter the Company's State (length 25)");
            state = scan.nextLine();

            if(locationArea.length() > 25 || city.length() > 25 || state.length() > 25)
            {
                System.out.println("The Location Area, City and State must be 25 characters or less. Please try again.");
                validInput = false;
            }
            if(street.length() > 100)
            {
                System.out.println("The Street address must be 100 characters or less. Please try again.");
                validInput = false;
            }

            if(validInput == false)
            {
                continue;
            }

            String name;
            boolean technicalExperience;
            int yearsAtCompany;

            System.out.println("Enter the Manager's name (length 100)");
            name = scan.nextLine();
            System.out.println("Enter 'Y' if the Manager has technical experience");
            String temp = scan.nextLine();
            if(temp == "Y" || temp == "y")
            {
                technicalExperience = true;
            }
            else
            {
                technicalExperience = false;
            }
            System.out.println("Enter the Manager's Years at the Company");
            yearsAtCompany = scan.nextInt();
            scan.nextLine();

            if(name.length() > 100)
            {
                System.out.println("The Manager's name is 100 characters or less. Please try again.");
                continue;
            }

            System.out.println("Does the job you are creating have any related jobs? Enter 'Y' for yes.");
            String relatedMaybe = scan.nextLine();

            int related1 = 0;
            int related2 = 0;
            int related3 = 0;
            int related4 = 0;
            int related5 = 0;

            if(relatedMaybe == "Y" || relatedMaybe == "y")
            {
                System.out.println("How many? Enter a number between 1 and 5.");
                int numOfRelated = scan.nextInt();

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

            if(validInput)
            {
                PreparedStatement pstStart = con.prepareStatement("START TRANSACTION");
                pstStart.execute();

                PreparedStatement pst2 = con.prepareStatement("INSERT INTO Job(jobId, jobTitle, industry, description, companyId, managerId, type) VALUES(?,?,?,?,?,?,?)");
                pst2.clearParameters();
                pst2.setInt(1, jobId);
                pst2.setString(2, jobTitle);
                pst2.setString(3, industry);
                pst2.setString(4, description);
                pst2.setInt(5, companyId);
                pst2.setInt(6, managerId);
                pst2.setString(7, type);

                pst2.executeUpdate();
                System.out.println("The Job has been created.");

                PreparedStatement pstc = con.prepareStatement("INSERT INTO Company(companyId, companyName, numEmployees, yearlyRevenue, stockPrice) VALUES(?,?,?,?,?)");
                pstc.clearParameters();
                pstc.setInt(1, companyId);
                pstc.setString(2, companyName);
                pstc.setInt(3, numEmployees);
                pstc.setFloat(4, yearlyRevenue);
                pstc.setFloat(5, stockPrice);

                pstc.executeUpdate();
                System.out.println("The Company has been created.");

                PreparedStatement psta = con.prepareStatement("INSERT INTO Competition(jobId, numOpenSpots, numApplicants) VALUES(?,?,?)");
                psta.clearParameters();
                psta.setInt(1, jobId);
                psta.setInt(2, numOpenSpots);
                psta.setInt(3, numApplicants);

                psta.executeUpdate();
                System.out.println("The Competition has been created.");

                if(type == "F")
                {
                    PreparedStatement pstb = con.prepareStatement("INSERT INTO FullTime(jobId, numStockOptions, signingBonus) VALUES(?,?,?)");
                    pstb.clearParameters();
                    pstb.setInt(1, jobId);
                    pstb.setInt(2, stockOptions);
                    pstb.setFloat(3, signingBonus);

                    pstb.executeUpdate();
                    System.out.println("The Full Time Position has been created.");
                }
                if (type == "I")
                {
                    PreparedStatement pstd = con.prepareStatement("INSERT INTO Internship(jobId, payPeriod, salary, season) VALUES(?,?,?,?)");
                    pstd.clearParameters();
                    pstd.setInt(1, jobId);
                    pstd.setString(2, payPeriod);
                    pstd.setFloat(3, salary);
                    pstd.setString(4, season);

                    pstd.executeUpdate();
                    System.out.println("The Internship has been created.");
                }

                PreparedStatement pste = con.prepareStatement("INSERT INTO Location(companyId, locationArea, street, city, state) VALUES(?,?,?,?,?)");
                pste.clearParameters();
                pste.setInt(1, companyId);
                pste.setString(2, locationArea);
                pste.setString(3, street);
                pste.setString(4, city);
                pste.setString(5, state);

                pste.executeUpdate();
                System.out.println("The Location has been created.");

                PreparedStatement pstf = con.prepareStatement("INSERT INTO Manager(managerId, name, technicalExperience, yearsAtCompany) VALUES(?,?,?,?)");
                pstf.clearParameters();
                pstf.setInt(1, managerId);
                pstf.setString(2, name);
                pstf.setBoolean(3, technicalExperience);
                pstf.setInt(4, yearsAtCompany);

                pstf.executeUpdate();
                System.out.println("The Manager has been created.");

                if(relatedMaybe == "Y" || relatedMaybe == "y")
                {
                    PreparedStatement pstg = con.prepareStatement("INSERT INTO RelatedJobs(jobId, related1, related2, related3, related4, related5) VALUES(?,?,?,?,?,?)");
                    pstg.clearParameters();
                    pstg.setInt(1, jobId);
                    pstg.setInt(2, related1);
                    pstg.setInt(3, related2);
                    pstg.setInt(4, related3);
                    pstg.setInt(5, related4);
                    pstg.setInt(6, related5);

                    pstg.executeUpdate();
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
}
