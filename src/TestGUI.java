import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.net.URL;

public class TestGUI extends JFrame {
    public TestGUI() {
        this.setTitle("Contact Manager LOGIN");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(440, 570);
        this.getContentPane().setBackground(new Color(123, 50, 250));

        // Load the image as a resource relative to the class
        URL imageUrl = getClass().getResource("/media/logo.png");
        if (imageUrl != null) {
            ImageIcon image = new ImageIcon(imageUrl);
            this.setIconImage(image.getImage());
        }

        // Create the label with image and text
        URL titleImageUrl = getClass().getResource("/media/logoo.png");
        if (titleImageUrl != null) {
            ImageIcon titleImage = new ImageIcon(titleImageUrl);
            JLabel labelTitle = new JLabel("CONTACT MANAGER", titleImage, JLabel.CENTER);
            labelTitle.setVerticalTextPosition(JLabel.BOTTOM);
            labelTitle.setHorizontalTextPosition(JLabel.CENTER);
            this.getContentPane().add(labelTitle);
        }

        this.setVisible(true);
    }


    public static void main(String[] args) {
        new TestGUI();
    }
}
