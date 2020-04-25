package BillboardAssignment.Authentication;

import BillboardAssignment.Database.DatabaseLogicException;
import BillboardAssignment.Database.DatabaseNotAccessibleException;
import BillboardAssignment.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.Database.Queryable;
import BillboardAssignment.User.User;
import BillboardAssignment.User.UserDataInput;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

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
        byte[] passwordSalt = new byte[16];
        randomNumGenerator.nextBytes(passwordSalt);

        /* Hash the password */
        byte[] twiceHashedPassword = hashPassword(user.getOnceHashedPassword(), passwordSalt);

        return new User(user.getID(), twiceHashedPassword, passwordSalt, user.getPrivileges());
    }

    /**
     * Returns a boolean indicating if the provied password and the hashed password on file match (after the input is hashed)
     *
     * @param user The user input object that holds the once hashed password and ID
     * @return Boolean indicating if the password credentials are correct
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectPasswordException
     */
    public User checkPasswordMatch(UserDataInput user) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectPasswordException {
        User userData = passwordDatabase.getObject(user.getID());

        /* Hash the input password using the user's salt */
        byte[] hashedInputPassword = hashPassword(user.getOnceHashedPassword(), userData.salt);

        /* Make sure that the hashed input password matches the hashed password on file */
        if (!Arrays.equals(hashedInputPassword, userData.twiceHashedPassword)) {
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
    private byte[] hashPassword(byte[] password, byte[] salt) {

        /* Initialise the algorithm data or the IDE will get mad */
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) { /* This will never happen as SHA-512 is definitely an algorithm */
            e.printStackTrace();
        }

        /* Set the hashing algo to use our salt */
        md.update(salt);

        byte[] hashedPassword = md.digest(password);

        return hashedPassword;
    }

    /**
     * Change the password of a given user
     *
     * @param inputUser             The data of the user that we want to change the password of, must have correct original password
     * @param onceHashedNewPassword The new password that we want to re-salt and hash and store
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectPasswordException
     * @throws DatabaseLogicException
     */
    public void changePassword(UserDataInput inputUser, byte[] onceHashedNewPassword) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseLogicException {
        checkPasswordMatch(inputUser);

        User oldUserData = passwordDatabase.getObject(inputUser.getID());
        passwordDatabase.removeObject(inputUser.getID());
        oldUserData.setOnceHashedPassword(onceHashedNewPassword);
        User newPasswordData = hashNewPassword(oldUserData);
        passwordDatabase.addObject(newPasswordData);

    }

}
