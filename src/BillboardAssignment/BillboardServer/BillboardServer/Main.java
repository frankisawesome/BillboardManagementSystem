package BillboardAssignment.BillboardServer.BillboardServer;

public class Main {
    public static void main (String[] args) {
        try {
            BillboardServer server = new BillboardServer(3005);
            server.start();
        }
        catch (Exception e) {
            System.out.print("Booger");
        }
    }
}
