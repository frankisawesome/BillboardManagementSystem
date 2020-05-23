package BillboardAssignment.BillboardServer.BillboardServer;

public class Main {
    public static void main (String[] args) {
        try {
            BillboardServer server = new BillboardServer();
            server.run();
        }
        catch (Exception e) {
            System.out.print("Booger");
        }
    }
}
