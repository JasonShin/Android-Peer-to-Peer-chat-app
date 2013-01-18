package org.konichiwa.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcImpl {

	private int dbResult;
	private String dbURL = "jdbc:mysql://localhost/konanalysis";
	private String userName = "root";
	private String password = "password";
	private String query = "SELECT * FROM application_status";
	private String query2 = "UPDATE application_status SET num_online_total = ";
	private Connection con;
	private Statement stm;

	public JdbcImpl() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			con = DriverManager.getConnection(dbURL, userName, password);
			
			System.out.println("connected!");
			stm = con.createStatement();
			// stm.executeQuery(query);
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				dbResult = rs.getInt(1);
				System.out.println(dbResult);
			}
			
			dbResult += 2;
			stm.executeUpdate(query2+dbResult);
			
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
