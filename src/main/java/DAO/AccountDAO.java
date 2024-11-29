package DAO;

// Java Built-in Imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Imports of classes from this project
import Model.Account; // To enable access to the Account class and the ability to create Account objects
import Util.ConnectionUtil; // To enable getting access to the database connection

/*
 *  This class will create methods that handle the transformation of data between the Java Account objects and the rows in the account table in the database. The database already contains an accounts table.
 */
public class AccountDAO {

    /**
     * METHOD FOR inserting new user account upon registration
     * 
     * @param account an Account object containing an account, which does not contain an account_id
     * @return
     */
    public Account insertAccount(Account account) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        try {

            // String variable for sql statement to insert account
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";

            // Prepared Statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Prepared Statement set methods
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            // Execute query
            preparedStatement.executeUpdate();

            // Get the keys and result set and store as ResultSet
            ResultSet rsKeys = preparedStatement.getGeneratedKeys();

            // Get the id and return new account object
            if(rsKeys.next()) {
                int auto_gen_id = (int) rsKeys.getLong(1);
                return new Account(auto_gen_id, account.getUsername(), account.getPassword());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

// --------------------------------------------------------------------------------------
    
    /**
     * METHOD FOR checking if a username already exists in the database
     * 
     * @param account an Account object containing the username entered,
     * @return boolean value: true if username already exists, false if it does not exist
     */
    public boolean userExists(Account account) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();
        
        // Boolean variable for telling if username appears in the database.
        boolean userExists = false;

        try {
            // SQL statement: Look for row with current username in the database
            String sql = "SELECT username FROM account WHERE username = ?;";
            
            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Prepared statement methods (dynamically set username for query)
            preparedStatement.setString(1, account.getUsername());

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();
            
            // If any rows returned, user already exists
            if(rs.first()) {
                userExists = true;
            } 

        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }
        return userExists;
    }



// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR authentication: Verify the username & password in the database
     * 
     * @param account an Account object containing the username and password entered, 
     * which are compared to the actual username and password in the database
     * @return a boolean value: true if credentials match values in the database, otherwise returns false 
     */
    public boolean verifyUser(String username, String password) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        // Boolean variable for telling if username & password appear together.
        boolean confirmUser = false;

        try {
            // SQL statement: Look for row with current username & password
            String sql = "SELECT username, password FROM account WHERE (username = ?) AND (password = ?);";
            
            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Prepared statement methods (dynamically set username and password for query)
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();
            
            // If any rows returned, confirm user
            if(rs.first()) {
                confirmUser = true;
            } 

        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }
        return confirmUser;
    }

// ---------------------------------------------------------------------------------------

    /**
     * 
     * @param username The username entered
     * @param password the password entered
     * @return the account, including account_id, username & password
     */    
    public Account getAccount(String username, String password) {

        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        try {

            // Create variable to hole sql SELECT statement
            String sql = "SELECT * FROM account WHERE (username = ?) AND (password = ?);";

            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // Prepared statement methods (dynamically set username and password for query)
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();

            // Traverse through results to get each column, even if empty
            if(rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        // Return the message
        return null;
    } 
}
