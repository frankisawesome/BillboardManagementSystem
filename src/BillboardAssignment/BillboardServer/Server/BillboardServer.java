package BillboardAssignment.BillboardServer.Server;

import BillboardAssignment.BillboardServer.Controllers.BillboardController;
import BillboardAssignment.BillboardServer.Controllers.ScheduleController;
import BillboardAssignment.BillboardServer.Controllers.TestController;
import BillboardAssignment.BillboardServer.Controllers.UserController;
import BillboardAssignment.BillboardServer.Database.*;
import BillboardAssignment.BillboardServer.Services.Authentication.PasswordManager;
import BillboardAssignment.BillboardServer.Services.Authentication.SessionKeyManager;
import BillboardAssignment.BillboardServer.Services.Authentication.UserSessionKey;
import BillboardAssignment.BillboardServer.Services.Billboard.Billboard;
import BillboardAssignment.BillboardServer.Services.Billboard.BillboardManager;
import BillboardAssignment.BillboardServer.Services.Billboard.Schedule;
import BillboardAssignment.BillboardServer.Services.Billboard.ScheduleManager;
import BillboardAssignment.BillboardServer.Services.User.User;
import BillboardAssignment.BillboardServer.Services.User.UserManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * Containing server setup and running methods
 */
public class BillboardServer {
    private ServerSocket server;
    //These are public for test suites
    public UserManager userManager;
    public BillboardManager billboardManager;
    public ScheduleManager scheduleManager;

    /**
     * A main method for creating a server instance and running it
     * @param args
     */
    public static void main (String[] args) {
        try {
            BillboardServer server = new BillboardServer();
            server.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for a server instance, it will try to initialise the server using server.props - this file should be placed under the project root
     */
    public BillboardServer() {
        try {
            //initialising the server with props read from file
            init(getPortFromProps());
            //setUpDbs();
            setUpSqlDbs();
        }
        //catch file not found before IO as there's only one possible cause
        catch (FileNotFoundException e) {
            System.err.println("Server props file not found, include server.props under the project folder");
        } catch (Exception e) {
            //IO exception could be caused by a number of factors so we print out the actual exception message
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method reads props from a file and returns the port
     * @return the port specified in server.props
     * @throws FileNotFoundException server.props missing
     * @throws IOException
     */
    private int getPortFromProps() throws FileNotFoundException, IOException{
        Properties props = new Properties();
        FileInputStream in = null;
        in = new FileInputStream("./server.props");
        props.load(in);
        in.close();
        return Integer.parseInt(props.getProperty("port"));
    }

    /**
     * Initialises the socket server on a specified port
     * @param port and integer
     * @throws IOException
     */
    private void init(int port) throws Exception {
        server = new ServerSocket(port);
    }

    /**
     * Set up in memory db arrays for session keys and users for testing, also creating 'first user' for testing
     * @throws Exception
     */
    private void setUpDbs() throws Exception {
        //Queryable<User> database = new UserSQLiteDatabase();
        Queryable<User> database = new DatabaseArray<>();
        database.initialiseDatabase("Users");

        Queryable<UserSessionKey> database2 = new DatabaseArray<UserSessionKey>();
        database2.initialiseDatabase("SessionKeys");

        Queryable<Billboard> billboardDb = new DatabaseArray<>();
        billboardDb.initialiseDatabase("Billboards");

        Queryable<Schedule> scheduleDb = new DatabaseArray<>();
        scheduleDb.initialiseDatabase("Schedules");

        userManager = new UserManager(new PasswordManager(database), new SessionKeyManager(database2), database);
        scheduleManager = new ScheduleManager(scheduleDb, userManager);
        billboardManager = new BillboardManager(billboardDb, database2, database);
        userManager.createFirstUser();
        billboardManager.createFirstBillboard();
        scheduleManager.scheduleFirstBillboard();
    }

    /**
     * Set up sqlite dbs, used in prod
     * @throws Exception
     */
    private void setUpSqlDbs() throws Exception {
        Queryable<User> database = new UserSQLiteDatabase();
        database.initialiseDatabase("Users");

        Queryable<UserSessionKey> database2 = new DatabaseArray<UserSessionKey>();
        database2.initialiseDatabase("SessionKeys");

        Queryable<Billboard> billboardDb = new BillboardSQLiteDatabase();
        billboardDb.initialiseDatabase("Billboards");

        Queryable<Schedule> scheduleDB = new ScheduleSQLiteDatabase();
        scheduleDB.initialiseDatabase("Schedules");
        userManager = new UserManager(new PasswordManager(database), new SessionKeyManager(database2), database);
        scheduleManager = new ScheduleManager(scheduleDB, userManager);
        billboardManager = new BillboardManager(billboardDb, database2, database);
        userManager.createFirstUser();
        billboardManager.createFirstBillboard();
    }

    /**
     * Get the serverrequest object from input stream, and use one of the controller classes
     * @param input input stream received from socket connection
     * @return a server response to be sent back
     */
    private ServerResponse useControllers(InputStream input) {
        ServerResponse<String> response;
        try {
            //Parse the input stream into a request object
            ObjectInputStream objectInputStream = new ObjectInputStream(input);
            ServerRequest request = (ServerRequest) objectInputStream.readObject();
            System.out.println("Request received, message: " + request.requestMessage);
            //Use controllers' static use method
            switch (request.requestType) {
                case TEST:
                    response = TestController.use(request.requestMessage);
                    break;
                case USER:
                    response = UserController.use(request.requestMessage, userManager, request.requestBody);
                    break;
                case BILLBOARD:
                    response = BillboardController.use(request.requestMessage, request.requestBody, billboardManager, scheduleManager);
                    break;
                case SCHEDUELE:
                    response = ScheduleController.use(request.requestMessage, request.requestBody, scheduleManager);
                    break;
                default:
                    response = new ServerResponse<String>("", "Request type must be of requestType enum");
            }
        } catch (Exception e) {
            //Return exception message - note that invalid requests are handled by each controller and a valid response will be returned, so this theoratically should only catch errors if the program really breaks.
            response = new ServerResponse<String>("", e.getMessage());
        }
        return response;
    }

    /**
     * Runs the socket server in a single threaded loop
     * @throws IOException
     */
    public void run() throws IOException {
        while (true) {
            //tells the user the server is up and running
            System.out.println("Server listening");
            //handles incoming socket connections
            Socket socket = server.accept();
            String responseBody;
            System.out.println("connection from" + socket);
            // get the input stream from the connected socket
            ServerResponse response = useControllers(socket.getInputStream());

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            objectOutputStream.writeObject(response);
        }
    }
}
