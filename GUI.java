import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.lang.NumberFormatException;

public class GUI extends JPanel
{
  private JTextField jobField;
  private JTextField industry;
  private JTextField description;
  private JFormattedTextField cID;
  private JFormattedTextField mID;
  private JTextField type;
  private JButton createJob = new JButton("New job");

  private JTextField companyName;
  private JFormattedTextField numEmployees;
  private JFormattedTextField revenue;
  private JFormattedTextField stockPrice;
  private JButton createCompany = new JButton("New company");

  private JTextField managerName;
  //@TODO - experience? figure out how booleans work
  private JFormattedTextField yearsAtCompany;
  private JButton createManager = new JButton("New manager");

  private NumberFormat intFormat;

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
    panel.add(createJob);
    createJob.addActionListener(new ButtonHandler());
    panel.add(createCompany);
    createCompany.addActionListener(new ButtonHandler());
    panel.add(createManager);
    createManager.addActionListener(new ButtonHandler());
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
    panel.add(new JLabel("Manager ID"), "align label");
    panel.add(mID, "wrap");
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

  //@TODO add more exception handling for save cases
  //@TODO deal with repainting
  private class ButtonHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      // JPanel jobPanel = null;
      // JPanel companyPanel = null;
      // JPanel managerPanel = null;
      JPanel jobPanel, companyPanel, managerPanel;
      switch(e.getActionCommand())
      {
        case("New job"):
          jobPanel = jobFields();
          add(jobPanel, BorderLayout.NORTH);
          createJob.setText("Save job");
          createManager.setText("New manager");
          createCompany.setText("New company");
          // try { companyPanel.removeAll();}
          // catch (Exception ex) {}
          // try { managerPanel.removeAll(); }
          // catch (Exception ex) {}
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
            j.jobCreated();
            createJob.setText("New job");
          }
          catch (java.lang.NumberFormatException ex)
          {
            System.out.println("Invalid ID format, try again");
          }
          break;
        case("New company"):
          companyPanel = companyFields();
          add(companyPanel, BorderLayout.NORTH);
          createCompany.setText("Save company");
          createJob.setText("New job");
          createManager.setText("New manager");
          // try { remove(jobPanel); }
          // catch (Exception ex) {}
          // try { remove(managerPanel); }
          // catch (Exception ex) {}
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
          managerPanel = managerFields();
          add(managerPanel, BorderLayout.NORTH);
          createManager.setText("Save manager");
          createJob.setText("New job");
          createCompany.setText("New company");
          // try { remove(jobPanel); }
          // catch (Exception ex) {}
          // try { remove(companyPanel); }
          // catch (Exception ex) {}
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
        default:
          break;
      }
    }
  }
}
