package org.example.application;

import org.example.database.DataBaseException;
import org.example.database.ManagerDao;
import org.example.models.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class LoginGUI extends JDialog{

    private JPasswordField txtPswrd;
    private JTextField txtUsername;
    private JCheckBox keepMeSignedInCheckBox;
    private JButton signInButton;
    private JButton signUpButton;
    private JButton forgotPasswordButton;
    private JPanel loginPanel;
    private Manager currentManager = null;

    public LoginGUI(JFrame parent) throws DataBaseException {
        super(parent);
        Manager currentManager0 = ManagerDao.login();
        if (currentManager0 != null){
            dispose();
            new HomeGUI(null, currentManager0);

        }
        else {


            setTitle("Contact Manager LOGIN");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);
            setContentPane(loginPanel);
            setModal(true);
            setLocationRelativeTo(parent);
            setSize(360, 380);


            // Load the image as a resource relative to the class
            URL imageUrl = getClass().getResource("/media/logo.png");
            if (imageUrl != null) {
                ImageIcon image = new ImageIcon(imageUrl);
                this.setIconImage(image.getImage());
            }


            signInButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String email = txtUsername.getText();
                    String password = String.valueOf(txtPswrd.getPassword());
                    boolean keepMe = keepMeSignedInCheckBox.isSelected();

                    if (email.equals("") || password.equals("")) {
                        JOptionPane.showMessageDialog(LoginGUI.this,
                                "Username or password are empty!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try {
                        currentManager = ManagerDao.login(email, password, keepMe);
                        if (currentManager != null) {
                            dispose();
                            new HomeGUI(null, currentManager);
                        } else {
                            JOptionPane.showMessageDialog(LoginGUI.this,
                                    "Username or password are not correct!",
                                    "Retry", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (DataBaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            signUpButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new SignupGUI(null);
                }
            });


            setVisible(true);
        }
    }

/*
    private void createUIComponents() {
        // TODO: place custom component creation code here


    }
*/



}
