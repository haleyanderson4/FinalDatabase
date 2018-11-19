import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI extends JPanel
{
  private JTextField jobField;
  private JTextField industry;
  private JTextField description;
  private JButton createButton = new JButton("New job");

  public static void main(String[] args)
  {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.getContentPane().add(new GUI());
    f.setSize(600, 280);
    f.setVisible(true);
  }

  public GUI()
  {
    jobField = new JTextField(25);
    industry = new JTextField(25);
    description = new JTextField(100);
    setLayout(new BorderLayout(5, 5));
    //add(initFields(), BorderLayout.NORTH);
    add(initButtons(), BorderLayout.CENTER);
  }

  private JPanel initButtons()
  {
    JPanel panel = new JPanel();
    panel.add(createButton);
    createButton.addActionListener(new ButtonHandler());
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
    return panel;
  }

  private class ButtonHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      Job j = new Job();
      switch(e.getActionCommand())
      {
        case("New job"):
          add(jobFields(), BorderLayout.NORTH);
          createButton.setText("save");
        case("save"):
          j.jobTitle = jobField.getText();
          j.industry = industry.getText();
          j.description = description.getText();
          j.jobCreated();
        default:
          break;
      }
    }
  }
}
