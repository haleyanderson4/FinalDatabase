import java.sql.Connection;
import java.sql.DriverManager;

public class Config
{
    public static Connection getMySqlConnection()
    {
        Connection mySqlConnection = null;

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:3306/cpsc408?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST"; //url based off database
            mySqlConnection = DriverManager.getConnection(connectionUrl, "root", "dia84monD!$*"); //mysql connection
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mySqlConnection;
    }
}

