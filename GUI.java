import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.lang.NumberFormatException;

/**
* @TODO list for GUI
* [X] Add job/manager/company
* [] Delete job/manager/company
* [] Search
* [] Update
* Bugs:
* [X] Repainting!!!!!!!!!!!!
* [X] A more efficient way of dealing with re-naming buttons
* [] Shit I have a lot to do
*/

public class GUI extends JPanel
{
  private JTextField jobField;
  private JTextField industry;
  private JTextField description;
  private JFormattedTextField jID;
  private JFormattedTextField cID;
  private JFormattedTextField mID;
  private JTextField type;
  private JButton createJob = new JButton("New job");

  private JTextField companyName;
  private JFormattedTextField numEmployees;
  private JFormattedTextField revenue;
  private JFormattedTextField stockPrice;
  private JTextField locationArea, streetAddress, city, state;
  private JButton createCompany = new JButton("New company");

  private JTextField managerName;
  //@TODO - experience? figure out how booleans work
  private JFormattedTextField yearsAtCompany;
  private JButton createManager = new JButton("New manager");

  private JButton deleteJob = new JButton("Delete job");
  private JButton deleteCompany = new JButton("Delete company");
  private JButton deleteManager = new JButton("Delete manager");

  private JButton displayAllJobs = new JButton("Display all jobs");
  private JButton searchJobs = new JButton("Search jobs");

  private JRadioButton internship, fullTime, eitherType;

  private NumberFormat intFormat;

  private Main main;

  public static void main(String[] args)
  {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(new GUI());
    f.setSize(600, 600);
    f.setVisible(true);
  }

  public GUI()
  {
    jobField = new JTextField(50);
    industry = new JTextField(25);
    description = new JTextField(100);
    intFormat = NumberFormat.getNumberInstance();
    jID = new JFormattedTextField(intFormat);
    jID.setValue(new Integer(1));
    cID = new JFormattedTextField(intFormat);
    cID.setValue(new Integer(1)); //@TODO don't have a default int value, or commit
    mID = new JFormattedTextField(intFormat);
    mID.setValue(new Integer(1));
    type = new JTextField(1);
    companyName = new JTextField(50);
    numEmployees = new JFormattedTextField(intFormat);
    numEmployees.setValue(new Integer(0));
    revenue = new JFormattedTextField(intFormat);
    revenue.setValue(new Float(0));
    stockPrice = new JFormattedTextField(intFormat);
    stockPrice.setValue(new Float(0));
    locationArea = new JTextField(25);
    streetAddress = new JTextField(100);
    city = new JTextField(25);
    state = new JTextField(2);
    managerName = new JTextField(50);
    yearsAtCompany = new JFormattedTextField(intFormat);
    yearsAtCompany.setValue(new Float(0));
    setLayout(new BorderLayout(5, 5));
    //add(initFields(), BorderLayout.NORTH);
    add(initButtons(), BorderLayout.CENTER);
  }

  public boolean jobFieldsEmpty()
  {
    return (jobField.getText().trim().isEmpty()
            || industry.getText().trim().isEmpty()
            || description.getText().trim().isEmpty()
            || cID.getText().trim().isEmpty()
            || mID.getText().trim().isEmpty()
            || type.getText().trim().isEmpty());
  }

  public boolean companyFieldsEmpty()
  {
    return (companyName.getText().trim().isEmpty()
            || numEmployees.getText().trim().isEmpty()
            || revenue.getText().trim().isEmpty()
            || stockPrice.getText().trim().isEmpty());
  }

  public boolean managerFieldsEmpty()
  {
    return (managerName.getText().trim().isEmpty()
          || yearsAtCompany.getText().trim().isEmpty());
  }

  private JPanel initButtons()
  {
    JPanel panel = new JPanel();
    panel.add(displayAllJobs);
    displayAllJobs.addActionListener(new ButtonHandler());
    panel.add(createJob);
    createJob.addActionListener(new ButtonHandler());
    panel.add(createCompany);
    createCompany.addActionListener(new ButtonHandler());
    panel.add(createManager);
    createManager.addActionListener(new ButtonHandler());
    panel.add(searchJobs);
    searchJobs.addActionListener(new ButtonHandler());
    panel.add(deleteJob);
    deleteJob.addActionListener(new ButtonHandler());
    // panel.add(deleteCompany);
    // deleteCompany.addActionListener(new ButtonHandler());
    panel.add(deleteManager);
    deleteManager.addActionListener(new ButtonHandler());

    return panel;
  }

  private JPanel initFields()
  {
    JPanel panel = new JPanel();
    panel.add(new JLabel("Job info"), "align label");
    panel.add(jobField, "wrap");
    return panel;
  }

  private Job getJobData()
  {
    Job j = new Job();
    return j;
  }

