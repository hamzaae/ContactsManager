package org.example.application;

import org.example.database.DataBaseException;
import org.example.database.ManagerDao;
import org.example.models.Manager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;

public class SignupGUI extends JDialog{
    private JTextField textFirstName;
    private JTextField textLastName;
    private JTextField textPhoneNumber;
    private JTextField textAdress;
    private JTextField textEmail;
    private JCheckBox autoSignInCheckBox;
    private JRadioButton manRadioButton;
    private JRadioButton womanRadioButton;
    private JPasswordField passwordFieldRegister;
    private JPasswordField passwordFieldRegisterConfirm;
    private JButton confirmButton;
    private JPanel signupPanel;
    private JLabel textFirstNameError;
    private JLabel textLastNameError;
    private JLabel textPhoneNumberError;
    private JButton cancelButton;


    public SignupGUI(JFrame parent){
        super(parent);
        setTitle("Contact Manager SIGNUP");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(signupPanel);
        setModal(true);
        setLocationRelativeTo(parent);
        setSize(360, 400);


        // Load the image
        URL imageUrl = getClass().getResource("/media/logo.png");
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            this.setIconImage(image.getImage());
        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerManager();
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        textFirstName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c)) {
                    e.consume(); // prevent the character from being added to the text field
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c) && e.getKeyCode() != 8 && e.getKeyCode() != 16 && e.getKeyCode() != 20) {
                    e.consume(); // prevent the character from being added to the text field
                    textFirstNameError.setText("* Error");
                } else if (isValidNameCharacter(c) || e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 20){
                    textFirstNameError.setText("");
                }
            }

        });

        textLastName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c)) {
                    e.consume(); // prevent the character from being added to the text field
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c) && e.getKeyCode() != 8 && e.getKeyCode() != 16 && e.getKeyCode() != 20) {
                    e.consume(); // prevent the character from being added to the text field
                    textLastNameError.setText("* Error");
                } else if (isValidNameCharacter(c) || e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 20){
                    textLastNameError.setText("");
                }
            }

        });

        textPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
                if (textPhoneNumber.getText().length()>9){
                    e.consume();
                    textPhoneNumberError.setText("* Error length");
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (textPhoneNumber.getText().length()<=10){
                    textPhoneNumberError.setText("");
                }
                if (!Character.isDigit(c) && e.getKeyCode() != 8 && e.getKeyCode() != 16 && e.getKeyCode() != 20) {
                    e.consume(); // prevent the character from being added to the text field
                    textPhoneNumberError.setText("* Error");
                } else if (Character.isDigit(c) || e.getKeyCode() == 8 || e.getKeyCode() == 16 || e.getKeyCode() == 20){
                    textPhoneNumberError.setText("");
                }

            }

        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    new LoginGUI(null);
                } catch (DataBaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        setVisible(true);
    }

    private void registerManager() throws DataBaseException {
        String email = textEmail.getText();
        String password =String.valueOf(passwordFieldRegister.getPassword());
        String passwordConfirm =String.valueOf(passwordFieldRegisterConfirm.getPassword());
        String firstName = textFirstName.getText();
        String lastName = textLastName.getText();
        String phoneNumber = textPhoneNumber.getText();
        String adress = textAdress.getText();
        Manager.Genre gender = null;
        if (manRadioButton.isSelected()) {gender = Manager.Genre.MAN;}
        else if (womanRadioButton.isSelected()){gender = Manager.Genre.WOMAN;}

        boolean autoSignIn = autoSignInCheckBox.isSelected();

        // check if any field is empty
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || firstName.isEmpty()
                || lastName.isEmpty() || phoneNumber.isEmpty() || adress.isEmpty() || gender==null){
            JOptionPane.showMessageDialog(this,
                    "Please enter all the fields",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(phoneNumber.length()!=10 || (!phoneNumber.startsWith("05") && !phoneNumber.startsWith("06") && !phoneNumber.startsWith("07"))){
            JOptionPane.showMessageDialog(this,
                    "Phone number length or format not valid (10 digits starts with 05 , 06 or 07)",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(regex)){
            JOptionPane.showMessageDialog(this,
                    "Email not valid",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!password.equals(passwordConfirm)){
            JOptionPane.showMessageDialog(this,
                    "Password and Confirm Password doesn't match",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Manager newManager = new Manager(firstName, lastName, phoneNumber, adress, gender, email, password, autoSignIn);
        Manager manager = ManagerDao.signup(newManager);

        // TODO : check the logique
            if(manager==null) {
                JOptionPane.showMessageDialog(this,
                        "New manager created successfully!",
                        "Done!", JOptionPane.PLAIN_MESSAGE);
                dispose();
                new LoginGUI(null);
            }
            else{
                JOptionPane.showMessageDialog(this,
                        "An error occured, or the manager already exists!",
                        "Try again", JOptionPane.ERROR_MESSAGE);
            }


    }

    public boolean isValidNameCharacter(char c) {
        return Character.isLetter(c) || c == ' ' || c == '-' || c == '\'' || Character.isWhitespace(c);
    }

}