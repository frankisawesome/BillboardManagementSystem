package BillboardAssignment.User;

public class User extends UserDataInput {

    public byte[] twiceHashedPassword;

    /**
     * Byte array containing the salt that was used to re-hash the given hashed password
     */
    public byte[] salt;

    /**
     * Constructor
     *
     * @param twiceHashedPassword
     * @param salt
     * @param ID
     */
    public User(int ID, byte[] twiceHashedPassword, byte[] salt, UserPrivilege[] privileges) {
        /** We don't ever store the once hashed password **/
        super(ID, "".getBytes(), privileges);

        this.twiceHashedPassword = twiceHashedPassword;

        this.salt = salt;
    }

    /**
     * Get the name of the object entity. E.G. If user data is being stored, then this should return "User".
     * This will be used to choose what database the object will be stored in.
     *
     * @return A descriptor of the entity that is being stored.
     */
    public static String getEntityName() {
        return "User";
        // TODO: Fix the whole automatic database name thingo
    }

    public void checkUserHasPriv(UserPrivilege[] privileges) throws InsufficentPrivilegeException {
        boolean foundPerm;
        for (int i = 0; i < privileges.length; i++) {
            foundPerm = false;
            for (int j = 0; j < this.getPrivileges().length; j++) {
                if (privileges[i] == this.getPrivileges()[j]) {
                    foundPerm = true;
                    break;
                }
            }

            if (!foundPerm) {
                throw new InsufficentPrivilegeException(privileges[i]);
            }
        }
    }

}
