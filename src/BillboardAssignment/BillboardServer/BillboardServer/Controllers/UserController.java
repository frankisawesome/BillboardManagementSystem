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

public class UserController extends Controller{
    private UserManager userManager;

    private UserController(String message, UserManager userManager, HashMap<String, String> body) {
        super(message, body);
        this.userManager = userManager;
    }

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
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    private ServerResponse getPermissions() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")));
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            return new ServerResponse(privileges, "ok");
        });
    }

    private ServerResponse addPrivilege() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("idToFind")));
            UserPrivilege[] privileges =  userManager.getPermissions(user, key);
            UserPrivilege[] newPrivileges = new UserPrivilege[privileges.length + 1];
            for (int i = 0; i < privileges.length; i++) {
                newPrivileges[i] = privileges[i];
            }
            newPrivileges[privileges.length] = parsePrivilege(body.get("newPrivilege"));
            userManager.setPermissions(user, newPrivileges, key);
            return new ServerResponse(newPrivileges, "ok");
        });
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
            return errorResponse("Incorrect credentials");
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
            return errorResponse("Failed to logout, some error occured when trying to perform db operation");
        }
    }

    private ServerResponse create() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            UserDataInput user = new UserDataInput(Integer.parseInt(body.get("newUserId")), body.get("password"));
            userManager.createUser(user, key);
            return new ServerResponse("Success! User created", "ok");
        });
    }

    public static ServerResponse use(String message, UserManager manager, HashMap<String, String> body) {
        UserController controller = new UserController(message, manager, body);
        controller.handle();
        return controller.response;
    }
}
