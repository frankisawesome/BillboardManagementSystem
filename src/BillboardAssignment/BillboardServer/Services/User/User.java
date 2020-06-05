package BillboardAssignment.BillboardServer.Services.User;

import java.io.Serializable;

public class User extends UserDataInput implements Comparable, Serializable {

    public String twiceHashedPassword;

    /**
     * String array containing the salt that was used to re-hash the given hashed password
     */
    public String salt;

    /**
     * Constructor
     *
     * @param twiceHashedPassword password string after it has been hashed by the front end and back end
     * @param salt                Salt that was used to hash the password
     * @param ID                  The user's ID
     * @param username            The unique username of the user
     * @param privileges          The users privledges in an array of enums
     */
    public User(int ID, String twiceHashedPassword, String salt, UserPrivilege[] privileges, String username) {
        /** We don't ever store the once hashed password **/
        super(ID, "", privileges, username);

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
    }

    /** BELOW HERE IS UTILITY METHODS FOR OPERATIONS ON THE USER OBEJECT */

    /**
     * Just checks only a specific Privilege, and does throw errors
     *
     * @param privileges privileges to test
     * @return boolean iff user has Privilege
     * @throws InsufficentPrivilegeException if they do not have one or more of the privileges
     */
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

    /**
     * Just checks only a specific Privilege, and doesn't throw errors
     *
     * @param privilege privilege to test
     * @return boolean iff user has Privilege
     */
    public boolean checkUserHasPriv(UserPrivilege privilege) {
        boolean foundPerm;
        foundPerm = false;
        for (int j = 0; j < this.getPrivileges().length; j++) {
            if (privilege == this.getPrivileges()[j]) {
                foundPerm = true;
                break;
            }
        }

        return foundPerm;
    }

    @Override
    public boolean equals(Object obj) {
        User user2 = (User) obj;
        return this.salt.equals(user2.salt) && this.getID() == user2.getID() && this.twiceHashedPassword.equals(user2.twiceHashedPassword) && this.getUsername().equals(user2.getUsername()) && assertSetEquals(this.getPrivileges(), user2.getPrivileges());
    }

    /**
     * Assert that two arrays are equal (using the concept of set equality). Not a test, just a helper function
     *
     * @param set1
     * @param set2
     * @throws Exception
     */
    boolean assertSetEquals(UserPrivilege[] set1, UserPrivilege[] set2) {
        boolean match;
        boolean allMatch = true;
        for (int i = 0; i < set1.length; i++) {
            match = false;
            for (int j = 0; j < set2.length; j++) {
                if (set1[i] == set2[j]) {
                    match = true;
                }
            }

            if (!match) {
                allMatch = false;
            }

        }
        return allMatch;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Object o) {
        User cast = (User) o;
        if (this.getID() == cast.getID()) {
            return 0;
        }
        if (this.getID() > cast.getID()) {
            return 1;
        }
        return -1;
    }
}

