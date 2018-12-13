/**
* Sarah Lasman and Haley Anderson
* GUI.java
*/

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.lang.NumberFormatException;
import java.util.Scanner;
import java.util.logging.*;

public class GUI extends JPanel
{
  private JFrame f;
  private JTable table;

  private JFormattedTextField jID;
  private JFormattedTextField cID;
  private JFormattedTextField mID;

  private JTextField jobField;
  private JTextField industry;
  private JTextField description;
  private JTextField type;
  private JFormattedTextField numOpenSpots;
  private JFormattedTextField numApplicants;
  private JButton createJob = new JButton("New job");

  private JTextField companyName;
  private JFormattedTextField numEmployees;
  private JFormattedTextField revenue;
  private JFormattedTextField stockPrice;
  private JTextField locationArea, streetAddress, city, state;
  private JButton createCompany = new JButton("New company");

  private JFormattedTextField stockOptions;
  private JFormattedTextField rate, signingBonus, salary;
  private JTextField season, payPeriod;
  private JFormattedTextField related1, related2, related3, related4, related5;

  private JTextField managerName;
  private JComboBox<String> hasExperience;
  private JFormattedTextField yearsAtCompany;
  private JButton createManager = new JButton("New manager");

  private JButton deleteJob = new JButton("Delete job");
  private JButton deleteCompany = new JButton("Delete company");

  private JButton displayAllJobs = new JButton("Display all jobs");
  private JButton searchJobs = new JButton("Search jobs");
  private JButton getJobInfo = new JButton("Get info on a job");

  private JButton updateJob = new JButton("Update job");
  private JButton updateCompany = new JButton("Update company");
  private JButton updateManager = new JButton("Update manager");

  private JButton generateReport = new JButton("Generate database report");

  private JRadioButton internship, fullTime, eitherType;

  private NumberFormat intFormat;

  private ResultSet rsInfo;
  private ResultSet rsType;
  private ResultSet rsRelated;
  private boolean isInternship;

  private JButton searchButton = new JButton("SEARCH");

  private JComboBox<String> categories;

  private Main main;
  Connection con;
  Scanner scan;
  Logger logger;

  /**
  * Initializes GUI and all common variables.
  * Also sets connection, scanner, and logger, to communicate with Main.
  */
  public GUI(Connection con, Scanner scan, Logger logger)
  {
    this.con = con;
    this.scan = scan;
    this.logger = logger;
    f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.addWindowListener(new WindowAdapterModified());
    jobField = new JTextField(50);
    industry = new JTextField(25);
    description = new JTextField(100);
    intFormat = NumberFormat.getNumberInstance();
    jID = new JFormattedTextField(intFormat);
    jID.setValue(null);
    cID = new JFormattedTextField(intFormat);
    cID.setValue(null);
    mID = new JFormattedTextField(intFormat);
    mID.setValue(null);
    numOpenSpots = new JFormattedTextField(intFormat);
    numOpenSpots.setValue(null);
    numApplicants = new JFormattedTextField(intFormat);
    numApplicants.setValue(null);
    type = new JTextField(1);
    companyName = new JTextField(50);
    numEmployees = new JFormattedTextField(intFormat);
    numEmployees.setValue(null);
    revenue = new JFormattedTextField(intFormat);
    revenue.setValue(null);
    stockPrice = new JFormattedTextField(intFormat);
    stockPrice.setValue(null);
    locationArea = new JTextField(25);
    streetAddress = new JTextField(100);
    city = new JTextField(25);
    state = new JTextField(2);
    managerName = new JTextField(50);
    String[] yn = {"Yes", "No"};
    hasExperience = new JComboBox<String>(yn);
    yearsAtCompany = new JFormattedTextField(intFormat);
    stockOptions = new JFormattedTextField(intFormat);
    signingBonus = new JFormattedTextField(intFormat);
    salary = new JFormattedTextField(intFormat);
    payPeriod = new JTextField(25);
    season = new JTextField(25);
    rate = new JFormattedTextField(intFormat);
    related1 = new JFormattedTextField(intFormat);
    related2 = new JFormattedTextField(intFormat);
    related3 = new JFormattedTextField(intFormat);
    related4 = new JFormattedTextField(intFormat);
    related5 = new JFormattedTextField(intFormat);
    yearsAtCompany.setValue(null);
    setLayout(new BorderLayout(5, 5));
    //add(initFields(), BorderLayout.NORTH);
    add(initButtons(), BorderLayout.CENTER);
    f.getContentPane().add(this);
    f.setSize(600, 600);
    f.setVisible(true);
    addButtonHandlers();
  }

