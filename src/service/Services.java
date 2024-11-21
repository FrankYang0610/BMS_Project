package service;
import model.*;
import model.database.Connection;
import model.entities.Administrator;
import model.entities.AttendeeAccount;
import model.managers.*;

import java.security.NoSuchAlgorithmException;


/**
 * <h3>The {@code Service} Class</h3>
 * This class provides certain methods for the frontend to use.
 * In MVC design pattern, this is the Service layer.
 * @author jimyang
 */
public class Services {
    public boolean userLogin(AttendeeAccountsManager attendeeAccountsManager, int username, String password) throws CannotGetServiceException {
        try {
            AttendeeAccount attendee = attendeeAccountsManager.getAttendee(username); // will throw ModelException
            return PasswordManager.verifyPassword(password, attendee.getPassword(), attendee.getSalt());
        } catch (ModelException e) {
            throw new CannotGetServiceException("Cannot get service: " + e.getMessage());
        }
    }

    public boolean adminLogin(AdministratorsManager administratorsManager, int username, String password)  throws CannotGetServiceException {
        try {
            Administrator administrator = administratorsManager.getAdministrator(username);
            return PasswordManager.verifyPassword(password, administrator.getPassword(), administrator.getSalt());
        } catch (ModelException e) {
            throw new CannotGetServiceException("Cannot get service: " + e.getMessage());
        }
    }
}
