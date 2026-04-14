import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class SoundManager {

    // Ansvarar för att spela upp ljud
    
    public enum Sound {
        // Samlar alla ljud som spelet använder
        PELLET("sounds/pellet.wav"),
        POWER("sounds/power.wav"),
        GHOST_EAT("sounds/ghost.wav"),
        PAC_DEATH("sounds/death.wav");
    

        private final String path; // Sökvägen till ljudfilen

        Sound(String path) {
            // Kopplar varje ljud till sin ljudfil
            this.path = path;
        }

        public String getPath() {
            // Hämtar rätt fil
            return path;
        }
    }

    public static void play(Sound sound) {
        try {
            // Leta efter ljudfilen i classpathen
            URL url = SoundManager.class.getClassLoader().getResource(sound.getPath());

            if (url == null) return; // Gör ingenting om filen inte hittas (undviker krasch)

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url); // Läser ljudfilen till ett ljudflöde
            
            Clip clip = AudioSystem.getClip(); // Skapa ett ljudklipp som kan spelas
            
            clip.open(audioIn); // Ladda ljudet och spela det direkt
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Kunde inte spela ljud: " + e.getMessage());
        }
    }
}