  /**
  * Creates all actionlisteners for the buttons.
  */
  public void addButtonHandlers()
  {
    displayAllJobs.addActionListener(new ButtonHandler());
    createJob.addActionListener(new ButtonHandler());
    createCompany.addActionListener(new ButtonHandler());
    createManager.addActionListener(new ButtonHandler());
    updateJob.addActionListener(new ButtonHandler());
    updateCompany.addActionListener(new ButtonHandler());
    updateManager.addActionListener(new ButtonHandler());
    searchJobs.addActionListener(new ButtonHandler());
    deleteJob.addActionListener(new ButtonHandler());
    getJobInfo.addActionListener(new ButtonHandler());
    generateReport.addActionListener(new ButtonHandler());
    searchButton.addActionListener(new ButtonHandler());
  }

  /**
  * Checks if there are any empty job fields.
  */
  public boolean jobFieldsEmpty()
  {
    return (jobField.getText().trim().isEmpty()
            || industry.getText().trim().isEmpty()
            || description.getText().trim().isEmpty()
            || cID.getText().trim().isEmpty()
            || numOpenSpots.getText().trim().isEmpty()
            || numApplicants.getText().trim().isEmpty()
            || type.getText().trim().isEmpty());
  }

  /**
  * Checks if there are any empty company fields.
  */
  public boolean companyFieldsEmpty()
  {
    return (companyName.getText().trim().isEmpty()
            || numEmployees.getText().trim().isEmpty()
            || revenue.getText().trim().isEmpty()
            || stockPrice.getText().trim().isEmpty());
  }

  /**
  * Checks if there are any empty manager fields.
  */
  public boolean managerFieldsEmpty()
  {
    return (managerName.getText().trim().isEmpty()
          || yearsAtCompany.getText().trim().isEmpty());
  }

  /**
  * Initializes buttons.
  */
  private JPanel initButtons()
  {
    JPanel panel = new JPanel();
    panel.add(displayAllJobs);
    panel.add(createJob);
    panel.add(createCompany);
    panel.add(createManager);
    panel.add(updateJob);
    panel.add(updateCompany);
    panel.add(updateManager);
    panel.add(searchJobs);
    panel.add(deleteJob);
    panel.add(getJobInfo);
    panel.add(generateReport);
    return panel;
  }

