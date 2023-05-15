package org.example.database;

import org.apache.log4j.Logger;
import org.example.models.Contact;
import org.example.models.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ContactDao {
    private Logger logger = Logger.getLogger(getClass());

    public Contact rechercherContactParNom(String pNom) throws  DataBaseException{
        List<Contact> list = new ArrayList<>();

        try {
            Connection c = DBConnection.getInstance();
            PreparedStatement stm = c.prepareStatement("SELECT * from contact where upper(nom)=?");
            stm.setString(1, pNom.toUpperCase());
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
        return list.get(0);
    }

    public void create(Contact pContact) throws DataBaseException{
        try {
            //Récupérer la connexion à la base de données
            Connection c = DBConnection.getInstance();
            //instruction SQl avec un paramètre
            String sqlInsert = "insert into Contact(id,nom,prenom,tel1,tel2,email_perso,email_profess,genre,email_manager) values(?,?,?,?,?,?,?,?,?)";
            //créer l'objet PreparedStatement
            PreparedStatement stm = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            //définir la valeur du paramètre de l'instruction SQL
            stm.setString(1, pContact.getId());
            stm.setString(2, pContact.getNom());
            stm.setString(3, pContact.getPrenom());
            stm.setString(4, pContact.getTel1());
            stm.setString(5, pContact.getTel2());
            stm.setString(6, pContact.getEmail_perso());
            stm.setString(7, pContact.getEmail_profess());
            stm.setString(8, String.valueOf(pContact.getGenre()));
            stm.setString(9, pContact.getManagerId());
            //Executer l'instruction SQL
            stm.executeUpdate();
        } catch (SQLException ex) {
            //tracer l'erreur
            logger.error("Erreur à cause de : ", ex);
            //remonter l'erreur
            throw new DataBaseException(ex);
        }
    }

    private Contact resultToContact(ResultSet rs) throws SQLException {
        return new Contact(rs.getString("id"), rs.getString("nom"), rs.getString("tel1"),
                rs.getString("tel2"), rs.getString("adress"), rs.getString("email_perso"),
                rs.getString("email_profess"), Contact.Genre.valueOf(rs.getString("genre")),
                (Manager) rs.getObject("manager"));
    }
}
