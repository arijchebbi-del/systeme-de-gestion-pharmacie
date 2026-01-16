package brainstorm.pharmacy_app.Main;

import java.util.*;
import java.sql.*;

public class App {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://127.0.0.1:3306/pharmacy_db";
		String user = "root";
		String password = "root";
		String query = "SELECT * FROM Produit"; 

		try (Connection conn = DriverManager.getConnection(url, user, password);
				Statement stmt = conn.createStatement();
				ResultSet res = stmt.executeQuery(query)) {

			System.out.println("Connexion réussie.");

			while (res.next()) {
				// example read
				String name = res.getString("name");
				int id = res.getInt("id");
				System.out.println(id + " | " + name);
			}

		} catch (SQLException e) {
			System.err.println("Erreur SQL: " + e.getMessage());
		}

	}
}