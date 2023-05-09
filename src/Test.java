import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class Test {
    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();

        System.out.println("Available Fonts:");
        for (Font font : fonts) {
            System.out.println(font.getFontName());
        }
    }
}