  /**
  * Sets the fields for creating a new job.
  */
  private JPanel jobFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Job title"), "align label");
    panel.add(jobField, "wrap");
    panel.add(new JLabel("Industry"), "align label");
    panel.add(industry, "wrap");
    panel.add(new JLabel("Description"), "align label");
    panel.add(description, "wrap");
    panel.add(new JLabel("Company ID"), "align label");
    panel.add(cID, "wrap");
    panel.add(new JLabel("Number of open spots"), "align label");
    panel.add(numOpenSpots, "wrap");
    panel.add(new JLabel("Current number of applicants"), "align label");
    panel.add(numApplicants, "wrap");
    panel.add(new JLabel("Type (I for internship, F for full-time)"), "align label");
    panel.add(type, "wrap");
    panel.add(new JLabel("Optional: Add the IDs of up to 5 related jobs."));
    panel.add(related1, "wrap");
    panel.add(related2, "wrap");
    panel.add(related3, "wrap");
    panel.add(related4, "wrap");
    panel.add(related5, "wrap");
    searchButton.setText("Next");
    panel.add(searchButton);
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Sets fields for creating a new internship.
  */
  private JPanel internshipFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Pay period"));
    panel.add(payPeriod, "wrap");
    panel.add(new JLabel("Rate"));
    panel.add(rate, "wrap");
    panel.add(new JLabel("Season"));
    panel.add(season, "wrap");
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Sets fields for creating a new full-time job.
  */
  private JPanel fullTimeFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Stock Options"));
    panel.add(stockOptions, "wrap");
    panel.add(new JLabel("Signing Bonus"));
    panel.add(signingBonus, "wrap");
    panel.add(new JLabel("Salary"));
    panel.add(salary, "wrap");
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Sets fields for creating anew company.
  */
  private JPanel companyFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Company name"), "align label");
    panel.add(companyName, "wrap");
    panel.add(new JLabel("Number of employees"), "align label");
    panel.add(numEmployees, "wrap");
    panel.add(new JLabel("Yearly revenue"), "align label");
    panel.add(revenue, "wrap");
    panel.add(new JLabel("Stock price"), "align label");
    panel.add(stockPrice, "wrap");
    panel.add(new JLabel("Location area"), "align label");
    panel.add(locationArea, "wrap");
    panel.add(new JLabel("Street address"), "align label");
    panel.add(streetAddress, "wrap");
    panel.add(new JLabel("City"), "align label");
    panel.add(city, "wrap");
    panel.add(new JLabel("State (2-letter abbreviation)"), "align label");
    panel.add(state, "wrap");
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Sets fields for new manager.
  */
  private JPanel managerFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Manager name"), "align label");
    panel.add(managerName, "wrap");
    panel.add(new JLabel("Technical experience"), "align label");
    panel.add(hasExperience, "wrap");
    hasExperience.setSelectedIndex(0);
    panel.add(new JLabel("Years at company"), "align label");
    panel.add(yearsAtCompany, "wrap");
    panel.add(new JLabel("Company ID"), "align label");
    panel.add(cID, "wrap");
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Gets information for someone conducting a search.
  */
  private JPanel searchInfo()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Search by location, company, and/or type"));
    panel.add(new JLabel("State: (2-letter abbreviation)"), "align label");
    panel.add(state, "wrap");
    panel.add(new JLabel("Company:"), "align label");
    panel.add(companyName, "wrap");
    panel.add(new JLabel("Industry:"), "align label");
    panel.add(industry, "wrap");
    panel.add(new JLabel("Type:"), "align label");
    ButtonGroup typeButtons = new ButtonGroup();
    internship = new JRadioButton("Internship");
    fullTime = new JRadioButton("Full time");
    eitherType = new JRadioButton("Either");
    eitherType.setSelected(true);
    typeButtons.add(eitherType);
    typeButtons.add(internship);
    typeButtons.add(fullTime);
    panel.add(eitherType);
    panel.add(internship);
    panel.add(fullTime);
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Gets info for someone searching for a speciic job.
  */
  private JPanel getJobInfoFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Job ID:"), "align label");
    panel.add(jID, "wrap");
    searchButton.setText("SEARCH FOR JOB");
    panel.add(searchButton);
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Called when updating a job.
  */
  private JPanel updateJobFields()
  {
    JPanel panel = new JPanel();
    panel.add(new JLabel("Choose a category to update:"), "align label");
    String[] options = {"General information", "Full-time specific info", "Internship specific info", "Related jobs"};
    categories = new JComboBox<String>(options);
    panel.add(categories);
    searchButton.setText("CONTINUE");
    panel.add(searchButton);
    panel = clearFields(panel);
    return panel;
  }

  private JPanel updateGeneralFields()
  {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.add(new JLabel("Job ID to update:"), "align label");
      panel.add(jID, "wrap");
      panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
      panel.add(new JLabel("Job title"));
      panel.add(jobField);
      panel.add(new JLabel("Industry"));
      panel.add(industry);
      panel.add(new JLabel("Description"));
      panel.add(description);
      panel = clearFields(panel);
      return panel;
  }

  private JPanel updateFullTimeFields()
  {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.add(new JLabel("Job ID to update:"), "align label");
      panel.add(jID, "wrap");
      panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
      panel.add(new JLabel("Salary"));
      panel.add(salary);
      panel.add(new JLabel("Signing Bonus"));
      panel.add(signingBonus);
      panel.add(new JLabel("Stock Options"));
      panel.add(stockOptions);
      panel = clearFields(panel);
      return panel;
  }

