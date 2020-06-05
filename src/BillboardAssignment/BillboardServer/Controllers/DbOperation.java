package BillboardAssignment.BillboardServer.Controllers;

import BillboardAssignment.BillboardServer.Server.ServerResponse;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.IncorrectSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.Authentication.OutOfDateSessionKeyException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.InsufficentPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.RemoveOwnEditUsersPrivilegeException;
import BillboardAssignment.BillboardServer.BusinessLogic.User.RemoveOwnUserException;
import BillboardAssignment.BillboardServer.Database.DatabaseLogicException;
import BillboardAssignment.BillboardServer.Database.DatabaseNotAccessibleException;
import BillboardAssignment.BillboardServer.Database.DatabaseObjectNotFoundException;

/**
 * A interface only used with lambda functions that perform db operations
 */
public interface DbOperation {
    ServerResponse execution() throws InsufficentPrivilegeException, IncorrectSessionKeyException, OutOfDateSessionKeyException, DatabaseObjectNotFoundException, DatabaseNotAccessibleException, DatabaseLogicException, PrivilegeStringInvalidException, RemoveOwnEditUsersPrivilegeException, RemoveOwnUserException, NoSuchFieldException;
}
