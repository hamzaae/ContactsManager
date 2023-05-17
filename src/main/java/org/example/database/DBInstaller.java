package org.example.database;


import org.apache.log4j.Logger;
import org.example.utils.FileManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;


public class DBInstaller {

    private static Logger LOGGER = Logger.getLogger(DBInstaller.class);

    public static void createDataBaseTables() throws DataBaseException {

        try {
            Connection con = DBConnection.getInstance();

            String sql = """
                    DROP TABLE IF EXISTS R;
                    DROP TABLE IF EXISTS CONTACT;
                    DROP TABLE IF EXISTS GROUPTABLE;
                    DROP TABLE IF EXISTS MANAGER;
                    CREATE TABLE MANAGER
                                    (idManager VARCHAR(255),
                                    firstName VARCHAR(50),
                                    lastName VARCHAR(50),
                                    phoneNumber VARCHAR(20),
                                    adress VARCHAR(255),
                                    genre VARCHAR(10),
                                    email VARCHAR(100),
                                    password VARCHAR(255),
                                    PRIMARY KEY (idManager));
                    CREATE TABLE CONTACT
                                    (id VARCHAR(255),
                                    nom VARCHAR(255),
                                    prenom VARCHAR(50),
                                    tel1 VARCHAR(20),
                                    tel2 VARCHAR(20),
                                    adresse VARCHAR(255),
                                    email_perso VARCHAR(100),
                                    email_profess VARCHAR(100),
                                    genre VARCHAR(10),
                                    id_Manager VARCHAR(255),
                                    PRIMARY KEY (id),
                                    FOREIGN KEY (id_Manager) REFERENCES MANAGER(idManager));
                    CREATE TABLE GROUPTABLE
                                    (idGroup VARCHAR(255),
                                    nomGroup VARCHAR(255),
                                    PRIMARY KEY (idGroup));
                    CREATE TABLE R
                                    (id_Contact VARCHAR(255),
                                    id_Group VARCHAR(255),
                                    FOREIGN KEY (id_Contact) REFERENCES CONTACT(id),
                                    FOREIGN KEY (id_Group) REFERENCES GROUPTABLE(idGroup));
                    """;
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }catch (Exception ex){
            LOGGER.error(ex);
            throw  new DataBaseException(ex);
        }

    }

    public static boolean checkIfAlreadyInstalled() throws IOException {

        String userHomeDirectory = System.getProperty("user.home");
        Properties dbProperties = DbPropertiesLoader.loadPoperties("conf.properties");
        String dbName = dbProperties.getProperty("db.name");
        String dataBaseFile = userHomeDirectory + "\\" + dbName + ".mv.db";
        return FileManager.fileExists(dataBaseFile);

    }



}
