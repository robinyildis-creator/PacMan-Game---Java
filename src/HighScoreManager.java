import java.io.*;
import java.util.ArrayList; // Används för att lagra poäng i minnet
import java.util.Collections; // Används för sortering av poänglistan
import java.util.List; // Används för att lagra poäng i minnet



public class HighScoreManager {

    // Klass för att läsa highscores från en fil,
    // lägga till nya resultat, och spara igen
    
    private static final String FILE_NAME = "highscores.txt"; // Namnet på filen där topplistan sparas
    private static final int MAX_SCORES = 5; // Max antal som kan sparas

    private final List<Integer> scores = new ArrayList<>(); // Lista som håller alla highscores i minnet

    public HighScoreManager() {
        // Läs in tidigare highscores från fil
        load();
    }

    public void addScore(int score) {
        // Anropas när spelet tar slut
        scores.add(score); // Lägg till ny poäng i listan
        Collections.sort(scores); // Sortera listan
        Collections.reverse(scores); // Vänd så högsta poängen hamnar först
        if (scores.size() > MAX_SCORES) {
            // Om listan är för lång, ta bort den sämsta poängen
            scores.remove(scores.size() - 1);
        }
        save(); // Spara den uppdaterade topplistan
    }

    public List<Integer> getHighScores() {
        // Returnera en kopia av listan
        return new ArrayList<>(scores);
    }

    private void load() {
        // Används för att läsa highscores från fil
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // Om filen inte finns händer ingenting

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Inläsning av fil
            String line;
            while ((line = reader.readLine()) != null) {
                // Läser filen rad för rad
                try {
                    scores.add(Integer.parseInt(line.trim())); // Försöker tolka varje rad som ett heltalsvärde
                } catch (NumberFormatException ignored) { }
            }
            Collections.sort(scores);
            Collections.reverse(scores);
            while (scores.size() > MAX_SCORES) {
                scores.remove(scores.size() - 1);
            }
        } catch (IOException e) {
            // Felhantering
            System.err.println("Kunde inte läsa highscores: " + e.getMessage());
        }
    }

    private void save() {
        // Spara highscores till en fil
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            // Öppnar filen för inskrivning av poäng
            for (int score : scores) {
                // Varje poäng skrivs på en egen rad
                writer.println(score);
            }
        } catch (IOException e) {
            // Felhantering
            System.err.println("Kunde inte spara highscores: " + e.getMessage());
        }
    }
}
