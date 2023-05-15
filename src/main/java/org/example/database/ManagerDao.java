package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Manager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class ManagerDao {
    private Logger logger = Logger.getLogger(getClass());
    private Manager currentManager;

    public Manager login(String pEmail, String pPassword) throws  DataBaseException{

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from manager where email=? and password=?");
            stm.setString(1, pEmail);
            String salt = BCrypt.gensalt();
            stm.setString(2, BCrypt.hashpw(pPassword, salt));
            ResultSet rs = stm.executeQuery();
            currentManager = resultToManager(rs);
            rs.close();
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }

        return currentManager;

    }

    public void signup(Manager pManager) throws DataBaseException{
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "insert into Manager(firstName, lastName, phoneNumber, adress, gender, email, password) " +
                    "values(?,?,?,?,?,?,?)";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pManager.getFirstName());
            stm.setString(2, pManager.getLastName());
            stm.setString(3, pManager.getPhoneNumber());
            stm.setString(4, pManager.getAdress());
            stm.setString(5, String.valueOf(pManager.getGenre()));
            //Executer l'instruction SQL
            stm.executeUpdate();
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    private Manager resultToManager(ResultSet rs) throws SQLException {
        return new Manager(rs.getString("fisrtName"), rs.getString("lastName"),
                rs.getString("phoneNumber"), rs.getString("adress"),
                Manager.Genre.valueOf(rs.getString("genre")), rs.getString("email"),
                rs.getString("password"));
    }
}
