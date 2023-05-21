package org.example.application;

import org.example.database.DataBaseException;
import org.example.database.ManagerDao;
import org.example.models.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeGUI extends JDialog{
    private Manager manager;
    private JButton RESETButton;
    private JButton logOutButton;
    private JButton logButton;
    private JButton editButton;
    private JPanel homePanel;
    private JTable table1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JComboBox comboBox1;
    private JButton addButton;
    private JButton clearButton1;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox comboBox2;
    private JTextField textField8;
    private JButton resetButton;
    private JButton searchButton;
    private JComboBox comboBox3;
    private JButton exportButton;
    private JButton importButton;
    private JLabel dateTimeLbl;
    private JLabel managerName;
    private JLabel managerEmail;
    private JLabel managerPhoneNumber;
    private JLabel managerNbrContacts;
    private JLabel managerNbrGroups;

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


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginGUI(null);
            }
        });


        setVisible(true);
    }

    private void updateDateTimeLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateFormat.format(new Date());
        dateTimeLbl.setText(formattedDateTime);
    }

    public static void main(String[] args) throws DataBaseException {
        new  HomeGUI(null, null);
    }
}
