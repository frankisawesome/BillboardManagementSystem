package BillboardAssignment.BillboardServer;

public class ConfigReader {
    /**
     *
     * @param filePath relative path to the properties file
     * @return port as integer
     */
    public static int readPort (String filePath) {
        return 3005; //yay
    }

    /**
     *
     * @param filePath relative path to the properties file
     * @return new DbConfig object
     */
    public static DbConfig readDbConfig (String filePath) {
        return new DbConfig("", "", "", "");
    }
}
