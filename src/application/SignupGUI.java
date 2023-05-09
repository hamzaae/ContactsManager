package application;

import javax.swing.*;
import java.net.URL;

public class SignupGUI extends JDialog{
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JCheckBox autoSignInCheckBox;
    private JRadioButton manRadioButton;
    private JRadioButton womanRadioButton;
    private JPasswordField passwordField3;
    private JPasswordField passwordField4;
    private JButton confirmButton;
    private JPanel signupPanel;


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

        setVisible(true);
    }


    public static void main(String[] args) {
        new SignupGUI(null);
    }
}
