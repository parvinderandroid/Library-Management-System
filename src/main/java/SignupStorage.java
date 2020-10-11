import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class SignupStorage {
	
	private static void createNewTable() {
		
		String url = "jdbc:sqlite:Books.sqlitedb";
		
		String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
				+ "	username text,\n"
				+ "	password text,\n"
				+ "	age integer,\n"
				+ "	gender text\n"
				+ ");";
		
		try (
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement()
			) {
				stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	
	}
	
	private static Connection connect() {
		String url = "jdbc:sqlite:Books.sqlitedb";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	static void writeData(String username, String password, int age, String gender) {
		
		createNewTable();
		
		String sql = "INSERT INTO accounts(username,password,age,gender) VALUES(?,?,?,?)";
		try (
			Connection conn = connect();
			PreparedStatement pstmt = conn.prepareStatement(sql)
			) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			pstmt.setInt(3, age);
			pstmt.setString(4, gender);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	static boolean usernameExists(String username) {
		
		createNewTable();
		
		username = "'" + username + "'";
		String sql = "SELECT * FROM accounts WHERE username = " + username ;
		
		try (
			Connection conn = connect();
			Statement stmt  = conn.createStatement();
			ResultSet rs	= stmt.executeQuery(sql)
			) {
				while (rs.next()) {
					return true;
				}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return false;

	}
	
	static boolean passwordMatches(String username, String password) {
		
		username = "'" + username + "'";
		password = "'" + password + "'";
		String sql = "SELECT * FROM accounts WHERE username = " + username + " AND password = " + password;
		
		try (
			Connection conn = connect();
			Statement stmt  = conn.createStatement();
			ResultSet rs	= stmt.executeQuery(sql)
			) {
				while (rs.next()) {
					return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
		
	}
	
}