  //@TODO work with main class to type-validate
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
    panel.add(new JLabel("Type (I for internship, F for full-time)"), "align label");
    panel.add(type, "wrap");
    return panel;
  }

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
    return panel;
  }

  private JPanel managerFields()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Manager name"), "align label");
    panel.add(managerName, "wrap");
    panel.add(new JLabel("Years at company"), "align label");
    panel.add(yearsAtCompany, "wrap");
    return panel;
  }

  private JPanel searchInfo()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(new JLabel("Search by location, company, and/or type"));
    panel.add(new JLabel("State: (2-letter abbreviation)"), "align label");
    panel.add(new JLabel("Company:"), "align label");
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
    return panel;
  }

  /**
  * Sets all buttons back to their original state except the current button.
  * @TODO may not be necessary - check
  */
  private void resetButtonsExcept(JButton button)
  {
    if (!button.equals(createJob)) createJob.setText("Create job");
    if (!button.equals(createManager)) createManager.setText("Create manager");
    if (!button.equals(createCompany)) createCompany.setText("Create company");
    if (!button.equals(deleteJob)) deleteJob.setText("Delete job");
    if (!button.equals(deleteManager)) deleteManager.setText("Delete manager");
    if (!button.equals(deleteCompany)) deleteCompany.setText("Delete company");
  }

  private void resetButtons()
  {
    removeAll();
    revalidate();
    add(initButtons(), BorderLayout.CENTER);
    createJob.setText("New job");
    createManager.setText("New manager");
    createCompany.setText("New company");
    deleteJob.setText("Delete job");
    deleteManager.setText("Delete manager");
    //deleteCompany.setText("Delete company");
  }

  /**
  * Displays a result set that can be obtained via a select statement.
  * @param rs the ResultSet to be displayed; will be obtained from main.
  * @TODO test
  */
  private void showTable(ResultSet rs)
  {
    JTable table = new JTable();
    try
    {
      int numColumns = rs.getMetaData().getColumnCount();
      int rowCount = 1;
      while (rs.next())
      {
        Object[] row = new Object[numColumns];
        for (int col = 0; col < numColumns; ++col)
        {
          table.getModel().setValueAt(rs.getObject(col+1), rowCount, col);
        }
        rowCount++;
      }
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, "Error showing the data: " + e);
    }

  }

  //@TODO add more exception handling for save cases
  //@TODO deal with repainting
  private class ButtonHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      JPanel jobPanel, companyPanel, managerPanel;
      jobPanel = new JPanel();
      companyPanel = new JPanel();
      managerPanel = new JPanel();
      switch(e.getActionCommand())
      {
        case("New job"):
          resetButtons();
          jobPanel = jobFields();
          add(jobPanel, BorderLayout.NORTH);
          createJob.setText("Save job");
          remove(companyPanel);
          remove(managerPanel);
          break;
        case("Save job"):
          if (jobFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          Job j = new Job();
          try
          {
            j.jobTitle = jobField.getText();
            j.industry = industry.getText();
            j.description = description.getText();
            j.companyId = ((Number)cID.getValue()).intValue();
            j.managerId = ((Number)mID.getValue()).intValue();
            j.type = type.getText();
            j.jobCreated();
            //main.getJobInfo(j.jobTitle, j.industry, j.description, j.companyId, j.managerId, j.type);
            createJob.setText("New job");
          }
          catch (java.lang.NumberFormatException ex)
          {
            System.out.println("Invalid ID format, try again");
          }
          break;
        case("New company"):
          resetButtons();
          companyPanel = companyFields();
          add(companyPanel, BorderLayout.NORTH);
          createCompany.setText("Save company");
          remove(jobPanel);
          remove(managerPanel);
          break;
        case("Save company"):
          if (companyFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          Company c = new Company();
          try
          {
            c.companyName = companyName.getText();
            c.numEmployees = ((Number)numEmployees.getValue()).intValue();
            c.yearlyRevenue = ((Number)revenue.getValue()).floatValue();
            c.stockPrice = ((Number)stockPrice.getValue()).floatValue();
            createCompany.setText("New company");
          }
          catch (Exception ex)
          {
            System.out.println("Error: " + ex);
          }
          break;
        case("New manager"):
          resetButtons();
          managerPanel = managerFields();
          add(managerPanel, BorderLayout.NORTH);
          createManager.setText("Save manager");
          remove(jobPanel);
          remove(companyPanel);
          break;
        case("Save manager"):
          if (managerFieldsEmpty())
          {
            JOptionPane.showMessageDialog(null, "All fields must have data.");
            return;
          }
          Manager m = new Manager();
          try
          {
            m.managerName = managerName.getText();
            m.yearsAtCompany = ((Number)yearsAtCompany.getValue()).intValue();
            createManager.setText("New manager");
          }
          catch (Exception ex)
          {
            System.out.println("Error: " + ex);
          }
        case("Delete company"):
          Panel deletePanel = new Panel();
          resetButtons();
          deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
          deletePanel.add(new JLabel("Company ID to be deleted"), "align label");
          deletePanel.add(cID, "wrap");
          add(deletePanel, BorderLayout.NORTH);
          deleteCompany.setText("Confirm delete company");
          break;
        case("Confirm delete company"):
          deleteCompany.setText("Delete company");
          //@TODO call main's delete call
          break;
        case("Delete manager"):
          resetButtons();
          deletePanel = new Panel();
          deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
          deletePanel.add(new JLabel("Manager ID to be deleted"), "align label");
          deletePanel.add(jID, "wrap");
          add(deletePanel, BorderLayout.NORTH);
          deleteManager.setText("Confirm delete manager");
          break;
        case("Confirm delete manager"):
          deleteManager.setText("Delete manager");
          //@TODO call main's delete call
          break;
        case("Delete job"):
          resetButtons();
          deletePanel = new Panel();
          deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.Y_AXIS));
          deletePanel.add(new JLabel("Job ID to be deleted"), "align label");
          deletePanel.add(jID, "wrap");
          add(deletePanel, BorderLayout.NORTH);
          deleteJob.setText("Confirm delete job");
          break;
        case("Confirm delete job"):
          deleteJob.setText("Delete job");
          //@TODO call main's delete call
          break;
        case("Display all jobs"):
          break;
        case("Search jobs"):
          add(searchInfo(), BorderLayout.NORTH);
          searchJobs.setText("Search");
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
          break;
        default:
          break;
      }
    }
  }
}
