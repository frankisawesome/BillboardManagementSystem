package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Services.Authentication.*;
import BillboardAssignment.BillboardServer.Services.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.Services.User.UserManager;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleManager {
    public Queryable<Schedule> scheduleDatabase;
    private UserManager userManager;

    public ScheduleManager(Queryable<Schedule> scheduleDatabase, UserManager userManager) {
        this.scheduleDatabase = scheduleDatabase;
        this.userManager = userManager;
    }

    // Add a new instance of a billboard to the schedule
    public Schedule addToSchedule(String billboardName, String scheduleDay, String startTime, String endTime, UserSessionKey key ) throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {
        userManager.checkSessionKeyPrivileges(key, UserPrivilege.ScheduleBillboards);
        int ID = scheduleDatabase.getMaxID() + 1;

        Schedule addBillboard = new Schedule (ID, billboardName, scheduleDay, LocalTime.parse(startTime), LocalTime.parse(endTime));

        scheduleDatabase.addObject(addBillboard);

        return addBillboard;
    }

    // When a billboard is removed from the system, remove all instances of it from scheduling
    public Queryable<Schedule> removeFromSchedule(String billboardToRemove, UserSessionKey key) throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, NoSuchFieldException, OutOfDateSessionKeyException, InsufficentPrivilegeException, IncorrectSessionKeyException {
        userManager.checkSessionKeyPrivileges(key, UserPrivilege.ScheduleBillboards);
        LocalTime fakeTime = LocalTime.parse("00:00");
        Schedule dummy = new Schedule(111, "name", "day", fakeTime, fakeTime);
        // Remove all billboards where name == billboardToRemove
        ArrayList<Schedule> Remove = scheduleDatabase.getWhere("name", billboardToRemove, dummy);
        if (Remove.size() > 0) {
            for (Schedule billboard : Remove) {
                scheduleDatabase.removeObject(billboard.id);
            }
        }

        return scheduleDatabase;
    }

    public ArrayList<Schedule> getAllSchedules() throws DatabaseNotAccessibleException {
        return scheduleDatabase.getAllObjects();
    }

    public void scheduleFirstBillboard() throws DatabaseNotAccessibleException, DatabaseLogicException {
        int ID = scheduleDatabase.getMaxID() + 1;

        Schedule addBillboard = new Schedule (ID, "first", LocalDate.now().getDayOfWeek().name(), LocalTime.parse("00:00"), LocalTime.parse("23:59"));

        scheduleDatabase.addObject(addBillboard);
    }

    public boolean checkIfScheduled(String name, UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, NoSuchFieldException {
        userManager.checkSessionKeyPrivileges(key, UserPrivilege.ScheduleBillboards);

        ArrayList<Schedule> schedules = scheduleDatabase.getWhere("name", name, new Schedule(0, "", "", LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        if (schedules.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    // Determine the billboard to be displayed at the current time
    public String scheduledBillboard() throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException {
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
            if (schedules.get(i).day.equals(currentDay) && /* Is the day of the schedule today? */
                    (current.isAfter(schedules.get(i).start) || current.equals(schedules.get(i).start)) /* Is the start time <= Right now? */ &&
                    current.isBefore(schedules.get(i).end) /* Is the end time > right now?*/) {
                schedulesToShowShortlist.add(schedules.get(i));
            }
        }

        /* If there are no billboards at this time then return an empty string */
        if (schedulesToShowShortlist.size() == 0){
            return "";
        }
        else {
            // Get the billboard with the latest starting time

            Schedule currentBoard = schedulesToShowShortlist.get(0);

            for (Schedule billboard : schedulesToShowShortlist) {
                if (billboard.start.isAfter(currentBoard.start) || billboard.start.equals(currentBoard.start)) {
                    currentBoard = billboard;
                }
            }

            return currentBoard.name;
        }
    }


}
