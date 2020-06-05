package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Database.Identifiable;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Class for the storage of billboard schedules
 */
public class Schedule implements Identifiable, Serializable {
    public int id;
    public String name;
    public String day;
    public LocalTime start;
    public LocalTime end;

    public Schedule (int ID, String billboardName, String scheduledDay, LocalTime startTime, LocalTime endTime) {
        this.id = ID;
        this.name = billboardName;
        this.day = scheduledDay;
        this.start = startTime;
        this.end = endTime;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void changeID(int newID) {
        id = newID;
    }
}
