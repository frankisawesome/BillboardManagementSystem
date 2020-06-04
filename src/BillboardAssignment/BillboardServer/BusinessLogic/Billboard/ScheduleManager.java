package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardControlPanel.Login;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.*;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Database.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleManager {
    public Queryable<Schedule> scheduleDatabase;

    public ScheduleManager(Queryable<Schedule> scheduleDatabase) {
        this.scheduleDatabase = scheduleDatabase;
    }

    // Add a new instance of a billboard to the schedule
    public Schedule addToSchedule(String billboardName, String scheduleDay, String startTime, String endTime ) throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {

        int ID = scheduleDatabase.getMaxID() + 1;

        Schedule addBillboard = new Schedule (ID, billboardName, scheduleDay, LocalTime.parse(startTime), LocalTime.parse(endTime));

        scheduleDatabase.addObject(addBillboard);

        return addBillboard;
    }

    // When a billboard is removed from the system, remove all instances of it from scheduling
    public Queryable<Schedule> removeFromSchedule(String billboardToRemove) throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, NoSuchFieldException {
        LocalTime fakeTime = LocalTime.parse("00:00");
        Schedule dummy = new Schedule(111, "name", "day", fakeTime, fakeTime);
        // Remove all billboards where name == billboardToRemove
        ArrayList<Schedule> Remove = scheduleDatabase.getWhere("name", billboardToRemove, dummy);
        for (Schedule billboard : Remove) {
            scheduleDatabase.removeObject(billboard.id);
        }

        return scheduleDatabase;
    }

    // Determine the billboard to be displayed at the current time
    public Schedule scheduledBillboard() throws DatabaseNotAccessibleException {
        LocalDateTime currentDayTime = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("EEEE HH:mm");
        String formattedDate = currentDayTime.format(myFormatObj);
        String currentDay = formattedDate.split(" ")[0];
        String currentTime = formattedDate.split(" ")[1];

        LocalTime current = LocalTime.parse(currentTime);

        // get all billboards where day == current day and time is between start and end

        ArrayList<Schedule> schedules = scheduleDatabase.getAllObjects();
        ArrayList<Schedule> schedulesToShowShortlist = new ArrayList<>();

        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).day == currentDay && /* Is the day of the schedule today? */
                    (current.isAfter(schedules.get(i).start) || current.equals(schedules.get(i).start)) /* Is the start time <= Right now? */ &&
                    current.isBefore(schedules.get(i).end) /* Is the end time > right now?*/) {
                schedulesToShowShortlist.add(schedules.get(i));
            }
        }

        // Get the billboard with the latest starting time
        Schedule currentBoard = schedules.get(0);

        for (Schedule billboard : schedules) {
            if (billboard.start.isAfter(currentBoard.start) || billboard.start.equals(currentBoard.start)) {
                currentBoard = billboard;
            }
        }

        return currentBoard;
    }
}
