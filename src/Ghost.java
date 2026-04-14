import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Ghost {
    
    // Klass som representerar ett spöke
    // Behandlar position, riktning, färg, och rörelselogik
    private int col; // Spökets position i rutnätet
    private int row;
    private final int startCol; // Startpositionen
    private final int startRow;

    private Direction direction = Direction.LEFT; // Nuvarande rörelseriktning, börjar åt vänster
    private final Color color;
    private final Random random = new Random(); // Slumpbeteende när spöket är frightened

    public Ghost(int startCol, int startRow, Color color) {
        // Konstruktor för spöke med position och färg
        this.col = startCol;
        this.row = startRow;
        this.startCol = startCol;
        this.startRow = startRow;
        this.color = color;
    }

    public int getCol() { return col; } // Getters för att ge GamePanel möjlighet att läsa positionen
    public int getRow() { return row; }

    public void resetPosition() {
        // Nollställer positionen
        col = startCol;
        row = startRow;
        direction = Direction.LEFT;
    }

    public void update(Maze maze, PacMan pacMan, boolean frightened) {

        // Uppdaterar spökets position ett steg i taget

        Direction bestDir = null;

        if (!frightened) {
            // När spöket inte är frightened
            int bestDistance = Integer.MAX_VALUE; // Börja med väldigt stort värde
            for (Direction dir : Direction.values()) {
                // Testar alla fyra riktningar och beräknar spökets position om den tar den riktningen
                int newCol = col + dir.dx;
                int newRow = row + dir.dy;
                if (maze.isWall(newCol, newRow)) continue; // Hoppa över fall där spöket går in i en vägg

                int dist = Math.abs(newCol - pacMan.getCol()) + Math.abs(newRow - pacMan.getRow()); // Beräkna avståndet till PacMan
                if (dist < bestDistance) {
                    // Välj den riktning som minskar avståndet mest
                    bestDistance = dist;
                    bestDir = dir;
                }
            }
        }

        if (frightened || bestDir == null) {
            // När spöket är frightened eller om ingen bästa riktning hittas
            Direction[] dirs = Direction.values(); // Hämtar alla möjliga riktningar
            for (int i = 0; i < dirs.length; i++) {
                Direction tryDir = dirs[random.nextInt(dirs.length)]; // Välj slumpmässig riktning
                int newCol = col + tryDir.dx;
                int newRow = row + tryDir.dy;
                if (!maze.isWall(newCol, newRow)) {
                    // Ta den första riktningen som inte är en vägg
                    bestDir = tryDir;
                    break;
                }
            }
        }

        if (bestDir != null) {
            // Uppdaterar riktning och position
            direction = bestDir;
            col += direction.dx;
            row += direction.dy;
        }
    }

    public boolean isCollidingWith(PacMan pacMan) {
        // Samma ruta = kollision
        return pacMan.getCol() == col && pacMan.getRow() == row;
    }

    public void draw(Graphics2D g2d, int tileSize, boolean frightened) {
        // Ritar upp spöket
        int px = col * tileSize;
        int py = row * tileSize;
        int offset = 2;
        int size = (tileSize) - (2*offset);
        int legsize = (size / 3);


        g2d.setColor(frightened ? Color.CYAN : color); // Färgen cyan om frightened, annars den vanliga färgen
        g2d.fillArc(px + offset, py + offset, size,size, 0, 180);
        int foty = (py + offset) + size - legsize;

        g2d.fillRect(px + offset, py + 1 + (size / 2), size, size / 2);

        g2d.fillOval(px + 3, foty, legsize,legsize);
        g2d.fillOval(px + 3 + legsize, foty, legsize, legsize);
        g2d.fillOval(px + 3 + (2 * legsize), foty, legsize, legsize);
        // --- RITA ÖGONEN ---

        g2d.setColor(Color.WHITE);

        // Räkna ut storlek och position relativt spökets storlek
        int eyeWidth = size / 3;
        int eyeHeight = size / 3;
        int leftEyeX = px + offset + (size / 7);         // Lite indragen från vänster
        int rightEyeX = px + offset + size - eyeWidth - (size / 7); // Lite indragen från höger
        int eyeY = py + offset + (size / 6);             // En bit ner på huvudet

        // Rita ögonvitorna
        g2d.fillOval(leftEyeX, eyeY, eyeWidth, eyeHeight);
        g2d.fillOval(rightEyeX, eyeY, eyeWidth, eyeHeight);

        // --- RITA PUPILLERNA ---

        g2d.setColor(Color.BLUE);

        int pupilSize = eyeWidth / 2;

        // Räkna ut "titta-offset" baserat på spökets rörelse (dx, dy)
        // Om du inte har dx/dy tillgängligt här, kan du hårdkoda 0 så länge.
        int lookX = 0;
        int lookY = 0;

        if (direction.dx > 0) lookX = 2; // Tittar höger
        if (direction.dx < 0) lookX = -2; // Tittar vänster
        if (direction.dy > 0) lookY = 2; // Tittar ner
        if (direction.dy < 0) lookY = -2; // Tittar upp

        // Centrera pupillen i ögat + lägg till offset
        // Formel: ÖgatsPos + (Halva Ögat - Halva Pupillen) + TittaOffset
        int pOffsetX = (eyeWidth / 2) - (pupilSize / 2) + lookX;
        int pOffsetY = (eyeHeight / 2) - (pupilSize / 2) + lookY;

        // Rita pupillerna
        g2d.fillOval(leftEyeX + pOffsetX, eyeY + pOffsetY, pupilSize, pupilSize);
        g2d.fillOval(rightEyeX + pOffsetX, eyeY + pOffsetY, pupilSize, pupilSize);


        


    }
}
