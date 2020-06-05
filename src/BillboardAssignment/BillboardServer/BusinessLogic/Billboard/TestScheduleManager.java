package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.Database.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestScheduleManager {
    ScheduleManager scheduleManager;

    // Test adding three billboards
    @Test
    void addToSchedule() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        System.out.println("Test adding billboards");
        Schedule test = scheduleManager.addToSchedule("Billboard", "Tuesday", "16:50", "17:00");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 2", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 3", "Tuesday", "09:50", "10:20");
        System.out.println(test.name);
    }

    // Test removing without any billboards in the database - Throws error currently due to unhandled exception in
    // getAllObjects method
    /*
    @Test
    void removeFromSchedule() throws DatabaseObjectNotFoundException, NoSuchFieldException, DatabaseNotAccessibleException, OutOfDateSessionKeyException, DatabaseLogicException, IncorrectSessionKeyException, InsufficentPrivilegeException {
        scheduleManager.removeFromSchedule("Billboard 2");
        ArrayList<Schedule> billboards = scheduleManager.scheduleDatabase.getAllObjects();
        for (Schedule billboard : billboards) {
            System.out.print(billboard.name);
        }
    }*/

    // Test removing with billboards
    @Test
    void removeFromSchedule2() throws DatabaseObjectNotFoundException, NoSuchFieldException, DatabaseNotAccessibleException, OutOfDateSessionKeyException, DatabaseLogicException, IncorrectSessionKeyException, InsufficentPrivilegeException {
        System.out.println("Test removing a billboard");
        Schedule test = scheduleManager.addToSchedule("Billboard", "Tuesday", "16:50", "17:00");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 2", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 3", "Tuesday", "09:50", "10:20");
        System.out.println(test.name);

        scheduleManager.removeFromSchedule("Billboard 2");
        ArrayList<Schedule> billboards = scheduleManager.scheduleDatabase.getAllObjects();
        for (Schedule billboard : billboards) {
            System.out.println(billboard.name);
        }
    }

    // Add two billboards, then remove one, then add another
    @Test
    void addThenRemove() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, NoSuchFieldException {
        System.out.println("Test adding and removing billboards");
        Schedule test = scheduleManager.addToSchedule("Billboard", "Tuesday", "16:50", "17:00");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 2", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 3", "Tuesday", "09:50", "10:20");
        System.out.println(test.name);

        scheduleManager.removeFromSchedule("Billboard 2");
        ArrayList<Schedule> billboards = scheduleManager.scheduleDatabase.getAllObjects();
        for (Schedule billboard : billboards) {
            System.out.println(billboard.name);
        }

        test = scheduleManager.addToSchedule("Billboard 4", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
    }

    // Test finding current billboard
    @Test
    void scheduledBillboard() throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, OutOfDateSessionKeyException, DatabaseLogicException, IncorrectSessionKeyException, InsufficentPrivilegeException {
        System.out.println("Test scheduled billboard");
        Schedule test = scheduleManager.addToSchedule("Billboard", "Tuesday", "16:50", "17:00");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 2", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 3", "Friday", "09:25", "09:50");
        System.out.println(test.name);

        String current = scheduleManager.scheduledBillboard();
        System.out.print("Current billboard: ");
        System.out.print(current);
    }

    // Test finding current billboard if there isn't one right now
    @Test
    void scheduledBillboard2() throws DatabaseNotAccessibleException, DatabaseObjectNotFoundException, OutOfDateSessionKeyException, DatabaseLogicException, IncorrectSessionKeyException, InsufficentPrivilegeException {
        System.out.println("Test without a scheduled billboard");
        Schedule test = scheduleManager.addToSchedule("Billboard", "Tuesday", "16:50", "17:00");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 2", "Tuesday", "12:30", "13:30");
        System.out.println(test.name);
        test = scheduleManager.addToSchedule("Billboard 3", "Friday", "09:25", "09:30");
        System.out.println(test.name);

        String current = scheduleManager.scheduledBillboard();
        System.out.print("Current billboard: ");
        System.out.print(current);
    }

    @BeforeEach
    void setUp() throws DatabaseNotAccessibleException {
        Queryable<Schedule> scheduleDatabase = new DatabaseArray<>();
        scheduleDatabase.initialiseDatabase("Schedule");
        scheduleManager = new ScheduleManager(scheduleDatabase);
    }
}
