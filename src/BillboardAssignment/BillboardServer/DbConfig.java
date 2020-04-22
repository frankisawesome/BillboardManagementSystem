package BillboardAssignment.BillboardServer;

/**
 * Stores database config information read from file
 */
public class DbConfig {
    protected String url;
    protected String schema;
    protected String username;
    protected String password;

    /**
     *
     * @param url db address
     * @param schema database name
     * @param username database username
     * @param password database password
     */
    public DbConfig(String url, String schema, String username, String password) {
        this.url = url;
        this.schema = schema;
        this.username = username;
        this.password = password;
    }

    /**
     *
     * @return array of configs, in order: url, schema, username, password
     */
    public String[] configArray() {
        return new String[] {url, schema, username, password};
    }
}
