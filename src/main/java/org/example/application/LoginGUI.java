package org.example.application;

import javax.swing.*;
import java.net.URL;

public class LoginGUI extends JDialog{

    private JPasswordField txtPswrd;
    private JTextField txtUsername;
    private JCheckBox keepMeSignedInCheckBox;
    private JButton signInButton;
    private JButton signUpButton;
    private JButton forgotPasswordButton;
    private JPanel loginPanel;

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
