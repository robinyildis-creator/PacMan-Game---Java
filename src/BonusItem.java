import java.awt.Color;
import java.awt.Graphics2D;

public class BonusItem {

    // Klass som representerar ett bonusföremål
    
    private int col; // Position i rutnätet
    private int row;
    private boolean active; // Anger om föremålet finns på kartan eller inte
    private int lifetimeTicks; // Hur länge föremålet ska finnas kvar
    private int points; // Hur många poäng det ger
    private Color color; 


    public BonusItem() {
        // Skapa ett inaktivt föremål
        this.active = false;
        this.color = Color.MAGENTA;  //standard färg
    }

    public boolean isActive() {
        // Används av GamePanel
        return active;
    }

    public int getCol() {
        // Getter för kolumn
        return col;
    }

    public int getRow() {
        // Getter för rad
        return row;
    }

    public int getPoints() {
        // Gör att GamePanel lägger till rätt antal poäng
        return points;
    }

    public void activate(int col, int row, int lifetimeTicks, int points, Color c) {
        // Aktiverar föremålet på korrekt plats
        this.col = col;
        this.row = row;
        this.lifetimeTicks = lifetimeTicks; // Hur länge bonusen ska finnas kvar
        this.points = points; // Poängvärdet
        this.active = true; // Gör bonusen synlig och aktiv
        this.color = c; //sparar färgen
    }

    public void update() {
        if (!active) return; // Gör ingenting om bonusen ej aktiv

        lifetimeTicks--; // Minska livslängden varje tick
        
        if (lifetimeTicks <= 0) {
            // Ta bort bonusen när tiden är slut
            active = false;
        }
    }

    public void collect() {
        // Anropas när PacMan går över bonusen
        active = false;
    }

    public void draw(Graphics2D g2d, int tileSize) {
        // Rita upp bonusen
        if (!active) return;

        int px = col * tileSize;
        int py = row * tileSize;

        g2d.setColor(this.color); // Ritar upp föremålet som en centrerad cirkel i magenta
        int r = tileSize - 6;
        g2d.fillOval(px + 3, py + 3, r, r);
    }
}
