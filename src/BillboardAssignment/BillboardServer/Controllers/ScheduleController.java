package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.BillboardManager;
import BillboardAssignment.BillboardServer.BusinessLogic.Billboard.ScheduleManager;
import BillboardAssignment.BillboardServer.Server.ServerResponse;

import java.util.HashMap;

public class ScheduleController extends Controller {
    private ScheduleManager scheduleManager;

    private ScheduleController (String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        super(message, body);
        this.scheduleManager = scheduleManager;
    }
    @Override
    void handle() {

    }

    public static ServerResponse use(String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        ScheduleController controller = new ScheduleController(message, body, scheduleManager);
        controller.handle();
        return controller.response;
    }
}
