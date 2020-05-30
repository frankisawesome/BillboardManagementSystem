package BillboardAssignment.BillboardServer.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.BillboardServer.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.User.UserManager;

import java.util.HashMap;

public class BillboardController extends Controller {

    private BillboardController(String message, HashMap<String, String> body) {
        super(message, body);
    }
    protected void handle() {
        switch (message) {
            case "get one":
                response = new ServerResponse("<billboard background=\"#8996FF\">\n" +
                        "<picture url=\"https://cloudstor.aarnet.edu.au/plus/s/5fhToroJL0nMKvB/download\"/>\n" +
                        "</billboard>", "ok");
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    public static ServerResponse use(String message, HashMap<String, String> body) {
        BillboardController controller = new BillboardController(message, body);
        controller.handle();
        return controller.response;
    }
}
