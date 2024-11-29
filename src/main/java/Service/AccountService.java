package Service;

// Imports classes from this project
import Model.Account;
import DAO.AccountDAO;

public class AccountService {

    // Reference variable of AccountDAO type
    AccountDAO accountDAO;

    // No args constructor to instantiate a plain AccountDAO
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Constructor for AccountService when an AccountDAO is provided
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

// -------------------------------------------------------------------------------------

// CHECK if we need to do this logic on the API instead, or send messages through API instead of console
    /**
     * METHOD FOR registering qualifying user accounts. This method will use both the 
     * userExists() and insertAccount() methods from the AccountDAO class.
     * The account qualifies if the username is not blank, the password is at least 
     * 4 characters long, and an Account with that username does not already exist.
     * * 
     * @param account an Account object, which does not contain an account_id
     * @return the new account
     */
    public Account addAccount(Account account) {

        // Create new account object with values of 0 or null
        Account newAccount = new Account();

        if(accountDAO.userExists(account) == true) {
            System.out.println("User account already exists. Try another username.");
        } else if (account.getUsername() == "") {
            System.out.println("Please, enter a username.");
        } else if (account.getPassword().length() < 4) {
            System.out.println("Password must be at least 4 characters long.");
        } else {
            // Set values of the object to the inserted account values
            newAccount = accountDAO.insertAccount(account);
        }
        return newAccount;
    }

// -------------------------------------------------------------------------------------

    /**
     * METHOD FOR using AccountDAO to verify the user login credentials
     * 
     * @param account an Account object containing the username and password entered
     * @return true if the username and password are correct, otherwise false
     */
    public boolean verifyUser(String username, String password) {

        boolean verified = accountDAO.verifyUser(username, password);

        return verified;
    }

// ----------------------------------------------------------------------------------------

    /**
     * 
     * @param username The username entered
     * @param password the password entered
     * @return the account, including account_id, username & password
     */
    public Account getAccount(String username, String password) {
        
        Account account = new Account();

        account = accountDAO.getAccount(username, password);

        return account;
    }
}
