package org.example;

import org.apache.log4j.Logger;
import org.example.application.LoginGUI;
import org.example.database.DBInstaller;

public class MainRun {
    private static Logger LOGGER = Logger.getLogger(MainRun.class);
    public static void main(String[] args) {
        try {
            //On vérifie que la base de données n'est pas encore crée
            if (!DBInstaller.checkIfAlreadyInstalled()) {
                //Créer la base de données
                DBInstaller.createDataBaseTables();
                LOGGER.info("La base de données est crée correctement");
            }
            new LoginGUI(null);
        }catch (Exception ex){
            //Dans le cas d'une erreur dans la création des tables on affiche un message d'erreur
            System.err.println("Erreur lors de la création de la base de données, voir le fichier log.txt pour plus de détails");
            //On arrete l'application ici avec un code d'erreur
            System.exit(-1);
        }

    }
}
