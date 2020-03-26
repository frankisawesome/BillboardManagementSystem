package BillboardAssignment.Authentication;

import BillboardAssignment.Database.DatabaseLogicException;
import BillboardAssignment.Database.DatabaseNotAccessibleException;
import BillboardAssignment.Database.DatabaseObjectNotFoundException;
import BillboardAssignment.Database.Queryable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Class for the storage and management of user passwords and salts
 */
public class PasswordManager {

    public Queryable<UserAuthDataOutput> passwordDatabase;

    /**
     * Generates a random salt, hashes the input byte array, and then returns a new object with the new hashed password
     * @param user A UserAuthDataInput object that contains an ID and onceHashedPassword
     * @return An object with ID, salt and the twiceHashedPassword, ready for storage.
     */
    private UserAuthDataOutput hashNewPassword(UserAuthDataInput user){

        /* Create a new random salt generator and use it to generate a salt for the given user */
        SecureRandom randomNumGenerator = new SecureRandom();
        byte[] passwordSalt = new byte[16];
        randomNumGenerator.nextBytes(passwordSalt);

        /* Hash the password */
        byte[] twiceHashedPassword = hashPassword(user.getOnceHashedPassword(), passwordSalt);

        return new UserAuthDataOutput(twiceHashedPassword, passwordSalt, user.getID());
    }

    /**
     * Returns a boolean indicating if the provied password and the hashed password on file match (after the input is hashed)
     * @param user The user input object that holds the once hashed password and ID
     * @return Boolean indicating if the password credentials are correct
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectPasswordException
     */
    public boolean checkPasswordMatch(UserAuthDataInput user) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectPasswordException {
        UserAuthDataOutput userData = passwordDatabase.getObject(user.getID());

        /* Hash the input password using the user's salt */
        byte[] hashedInputPassword = hashPassword(user.getOnceHashedPassword(), userData.salt);

        /* Make sure that the hashed input password matches the hashed password on file */
        if (!Arrays.equals(hashedInputPassword,userData.twiceHashedPassword)){
            throw new IncorrectPasswordException(user.getID(), user.getOnceHashedPassword());
        }
        return true;
    }


    /**
     * Hash a given password using the SHA-512 algorithm
     * @param password
     * @param salt
     * @return A byte array of the hashed password
     */
    private byte[] hashPassword(byte[] password, byte[] salt){

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
     * Constructor for the password manager class
     * @param passwordDatabase Takes an input of the database that we wish to link to the password manager. Stores objects
     *                         of type UserAuthDataOutput (Essentially just UserID + salt + twicehashedpassword)
     */
    public PasswordManager(Queryable<UserAuthDataOutput> passwordDatabase){
        this.passwordDatabase = passwordDatabase;
    }

    /**
     * Generates the salt and twice hashed password for the input object, and adds this info to the database
     * Use changePassword if the user already has an ID in the database
     * @param inputUser The input data needed to record the password (User ID and hashed password)
     * @throws DatabaseNotAccessibleException
     * @throws DatabaseLogicException
     */
    public void addPasswordData(UserAuthDataInput inputUser) throws DatabaseNotAccessibleException, DatabaseLogicException {
        UserAuthDataOutput hashedUserData = hashNewPassword(inputUser);

        passwordDatabase.addObject(hashedUserData);
    }

    /**
     * Change the password of a given user
     * @param inputUser The data of the user that we want to change the password of, must have correct original password
     * @param onceHashedNewPassword The new password that we want to re-salt and hash and store
     * @throws DatabaseObjectNotFoundException
     * @throws DatabaseNotAccessibleException
     * @throws IncorrectPasswordException
     * @throws DatabaseLogicException
     */
    public void changePassword(UserAuthDataInput inputUser, byte[] onceHashedNewPassword) throws DatabaseObjectNotFoundException, DatabaseNotAccessibleException, IncorrectPasswordException, DatabaseLogicException {
        if (checkPasswordMatch(inputUser)){
            passwordDatabase.removeObject(inputUser.getID());
            inputUser.setOnceHashedPassword(onceHashedNewPassword);
            addPasswordData(inputUser);
        }
    }

}