  private JPanel updateInternshipFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Job ID to update:"), "align label");
    panel.add(jID, "wrap");
    panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
    panel.add(new JLabel("Rate"));
    panel.add(rate);
    panel.add(new JLabel("Pay period"));
    panel.add(payPeriod);
    panel.add(new JLabel("Season"));
    panel.add(season);
    panel = clearFields(panel);
    return panel;
  }

  private JPanel updateRelatedFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Job ID to update:"), "align label");
    panel.add(jID, "wrap");
    panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
    panel.add(new JLabel("Related 1"));
    panel.add(related1);
    panel.add(new JLabel("Related 2"));
    panel.add(related2);
    panel.add(new JLabel("Related 3"));
    panel.add(related3);
    panel.add(new JLabel("Related 4"));
    panel.add(related4);
    panel.add(new JLabel("Related 5"));
    panel.add(related5);
    panel = clearFields(panel);
    return panel;
  }

  private JPanel updateManagerFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Manager ID to update:"), "align label");
    panel.add(mID, "wrap");
    panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
    panel.add(new JLabel("Manager name"), "align label");
    panel.add(managerName);
    panel.add(new JLabel("Technical experience"), "align label");
    panel.add(hasExperience);
    panel.add(new JLabel("Years at company"), "align label");
    panel.add(yearsAtCompany);
    panel = clearFields(panel);
    return panel;
  }

  private JPanel updateCompanyFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Company ID to update:"), "align label");
    panel.add(cID, "wrap");
    panel.add(new JLabel("Updatable fields: Only fill in data for the fields you wish to update."), "align label");
    panel.add(new JLabel("Company name"), "align label");
    panel.add(companyName);
    panel.add(new JLabel("Number of employees"), "align label");
    panel.add(numEmployees);
    panel.add(new JLabel("Yearly revenue"), "align label");
    panel.add(revenue);
    panel.add(new JLabel("Stock price"), "align label");
    panel.add(stockPrice);
    panel.add(new JLabel("Location area"), "align label");
    panel.add(locationArea);
    panel.add(new JLabel("Street address"), "align label");
    panel.add(streetAddress);
    panel.add(new JLabel("City"), "align label");
    panel.add(city);
    panel.add(new JLabel("State"), "align label");
    panel.add(state);
    panel = clearFields(panel);
    return panel;
  }

  /**
  * Clears fields of any data that may have already been there.
  */
  private JPanel clearFields(JPanel panel)
  {
    for (Component component : panel.getComponents())
    {
      if (component instanceof JTextField)
      {
        JTextField field = (JTextField) component;
        field.setText("");
      }
      else if (component instanceof JFormattedTextField)
      {
        JFormattedTextField field = (JFormattedTextField) component;
        field.setText(null);
      }
    }
    return panel;
  }

  /**
  * Resets button text and repaints the frame.
  */
  private void resetButtons()
  {
    removeAll();
    revalidate();
    add(initButtons(), BorderLayout.CENTER);
    createJob.setText("New job");
    createManager.setText("New manager");
    createCompany.setText("New company");
    deleteJob.setText("Delete job");
    getJobInfo.setText("Get info on a job");
    updateJob.setText("Update job");
    updateManager.setText("Update manager");
    updateCompany.setText("Update company");
    //deleteCompany.setText("Delete company");
  }

  public void setRSInfo(ResultSet rs)
  {
    rsInfo = rs;
  }

  public void setRSType(ResultSet rs, boolean isInternship)
  {
    rsType = rs;
    this.isInternship = isInternship;
  }

  public void setRSRelated(ResultSet rs)
  {
    rsRelated = rs;
  }

  /**
  * Displays a result set that can be obtained via a select statement.
  * @param rs The result set to be displayed\
  */
  public JPanel showTable(ResultSet rs, int rows, int cols, String[] columnNames)
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    TableModel dataModel = new DefaultTableModel(rows, cols) {
      @Override
      public String getColumnName(int index)
      {
        return columnNames[index];
      }
    };
    table = new JTable(dataModel);
    JScrollPane scrollpane = new JScrollPane(table);
    scrollpane.setPreferredSize(new Dimension(600,400));
    try
    {
      int numColumns = rs.getMetaData().getColumnCount();
      rs.first();
      int rowCount = 1;
      do
      {
        for (int col = 0; col < numColumns; ++col)
        {
          table.getModel().setValueAt(rs.getObject(col+1), table.convertRowIndexToModel(rowCount), col);
        }
        rowCount++;
      } while (rs.next());
      //panel.add(table);
      panel.add(scrollpane);
      panel.add(initButtons(), BorderLayout.CENTER);
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "Error showing the data: " + e);
    }
    panel = clearFields(panel);
    return panel;

  }

  /**
  * Gets string representation of internship vs. full time jobs.
  */
  private String isInternship(boolean isInternship)
  {
    if (isInternship) return "Internship";
    else return "Full time";
  }

  /**
  * Format basic info about a job.
  */
  private String getBasicInfo() throws Exception
  {
    rsInfo.first();
    String info = "<html><b>Job ID:</b> " + rsInfo.getInt(1) + " <br><b>Job Title:</b> " + rsInfo.getString(2) + "<br><b>Industry:</b> " + rsInfo.getString(3) + "<br><b>Description:</b> " + rsInfo.getString(4) + "<br><b>Company ID:</b> " + rsInfo.getInt(5)
            + "<br><b>Type:</b>" + isInternship(isInternship) + "<br>"
            + "<b>Company Name:</b> " + rsInfo.getString(6) + "<br><b>Number of Employees:</b> " + rsInfo.getInt(7) + "<br><b>Yearly Revenue: </b>$" + String.format("%.2f", rsInfo.getFloat(8)) + "<br><b>Stock Price:</b> $" + String.format("%.2f", rsInfo.getFloat(9)) + "<br>"
            + "<b>Location Area:</b> " + rsInfo.getString(10) + "<br><b>Address:</b> " + rsInfo.getString(11) + ", " + rsInfo.getString(12) + "</html>";
    return info;
  }

  /**
  * Format type-specific info about a job.
  */
  private String getTypeInfo() throws Exception
  {
    rsType.first();
    String info;
    if (isInternship)
    {
      info = "<html><br><b>Internship Pay Period:</b> " + rsType.getString(1) + "<br><b>Hourly Rate: </b>$" + String.format("%.2f", rsType.getFloat(2)) + "<br><b>Season:</b> " + rsType.getString(3) + "</html>";
    }
    else
    {
      info = "<html><br><b>Yearly Salary:</b> $" + String.format("%.2f", rsType.getFloat(1)) + "<br><b>Number of Stock Options: </b>" + rsType.getInt(2) + "<br><b>Signing Bonus: </b> $" + String.format("%.2f", rsType.getFloat(3)) + "</html>";
    }
    return info;
  }

  /**
  * Format related jobs about a job.
  */
  private String getRelatedInfo() throws Exception
  {
    rsRelated.first();
    String info;
    if (rsRelated.getInt(1) != 0)
    {
      info = "<html><br><b>Related Job 1:</b> " + rsRelated.getInt(2) + " <br><b>Related Job 2:</b> " + rsRelated.getInt(3) + " <br><b>Related Job 3:</b> " + rsRelated.getInt(4) + "<br><b>Related Job 4:</b> " + rsRelated.getInt(5) + " <br><b>Related Job 5: </b>" + rsRelated.getInt(6) + "</html>";
    }
    else {
      info = "No related jobs.";
    }
    return info;
  }

  /**
  * Displays the info of a job.
  */
  private JPanel displayInfo()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    try
    {
      String info = getBasicInfo();
      panel.add(new JLabel(info), "align label");
      info = getTypeInfo();
      panel.add(new JLabel(info), "align label");
      info = getRelatedInfo();
      panel.add(new JLabel(info), "align label");
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "Error showing the data: " + e);
    }
    panel = clearFields(panel);
    return panel;
  }

  public void displayMessage(String msg)
  {
    JOptionPane.showMessageDialog(null, msg);
  }

  public boolean isOpen()
  {
    return true;
  }

  /**
  * Runs the prepared statement for update.
  * Different than in main, so has its own method here to avoid confusion.
  */
  public void updateJobPST() throws SQLException
  {
    if(!jobField.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Job SET jobTitle=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), jobField.getText());
    if(!industry.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Job SET industry=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), industry.getText());
    if(!description.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Job SET description=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), description.getText());
    if(!salary.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE FullTime SET salary=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)salary.getValue()).floatValue());
    if(!signingBonus.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE FullTime SET signingBonus=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)signingBonus.getValue()).floatValue());
    if(!stockOptions.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE FullTime SET stockOptions=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)stockOptions.getValue()).intValue());
    if(!rate.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Internship SET rate=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)rate.getValue()).floatValue());
    if(!season.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Internship SET season=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), season.getText());
    if(!payPeriod.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE Internship SET payPeriod=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), payPeriod.getText());
    if(!related1.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE RelatedJobs SET related1=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)related1.getValue()).intValue());
    if(!related2.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE RelatedJobs SET related2=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)related2.getValue()).intValue());
    if(!related3.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE RelatedJobs SET related3=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)related3.getValue()).intValue());
    if(!related4.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE RelatedJobs SET related4=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)related4.getValue()).intValue());
    if(!related5.getText().trim().isEmpty())
      Main.executePST(con, logger, con.prepareStatement("UPDATE RelatedJobs SET related5=? WHERE jobId=?"), ((Number)jID.getValue()).intValue(), ((Number)related5.getValue()).intValue());
  }

  private class WindowAdapterModified extends WindowAdapter
  {
    public void windowClosing(WindowEvent e)
    {
      try
      {
        con.close();
        System.out.println("Thank you for using this database.");
      }
      catch (Exception ex)
      {
        System.out.println("Error " + ex);
      }
    }
  }

  //@TODO add more exception handling for save cases
  private class ButtonHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      JPanel jobPanel, companyPanel, managerPanel;
      jobPanel = new JPanel();
      companyPanel = new JPanel();
      managerPanel = new JPanel();
      Job j = new Job();
      switch(e.getActionCommand())
      {
        case("New job"):
          resetButtons();
          jobPanel = jobFields();
          add(jobPanel, BorderLayout.NORTH);
          break; //Return to
        case("Next"):
          if (jobFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          try
          {
            j.jobTitle = jobField.getText();
            j.industry = industry.getText();
            j.description = description.getText();
            j.companyId = ((Number)cID.getValue()).intValue();
            j.type = type.getText().toLowerCase().charAt(0);
          }
          catch (java.lang.NumberFormatException ex)
          {
            JOptionPane.showMessageDialog(null, "Invalid format, try again");
            return;
          }
          resetButtons();
          if (j.type == 'f')
          {
            add(fullTimeFields(), BorderLayout.NORTH);
          }
          else if (j.type == 'i')
          {
            add(internshipFields(), BorderLayout.NORTH);
          }
          else
          {
            JOptionPane.showMessageDialog(null, "Type field must be i or f.");
            return;
          }
          createJob.setText("Save job");
          break; //Return to
        case("Save job"): //Return to
          boolean success;
          if (j.type == 'f')
          {
            try
            {
              j.signingBonus = ((Number)signingBonus.getValue()).floatValue();
              j.stockOptions = ((Number)stockOptions.getValue()).intValue();
              j.salary = ((Number)salary.getValue()).floatValue();
              success = Main.createNewPosting(con, scan, logger, j, true);
            }
            catch(Exception ex)
            {
              JOptionPane.showMessageDialog(null, "Invalid format, try again");
              return;
            }
            if (signingBonus.getText().trim().equals("") || stockOptions.getText().trim().equals("")
                || salary.getText().trim().equals(""))
            {
              JOptionPane.showMessageDialog(null, "All fields must have data");
              return;
            }
          }
          else
          {
            try
            {
              j.season = salary.getText();
              j.rate = ((Number)rate.getValue()).floatValue();
              j.payPeriod = payPeriod.getText();
              success = Main.createNewPosting(con, scan, logger, j, true);
            }

            catch(Exception ex)
            {
              JOptionPane.showMessageDialog(null, "Invalid format, try again");
              logger.info(""+ex);
              return;
            }
            if (season.getText().trim().equals("") || rate.getText().trim().equals("")
                || payPeriod.getText().trim().equals(""))
            {
              JOptionPane.showMessageDialog(null, "All fields must have data");
              return;
            }
          }
          if (success)
            JOptionPane.showMessageDialog(null, "Job created");
          createJob.setText("New job");
          remove(jobPanel);
          resetButtons();
          break;
        case("New company"): //DONE
          resetButtons();
          companyPanel = companyFields();
          add(companyPanel, BorderLayout.NORTH);
          createCompany.setText("Save company");
          break;
        case("Save company"): //DONE
          if (companyFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          try
          {
            String companyNameS = companyName.getText();
            int numEmployeesS = ((Number)numEmployees.getValue()).intValue();
            float yearlyRevenueS = ((Number)revenue.getValue()).floatValue();
            float stockPriceS = ((Number)stockPrice.getValue()).floatValue();
            String locationAreaS = locationArea.getText();
            String streetS = streetAddress.getText();
            String cityS = city.getText();
            String stateS = state.getText();
            int companyId = 0;
            PreparedStatement pstC = con.prepareStatement("INSERT INTO Company(companyName, numEmployees, yearlyRevenue, stockPrice) VALUES (?,?,?,?);");
            pstC.clearParameters();
            pstC.setString(1, companyNameS);
            pstC.setInt(2, numEmployeesS);
            pstC.setFloat(3, yearlyRevenueS);
            pstC.setFloat(4, stockPriceS);
            PreparedStatement pstL = con.prepareStatement("INSERT INTO Location(companyId, locationArea, street, city, state) VALUES (?,?,?,?,?);");
            PreparedStatement pstId = con.prepareStatement("SELECT MAX(companyId) FROM Company;");
            ResultSet rsId = pstId.executeQuery();
            while(rsId.next())
            {
                companyId = rsId.getInt(1);
            }
            pstL.setInt(1, companyId);
            pstL.setString(2, locationAreaS);
            pstL.setString(3, streetS);
            pstL.setString(4, cityS);
            pstL.setString(5, stateS);
            pstC.executeUpdate();
            pstL.executeUpdate();
            createCompany.setText("New company");
            logger.info(""+pstC);
            logger.info(""+pstL);
            JOptionPane.showMessageDialog(null, "Company successfully created");
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
            logger.info(""+ex);
          }
          break;
        case("New manager"): //DONE
          resetButtons();
          managerPanel = managerFields();
          add(managerPanel, BorderLayout.NORTH);
          createManager.setText("Save manager");
          remove(jobPanel);
          remove(companyPanel);
          break;
        case("Save manager"): //DONE
          if (managerFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          Manager m = new Manager();
          try
          {
            m.managerName = managerName.getText();
            if(hasExperience.getSelectedItem().equals("Yes"))
              m.hasExperience = true;
            else
              m.hasExperience = false;
            m.yearsAtCompany = ((Number)yearsAtCompany.getValue()).intValue();
            m.companyId = ((Number)cID.getValue()).intValue();
            Main.createManager(con.prepareStatement("INSERT INTO MANAGER(companyId, name, technicalExperience, yearsAtCompany) VALUES(?,?,?,?);"), scan, con, logger, m, true);
            JOptionPane.showMessageDialog(null, "Manager created");
            remove(managerPanel);
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "Error creating manager: " + ex);
          }
          createManager.setText("New manager");
          break;
        case("Delete job"): //Done, test edge cases
          resetButtons();
          JPanel deletePanel = new JPanel();
          deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
          deletePanel.add(new JLabel("Job ID to be deleted"), "align label");
          deletePanel.add(jID, "wrap");
          add(deletePanel, BorderLayout.NORTH);
          deleteJob.setText("Confirm delete job");
          break;
        case("Confirm delete job"): //Done, test edge cases
          deleteJob.setText("Delete job");
          if (Main.deleteCall(con, scan, true, ((Number)jID.getValue()).intValue(), logger))
          {
            JOptionPane.showMessageDialog(null, "Job id " + jID.getValue() + " successfully deleted.");
            break;
          }
          else
          {
            JOptionPane.showMessageDialog(null, "There was an error with your delete request. Check to ensure your id number is valid.");
          }
          break;
        case("Update job"): //Done, test edge cases
          resetButtons();
          add(updateJobFields());
          break;
        case("CONTINUE"): //Done, test edge cases
          resetButtons();
          if(categories.getSelectedItem().equals("General information"))
            {
              add(updateGeneralFields(), BorderLayout.NORTH);
            }
          else if(categories.getSelectedItem().equals("Full-time specific info"))
            {
              String[] moreOptions ={"Number of stock options", "Signing bonus", "Salary"};
              add(updateFullTimeFields(), BorderLayout.NORTH);
            }
          else if (categories.getSelectedItem().equals("Internship specific info"))
            {
              String[] moreOptions ={"Pay period", "Rate", "Season"};
              add(updateInternshipFields(), BorderLayout.NORTH);
            }
          else if(categories.getSelectedItem().equals("Related jobs"))
          {
            String[] moreOptions ={"Related job 1", "Related job 2", "Related job 3", "Related job 4", "Related job 5"};
            add(updateRelatedFields(), BorderLayout.NORTH);
          }
          updateJob.setText("Confirm update job");
          break;
        case("Confirm update job"): //Done, test edge cases
          if (jID.getText().trim().isEmpty())
          {
            JOptionPane.showMessageDialog(null, "Must have job id");
            return;
          }
          try
          {
            updateJobPST();
            JOptionPane.showMessageDialog(null, "Job updated");
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "Error updating job: " + e);
          }
          break;
        case("Update manager"): //Done
          resetButtons();
          updateManager.setText("Confirm update manager");
          managerPanel = updateManagerFields();
          managerPanel.setLayout(new BoxLayout(managerPanel, BoxLayout.Y_AXIS));
          add(managerPanel, BorderLayout.NORTH);
          break;
        case("Confirm update manager"): //Done
          if (cID.getText().trim().isEmpty())
          {
            JOptionPane.showMessageDialog(null, "Must have manager id");
            return;
          }
          try
          {
            if (!managerName.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Manager SET name=? WHERE managerId=?"), ((Number)mID.getValue()).intValue(), managerName.getText());
            if (hasExperience.getSelectedItem().equals("Yes"))
              Main.executePST(con, logger, con.prepareStatement("UPDATE Manager SET technicalExperience=TRUE WHERE managerId=?"), ((Number)mID.getValue()).intValue());
            if (hasExperience.getSelectedItem().equals("No"))
              Main.executePST(con, logger, con.prepareStatement("UPDATE Manager SET technicalExperience=FALSE WHERE managerId=?"), ((Number)mID.getValue()).intValue());
            if (!yearsAtCompany.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Manager SET yearsAtCompany=? WHERE managerID=?"), ((Number)mID.getValue()).intValue(), ((Number)yearsAtCompany.getValue()).intValue());
            JOptionPane.showMessageDialog(null, "Manager updated");
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "There was an error updating: " + ex);
            break;
          }
          resetButtons();
          break;
        case ("Update company"): //Done
          resetButtons();
          updateCompany.setText("Confirm update company");
          companyPanel = updateCompanyFields();
          companyPanel.setLayout(new BoxLayout(companyPanel, BoxLayout.Y_AXIS));
          add(companyPanel, BorderLayout.NORTH);
          break;
        case ("Confirm update company"): //Done
          try
          {
            if(!companyName.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Company SET companyName=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), companyName.getText());
            if(!numEmployees.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Company SET numEmployees=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), ((Number)numEmployees.getValue()).intValue());
            if(!revenue.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Company SET yearlyRevenue=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), ((Number)revenue.getValue()).floatValue());
            if(!stockPrice.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Company SET stockPrice=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), ((Number)stockPrice.getValue()).floatValue());
            if(!locationArea.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Location SET locationArea=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), locationArea.getText());
            if(!streetAddress.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Location SET street=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), streetAddress.getText());
            if(!city.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Location SET city=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), city.getText());
            if(!state.getText().trim().isEmpty())
              Main.executePST(con, logger, con.prepareStatement("UPDATE Location SET state=? WHERE companyId=?"), ((Number)cID.getValue()).intValue(), state.getText());
            JOptionPane.showMessageDialog(null, "Company updated");
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "There was an error updating: " + ex);
            break;
          }
          resetButtons();
          break;
        case("Display all jobs"): //done for now
          resetButtons();
          if (!Main.queryMethod(con, scan, true))
          {
            JOptionPane.showMessageDialog(null, "There was an error with your request. Please try again later.");
          }
          break;
        case("Search jobs"): //DONE
          resetButtons();
          add(searchInfo(), BorderLayout.NORTH);
          searchJobs.setText("Search");

          break;
        case ("Search"): //Fix buttons
          resetButtons();
          boolean checkType = true;
          boolean isInternship = false;
          if (internship.isSelected())
          {
            isInternship = true;
          }
          else if (eitherType.isSelected())
          {
            checkType = false;
          }
          boolean searchByState, searchByCompany, searchByIndustry;
          searchByState = searchByCompany = searchByIndustry = false;
          String pstSearchString = "SELECT jobId, industry, jobTitle, description, c.companyName, l.state FROM Job j, Company c, Location l WHERE"
                + " c.companyId = j.companyId AND j.companyId = l.companyId";
          if (state.getText().length() == 2)
          {
            pstSearchString += " AND l.state=?";
            searchByState = true;
          }
          if (companyName.getText().length() > 0)
          {
            pstSearchString += " AND c.companyName=?";
            searchByCompany = true;
          }
          if (industry.getText().length() > 0)
          {
            pstSearchString += " AND j.industry=?";
            searchByIndustry = true;
          }
          if (checkType)
          {
            if (isInternship)
            {
              pstSearchString += " AND j.isInternship = 1";
            }
            else
            {
              pstSearchString += " AND j.isInternship = 0";
            }
          }
          try
          {
            int currentValue = 1;
            PreparedStatement pst = con.prepareStatement(pstSearchString);
            if (searchByState)
            {
              pst.setString(currentValue, state.getText());
              currentValue++;
            }
            if (searchByCompany)
            {
              pst.setString(currentValue, companyName.getText());
              currentValue++;
            }
            if (searchByIndustry)
            {
              pst.setString(currentValue, industry.getText());
              currentValue++;
            }
            if (checkType)
            {
              pst.setBoolean(currentValue, isInternship);
            }
            ResultSet rs = pst.executeQuery();
            rs.last();
            String[] columnNames = {"Job ID", "Job Title", "Industry", "Description", "Company", "State"};
            add(showTable(rs, rs.getRow()+1, 6, columnNames), BorderLayout.NORTH);
          }
          catch (Exception ex)
          {
            JOptionPane.showMessageDialog(null, "There was an error with your search.");
            logger.info("" + ex);
          }
          break;
        case("Get info on a job"): //DONE
          resetButtons();
          add(getJobInfoFields(), BorderLayout.NORTH);
          break;
        case("SEARCH FOR JOB"): //DONE
          resetButtons();
          if(!Main.jobInfo(con, scan, ((Number)jID.getValue()).intValue(), true, logger))
          {
            JOptionPane.showMessageDialog(null, "There was an error with your request. Check to ensure your id number is valid.");
          }
          else
          {
            add(displayInfo(), BorderLayout.NORTH);
          }
          break;
        case("Generate database report"):
          resetButtons();
          if (Main.generateReport(con, logger))
            JOptionPane.showMessageDialog(null, "Reports generated.");
          else
            JOptionPane.showMessageDialog(null, "There was an error generating your report.");
          break;
        default:
          break;
      }
    }
  }
}
