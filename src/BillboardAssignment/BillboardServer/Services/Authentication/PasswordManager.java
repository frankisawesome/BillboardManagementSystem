package BillboardAssignment.BillboardServer.Services.Authentication;

import BillboardAssignment.BillboardServer.Database.DatabaseLogicException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.BillboardServer.Database.Queryable;
import BillboardAssignment.BillboardServer.Services.User.User;
import BillboardAssignment.BillboardServer.Services.User.UserDataInput;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Class for the storage and management of user passwords and salts
 */
public class PasswordManager {

    public Queryable<User> passwordDatabase;

    /**
     * Constructor for the password manager class
     *
     * @param passwordDatabase Takes an input of the database that we wish to link to the password manager. Stores objects
     *                         of type User (Essentially just UserID + salt + twicehashedpassword, + some other things we don't need)
     */
    public PasswordManager(Queryable<User> passwordDatabase) {
        this.passwordDatabase = passwordDatabase;
    }

    /**
     * Generates a random salt, hashes the input byte array, and then returns a new object with the new hashed password
     *
     * @param user A UserDataInput object that contains an ID and onceHashedPassword
     * @return Input object plus the new fields salt and the twiceHashedPassword, ready for storage. The once hashed password is not in the output object
     */
    public User hashNewPassword(UserDataInput user) {

        /* Create a new random salt generator and use it to generate a salt for the given user */
        SecureRandom randomNumGenerator = new SecureRandom();
        int size_of_salt = 3;
        byte[] passwordSalt = new byte[size_of_salt];
        randomNumGenerator.nextBytes(passwordSalt);

        String passwordSaltString = null;
        try {
            passwordSaltString = new String(passwordSalt, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); /* Will never happen */
        }

        /* Hash the password */
        String twiceHashedPassword = hashPassword(user.getOnceHashedPassword(), passwordSaltString);

        return new User(user.getID(), twiceHashedPassword, passwordSaltString, user.getPrivileges(), user.getUsername());
    }

    /**
     * Returns a boolean indicating if the provided password and the hashed password on file match (after the input is hashed)
     *
     * @param user The user input object that holds the once hashed password and ID
     * @return Boolean indicating if the password credentials are correct
     * @throws DatabaseObjectNotFoundException If the user being checked doesnt exist in the database
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws IncorrectPasswordException      If the given password doesn't match the one we have on file
     */
    public User checkPasswordMatch(UserDataInput user) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectPasswordException {
        User userData = passwordDatabase.getObject(user.getID());

        /* Hash the input password using the user's salt */
        String hashedInputPassword = hashPassword(user.getOnceHashedPassword(), userData.salt);

        /* Make sure that the hashed input password matches the hashed password on file */
        if (!hashedInputPassword.equals(userData.twiceHashedPassword)) {
            throw new IncorrectPasswordException(user.getID(), user.getOnceHashedPassword());
        }
        return userData;
    }

    /**
     * Hash a given password using the SHA-512 algorithm
     *
     * @param password
     * @param salt
     * @return A byte array of the hashed password
     */
    private String hashPassword(String password, String salt) {

        /* Initialise the algorithm data or the IDE will get mad */
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) { /* This will never happen as SHA-512 is definitely an algorithm */
            e.printStackTrace();
        }

        /* Set the hashing also to use our salt */
        md.update(salt.getBytes());
        String hashedPassword = null;
        try {
            hashedPassword = new String(md.digest(password.getBytes()), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); /* Will never happen */
        }
        return hashedPassword;
    }

    /**
     * Change the password of a given user
     *
     * @param inputUser             The data of the user that we want to change the password of
     * @param onceHashedNewPassword The new password that we want to re-salt and hash and store
     * @throws DatabaseObjectNotFoundException If the user being checked doesnt exist in the database
     * @throws DatabaseNotAccessibleException  If the database can't be connected to
     * @throws DatabaseLogicException          If there exists two users with same ID in the database already (shouldn't happen)
     */
    public void changePassword(UserDataInput inputUser, String onceHashedNewPassword) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseLogicException {
        User oldUserData = passwordDatabase.getObject(inputUser.getID());
        passwordDatabase.removeObject(inputUser.getID());
        oldUserData.setOnceHashedPassword(onceHashedNewPassword);
        User newPasswordData = hashNewPassword(oldUserData);
        passwordDatabase.addObject(newPasswordData);

    }

}
