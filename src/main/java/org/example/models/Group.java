package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String idGroup;
    private String nomGroup;
    private List<Contact> contacts = new ArrayList<Contact>();

    public Group(String idGroup, String nomGroup, List<Contact> contacts) {
        this.idGroup = idGroup;
        this.nomGroup = nomGroup;
        this.contacts = contacts;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getNomGroup() {
        return nomGroup;
    }

    public void setNomGroup(String nomGroup) {
        this.nomGroup = nomGroup;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
