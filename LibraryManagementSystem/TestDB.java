import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "0786";
        try {
            System.out.println("Loading driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Getting connection...");
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Success! " + conn);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
