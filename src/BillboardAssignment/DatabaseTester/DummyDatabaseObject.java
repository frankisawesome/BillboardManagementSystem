package BillboardAssignment.DatabaseTester;

import BillboardAssignment.Database.Identifiable;

/**
 * A fake object just for testing if the database methods work
 */

public class DummyDatabaseObject implements Identifiable {

    private int ID;
    /**
     * Get the unique identifier of the Identifiable object
     *
     * @return The integer identifier associated with the given object
     */
    @Override
    public int getID() {
        return ID;
    }

    /**
     * Change the given Identifiable object's ID to the supplied Integer ID
     *
     * @param newID The integer ID that we want to replace the old ID with
     * @return
     */
    @Override
    public void changeID(int newID) {
        ID = newID;
    }

    public DummyDatabaseObject(int ID){
        this.ID = ID;
    }

}
