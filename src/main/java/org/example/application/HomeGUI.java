package org.example.application;

import org.example.MainRun;
import org.example.database.ContactDao;
import org.example.database.DataBaseException;
import org.example.database.GroupDao;
import org.example.database.ManagerDao;
import org.example.models.Contact;
import org.example.models.Manager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeGUI extends JDialog{
    private Manager manager;
    private String selectedContactId;
    private JButton RESETButton;
    private JButton logOutButton;
    private JButton logButton;
    private JButton editButton;
    private JPanel homePanel;
    private JTable tableContacts;
    private JTextField inputFirstName;
    private JTextField inputPersonalNumber;
    private JTextField inputPersonalEmail;
    private JTextField inputLastName;
    private JTextField inputProfessionalNumber;
    private JTextField inputProfessionalEmail;
    private JTextField inputAdress;
    private JComboBox comboBoxGender;
    private JButton addButton;
    private JButton clearButton1;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox comboBoxSearchBy;
    private JTextField inputSearchBy;
    private JButton resetButton;
    private JButton searchButton;
    private JComboBox<String> comboBoxGroup;
    private JButton exportButton;
    private JButton importButton;
    private JLabel dateTimeLbl;
    private JLabel managerName;
    private JLabel managerEmail;
    private JLabel managerPhoneNumber;
    private JLabel managerNbrContacts;
    private JLabel managerNbrGroups;
    private DefaultTableModel tableModel = new DefaultTableModel();

    public HomeGUI(JFrame parent, Manager manager) throws DataBaseException {
        super(parent);
        this.manager = manager;
        setTitle("Contact Manager LOGIN");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(homePanel);
        setModal(true);
        setLocationRelativeTo(parent);
        setSize(700, 500);
        // Load the image as a resource relative to the class
        URL imageUrl = getClass().getResource("/media/logo.png");
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            this.setIconImage(image.getImage());
        }
        // Format the current date and time
        updateDateTimeLabel();
        Timer timer = new Timer(1000, e -> updateDateTimeLabel());
        timer.start();
        // Set the manager info
        managerName.setText(manager.getFirstName()+" "+manager.getLastName());
        managerPhoneNumber.setText(manager.getPhoneNumber());
        managerEmail.setText(manager.getEmail());
        managerNbrContacts.setText(String.valueOf(ManagerDao.countContacts(manager)));
        managerNbrGroups.setText(String.valueOf(ManagerDao.countContacts(manager)));

        //
        showAllContacts(ContactDao.getAllContacts(manager.getIdManager()));

        //
        initGroupsBox();


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ManagerDao.logout(manager.getEmail());
                    dispose();
                    new LoginGUI(null);
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addContact();
                    showAllContacts(ContactDao.getAllContacts(manager.getIdManager()));
                    managerNbrContacts.setText(String.valueOf(ManagerDao.countContacts(manager)));
                    managerNbrGroups.setText(String.valueOf(ManagerDao.countContacts(manager)));
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        clearButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAllFields();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateContact();
                    showAllContacts(ContactDao.getAllContacts(manager.getIdManager()));
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Add selection listener to tableContacts
        tableContacts.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if the selection is still adjusting and if there is a selected row
                if (!e.getValueIsAdjusting() && tableContacts.getSelectedRow() != -1) {
                    int selectedRow = tableContacts.getSelectedRow();
                    selectedContactId = tableModel.getValueAt(selectedRow, 9).toString();

                    // Get the data from the selected row in the table model
                    String firstName = tableModel.getValueAt(selectedRow, 0).toString();
                    String lastName = tableModel.getValueAt(selectedRow, 1).toString();
                    String personalNumber = tableModel.getValueAt(selectedRow, 2).toString();
                    String professionalNumber = tableModel.getValueAt(selectedRow, 3).toString();
                    String personalEmail = tableModel.getValueAt(selectedRow, 4).toString();
                    String professionalEmail = tableModel.getValueAt(selectedRow, 5).toString();
                    String address = tableModel.getValueAt(selectedRow, 6).toString();
                    Contact.Genre gender = (Contact.Genre) tableModel.getValueAt(selectedRow, 7);
                    String group = tableModel.getValueAt(selectedRow, 8).toString();


                    // Populate the input fields with the retrieved data
                    inputFirstName.setText(firstName);
                    inputLastName.setText(lastName);
                    inputPersonalNumber.setText(personalNumber);
                    inputProfessionalNumber.setText(professionalNumber);
                    inputPersonalEmail.setText(personalEmail);
                    inputProfessionalEmail.setText(professionalEmail);
                    inputAdress.setText(address);
                    if (gender== Contact.Genre.MAN){
                        comboBoxGender.setSelectedIndex(0);
                    }else{
                        comboBoxGender.setSelectedIndex(1);
                    }
                    comboBoxGroup.setSelectedItem(group);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this contact?", "Confirmation", JOptionPane.YES_NO_OPTION);

                    // Check the user's choice
                    if (option == JOptionPane.YES_OPTION) {
                        ContactDao.deleteContact(selectedContactId);
                        showAllContacts(ContactDao.getAllContacts(manager.getIdManager()));
                    }

                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInputDialog();
            }
        });

        setVisible(true);
    }


    private void initGroupsBox() {
        ArrayList<String> listGroups = null;
        try {
            listGroups = GroupDao.getAllGroups();
        } catch (DataBaseException e) {
            throw new RuntimeException(e);
        }
        for (String group:listGroups){
            comboBoxGroup.addItem(group);
        }
    }

    private void clearAllFields() {
        inputFirstName.setText("");
        inputLastName.setText("");
        inputPersonalNumber.setText("");
        inputProfessionalNumber.setText("");
        inputPersonalEmail.setText("");
        inputProfessionalEmail.setText("");
        inputAdress.setText("");
        comboBoxGroup.setSelectedIndex(0);
        comboBoxGender.setSelectedIndex(0);
    }

    private void showAllContacts(ArrayList<Contact> contacts) {

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        // Create the table model with column names
        tableContacts.setModel(tableModel);


        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Personal Number");
        tableModel.addColumn("Professional Number");
        tableModel.addColumn("Personal Email");
        tableModel.addColumn("Professional Email");
        tableModel.addColumn("Address");
        tableModel.addColumn("Gender");
        tableModel.addColumn("GroupId");
        tableModel.addColumn("ContactId");


        // Add the contacts to the table model
        for (Contact contact : contacts) {
            Object[] rowData = new Object[10];
            rowData[0] = contact.getNom();
            rowData[1] = contact.getPrenom();
            rowData[2] = contact.getTel1();
            rowData[3] = contact.getTel2();
            rowData[4] = contact.getEmail_perso();
            rowData[5] = contact.getEmail_profess();
            rowData[6] = contact.getAdresse();
            rowData[7] = contact.getGenre();
            try {
                rowData[8] = GroupDao.rechercherGroupParId(contact.getGroupId());
            } catch (DataBaseException e) {
                throw new RuntimeException(e);
            }
            rowData[9] = contact.getId();
            tableModel.addRow(rowData);
        }
        // Hide the ID column
        TableColumn column = tableContacts.getColumnModel().getColumn(9);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setWidth(0);
        column.setPreferredWidth(0);
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    private void updateDateTimeLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateFormat.format(new Date());
        dateTimeLbl.setText(formattedDateTime);
    }

    private void addContact() throws DataBaseException {
        Contact contact = null;
        String firstName = inputFirstName.getText();
        String lastName = inputLastName.getText();
        String personalNumber = inputPersonalNumber.getText();
        String professionalNumber = inputProfessionalNumber.getText();
        String personalEmail = inputPersonalEmail.getText();
        String professionalEmail = inputProfessionalEmail.getText();
        String adress = inputAdress.getText();
        Contact.Genre gender;
        if (comboBoxGender.getSelectedIndex()==0) {gender = Contact.Genre.MAN;}
        else {gender = Contact.Genre.WOMAN;}
        String group = String.valueOf(comboBoxGroup.getSelectedItem());

        // check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || personalNumber.isEmpty()
                || personalEmail.isEmpty() || adress.isEmpty() ){
            JOptionPane.showMessageDialog(this,
                    "Please enter all the necessery fields",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(personalNumber.length()!=10 || (!personalNumber.startsWith("05") && !personalNumber.startsWith("06") && !personalNumber.startsWith("07"))){
            JOptionPane.showMessageDialog(this,
                    "Personal Phone number length or format not valid (10 digits starts with 05 , 06 or 07)",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(professionalNumber.length()!=10 || (!professionalNumber.startsWith("05") && !professionalNumber.startsWith("06") && !professionalNumber.startsWith("07"))){
            JOptionPane.showMessageDialog(this,
                    "Professional Phone number length or format not valid (10 digits starts with 05 , 06 or 07)",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!personalEmail.matches(regex)){
            JOptionPane.showMessageDialog(this,
                    "Personal Email not valid",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }if(!professionalEmail.matches(regex)){
            JOptionPane.showMessageDialog(this,
                    "Professional Email not valid",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (group.equals("NONE")){
            group = "0";
        } else if (group.equals("AUTO")) {
            group = GroupDao.rechercherGroupParNom0(lastName);
        } else if (group.equals("NEW")) {
            group = "";
            while (group.equals("")){
                group = JOptionPane.showInputDialog("New Group Name?");
            }
            comboBoxGroup.addItem(group);
            group = GroupDao.addGroup(group);
        }
        else{
            group = GroupDao.rechercherGroupParNom0(lastName);
        }
        //
        Contact newContact = new Contact(firstName,lastName,personalNumber,professionalNumber,adress,personalEmail,
                professionalEmail,gender,manager.getIdManager(), group);
        contact = ContactDao.create(newContact, manager.getIdManager());
        if (contact != null) {

                JOptionPane.showMessageDialog(this,
                        "New Contact added successfully!",
                        "Done!", JOptionPane.PLAIN_MESSAGE);

        }
        else {
            JOptionPane.showMessageDialog(this,
                    "An error occurred, please try again later",
                    "Sorry", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void updateContact() throws DataBaseException {
        String firstName = inputFirstName.getText();
        String lastName = inputLastName.getText();
        String personalNumber = inputPersonalNumber.getText();
        String professionalNumber = inputProfessionalNumber.getText();
        String personalEmail = inputPersonalEmail.getText();
        String professionalEmail = inputProfessionalEmail.getText();
        String adress = inputAdress.getText();
        Contact.Genre gender;
        if (comboBoxGender.getSelectedIndex()==0) {gender = Contact.Genre.MAN;}
        else {gender = Contact.Genre.WOMAN;}
        String group = String.valueOf(comboBoxGroup.getSelectedItem());

        // check if any field is empty
        if (firstName.isEmpty() || lastName.isEmpty() || personalNumber.isEmpty()
                || personalEmail.isEmpty() || adress.isEmpty() ){
            JOptionPane.showMessageDialog(this,
                    "Please enter all the necessery fields",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(personalNumber.length()!=10 || (!personalNumber.startsWith("05") && !personalNumber.startsWith("06") && !personalNumber.startsWith("07"))){
            JOptionPane.showMessageDialog(this,
                    "Personal Phone number length or format not valid (10 digits starts with 05 , 06 or 07)",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(professionalNumber.length()!=10 || (!professionalNumber.startsWith("05") && !professionalNumber.startsWith("06") && !professionalNumber.startsWith("07"))){
            JOptionPane.showMessageDialog(this,
                    "Professional Phone number length or format not valid (10 digits starts with 05 , 06 or 07)",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!personalEmail.matches(regex)){
            JOptionPane.showMessageDialog(this,
                    "Personal Email not valid",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }if(!professionalEmail.matches(regex)){
            JOptionPane.showMessageDialog(this,
                    "Professional Email not valid",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (group.equals("NONE")){
            group = "0";
        } else if (group.equals("AUTO")) {
            group = GroupDao.rechercherGroupParNom0(lastName);
        } else if (group.equals("NEW")) {
            group = "";
            while (group.equals("")){
                group = JOptionPane.showInputDialog("New Group Name?");
            }
            comboBoxGroup.addItem(group);
            group = GroupDao.addGroup(group);
        }
        else{
            group = GroupDao.rechercherGroupParNom0(lastName);
        }
        //
        Contact contact = new Contact(selectedContactId,firstName,lastName,personalNumber,professionalNumber,adress,personalEmail,
                professionalEmail,gender,manager.getIdManager(), group);
        if(!ContactDao.update(contact)){
            JOptionPane.showMessageDialog(this,
                    "Contact has not been updated,  an error occured!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }


    }

    private void showInputDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField pswrdField = new JTextField();
        JTextField confirmpswrdField = new JTextField();
        panel.add(new JLabel("New password:"));
        panel.add(pswrdField);
        panel.add(new JLabel("Confirm password:"));
        panel.add(confirmpswrdField);

        int option = JOptionPane.showOptionDialog(null, panel, "Change password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String pswrd = pswrdField.getText();
            String confirmpswrd = confirmpswrdField.getText();
            if (!pswrd.equals(confirmpswrd)){
                JOptionPane.showMessageDialog(null, "Password and confirm password mismatch");
            }
            else{
                try {
                    if(ContactDao.changePassword(pswrd,manager.getIdManager())){
                        JOptionPane.showMessageDialog(null, "Password updated successfully");
                    }
                } catch (DataBaseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



}
