package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Contact;
import org.example.models.Manager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactDao {
    private static Logger logger = Logger.getLogger(ContactDao.class);

    public static boolean update(Contact pContact) throws DataBaseException {
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "UPDATE contact SET nom=?,prenom=?,tel1=?,tel2=?,adresse=?,email_perso=?,email_profess=?,genre=?,group_id=? where id=?";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pContact.getNom());
            stm.setString(2, pContact.getPrenom());
            stm.setString(3, pContact.getTel1());
            stm.setString(4, pContact.getTel2());
            stm.setString(5, pContact.getAdresse());
            stm.setString(6, pContact.getEmail_perso());
            stm.setString(7, pContact.getEmail_profess());
            stm.setString(8, String.valueOf(pContact.getGenre()));
            stm.setString(9, pContact.getGroupId());
            stm.setString(10, pContact.getId());
            //Executer l'instruction SQL

            boolean hasResultSet = stm.execute();
            return hasResultSet;
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    public static void deleteContact(String selectedContactId) throws DataBaseException{
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "DELETE FROM Contact WHERE id = ?";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, selectedContactId);

            int hasResultSet = stm.executeUpdate();

        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    public static ArrayList<Contact> searchByName(String searchFor) throws DataBaseException{
        ArrayList<Contact> list = new ArrayList<>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * FROM contact WHERE SOUNDEX(nom) = SOUNDEX(?) OR SOUNDEX(prenom) = SOUNDEX(?)");
            stm.setString(1, searchFor);
            stm.setString(2, searchFor);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(resultToContact(rs));
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

    public static ArrayList<Contact> searchByNumber(String searchFor) throws DataBaseException{
        ArrayList<Contact> list = new ArrayList<>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from contact where tel1=? or tel2=?");
            stm.setString(1, searchFor);
            stm.setString(2, searchFor);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(resultToContact(rs));
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

    public static ArrayList<Contact> searchByGroup(String searchFor) throws DataBaseException{
        ArrayList<Contact> list = new ArrayList<>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from grouptable where upper(nomgroup)=?");
            stm.setString(1, searchFor.toUpperCase());
            ResultSet rs = stm.executeQuery();
            String idGroup;
            if (rs.next()) { // Move the cursor to the first row
                idGroup = rs.getString("idgroup");
                if (idGroup != null) {
                    stm = c.prepareStatement("SELECT * from contact where group_id=?");
                    stm.setString(1, idGroup);
                    rs = stm.executeQuery();
                    while (rs.next()) {
                        list.add(resultToContact(rs));
                    }
                }
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

    public static ArrayList<Contact> getAllContacts(String pmanager) throws  DataBaseException{
        ArrayList<Contact> list = new ArrayList<Contact>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from contact where id_manager=?");
            stm.setString(1, pmanager);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(resultToContact(rs));
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

    public static boolean create(Contact pContact, String pmanagerId) throws DataBaseException{
        Contact contact = null;
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "insert into Contact(id,nom,prenom,tel1,tel2,adresse,email_perso,email_profess,genre,id_manager,group_id) values(?,?,?,?,?,?,?,?,?,?,?)";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pContact.getId());
            stm.setString(2, pContact.getNom());
            stm.setString(3, pContact.getPrenom());
            stm.setString(4, pContact.getTel1());
            stm.setString(5, pContact.getTel2());
            stm.setString(6, pContact.getAdresse());
            stm.setString(7, pContact.getEmail_perso());
            stm.setString(8, pContact.getEmail_profess());
            stm.setString(9, String.valueOf(pContact.getGenre()));
            stm.setString(10, pmanagerId);
            stm.setString(11, pContact.getGroupId());
            //Executer l'instruction SQL

            return stm.execute();

        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }

    }

    public static ArrayList<Contact> getAllContactsSorted(String idManager) throws DataBaseException {
        ArrayList<Contact> list = new ArrayList<Contact>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from contact where id_manager=? ORDER BY nom, prenom");
            stm.setString(1, idManager);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(resultToContact(rs));
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

    public static void makeGroupNone(String idGroup) throws DataBaseException {
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "UPDATE contact SET group_id='0' WHERE group_id=?";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, idGroup);

            //Executer l'instruction SQL
            stm.execute();

        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    private static Contact resultToContact(ResultSet rs) throws SQLException {
        return new Contact(rs.getString("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tel1"),
                rs.getString("tel2"), rs.getString("adresse"), rs.getString("email_perso"),
                rs.getString("email_profess"), Contact.Genre.valueOf(rs.getString("genre")),
                rs.getString("id_manager"), rs.getString("group_id"));
    }


}
