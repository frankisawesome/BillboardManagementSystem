package BillboardAssignment.BillboardServer.BusinessLogic.Billboard;

import BillboardAssignment.BillboardServer.Database.Identifiable;

public class Billboard implements Identifiable {
    private int id;
    private String name;
    private String xml;
    private int creatorId;


    public Billboard(String name, String xml, int creatorId) {
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
