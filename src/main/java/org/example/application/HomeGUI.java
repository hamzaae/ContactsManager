package org.example.application;

import org.example.database.ContactDao;
import org.example.database.DataBaseException;
import org.example.database.GroupDao;
import org.example.database.ManagerDao;
import org.example.models.Contact;
import org.example.models.Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    private JComboBox comboBoxGroup;
    private JButton exportButton;
    private JButton importButton;
    private JLabel dateTimeLbl;
    private JLabel managerName;
    private JLabel managerEmail;
    private JLabel managerPhoneNumber;
    private JLabel managerNbrContacts;
    private JLabel managerNbrGroups;
    private DefaultTableModel tableModel;

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



        setVisible(true);
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
        // Create the table model with column names
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Personal Number");
        tableModel.addColumn("Professional Number");
        tableModel.addColumn("Personal Email");
        tableModel.addColumn("Professional Email");
        tableModel.addColumn("Address");
        tableModel.addColumn("Gender");


        // Add the contacts to the table model
        for (Contact contact : contacts) {
            Object[] rowData = new Object[8];
            rowData[0] = contact.getNom();
            rowData[1] = contact.getPrenom();
            rowData[2] = contact.getTel1();
            rowData[3] = contact.getTel2();
            rowData[4] = contact.getEmail_perso();
            rowData[5] = contact.getEmail_profess();
            rowData[6] = contact.getAdresse();
            rowData[7] = contact.getGenre();
            tableModel.addRow(rowData);
        }

        tableContacts.setModel(tableModel);

        // Set column headers
        JTableHeader tableHeader = tableContacts.getTableHeader();
        String[] columnHeaders = {"First Name", "Last Name", "Personal Number", "Professional Number",
                "Personal Email", "Professional Email", "Address", "Gender"};
        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn column = tableHeader.getColumnModel().getColumn(i);
            column.setHeaderValue(columnHeaders[i]);
        }
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

    private static String createInputWindow() {
        JFrame frame = new JFrame("Input Window");
        frame.setSize(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField inputField = new JTextField(20);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String userInput = inputField.getText();
            frame.dispose(); // Close the input window
            synchronized (HomeGUI.class) {
                HomeGUI.class.notify(); // Resume execution in the main thread
            }
        });

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(submitButton);

        frame.add(panel);
        frame.setVisible(true);

        synchronized (HomeGUI.class) {
            try {
                HomeGUI.class.wait(); // Wait for user input
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return inputField.getText();
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

        if (group.equals("None")){
            group = "0";
        } else if (group.equals("Auto")) {
            group = GroupDao.rechercherGroupParNom0(lastName);
        } else if (group.equals("New")) {
            group = "";
            while (group.equals("")){
                group = createInputWindow();
            }
            comboBoxGroup.addItem(group);
            group = GroupDao.addGroup(group);
        }
        //
        Contact newContact = new Contact(firstName,lastName,personalNumber,professionalNumber,adress,personalEmail,
                professionalEmail,gender,manager.getIdManager(), group);
        contact = ContactDao.create(newContact, manager.getIdManager());
        if (contact != null) {
            if(manager.getIdManager()==null) {
                JOptionPane.showMessageDialog(this,
                        "Email or phone number already exists!",
                        "Try again", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this,
                        "New manager created successfully!",
                        "Done!", JOptionPane.PLAIN_MESSAGE);
                dispose();
                new HomeGUI(null, manager);
            }
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "An error occurred, please try again later",
                    "Sorry", JOptionPane.ERROR_MESSAGE);
        }

    }


}
