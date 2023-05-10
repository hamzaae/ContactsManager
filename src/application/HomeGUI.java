package application;

import javax.swing.*;
import java.net.URL;

public class HomeGUI extends JDialog{
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
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
    private JButton clearButton;
    private JButton searchButton;

    public HomeGUI(JFrame parent){
        super(parent);
        setTitle("Contact Manager LOGIN");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setContentPane(homePanel);
        setModal(true);
        setLocationRelativeTo(parent);
        setSize(700, 450);


        // Load the image as a resource relative to the class
        URL imageUrl = getClass().getResource("/media/logo.png");
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            this.setIconImage(image.getImage());
        }

        setVisible(true);
    }

    public static void main(String[] args) {
        new  HomeGUI(null);
    }
}
