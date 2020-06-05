package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Database.DatabaseLogicException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.BillboardServer.Database.Queryable;
import BillboardAssignment.BillboardServer.Services.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.Services.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Services.User.UserManager;
import BillboardAssignment.BillboardServer.Services.User.UserPrivilege;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class for managing billboard schedules
 *
 * Note that most validation is done in the front-end, so this class doesn't have a massive amount of error checking
 */
public class ScheduleManager {
    public Queryable<Schedule> scheduleDatabase;
    private UserManager userManager;

    public ScheduleManager(Queryable<Schedule> scheduleDatabase, UserManager userManager) {
        this.scheduleDatabase = scheduleDatabase;
        this.userManager = userManager;
    }

    // Add a new instance of a billboard to the schedule

    /**
     * Adds a new scheduled billboard time to the schedule.
     * @param billboardName Name of billboard to be scheduled
     * @param scheduleDay Day of billboard to be scheduled, in full Capitalised form: Monday, Tuesday, etc.
     * @param startTime Startime in format hh:mm
     * @param endTime end time in the format hh:mm
     * @param key Session key with correct user permissions
     * @return The schedule object that was added
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     * @throws DatabaseObjectNotFoundException
     * @throws InsufficentPrivilegeException
     * @throws OutOfDateSessionKeyException
     * @throws IncorrectSessionKeyException
     */
    public Schedule addToSchedule(String billboardName, String scheduleDay, String startTime, String endTime, UserSessionKey key ) throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, InsufficentPrivilegeException, OutOfDateSessionKeyException, IncorrectSessionKeyException {
        userManager.checkSessionKeyPrivileges(key, UserPrivilege.ScheduleBillboards);
        int ID = scheduleDatabase.getMaxID() + 1;

        Schedule addBillboard = new Schedule (ID, billboardName, scheduleDay, LocalTime.parse(startTime), LocalTime.parse(endTime));

        scheduleDatabase.addObject(addBillboard);

        return addBillboard;
    }


    /**
     * When a billboard is removed from the system, remove all instances of it from scheduling
     * @param billboardToRemove Name of billboard to be removed
     * @param key Session key with valid rights
     * @return current Schedule database
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseObjectNotFoundException
     * @throws NoSuchFieldException
     * @throws OutOfDateSessionKeyException
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     */
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

    /**
     * Get all schedules
     * @return All schedule objects in DB
     * @throws DatabaseNotAccessibleException
     */
    public ArrayList<Schedule> getAllSchedules() throws DatabaseNotAccessibleException {
        return scheduleDatabase.getAllObjects();
    }

    /**
     * Schedule the set initial placeholder billboard
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     * @throws DatabaseObjectNotFoundException
     * @throws NoSuchFieldException
     */
    public void scheduleFirstBillboard() throws DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, NoSuchFieldException {
        if (!checkIfScheduled("first")) {
            int ID = scheduleDatabase.getMaxID() + 1;

            Schedule addBillboard = new Schedule (ID, "first", LocalDate.now().getDayOfWeek().name(), LocalTime.parse("08:00"), LocalTime.parse("10:00"));

            scheduleDatabase.addObject(addBillboard);
        }
    }

    /**
     * Check if a given billboard has a schedule in the database
     * @param name Name of the billboard
     * @param key Session key with rights
     * @return iff billboard is in the schedule database
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws NoSuchFieldException
     */
    public boolean checkIfScheduled(String name, UserSessionKey key) throws OutOfDateSessionKeyException, DatabaseNotAccessibleException, InsufficentPrivilegeException, IncorrectSessionKeyException, DatabaseObjectNotFoundException, NoSuchFieldException {
        userManager.checkSessionKeyPrivileges(key, UserPrivilege.ScheduleBillboards);

        ArrayList<Schedule> schedules = scheduleDatabase.getWhere("name", name, new Schedule(0, "", "", LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        if (schedules.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if a given billboard has a schedule in the database - no key required version
     * @param name Name of the billboard
     * @return iff billboard is in the schedule database
     * @throws OutOfDateSessionKeyException
     * @throws DatabaseNotAccessibleException
     * @throws InsufficentPrivilegeException
     * @throws IncorrectSessionKeyException
     * @throws DatabaseObjectNotFoundException
     * @throws NoSuchFieldException
     */
    public boolean checkIfScheduled(String name) throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, NoSuchFieldException {
        try {
            ArrayList<Schedule> schedules = scheduleDatabase.getWhere("name", name, new Schedule(0, "", "", LocalTime.parse("00:00"), LocalTime.parse("00:00")));
        } catch (DatabaseObjectNotFoundException e){
            return false;
        }
        return true;
    }

    /**
     * Determine the current billboard to be displayed, depending on the scheduled times
     * @return Either the name of the billboard that should be displayed right now, or an empty string
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseObjectNotFoundException
     */
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
