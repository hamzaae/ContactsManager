import org.apache.log4j.Logger;
import org.example.database.DBInstaller;
import org.example.managers.*;
import org.example.application.*;
import org.example.models.*;

public class Main {
    //Utilisé pour le logging
    private static Logger LOGGER = Logger.getLogger(Main.class);
    public static void main(String[] args) {


        //création d'une instance de la classe qui gère la logique métier
        ContactManager contactManager = new ContactManager();
        try {
            //On vérifie que la base de données n'est pas encore crée
            if (!DBInstaller.checkIfAlreadyInstalled()) {
                //Créer la base de données
                DBInstaller.createDataBaseTables();
                LOGGER.info("La base de données est crée correctement");
            }
        }catch (Exception ex){
            //Dans le cas d'une erreur dans la création des tables on affiche un message d'erreur
            System.err.println("Erreur lors de la création de la base de données, voir le fichier log.txt pour plus de détails");
            //On arrete l'application ici avec un code d'erreur
            System.exit(-1);
        }
        /*System.out.println("Hello world!");
        //creer contacts
        Contact contact1 = new Contact("nom1","prenom1","0611111111",
                "0611111111","123 city","nom1@gmail.com",
                "prenom1@gmail.com",'m');
        Contact contact2 = new Contact("nom2","prenom1","0611111112",
                "0611111112","123 city","nom2@gmail.com",
                "prenom2@gmail.com",'f');
        Contact contact4 = new Contact("nom4","prenom4","0611111114",
                "0611111114","123 city","nom4@gmail.com",
                "prenom4@gmail.com",'m');
        Contact contact3 = new Contact("nom3","prenom3","0611111113",
                "0611111113","123 city","nom3@gmail.com",
                "prenom3@gmail.com",'f');

        // ContactsManager
        ContactManager contactManager = new ContactManager();
        contactManager.ajouterContact(contact1);
        contactManager.ajouterContact(contact3);
        contactManager.ajouterContact(contact2);
        contactManager.ajouterContact(contact4);

        //afficher
        contactManager.showAll();
        //par ordre
        contactManager.afficherContactsParOrdreAlphabetique();
        //supprimer
        contactManager.supprimerContact(contact1);
        contactManager.showAll();*/

    }
}