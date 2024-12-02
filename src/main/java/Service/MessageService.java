package Service;

// Java Built-in Imports
import java.util.ArrayList;
import java.util.List;

// Imports of classes from this project
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    // Reference variable of MessageDAO type
    MessageDAO messageDAO;

    // No args constructor to instantiate a plain MessageDAO
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    // Constructor for MessageService when a MessageDAO is provided
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using the MessageDAO to ADD a new message to the database
     * 
     * @param message an object containing a message, which does not contain a message_id
     * @return the new message
     */
    public Message addMessage(Message message) {

        // Create new message object with values of 0 or null
        Message newMessage = new Message();

        // Set values of the object to the inserted message values
        newMessage = messageDAO.insertMessage(message);

        return newMessage;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using MessageDAO to UPDATE an existing message in the database.
     * 
     * @param message_id the id for a message to be found in the database
     * @param message an object containing a message, which does not contain a message_id
     * @return the updated (amended) message. Return null if the specified message_id does not exist in the database. 
     */
    public Message updateMessage(int message_id, Message message) {

        // Create new message object with values of 0 or null
        Message newMessage = new Message();
        
        String str = "";
        
        if (messageDAO.messageExists(message_id) == false) {
            System.out.println("No such message id exists.");
            newMessage = null;
        } else if (message.getMessage_text().equals(str)) {
            System.out.println("Message text must not be blank");
            newMessage = null;
        } else if (message.getMessage_text().length() > 255) {
            System.out.println("Message too long!");
            newMessage = null;
        } else {
            // Set values of the object to the inserted message values
            messageDAO.updateMessage(message_id, message);
            newMessage = getMessageById(message_id);
        }
        return newMessage;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using MessageDAO to RETRIEVE ALL messages from the database
     * 
     * @return all messages currently in the database
     */
    public List<Message> getAllMessages(){

        // Create empty arrayList for messages
        List<Message> messageList = new ArrayList<>();

        // Add all messages to teh list
        messageList.addAll(messageDAO.getAllMessages());

        return messageList;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using MessageDAO to REMOVE a message from the database
     * 
     * @param message_id the id pertaining to a message
     */
    public void deleteMessage(int message_id) {

        // if message exists
        // if(messageDAO.getMessageById(message_id) == null) {
        //     // Delete the message at the specified id
        //     messageDAO.deleteMessage(message_id);
        //     return messageDAO.getMessageById(message_id);
        // } else {
        //     messageDAO.deleteMessage(message_id);
        // }
        messageDAO.deleteMessage(message_id);
        // if message does not exist, return the message
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using MessageDAO to, by account_id, RETRIEVE all of a USER's messages
     * 
     * @param account_id the id for an account holder
     * @return a list of all messages created by that user
     */
    public List<Message> getUserMessages(int account_id) {

        // Add all of a user's messages to the list
        List<Message> userMessages = messageDAO.getUserMessages(account_id);

        return userMessages;
    }

// -------------------------------------------------------------------------------------

    // **METHOD** FOR using MessageDAO to RETRIEVE a message by id
    public Message getMessageById(int message_id) {

        // Obtain the message from the database by message_id
        Message message = messageDAO.getMessageById(message_id);
        return message;
    }


    public boolean messageExists(int message_id) {

        // Tell whether the message with specified id exists in the database
        boolean exists = messageDAO.messageExists(message_id);
        return exists;
    }
}

