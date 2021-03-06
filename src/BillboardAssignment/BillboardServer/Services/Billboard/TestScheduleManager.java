package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.Services.AuthAndUserDatabaseTesting.FatherTester;
import BillboardAssignment.BillboardServer.Services.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.Services.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.Services.User.InsufficentPrivilegeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScheduleManager extends FatherTester {

    private ScheduleManager scheduleManager;

    @BeforeEach
    void setupScheduleManager() {
        Queryable<Schedule> scheduleDatabase = new DatabaseArray<Schedule>();
        try {
            scheduleDatabase.initialiseDatabase("Schedules");
        }
        catch (Exception e){

        }

        scheduleManager = new ScheduleManager(scheduleDatabase, userManager);

    }

    @Test
    public void addToSchedule() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException {
        scheduleManager.addToSchedule("first",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "00:01", "11:00", "admin", adminKey);
        scheduleManager.addToSchedule("test",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "10:00", "11:00", "admin", adminKey );

        assertEquals(scheduleManager.getAllSchedules().get(0).name, "first");
        assertEquals(scheduleManager.getAllSchedules().get(1).name, "test");
    }

    @Test
    public void removeFromSchedule() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, NoSuchFieldException {
        scheduleManager.addToSchedule("test",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "10:01", "11:00", "admin", adminKey);
        scheduleManager.addToSchedule("first",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "00:01", "11:00", "admin", adminKey);
        scheduleManager.addToSchedule("test",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "10:00", "11:00", "admin", adminKey);

        scheduleManager.removeFromSchedule("test", adminKey);

        assertEquals(scheduleManager.getAllSchedules().size(), 1);
        assertEquals(scheduleManager.getAllSchedules().get(0).name, "first");

        scheduleManager.removeFromSchedule("first", adminKey);
        assertEquals(scheduleManager.getAllSchedules().size(), 0);
    }

    @Test
    public void scheduleFirstBillboard() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, NoSuchFieldException {
        scheduleManager.scheduleFirstBillboard();

        assertEquals(scheduleManager.getAllSchedules().size(), 1);
        assertEquals(scheduleManager.getAllSchedules().get(0).name, "first");
    }

    @Test
    public void scheduledBillboard() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseNotAccessibleException, DatabaseLogicException, DatabaseObjectNotFoundException, NoSuchFieldException {
        scheduleManager.addToSchedule("test",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "11:01", "23:59","admin", adminKey);
        scheduleManager.addToSchedule("first",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "00:01", "11:00", "admin", adminKey);
        scheduleManager.addToSchedule("test",  LocalDate.now().getDayOfWeek().name().substring(0,1) + LocalDate.now().getDayOfWeek().name().substring(1).toLowerCase() , "10:00", "11:00", "admin", adminKey);

        assertEquals(scheduleManager.scheduledBillboard(), "test");
        scheduleManager.removeFromSchedule("test", adminKey);
        scheduleManager.removeFromSchedule("first", adminKey);
        assertEquals(scheduleManager.scheduledBillboard(), "");
    }


}
