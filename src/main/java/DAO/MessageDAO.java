package DAO;

// Java Built-in Imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Imports of classes from this project
// import Model.Account; // To enable access to the Account class and the ability to create Account objects
import Model.Message; // To enable access to the Message class and the ability to create Message objects
import Util.ConnectionUtil; // To enable getting access to the database connection

/*
 *  This class will create methods that handle the transformation of data between the Java Message objects and the rows in the message table in the database. The database already contains a message table.
 */
public class MessageDAO {

    /**
     * METHOD FOR retrieving all messages from the database
     * 
     * @return all messages from the database
     * */
    public List<Message> getAllMessages() {

        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        // Create empty arraylist for the messages
        List<Message> messages = new ArrayList<>();

        try {

            // String for SELECT statement to retrieve all messages
            String sql = "SELECT * FROM message;";

            // Create PreparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Execute the query and store as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();

            // Iterate through the results and add each to the messages list
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")); // Should time posted by Long or Int?
                messages.add(message);
            }

        } catch (SQLException e) {
            e.getStackTrace();
        }
        return messages;
    }

// -------------------------------------------------------------------------------------

    /**
     * **METHOD** FOR retrieving a message from the database by message_id.
     * 
     * @param message_id the id for a specific message
     * @return message
     */
    public Message getMessageById(int message_id) {

        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        // Create empty arraylist for the messages
        Message myMessage = new Message();

        try {

            // Create variable to hole sql SELECT statement
            String sql = "SELECT * FROM message WHERE message_id = ?;";

            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set message_id to replace the ? in the preparedStatement object
            preparedStatement.setInt(1, message_id);

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();

            // Traverse through results to get each column, even if empty
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                myMessage = message;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        // Return the message
        return myMessage;
    } 

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR creating new messages
     * 
     * @param message an object containing a message, which does not contain a message_id
     * @return null
     */
    public Message insertMessage(Message message) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        try {
            // String variable for sql statement
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";

            // Prepared Statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Prepared Statement set methods
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            // Execute query
            preparedStatement.executeUpdate();

            // Get the keys and result set and store as ResultSet
            ResultSet rsKeys = preparedStatement.getGeneratedKeys();

            String str = "";

            // Get the id and return new Message object if the message_text is not blank
            if(rsKeys.next() && (!(message.getMessage_text().equals(str)))) {
                int auto_gen_id = (int) rsKeys.getLong(1);
                return new Message(auto_gen_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR checking if a message exists in the
     * 
     * @param message_id
     * @return
     */
    public boolean messageExists(int message_id) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();
        
        // Boolean variable for telling if username appears in the database.
        boolean userExists = false;

        try {
            // SQL statement: Look for row with current username in the database
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            
            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Prepared statement methods (dynamically set username for query)
            preparedStatement.setInt(1, message_id);

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();
            
            // If any rows returned, user already exists
            if(rs.next()) {
                userExists = true;
            } 

        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }
        return userExists;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR updating a message in the database by message_id
     * 
     * @param message_id the id for a specific message
     * @param message an object containing a message, which does not contain a message_id
     */
    public void updateMessage(int message_id, Message message) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            // String variable for sql statement
            String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?;";
            
            // Prepared Statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            // Prepared Statement set methods
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.setInt(4, message_id);
            
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
// -------------------------------------------------------------------------------------

    // 
    /**
     * METHOD FOR deleting a message from the database by message_id
     * 
     * @param message_id the id of a message
     */
    public void deleteMessage(int message_id){
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        try {
            
            // String variable for sql statement
            String sql = "DELETE FROM message WHERE message_id = ?;";

            // Prepared Statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Prepared Statement set methods
            preparedStatement.setInt(1, message_id);

            // Execute query
            preparedStatement.executeUpdate();

        } catch(SQLException e) {
            System.out.println(e.getMessage());

        }
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR retrieving all messages created by a particular account_id
     * 
     * @param account_id
     * @return
     */
    public List<Message> getUserMessages(int account_id) {
        // Establish connection
        Connection connection = ConnectionUtil.getConnection();

        // Create empty arraylist for the messages
        List<Message> messages = new ArrayList<>();

        try {
            // Create variable to hold sql SELECT statement that returns all messages for 
            String sql = "SELECT * FROM message WHERE posted_by = ?;";

            // Create preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set message_id to replace the ? in the preparedStatement object
            preparedStatement.setInt(1, account_id);

            // Execute the query and store it as ResultSet object
            ResultSet rs = preparedStatement.executeQuery();

            // Traverse through results to get each column
            while(rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getInt("time_posted_epoch"));
                messages.add(message);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
