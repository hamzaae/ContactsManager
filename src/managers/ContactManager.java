package managers;
import models.Contact;

import java.util.*;

public class ContactManager {
    private List<Contact> contacts;

    public ContactManager() {
        contacts = new ArrayList<>();
    }

    public void ajouterContact(Contact contact){
        contacts.add(contact);
    }


    public void afficherContactsParOrdreAlphabetique() {
        if (contacts.isEmpty()) {
            System.out.println("La liste des contacts est vide.");
            return;
        }

        contacts.sort(new Comparator<Contact>() {
            public int compare(Contact contact1, Contact contact2) {
                int result = contact1.getNom().compareTo(contact2.getNom());

                if (result == 0) {
                    result = contact1.getPrenom().compareTo(contact2.getPrenom());
                }

                return result;
            }
        });

        // Afficher les contacts dans l'ordre alphabétique
        System.out.println("Liste des contacts par ordre alphabétique :");
        for (Contact contact : contacts) {
            System.out.println(contact.getNom() + " " + contact.getPrenom());
        }
    }

    public void supprimerContact(Contact contact) {
        // Parcourir la liste des contacts et supprimer le contact correspondant
        Iterator<Contact> iterator = contacts.iterator();
        while (iterator.hasNext()) {
            Contact c = iterator.next();
            if (c.getId().equals(contact.getId())) {
                iterator.remove();
                System.out.println("Contact supprimé avec succès.");
                return;
            }
        }

        System.out.println("Le contact spécifié n'existe pas dans la liste des contacts.");
    }

    public void showAll(){
        for(Contact contact:contacts){
            System.out.println(contact);
        }
    }
}

