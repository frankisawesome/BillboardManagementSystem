package BillboardAssignment.BillboardServer.Server.Controllers;

import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.Billboard;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.BillboardManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class BillboardController extends Controller {
    private BillboardManager billboardManager;

    private BillboardController(String message, HashMap<String, String> body, BillboardManager billboardManager) {
        super(message, body);
        this.billboardManager = billboardManager;
    }
    protected void handle() {
        switch (message) {
            case "get one":
                response = new ServerResponse("<billboard background=\"#8996FF\">\n" +
                        "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/5fhToroJL0nMKvB/download\"/>\n" +
                        "</billboard>", "ok");
                break;
            case "create":
                response = create();
                break;
            case "list billboards":
                response = list();
                break;
            case "rename billboard":
                response = rename();
                break;
            case "edit billboard":
                response = edit();
                break;
            case "delete billboard":
                response = delete();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    private ServerResponse create() {
        return useDbTryCatch(() -> {
            Billboard billboard = new Billboard(body.get("billboardName"), body.get("content"),Integer.parseInt(body.get("keyId")));
            UserSessionKey key = reconstructKey();
            billboardManager.create(billboard, key);
            return new ServerResponse("Billboard created", "ok");
        });
    }

    private ServerResponse list() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            ArrayList<Billboard> billboards = billboardManager.list(key);
            return new ServerResponse(billboards, "ok");
        });
    }

    private ServerResponse delete() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.delete(Integer.parseInt(body.get("billboardId")), key);
            return new ServerResponse("Billboard deleted", "ok");
        });
    }

    private ServerResponse rename() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.rename(Integer.parseInt(body.get("billboardId")), body.get("newName"), key);
            return new ServerResponse("Rename success", "ok");
        });
    }

    private ServerResponse edit() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            billboardManager.edit(Integer.parseInt(body.get("billboardId")), body.get("newContent"), key);
            return new ServerResponse("Edit success", "ok");
        });
    }

    public static ServerResponse use(String message, HashMap<String, String> body, BillboardManager billboardManager) {
        BillboardController controller = new BillboardController(message, body, billboardManager);
        controller.handle();
        return controller.response;
    }
}