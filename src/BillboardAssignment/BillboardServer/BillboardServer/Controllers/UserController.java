package BillboardAssignment.BillboardServer.BillboardServer.Controllers;


import BillboardAssignment.BillboardServer.BillboardServer.ServerRequest;
import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectPasswordException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserDataInput;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserPrivilege;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;

import java.util.HashMap;

public class UserController {
    private String message;
    private HashMap<String, String> body;
    private UserManager userManager;
    private ServerResponse response;

    private UserController(String message, UserManager userManager, HashMap<String, String> body) {
        this.message = message;
        this.userManager = userManager;
        this.body = body;
    }

    private void Handle() {
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
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    private ServerResponse getPermissions() {
        try {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")));
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            return new ServerResponse(privileges, "ok");
        } catch (InsufficentPrivilegeException e) {
            return new ServerResponse("", "User doesn't have the privilege EditUser");
        } catch (IncorrectSessionKeyException e) {
            return new ServerResponse("", "Session key invalid");
        } catch (OutOfDateSessionKeyException e) {
            return new ServerResponse("", "Session key has expired");
        } catch (Exception e) {
            return new ServerResponse("", e.getMessage());
        }
    }

    private ServerResponse addPrivilege() {
        return new ServerResponse("", "");
    }

    private ServerResponse removePrivilege() {
        return new ServerResponse("", "");
    }

    private ServerResponse changePassword() {
        return new ServerResponse("", "");
    }

    private ServerResponse login() {
        try {
            UserSessionKey sessionKey = userManager.login(new UserDataInput(Integer.parseInt(body.get("id")), body.get("password")));
            return new ServerResponse<UserSessionKey>(sessionKey, "ok");
        } catch (IncorrectPasswordException | DatabaseObjectNotFoundException e) {
            return new ServerResponse("", "Incorrect id or password");
        } catch (Exception e) {
            return new ServerResponse("", e.getMessage());
        }
    }

    private ServerResponse logout() {
        try {
            UserSessionKey parsedSessionKey = reconstructKey();
            userManager.logout(parsedSessionKey);
            return new ServerResponse(true, "ok");
        } catch (DatabaseNotAccessibleException e) {
            return new ServerResponse("", "Failed to logout, some error occured when trying to perform db operation");
        }
    }

    private ServerResponse create() {
        try {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("newUserId")), body.get("password"));
            userManager.createUser(user, key);
            return new ServerResponse("Success! User created", "ok");
        } catch (InsufficentPrivilegeException e) {
            return new ServerResponse("", "Failed to create user, user doesn't have required privilage");
        } catch (IncorrectSessionKeyException e) {
            return new ServerResponse("", "Failed to create user, invalid session key");
        } catch (OutOfDateSessionKeyException e) {
            return new ServerResponse("", "Failed to create user, session key expired");
        } catch (Exception e) {
            return new ServerResponse("", "Failed to create user, this is the catch all exception - something horrible must've happened");
        }
    }

    public static ServerResponse use(String message, UserManager manager, HashMap<String, String> body) {
        UserController controller = new UserController(message, manager, body);
        controller.Handle();
        return controller.response;
    }

    private UserSessionKey reconstructKey() {
        return new UserSessionKey(Integer.parseInt(body.get("keyId")), body.get("key"));
    }
}
