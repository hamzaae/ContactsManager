package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Contact;
import org.example.models.Group;
import org.example.models.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {
    private static Logger logger = Logger.getLogger(ContactDao.class);

    public static String rechercherGroupParNom0(String pNom) throws  DataBaseException{

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from grouptable where upper(nomgroup)=?");
            stm.setString(1, pNom.toUpperCase());
            ResultSet rs = stm.executeQuery();
            if (!rs.next()){
                Group newGroup = new Group(pNom);
                stm = c.prepareStatement("INSERT INTO grouptable VALUES (?,upper(?))");
                stm.setString(1, newGroup.getIdGroup());
                stm.setString(2, pNom.toUpperCase());
                stm.execute();
                rs.close();
                return newGroup.getIdGroup();
            }
            String result = rs.getString("idgroup");
            rs.close();
            return result;
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }

    }
    public static String rechercherGroupParId(String pid) throws DataBaseException {
        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * FROM grouptable WHERE idgroup = ?");
            stm.setString(1, pid);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String group = rs.getString("nomgroup");
                rs.close();
                return group;
            } else {
                rs.close();
                throw new DataBaseException("No data found for the specified ID");
            }
        } catch (SQLException ex) {
            // tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            // remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    public static ArrayList<String> getAllGroups() throws DataBaseException {
        ArrayList<String> list = new ArrayList<String>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from grouptable");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("nomgroup"));
            }
            rs.close();
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }




    public static String addGroup(String  pNom) throws DataBaseException{
        try {
            Group newGroup = new Group(pNom);

            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            String sqlInsert = "INSERT INTO grouptable VALUES (?,upper(?))";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, newGroup.getIdGroup());
            stm.setString(2, pNom);
            stm.execute();
            return newGroup.getIdGroup();
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }

    }
    private static Contact resultToContact(ResultSet rs) throws SQLException {
        return new Contact(rs.getString("id"), rs.getString("nom"), rs.getString("tel1"),
                rs.getString("tel2"), rs.getString("adresse"), rs.getString("email_perso"),
                rs.getString("email_profess"), Contact.Genre.valueOf(rs.getString("genre")),
                rs.getString("id_manager"), rs.getString("group_id"));
    }

}
