package brainstorm.pharmacy_app.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	//To make a connection you need create instance of DBConnection with user
	private static String  URL = "jdbc:mysql://127.0.0.1:3306/pharmacy_db";
	private static String ADMIN_USER="root";
	private static String EMP_USER="employee";
	private static String PASSWORD = "admin123";

	public static Connection getEmployeeConnection() throws SQLException {
		return DriverManager.getConnection(URL, EMP_USER, PASSWORD);
	}
	public static Connection getAdminConnection() throws SQLException {
		return DriverManager.getConnection(URL, ADMIN_USER, PASSWORD);
	}
}
