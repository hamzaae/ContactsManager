package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Manager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class ManagerDao {
    private static Logger logger = Logger.getLogger(ManagerDao.class);
    private static Manager currentManager;

    public static Manager login(String pEmail, String pPassword, boolean pkeepMe) throws  DataBaseException{

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from manager where email=?");
            stm.setString(1, pEmail);
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                // No data found
                rs.close();
                return currentManager;
            } else {
                // Data found
                String storedHashedPassword = rs.getString("password");

                // Check if the entered password matches the stored hashed password
                boolean passwordMatches = BCrypt.checkpw(pPassword, storedHashedPassword);

                if (passwordMatches) {
                    // Password is correct
                    currentManager = resultToManager2(rs);

                    //
                    stm = c.prepareStatement("UPDATE manager set keepme = TRUE where email=?");
                    stm.setString(1, pEmail);
                    stm.executeUpdate();
                    rs.close();
                    return currentManager;
                } else {
                    // Password is incorrect
                    rs.close();
                    return currentManager;
                }
            }
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }
    public static Manager login() throws  DataBaseException{
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from manager where keepme=TRUE");
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                // No data found
                rs.close();
                return null;
            } else {
                // Data found
                currentManager = resultToManager2(rs);
                return currentManager;
            }
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    public static void logout(String pEmail) throws  DataBaseException{

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("UPDATE manager set keepme = FALSE where email=?");
            stm.setString(1, pEmail);
            stm.executeUpdate();

        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }



    public static Manager signup(Manager pManager) throws DataBaseException{
        Manager manager = null;
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();

            String sqlInsert = "SELECT email from manager where email=? or phoneNumber=?";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pManager.getEmail());
            stm.setString(2, pManager.getPhoneNumber());
            ResultSet rs = stm.executeQuery();
            if (rs.next()){
                manager = new Manager();
                System.out.println(manager);
                return manager;
            }

            sqlInsert = "insert into Manager(idManager, firstName, lastName, phoneNumber, adress, genre, email, password) " +
                    "values(?,?,?,?,?,?,?,?)";
            //créer l'objet PreparedStatement
            stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pManager.getIdManager());
            stm.setString(2, pManager.getFirstName());
            stm.setString(3, pManager.getLastName());
            stm.setString(4, pManager.getPhoneNumber());
            stm.setString(5, pManager.getAdress());
            stm.setString(6, String.valueOf(pManager.getGenre()));
            stm.setString(7, pManager.getEmail());
            stm.setString(8, pManager.getPassword());
            //Executer l'instruction SQL
            boolean hasResultSet = stm.execute();
            if (hasResultSet) {
                ResultSet resultSet = stm.getResultSet();
                manager = resultToManager(resultSet);
                System.out.println(manager);
                return manager;
            }

        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
        return manager;
    }

    public static int countContacts(Manager manager) throws DataBaseException {
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();

            String sqlInsert = "SELECT count(*) as cc from contact where id_manager=?";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, manager.getIdManager());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int contactCount = rs.getInt("cc");
                rs.close();
                return contactCount;
            } else {
                // No data found
                rs.close();
                return 0;
            }
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }
    public static int countGroups(Manager manager) throws DataBaseException {
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();

            String sqlInsert = "";// TODO: sql statement for groups count for a user
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, manager.getIdManager());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int contactCount = rs.getInt("cc");
                rs.close();
                return contactCount;
            } else {
                // No data found
                rs.close();
                return 0;
            }
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    private static Manager resultToManager(ResultSet rs) throws SQLException {
        return new Manager(rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("phoneNumber"), rs.getString("adress"),
                Manager.Genre.valueOf(rs.getString("genre")), rs.getString("email"),
                rs.getString("password"));
    }

    private static Manager resultToManager2(ResultSet rs) throws SQLException {
        return new Manager(rs.getString("idManager"), rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("phoneNumber"), rs.getString("adress"),
                Manager.Genre.valueOf(rs.getString("genre")), rs.getString("email"),
                rs.getString("password"), rs.getBoolean("keepMe"));
    }
}
