package model.managers;
import model.ModelException;
import model.entities.Registrations;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * <h2> Registration Class </h2>
 * @author jimyang
 * This class provides functionality to interact with the registrations table in the database.
 * It also initializes the registrations table.
 *
 * Implements basic operations {@code getAllRegistrations}, {@code getRegistration}, {@code updateRegistration}, {@code deleteRegistration}
 */

public class RegistrationManager {
    private final Connection con;
    public RegistrationManager(Connection con) {
        this.con = con;
    }

    private void initializeRegistrations() throws ModelException {
        String stmt = """
            CREATE TABLE IF NOT EXISTS REGISTRATIONS (
                ID INTEGER NOT NULL PRIMARY KEY,
                AttendeeID VARCHAR(255) NOT NULL,
                GuestName VARCHAR(255) NOT NULL,
                BIN INTEGER NOT NULL,
                MealID VARCHAR(255) NOT NULL,
                Drink VARCHAR(255) NOT NULL,
                Seat VARCHAR(255) NOT NULL,
           )
        """;
        try(PreparedStatement pstmt = con.prepareStatement(stmt)) {
            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new ModelException("Cannot initialize database table");
        }

    }

     /** Retrieves all the Registration records from the database.
     * <p>This method executes a SQL query to select all entries from the {@code Registrations}
     * table and constructs a list of {@code Registrations} objects representing each record.</p>
     * @return A {@code List} containing all {@code Registrations} objects from the database.
     * @throws ModelException if any errors encountered.
     * */

     public List<Registrations> getAllRegistrations() throws ModelException {
         String selectAllSQL = "SELECT * FROM REGISTRATIONS";

         List<Registrations> registrations = new ArrayList<>();
         try(PreparedStatement pstmt = con.prepareStatement(selectAllSQL)){
             ResultSet resultSet = pstmt.executeQuery();
             while(resultSet.next()) {
                 registrations.add(new Registrations(resultSet.getInt(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getInt(4),
                                resultSet.getString(5),
                                resultSet.getString(6),
                                resultSet.getString(7)
                         ));
             }

            return registrations;
         }

         catch(SQLException e) {
             throw new ModelException("Database error: " + e.getMessage());
         }
     }
    /**
     * Retrieves a {@code Registration} object from the database based on the provided Registration ID.
     *
     * @param ID the unique identifier of the Registration to retrieve.
     * @return the {@code Registration} object corresponding to the provided ID.
     * @throws ModelException if any errors encountered.
     */
     public Registrations getRegistration(int ID) throws ModelException {
         String selectSQL = "SELECT * FROM REGISTRATIONS WHERE ID = " + ID;

         try(PreparedStatement pstmt = con.prepareStatement(selectSQL)){
                ResultSet resultSet = pstmt.executeQuery();
                if(resultSet.next()) {
                    return new Registrations(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7)
                    );
                }
                else {
                    throw new ModelException("Registration with ID" + ID + "Registration not found");
                }
         }

         catch (SQLException e) {
             throw new ModelException("Database error: " + e.getMessage());
         }

     }
    /**
     * Retrieves a list of {@code Registration} objects from the database based on a specified column and value.
     * <p>
     * This method executes a SQL query to select all entries from the {@code Registration}
     * table where the specified column matches the provided value.
     *
     * <h4>Usage examples</h4>
     * <blockquote><pre>
     *     List<Registration> testRegistration = getRegistration("Name", "TesteRegistration");
     * </pre></blockquote>
     *
     * @param attribute the column name to filter the Registrations by. Must be one of the allowed columns.
     * @param value the value to match in the specified column.
     * @return a {@code List} containing {@code Registration} objects that match the criteria.
     * @throws ModelException if any errors encountered.
     */

     public List<Registrations> getRegistrations(String attribute, String value) throws ModelException {
         String stmt = "SELECT * FROM REGISTRATIONS WHERE " + attribute + " = " + value;

         List<Registrations> registrations = new ArrayList<>();

         try(PreparedStatement pstmt = con.prepareStatement(stmt)){
             ResultSet resultSet = pstmt.executeQuery();
             while(resultSet.next()) {
                 registrations.add(new Registrations(resultSet.getInt(1),
                         resultSet.getString(2),
                         resultSet.getString(3),
                         resultSet.getInt(4),
                         resultSet.getString(5),
                         resultSet.getString(6),
                         resultSet.getString(7)
                 ));
             }
             return registrations;
         }

         catch(SQLException e) {
             throw new ModelException("Database error: " + e.getMessage());
         }
     }

    /**
     * Updates a specific field of an {@code Registration} record in the database.
     *
     * <p>This method updates the value of a specified column for the registration identified by
     * the provided registration ID.</p>
     *
     * @param ID the unique identifier of the Registration to update.
     * @param attribute    the column name to update. Must be one of the allowed columns.
     * @param newValue     the new value to set for the specified column.
     * @throws ModelException if any errors encountered.
     */
    public void updateRegistration(int ID, String attribute, String newValue) throws ModelException { // This method should be improved later.
        String stmt = "UPDATE REGISTRATIONS SET " + attribute + " = " + newValue + " WHERE ID = " + ID; // should this be adopted?

        try (PreparedStatement pstmt = con.prepareStatement(stmt)) {
            if (/* affectedRowCnt = */ pstmt.executeUpdate() == 0) {
                throw new NoSuchElementException("Registration with ID " + ID + " not found.");
            }
        } catch (SQLException e) {
            throw new ModelException("Database error: " + e.getMessage());
        }
    }

    /**
     * Deletes an {@code registration} record from the database based on the provided Register ID.
     *
     * @param ID the unique identifier of the registration to delete.
     * @throws ModelException if any errors encountered.
     */
    public void deleteRegistration(int ID) throws ModelException {
        String stmt = "DELETE FROM REGISTRATIONS WHERE ID = " + ID;

        try (PreparedStatement pstmt = con.prepareStatement(stmt)) {
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ModelException("Registration with ID " + ID + " not found.");
            }
        } catch (SQLException e) {
            throw new ModelException("Database error: " + e.getMessage());
        }
    }
}
