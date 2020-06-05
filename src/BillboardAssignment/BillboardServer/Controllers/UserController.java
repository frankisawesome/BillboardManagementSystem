package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Server.UserData;
import BillboardAssignment.BillboardServer.Services.Authentication.IncorrectPasswordException;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.User.User;
import BillboardAssignment.BillboardServer.Services.User.UserDataInput;
import BillboardAssignment.BillboardServer.Services.User.UserManager;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Controller for all user related requests
 */
public class UserController extends Controller{
    private UserManager userManager;

    /**
     * Constructor, only used with the use static method
     * @param message request message
     * @param userManager user manager containing related db information
     * @param body request body
     */
    private UserController(String message, UserManager userManager, HashMap<String, String> body) {
        super(message, body);
        this.userManager = userManager;
    }

    /**
     * Invoke relevant private methods based on the request message, to populate the response field
     */
    @Override
    protected void handle() {
        switch (message) {
            case "login":
                response = login();
                break;
            case "logout":
                response = logout();
                break;
            case "create":
                response = create();
                break;
            case "get privileges":
                response = getPermissions();
                break;
            case "add privilege":
                response = addPrivilege();
                break;
            case "remove privilege":
                response = removePrivilege();
                break;
            case "change password":
                response = changePassword();
                break;
            case "list users":
                response = listUsers();
                break;
            case "delete user":
                response = deleteUser();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    /**
     * Static method for this singleton class, takes request information, handles the request and returns response
     * @param message request message
     * @param manager user manager
     * @param body request body
     * @return server response, of type any
     */
    public static ServerResponse use(String message, UserManager manager, HashMap<String, String> body) {
        UserController controller = new UserController(message, manager, body);
        controller.handle();
        return controller.response;
    }

    /**
     * gets the permissions in an array for a particular user
     * @return server response containing array of privileges
     */
    private ServerResponse getPermissions() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")), "");
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            return new ServerResponse(privileges, "ok");
        });
    }

    /**
     * Add a privilege for a given user
     * @return success or error message
     */
    private ServerResponse addPrivilege() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")), "");
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            UserPrivilege[] newPrivileges = new UserPrivilege[privileges.length + 1];
            for (int i = 0; i < privileges.length; i++) {
                if (privileges[i] == parsePrivilege(body.get("newPrivilege"))) {
                    return new ServerResponse("", "User already have the privilege");
                }
                newPrivileges[i] = privileges[i];
            }
            newPrivileges[privileges.length] = parsePrivilege(body.get("newPrivilege"));
            userManager.setPermissions(user, newPrivileges, key);
            return new ServerResponse(newPrivileges, "ok");
        });
    }

    /**
     * Remove a privilege from a given user
     * @return success or error message
     */
    private ServerResponse removePrivilege() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")), "");
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            ArrayList<UserPrivilege> newPrivileges = new ArrayList<UserPrivilege>();
            for (int i = 0; i < privileges.length; i++) {
                if (privileges[i] != parsePrivilege(body.get("privilegeToRemove"))) {
                    newPrivileges.add(privileges[i]);
                }
            }
            UserPrivilege[] newPrivilegesArr =  newPrivileges.toArray(new UserPrivilege[0]);
            userManager.setPermissions(user, newPrivilegesArr, key);
            return new ServerResponse(newPrivilegesArr, "ok");
        });
    }

    /**
     * Change the password for a given user
     * @return success or error message
     */
    private ServerResponse changePassword() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")), body.get("password"));
            userManager.setPassword(user, body.get("newPassword"), key);
            return new ServerResponse("Password changed!", "ok");
        });
    }

    /**
     * Log in with name and password(hashed by front end)
     * @return session key
     */
    private ServerResponse login() {
        try {
            int id = userManager.mapUsernameToID(body.get("username"));
            UserSessionKey sessionKey = userManager.login(new UserDataInput(id, body.get("password")));
            return new ServerResponse<UserSessionKey>(sessionKey, "ok");
        } catch (IncorrectPasswordException | DatabaseObjectNotFoundException e) {
            return errorResponse("Incorrect credentials");
        } catch (Exception e) {
            return new ServerResponse("", e.getMessage());
        }
    }

    /**
     * Log out a session key
     * @return boolean indicating success status
     */
    private ServerResponse logout() {
        try {
            UserSessionKey parsedSessionKey = reconstructKey();
            userManager.logout(parsedSessionKey);
            return new ServerResponse(true, "ok");
        } catch (DatabaseNotAccessibleException e) {
            return errorResponse("Failed to logout, some error occured when trying to perform db operation");
        }
    }

    /**
     * Create new user
     * @return success or error message
     */
    private ServerResponse create() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(userManager.getNextID(), body.get("password"), body.get("newUserName"));
            userManager.createUser(user, key);
            return new ServerResponse("Success! User created", "ok");
        });
    }

    /**
     * Delete user from db
     * @return success or error message
     */
    private ServerResponse deleteUser() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")), "");
            userManager.deleteUser(user, key);
            return new ServerResponse("Success! User created", "ok");
        });
    }

    /**
     * Get a list of all users
     * @return array of userdata objects
     */
    private ServerResponse listUsers() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            User[] users = userManager.listUsers(key);
            UserData[] userData = new UserData[users.length];
            for (int i = 0; i < users.length; i++) {
                userData[i] = new UserData(users[i].getID(), users[i].username, users[i].getPrivileges());
            }
            return new ServerResponse(userData, "ok");
        });
    }
}
