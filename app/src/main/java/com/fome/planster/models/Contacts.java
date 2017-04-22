package com.fome.planster.models;

import java.util.ArrayList;

/**
 * Created by Alex on 03.04.2017.
 */
public class Contacts {

    private ArrayList <Contact> contacts; // All contacts on device
    private ArrayList <Contact> selectedContacts; // Contacts, involved it a certain task

    public Contacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        selectedContacts = new ArrayList<>();
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ArrayList<Contact> getSelectedContacts() {
        return selectedContacts;
    }

    public void selectContact (Contact contact) {
        selectedContacts.add(contact);
    }

    public void deselectContact (Contact contact) {
        selectedContacts.remove(contact);
    }

    public int getSize () {
        return contacts.size();
    }
}
