package org.example.managers;
import org.apache.log4j.Logger;
import org.example.database.ContactDao;
import org.example.database.DataBaseException;
import org.example.database.GroupDao;
import org.example.database.ManagerDao;
import org.example.models.Contact;

import java.util.*;

public class ContactManager {
    private static Logger LOGGER = Logger.getLogger(ContactManager.class);

    private ContactDao contactDao = new ContactDao();
    private GroupDao groupDao = new GroupDao();
    private ManagerDao managerDao = new ManagerDao();

    /**
     * Ajouter un contact
     */
    /*public void ajouterContact(Contact pContact) throws DataBaseException, BusinessLogicException {
        //on vérifie d'abord que le thème n'existe pas
        Contact contact = contactDao.findThemeByIntitule(pTheme.getIntitule());
        //Si le theme existe déjà on remonte une exception
        if (theme != null) {
            throw new BusinessLogicException("Le thème existe déjà");
        }
        //sinon on enregistre le thème
        themeDao.create(pTheme);
    }

    *//**
     * Ajouter un auteur
     *//*
    public void ajouterAuteur(Auteur pAuteur) throws DataBaseException, BusinessLogicException {
        //on vérifie d'abord que l'auteur n'existe pas
        Auteur auteur = rechercherAuteurParNom(pAuteur.getNom(), pAuteur.getPrenom());
        //Si l'auteur existe déjà on remonte une exception
        if (auteur != null) {
            throw new BusinessLogicException("L'auteur existe déjà");
        }
        //sinon on enregistre le livre
        auteurDao.create(pAuteur);
    }

    public Auteur rechercherAuteurParNom(String pNom, String pPrenom) throws DataBaseException {
        //on vérifie d'abord que l'auteur n'existe pas
        Auteur auteur = auteurDao.rechercherAuteurParNom(pNom, pPrenom);
        return auteur;
    }

    //TODO : cette méthode devrai s'executer dans une transaction
    public void AjouterLivre(Livre pLivre) throws DataBaseException, BusinessLogicException {
        //on vérifie d'abord que le livre n'existe pas
        Livre livre = livreDao.rechercherLivreParISBN(pLivre.getIsbn());
        //Si le livre existe déjà on remonte une exception
        if (livre != null) {
            throw new BusinessLogicException("Le livre existe déjà");
        }

        Theme theme = themeDao.findThemeByIntitule(pLivre.getTheme().getIntitule());
        Auteur auteur = auteurDao.rechercherAuteurParNom(pLivre.getAuteur().getNom(), pLivre.getAuteur().getPrenom());

        //Si le thème n'existe pas on l'ajoute
        if (theme == null) {
            themeDao.create(pLivre.getTheme());
        } else {
            pLivre.setTheme(theme);
        }
        //Si le auteur n'existe pas on l'ajoute
        if (auteur == null) {
            auteurDao.create(pLivre.getAuteur());
        } else {
            pLivre.setAuteur(auteur);
        }
        //on enregistre le livre

        livreDao.create(pLivre);
    }

    public void export() throws ExportException {

        try {
            List<Livre> livres = livreDao.getAll();
            String[][] data = new String[livres.size()][];

            int i = 0;
            for (Livre it : livres) {
                String[] excelRow = new String[]{it.getIsbn(), it.getTitre(),it.getAuteur().getNom()+" "+it.getAuteur().getPrenom(), it.getTheme().getIntitule(), String.valueOf(it.getQuantite())};
                data[i++] = excelRow;
            }
            ExcelExporter exporter = new ExcelExporter(new String[]{"ISBN", "Titre", "Auteur", "Thème", "Quantité"}, data, "livres");
            exporter.export(System.getProperty("user.home")+"\\livres.xlsx");
        }catch (Exception ex){
            LOGGER.error(ex);
            throw new ExportException("Erreur d'export excel",ex);
        }
    }

    public List<Theme> getAllTheme() throws DataBaseException {
        return themeDao.getAll();
    }*/
}

