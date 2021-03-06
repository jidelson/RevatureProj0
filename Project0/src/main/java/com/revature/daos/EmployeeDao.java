package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.revature.models.Employee;
import com.revature.utils.ConnectionUtil;

public class EmployeeDao implements EmployeeDaoInterface {
	
	@Override
	public List<Employee> getEmployees() {
		
		try(Connection conn = ConnectionUtil.getConnection()){ //try to establish a DB connection, so we can run a query
			
			ResultSet rs = null; //initialize an empty ResultSet that will store the results of our query 
			
			String sql = "SELECT * FROM \"DiabloFoods\".employeeBasicDetails;"; //write the query, assign it to a String variable
			
			Statement s = conn.createStatement(); //creating an object to send the query to our DB
			
			rs = s.executeQuery(sql); //execute the query (sql) using our Statement object (s), put it in our ResultSet (rs) 
			
			
			List<Employee> employeeList = new ArrayList<>(); //create a List that will be populated with the returned employees
			
			
			while(rs.next()) { //while there are results left in the ResultSet (rs)
				
				//Create a new Employee Object from each returned record
				Employee employee = new Employee(
						rs.getInt("employee_id"),
						rs.getString("f_name"),
						rs.getString("m_name"),
						rs.getString("l_name"),
						rs.getString("dob"),
						rs.getString("phone"),
						rs.getString("street_address"),
						rs.getString("city"),
						rs.getString("zip_code"),
						rs.getString("gender"),
						rs.getString("marital_status"),
						rs.getString("hire_date"),
						rs.getInt("role_id")
					);
				
				//add the newly created Employee object into the ArrayList of Employees
				employeeList.add(employee);

			}
			
			return employeeList; //Finally, if successful, return the List of Employees
			
		} catch (SQLException e) { //if something goes wrong accessing our data, it will get caught
			System.out.println("Something went wrong when trying to access your DB");
			e.printStackTrace();
		}
		return null; // java will yell if we don't have this because the try is not guaranteed to succeed
	}

	@Override
	public void addEmployee(Employee emp) {

		try(Connection conn = ConnectionUtil.getConnection()){
					
			// This is the way I want to get the current date in the appropriate format
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = new Date(); //new date from java.util package
			
			String currentDate = dateFormat.format(date); // make a String that represents today's date in the format we want (line 68)
			
			
			
			
			DateFormat dobFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			Date dob = new Date();
			
			String dateOfBirth = dobFormat.format(dob);
			
			
			
			
			// we're going to create a sql statement using parameters to insert a new Employee
			String sql = "INSERT INTO \"DiabloFoods\".employeeBasicDetails (f_name, m_name, l_name, dob, phone, street_address, city, zip_code, gender, marital_status, hire_date, role_id)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"; // these are parameters!!! We have to now specify the value of each "?"
			
			PreparedStatement ps = conn.prepareStatement(sql); // we use PreparedStatements for SQL commands with parameters
			
			// use the PreparedStatement object to insert values into the SQL query
			// the values will come from the Employee object we sent in
			// this requires two arguments, the number of the "?", and the value to give it
			ps.setString(1, emp.getF_name());
			ps.setString(2, emp.getM_name());
			ps.setString(3, emp.getL_name());
			ps.setDate(4, java.sql.Date.valueOf(dateOfBirth));
			ps.setString(5, emp.getPhone());
			ps.setString(6, emp.getStreet_address());
			ps.setString(7, emp.getCity());
			ps.setString(8, emp.getZip_code());
			ps.setString(9, emp.getGender());
			ps.setString(10, emp.getMarital_status());
			ps.setDate(11, java.sql.Date.valueOf(currentDate)); // this takes our Java Date, and turns it into a SQL Date.
			ps.setInt(12, emp.getRole_id());
			
			// this method actually executes the SQL command that we built
			ps.executeUpdate(); // we use executeUpdate for insertions, updates, and deletes.
			
			// send confirmation to the console if successful
			System.out.println("Employee " + emp.getF_name() + " created. Welcome Aboard!");
			
			
			
		} catch(SQLException e) {
			System.out.println("Add employee failed!");
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void changeRole(int empId, int roleId) {

		try(Connection conn = ConnectionUtil.getConnection()){
			
			// notice how there are no ResultSet object in methods that don't include select statements
			// because we aren't returning anything! Only chaning stuff in the DB, not getting data from it!
			
			String sql = "UPDATE \"DiabloFoods\".employeeBasicDetails SET role_id = ? WHERE employee_id = ?;";
			
			PreparedStatement ps = conn.prepareStatement(sql); // make a PreparedStatement using the SQL String we made
			
			// adding values to the wildcard parameters based on the user's input
			ps.setInt(1, roleId);
			ps.setInt(2, empId);
			
			ps.executeUpdate(); // Run the PreparedStatement now that we've given values to its parameters
			
			System.out.println("Employee role_id changed to: " + roleId);
			
			//foolproofing description
//			if(roleId == 2) {
//				System.out.println();
//			}
			
		} catch(SQLException e) {
			System.out.println("Change role failed!");
			e.printStackTrace();
		}
	}

	@Override
	public void removeEmployee(int empId, int roleId) {
		
		try(Connection conn = ConnectionUtil.getConnection()){
			
			String sql = "DELETE FROM \"DiabloFoods\".employeeBasicDetails WHERE employee_id = ?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setInt(1, empId);
			ps.executeUpdate();
			
			System.out.println("Get out of here employee # " + empId);
			
		} catch(SQLException e) {
			System.out.println("Delete employee failed!");
			e.printStackTrace();
		}
		
	}
	

}
