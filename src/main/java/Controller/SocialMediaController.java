package Controller;

// Imports from Javalin and Jackson
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// Imports of Model and Service classes in this project
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    
    // Reference variables declared
    AccountService accountService;
    MessageService messageService;

    // No args constructor to instantiate both AccountService and MessageService
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::postAccountHandler); // Post for inserting accounts (registration)
        app.post("login", this::loginHandler); // Post for a user to verify login credentials
        app.post("messages", this::postMessageHandler); // Post for inserting messages
        app.get("messages", this::getAllMessagesHandler); // GET for retrieving all messages
        app.get("messages/{message_id}", this::getMessageByIdHandler); // GET for retrieving a message by message_id
        app.delete("messages/{message_id}", this::deleteMessageHandler); // DELETE a message by message_id
        app.put("messages/{message_id}", this::updateMessageHandler); // Put for updating messages
        app.get("accounts/{account_id}", this::getUserMessagesHandler); // GET all messages created by a certain user

        return app;
    }

// --------------------------------------------------------------------------------------------

    /**
     * HANDLER to post a new message (insert message into the database)
     * 
     * @param ctx app.post above makes ctx available for this method, handles http request and response
     * @throws JsonProcessingException
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        // Jackson ObjectMapper converst JSON of the POST request into a Message object
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);

        // If messageService returns a null (post unsuccessful) the API will return 400 message (client error).
        if(addedMessage == null) {
            ctx.status(400);
        // else, convert to JSON, if issue arises, throw JsonProcessingException
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

// --------------------------------------------------------------------------------------------    

    /**
     * HANDLER to post a new account (insert account into the database)
     * 
     * @param ctx app.post above makes ctx available for this method, handles http request and response
     * @throws JsonProcessingException
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        // Jackson ObjectMapper converts JSON of the POST request into a Account object
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        // If accountService returns a null (post unsuccessful) the API will return 400 account (client error).
        if(addedAccount.username == null) {
            ctx.status(400);
        // else, convert to JSON, if issue arises, throw JsonProcessingException
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }
    
// --------------------------------------------------------------------------------------------

    /**
     * Handles update requests
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        
        // Object mapper to read request body
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        // obtain message id, then use the id and message to update
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);

        System.out.println(updatedMessage);

        // If accountService returns a null (post unsuccessful) the API will return 400 account (client error).
        if (updatedMessage == null) {
            ctx.status(400);
        // else, convert to JSON, if issue arises, throw JsonProcessingException 
        } else {

        }
        ctx.json(mapper.writeValueAsString(updatedMessage));
    }

// --------------------------------------------------------------------------------------------

    // HANDLER FOR retrieving all messages
    /**
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

// --------------------------------------------------------------------------------------------

    /**
     * Handler for getting a message by id
     * 
     * @param ctx
     */
    private void getMessageByIdHandler(Context ctx) {
        
        // Get message id
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        // If the message exists, return the message
        if (messageService.messageExists(message_id) == true) {

            Message message = messageService.getMessageById(message_id);
            ctx.json(message);
        // if not, return empty response body
        } else {
            // return empty body
            String str = "";
            ctx.json(str);
        }
        
    }

// --------------------------------------------------------------------------------------------

    /**
     * METHOD FOR getting messages by a specific account id (user)
     * 
     * @param ctx
     */
    private void getUserMessagesHandler(Context ctx) {
            
        // obtain account id,
        int account_id = Integer.parseInt(ctx.pathParam("posted_by"));
        
        // Get the message by account_id
        ctx.json(messageService.getUserMessages(account_id));
    }

// --------------------------------------------------------------------------------------------

    /**
     * HANDLER FOR deleting a message by id
     * 
     * @param ctx
     */
    private void deleteMessageHandler(Context ctx) {

        // obtain message id,
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        // if message exists in the database, select the message and then delete it
        if (messageService.messageExists(message_id) == true) {
            ctx.json(messageService.getMessageById(message_id));
            messageService.deleteMessage(message_id);
            // if the message does not appear in the database, delete it anyway
        } else {
            messageService.deleteMessage(message_id);
        }
    }
// --------------------------------------------------------------------------------------------

    /**
     * Handler for verifying a user's username & password
     * 
     * @param ctx
     */
    private void loginHandler(Context ctx) {
        
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        // If the username and password are correct, "Login succcessful"
        if(accountService.verifyUser(username, password)) {
            ctx.result("Login successful!");
        // If not, Unauthorized response
        } else {
            throw new UnauthorizedResponse("Invalid credentials");
        }
    }
}