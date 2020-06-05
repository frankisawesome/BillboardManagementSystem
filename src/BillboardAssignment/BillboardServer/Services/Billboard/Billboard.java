package BillboardAssignment.BillboardServer.Services.Billboard;

import BillboardAssignment.BillboardServer.Database.Identifiable;

import java.io.Serializable;

public class Billboard implements Identifiable, Serializable {
    private int id;
    public String name;
    public String xml;
    public int creatorId;


    public Billboard(String name, String xml, int creatorId) {
        this.name = name;
        this.xml = xml;
        this.creatorId = creatorId;
    }

    public Billboard(int id, String name, String xml, int creatorId) {
        this.id = id;
        this.name = name;
        this.xml = xml;
        this.creatorId = creatorId;
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
