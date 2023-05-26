package org.example.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Group {
    private String idGroup;
    private String nomGroup;


    public Group(String nomGroup) {
        this.idGroup = UUID.randomUUID().toString();;
        this.nomGroup = nomGroup;

    }

    public Group() {

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

}
