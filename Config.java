import java.sql.Connection;
import java.sql.DriverManager;

public class Config
{
    public static Connection getMySqlConnection()
    {
        Connection mySqlConnection = null;
        String pw = ""; //Enter your password when editing :)

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:3306//*databaseName*/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=PST"; //url based off database
            mySqlConnection = DriverManager.getConnection(connectionUrl, "root", pw); //mysql connection
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mySqlConnection;
    }
}
