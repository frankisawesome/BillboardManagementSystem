package BillboardAssignment.Authentication;

/**
 * A class to group together data that will be stored in the passwords database, after hashing.
 * Namely the user ID, salt and twice hashed password. Consumed by PasswordManager
 * @see PasswordManager
 * @see UserAuthDataInput
 */
public class UserAuthDataOutput extends UserAuthDataInput {

    public byte[] twiceHashedPassword;

    /**
     * Byte array containing the salt that was used to re-hash the given hashed password
     */
    public byte[] salt;

    /**
     * Get the name of the object entity. E.G. If user data is being stored, then this should return "User".
     * This will be used to choose what database the object will be stored in.
     * @return A descriptor of the entity that is being stored.
     */
    public static String getEntityName(){
        return "UserAuthDataOutput";

    }

    /**
     * Constructor
     * @param twiceHashedPassword
     * @param salt
     * @param ID
     */
    public UserAuthDataOutput(byte[] twiceHashedPassword, byte[] salt, int ID){
        super(ID);

        this.twiceHashedPassword = twiceHashedPassword;

        this.salt = salt;

    }

}
