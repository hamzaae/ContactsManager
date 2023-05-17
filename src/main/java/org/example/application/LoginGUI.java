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

    public LoginGUI(JFrame parent){
        super(parent);
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
                if (email.equals("") || password.equals("")){
                    JOptionPane.showMessageDialog(LoginGUI.this,
                            "Username or password are empty!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    currentManager = ManagerDao.login(email,password);
                    if (currentManager != null){
                        System.out.println(currentManager.getLastName());
                        dispose();
                    }
                    else {
                        System.out.println("error no user");
                    }
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        setVisible(true);
    }

/*
    private void createUIComponents() {
        // TODO: place custom component creation code here


    }
*/


    public static void main(String[] args) {
        new LoginGUI(null);
    }
}
