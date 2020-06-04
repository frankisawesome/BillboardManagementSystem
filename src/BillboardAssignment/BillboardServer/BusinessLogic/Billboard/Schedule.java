package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardServer.Database.Identifiable;

import java.time.LocalTime;

public class Schedule implements Identifiable {
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
        return 0;
    }

    @Override
    public void changeID(int newID) {

    }
}
