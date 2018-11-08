import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
            System.out.println("Options on what to do: \n1. Display all Jobs \n2. Add a new Job \n3. Update a Job Posting \n4. Remove a Job \n5. Search by Location, Company, or Type \n6. Quit\n");


            while(loop)
            {
                try
                {
                    System.out.println("What would you like to do: \n");
                    editOption = scan.nextInt();

                    if (editOption < 1 || editOption > 6)
                    {
                        System.out.println("Please enter a number between 1 and 6\n");
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Please enter a number between 1 and 6\n");
                    scan.nextLine();
                }

                if(editOption == 1)
                {
                    System.out.println("The Job Database:");
                    PreparedStatement pst1 = con.prepareStatement("select * from Job");
                    ResultSet rs = pst1.executeQuery();
                    while(rs.next())
                    {
                        System.out.println(rs.getInt(1)+ " " + rs.getString(2) + " "+ rs.getString(3) + " " + rs.getFloat(4) + " " + rs.getString(5) + " " + rs.getString(6));
                    }
                    System.out.println("");
                }

                if(editOption == 2) //needs to be finished with other tables
                {
                    int jobId;
                    String jobTitle;
                    String industry;
                    String description;
                    int companyId;
                    int managerId;
                    String type;
                    boolean validInput = true;

                    try
                    {
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
                        if (type.length() > 1)
                        {
                            System.out.println("Type needs to be 1 character only. Please try again.");
                            validInput = false;
                        }
                        if(jobTitle == "" || industry == "" || description == "" || type == "")
                        {
                            System.out.println("All fields must be entered.");
                            validInput = false;
                        }

                        if(validInput)
                        {
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
                        }

                        //add to all other tables!!
                    }
                    catch (Exception e)
                    {
                        System.out.println("Please enter this information again, and be sure that the ID number chosen is available.");
                    }
                }

                if(editOption == 3) //this needs to be done
                {
                    int studentId = 0;
                    String major = "";
                    String advisor = "";

                    try
                    {
                        try {
                            System.out.println("A Student's Major and Faculty Advisor can be updated.");
                            System.out.println("If you don't want to update that field, leave it blank.");

                            System.out.println("Please enter the Student's ID");
                            studentId = scan.nextInt();
                            scan.nextLine();

                            System.out.println("Enter the Student's new Major");
                            major = scan.nextLine();
                            System.out.println("Enter the Student's new Faculty Advisor");
                            advisor = scan.nextLine();

                            if (advisor.length() > 25)
                            {
                                System.out.println("Faculty Advisor needs to be 25 characters or less. Please try again.");
                            }
                            if (major.length() > 10)
                            {
                                System.out.println("Major needs to be 10 characters or less. Please try again.");
                            }

                        }
                        catch(Exception e)
                        {
                            System.out.println("Please enter the variables in the correct format.");
                        }

                        if (major.length() != 0)
                        {
                            PreparedStatement pst3 = con.prepareStatement("UPDATE Student SET Major=? WHERE StudentId=?");
                            pst3.clearParameters();
                            pst3.setString(1, major);
                            pst3.setInt(2, studentId);
                            pst3.executeUpdate();
                        }
                        if (advisor.length() != 0)
                        {
                            PreparedStatement pst4 = con.prepareStatement("UPDATE Student SET FacultyAdvisor=? WHERE StudentId =?");
                            pst4.clearParameters();
                            pst4.setString(1, advisor);
                            pst4.setInt(2, studentId);
                            pst4.executeUpdate();
                        }
                    }
                    catch(Exception e)
                    {
                        System.out.println("Please enter the variables in the correct format.");
                    }

                    System.out.println("The Student has been updated.");
                }

                if(editOption == 4) //this should be done
                {
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

                if(editOption == 5) //probably not fully done
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
                            PreparedStatement pst6 = con.prepareStatement("select * from Job j, Company c, Location l WHERE j.companyId = c.companyId");
                            ResultSet rs = pst6.executeQuery();
                            while(rs.next())
                            {
                                System.out.println(rs.getInt(1)+ " " + rs.getString(2) + " "+ rs.getString(3) + " " + rs.getFloat(4) + " " + rs.getString(5) + " " + rs.getString(6));
                            }
                        }
                        else if(searchOption == 3)
                        {

                            System.out.println("Please enter the Type of Job you want to search for (I/F)");
                            String type = scan.nextLine();

                            if (type.length() > 1)
                            {
                                System.out.println("Type needs to be of length 1. Please try again.");
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
                                    System.out.println(rs.getInt(1) + " " + rs.getString(2) + " "+ rs.getString(3) + " " + rs.getString(4) + " " + rs.getInt(5) + " " + rs.getInt(6) + " " + rs.getString(7));
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

                if(editOption == 6)
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
}
