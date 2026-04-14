import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Game {

    // Startklassen för programmet
    // Startar programmet, skapar fönstret, lägger in GamePanel

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Säkerställer att all Swingkod körs korrekt
            JFrame frame = new JFrame("Pac-Man"); // Skapar ett nytt fönster
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Programmet avslutas när man klickar på X

            GamePanel panel = new GamePanel(); // Skapar huvudpanelen
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null); // Centrering
            frame.setVisible(true);
        });
    }
}
