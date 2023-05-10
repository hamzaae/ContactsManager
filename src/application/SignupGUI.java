package application;

import managers.Manager;

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


    public SignupGUI(JFrame parent){
        super(parent);
        setTitle("Contact Manager SIGNUP");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(signupPanel);
        setModal(true);
        setLocationRelativeTo(parent);
        setSize(360, 400);


        // Load the image as a resource relative to the class
        URL imageUrl = getClass().getResource("/media/logo.png");
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            this.setIconImage(image.getImage());
        }

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerManager();
            }
        });
        textFirstName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c) && !isControlKey(e)) {
                    e.consume(); // prevent the character from being added to the text field
                    textFirstNameError.setText("* Error");
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (isValidNameCharacter(c) || isControlKey(e)){
                    textFirstNameError.setText("");
                }

            }

        });//TODO : check logique
        textLastName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!isValidNameCharacter(c) && !isControlKey(e)) {
                    e.consume(); // prevent the character from being added to the text field
                    textLastNameError.setText("* Error");
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (isValidNameCharacter(c) || isControlKey(e)){
                    textLastNameError.setText("");
                }

            }

        });
        textPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && !isControlKey(e)) {
                    e.consume();
                    textPhoneNumberError.setText("* Error numbers");
                }
                if (textPhoneNumber.getText().length()>9){
                    e.consume();
                    textPhoneNumberError.setText("* Error length");
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || isControlKey(e)){
                    textLastNameError.setText("");
                }
                if (textPhoneNumber.getText().length()<=10){
                    textPhoneNumberError.setText("");
                }

            }

        });


        setVisible(true);
    }

    private void registerManager() {
        String email = textEmail.getText();
        String password =String.valueOf(passwordFieldRegister.getPassword());
        String passwordConfirm =String.valueOf(passwordFieldRegisterConfirm.getPassword());
        String firstName = textFirstName.getText();
        String lastName = textLastName.getText();
        String phoneNumber = textPhoneNumber.getText();
        String adress = textAdress.getText();
        String gender;
        if (manRadioButton.isSelected()) {gender = "Man";}
        else if (womanRadioButton.isSelected()) {gender = "Woman";}
        else {gender = "None selected";}
        Boolean autoSignIn = autoSignInCheckBox.isSelected();

        // check if any field is empty
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty() || firstName.isEmpty()
                || lastName.isEmpty() || phoneNumber.isEmpty() || adress.isEmpty() || gender.equals("None selected")){
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
        manager = addManagerToDb(email, password, firstName, lastName, phoneNumber, adress, gender, autoSignIn);
        if (manager != null) {dispose();}
        else {
            JOptionPane.showMessageDialog(this,
                    "An error occurred, please try again later",
                    "Sorry", JOptionPane.ERROR_MESSAGE);
        }
    }

    Manager manager;
    private Manager addManagerToDb(String email, String password, String firstName, String lastName, String phoneNumber, String adress, String gender, Boolean autoSignIn) {

        return null;
    }

    public boolean isValidNameCharacter(char c) {
        return Character.isLetter(c) || c == ' ' || c == '-' || c == '\'';
    }
    public boolean isControlKey(KeyEvent e) {
        int keyCode = e.getKeyCode();
        return keyCode == KeyEvent.VK_BACK_SPACE || keyCode == KeyEvent.VK_TAB ||
                keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_DELETE;
    }
    public static void main(String[] args) {
        new SignupGUI(null);
    }
}