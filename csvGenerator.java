import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.*;


public class csvGenerator
{
  public static boolean write(Connection con) throws FileNotFoundException
  {
    System.out.println("JobWriter" + jobWriter(con));
    System.out.println("Company" + companyWriter(con));
    System.out.println("Manager" + managerWriter(con));
    System.out.println("Internship" + internshipWriter(con));
    if(!jobWriter(con) || !companyWriter(con) || !managerWriter(con) || !internshipWriter(con) || !fullTimeWriter(con))
    {
      return false;
    }

    System.out.println("Report Generation was successful.");
    return true;
  }

  public static boolean relatedJobWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("RelatedJobs.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Job ID");
      sb.append(',');
      sb.append("Related Job 1");
      sb.append(',');
      sb.append("Related Job 2");
      sb.append(',');
      sb.append("Related Job 3");
      sb.append(',');
      sb.append("Related Job 4");
      sb.append(',');
      sb.append("Related Job 5");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT r.jobID, r.related1, r.related2, r.related3, r.related4, r.related5 FROM RelatedJobs r");
      ResultSet rs = pst.executeQuery();

      while (rs.next())
      {
        for (int i = 1; i <=6; ++i)
        {
          sb.append(rs.getInt(i));
          sb.append(',');
        }
        sb.append('\n');
      }
      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the RelatedJobs file creation.");
      return false;
    }
  }

  public static boolean competitionWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("Competition.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Job ID");
      sb.append(',');
      sb.append("Number of Open Spots");
      sb.append(',');
      sb.append("Number of Applicants");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT * FROM Competition");
      ResultSet rs = pst.executeQuery();
      while (rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getInt(2));
        sb.append(',');
        sb.append(rs.getInt(3));
        sb.append('\n');
      }
      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the Competition file creation.");
      return false;
    }

  }


  public static boolean jobWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("Job.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Job ID");
      sb.append(',');
      sb.append("Job Title");
      sb.append(',');
      sb.append("Industry");
      sb.append(',');
      sb.append("Description");
      sb.append(',');
      sb.append("Company ID");
      sb.append(',');
      sb.append("Is Internship");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT j.jobdId, j.jobTitle, j.industry, j.description, j.companyId, j.isInternship FROM Job j");
      ResultSet rs = pst.executeQuery();
      while(rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getString(2));
        sb.append(',');
        sb.append(rs.getString(3));
        sb.append(',');
        sb.append(rs.getString(4));
        sb.append(',');
        sb.append(rs.getInt(5));
        sb.append(',');
        String isInternship = "Full Time";
        if(rs.getBoolean(6))
        {
          isInternship = "Internship";
        }
        sb.append(isInternship);
        sb.append('\n');
      }

      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the Job file creation.");
      return false;
    }
  }

  public static boolean companyWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("Company.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Company ID");
      sb.append(',');
      sb.append("Company Title");
      sb.append(',');
      sb.append("Number Employees");
      sb.append(',');
      sb.append("Yearly Revenue");
      sb.append(',');
      sb.append("Stock Price");
      sb.append(',');
      sb.append("Location Area");
      sb.append(',');
      sb.append("Street");
      sb.append(',');
      sb.append("City");
      sb.append(',');
      sb.append("State");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT c.companyId, c.companyName, c.numEmployees, c.yearlyRevenue, c.stockPrice, l.locationArea, l.street, l.city, l.state  FROM Company c, Location l");
      ResultSet rs = pst.executeQuery();
      while(rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getString(2));
        sb.append(',');
        sb.append(rs.getInt(3));
        sb.append(',');
        sb.append(rs.getFloat(4));
        sb.append(',');
        sb.append(rs.getFloat(5));
        sb.append(',');
        sb.append(rs.getString(6));
        sb.append(',');
        sb.append(rs.getString(7));
        sb.append(',');
        sb.append(rs.getString(8));
        sb.append(',');
        sb.append(rs.getString(9));
        sb.append('\n');
      }

      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the Company file creation.");
      return false;
    }
  }

  public static boolean managerWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("Manager.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Manager ID");
      sb.append(',');
      sb.append("Company ID");
      sb.append(',');
      sb.append("Name");
      sb.append(',');
      sb.append("Techincal Experience");
      sb.append(',');
      sb.append("Years at Company");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT * FROM Manager");
      ResultSet rs = pst.executeQuery();
      while(rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getInt(2));
        sb.append(',');
        sb.append(rs.getString(3));
        sb.append(',');
        String isInternship = "No";
        if(rs.getBoolean(4))
        {
          isInternship = "Yes";
        }
        sb.append(isInternship);
        sb.append(',');
        sb.append(rs.getInt(5));
        sb.append('\n');
      }

      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the file creation.");
      return false;
    }
  }

  public static boolean internshipWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("Internship.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Job ID");
      sb.append(',');
      sb.append("Pay Period");
      sb.append(',');
      sb.append("Rate");
      sb.append(',');
      sb.append("Season");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT * FROM Internship");
      ResultSet rs = pst.executeQuery();
      while(rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getFloat(2));
        sb.append(',');
        sb.append(rs.getString(3));
        sb.append(',');
        sb.append(rs.getString(4));
        sb.append('\n');
      }

      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the file creation.");
      return false;
    }
  }

  public static boolean fullTimeWriter(Connection con) throws FileNotFoundException
  {
    try
    {
      PrintWriter pw = new PrintWriter(new File("FullTime.csv"));
      StringBuilder sb = new StringBuilder();
      sb.append("Job ID");
      sb.append(',');
      sb.append("Number of Stock Options");
      sb.append(',');
      sb.append("Signing Bonus");
      sb.append(',');
      sb.append("Salary");
      sb.append('\n');

      PreparedStatement pst = con.prepareStatement("SELECT * FROM FullTime");
      ResultSet rs = pst.executeQuery();
      while(rs.next())
      {
        sb.append(rs.getInt(1));
        sb.append(',');
        sb.append(rs.getInt(2));
        sb.append(',');
        sb.append(rs.getFloat(3));
        sb.append(',');
        sb.append(rs.getFloat(4));
        sb.append('\n');
      }

      pw.write(sb.toString());
      pw.close();
      return true;
    }
    catch (Exception e)
    {
      System.out.println("There was an issue with the file creation.");
      return false;
    }
  }
}
