package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.Billboard.ScheduleManager;

import java.util.HashMap;

/**
 * Schedulinbg controller
 */
public class ScheduleController extends Controller {
    private ScheduleManager scheduleManager;
    private ServerResponse response;

    /**
     * Take in shcedule manager
     * @param message request message
     * @param body request body
     * @param scheduleManager schedule manager
     */
    private ScheduleController (String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        super(message, body);
        this.scheduleManager = scheduleManager;
    }
    @Override
    void handle() {
        switch (message) {
            case "set":
                response = set();
                break;
            case "remove":
                response = remove();
                break;
            case "get schedule":
                response = getSchedule();
                break;
            case "schedule list":
                response = scheduleList();
                break;
            default:
                response = new ServerResponse("", "Request message invalid");
        }
    }

    /**
     * Set a schedule for a database
     * @return success or error message
     */
    private ServerResponse set() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            scheduleManager.addToSchedule(body.get("billboardName"), body.get("day"), body.get("startTime"), body.get("endTime"), body.get("creatorName"), key);
            return new ServerResponse("Schedule set", "ok");
        });
    }

    /**
     * Get al schedules
     * @return list of schedules
     */
    private ServerResponse scheduleList() {
        return useDbTryCatch(() -> new ServerResponse(scheduleManager.getAllSchedules(), "ok"));
    }

    /**
     * Removes schedule
     * @return success or error message
     */
    private ServerResponse remove() {
        return useDbTryCatch(() -> {
            UserSessionKey key = reconstructKey();
            scheduleManager.removeFromSchedule(body.get("billboardName"), key);
            return new ServerResponse("Schedule removed", "ok");
        });
    }

    /**
     * Get scheduled billboard
     * @return
     */
    private ServerResponse getSchedule() {
        return useDbTryCatch(() -> {
            String schedule = scheduleManager.scheduledBillboard();
            return new ServerResponse(schedule, "ok");
        });
    }

    /**
     * Uses the class by instantiating a controller, handles request and returns response
     * @param message request message
     * @param body request body
     * @param scheduleManager schedule manager
     * @return response
     */
    public static ServerResponse use(String message, HashMap<String, String> body, ScheduleManager scheduleManager) {
        ScheduleController controller = new ScheduleController(message, body, scheduleManager);
        controller.handle();
        return controller.response;
    }
}
