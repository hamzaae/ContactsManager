package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Manager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class ManagerDao {
    private static Logger logger = Logger.getLogger(ManagerDao.class);
    private static Manager currentManager;

    public static Manager login(String pEmail, String pPassword) throws  DataBaseException{

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from manager where email=? and password=?");
            stm.setString(1, pEmail);
            String salt = BCrypt.gensalt();
            stm.setString(2, BCrypt.hashpw(pPassword, salt));
            ResultSet rs = stm.executeQuery();
            if (!rs.next()) {
                // No data found
                rs.close();
                return currentManager;
            } else {
                // Data found
                currentManager = resultToManager(rs);
                rs.close();
                return currentManager;
            }
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

    private static Manager resultToManager(ResultSet rs) throws SQLException {
        return new Manager(rs.getString("firstName"), rs.getString("lastName"),
                rs.getString("phoneNumber"), rs.getString("adress"),
                Manager.Genre.valueOf(rs.getString("genre")), rs.getString("email"),
                rs.getString("password"));
    }
}
