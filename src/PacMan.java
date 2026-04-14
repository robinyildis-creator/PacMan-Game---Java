import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

public class PacMan {

    // Klass som representerar PacMan
    // Behandlar position, riktning, power mode, animation, dödsstatus
    // Känner inte till poäng, liv, eller nivåer i spelet
    
    public int col; // Position i rutnätet
    public int row;
    private final int startCol; // Sparar startpositionen
    private final int startRow;

    private Direction direction = Direction.LEFT; // Nuvarande riktning, startar åt vänster

    private boolean poweredUp; // Om PacMan är powered up
    private int powerTicksRemaining; // Hur länge effekten är kvar
    private static final int POWER_MODE_TICKS = 40; // Hur länge power up varar

    private int animationTick; // Räknar för munanimation
    private boolean dying; // Om PacMan dör
    private int dyingTicksRemaining; // Hur länge dödsanimationen ska pågå

    public PacMan(int startCol, int startRow) {
        // Skapar PacMan på angiven startposition
        this.col = startCol;
        this.row = startRow;
        this.startCol = startCol;
        this.startRow = startRow;
    }

    public int getCol() { return col; } // Getters för PacMans position
    public int getRow() { return row; }

    public void setDirection(Direction dir) {
        // Uppdaterar riktning baserat på input från piltangenter
        this.direction = dir;
    }

    public boolean isPoweredUp() {
        // Används av GamePanel och Ghost
        return poweredUp;
    }

    public void startPowerMode() {
        // Aktiverar powerläge, nollställer timer
        poweredUp = true;
        powerTicksRemaining = POWER_MODE_TICKS;
    }

    public void clearPowerMode() {
        // Används när PacMan dör eller respawnar
        poweredUp = false;
        powerTicksRemaining = 0;
    }

    public void resetPosition() {
        // Flyttar tillbaka till startpositionen
        col = startCol;
        row = startRow;
    }

    public void startDyingAnimation() {
        // Dödsanimationen
        dying = true;
        dyingTicksRemaining = 15;
    }

    public boolean isDying() {
        return dying;
    }

    public void update(Maze maze) {

        if (dying) {
            // Ingen rörelse, endast dödsanimation
            dyingTicksRemaining--;
            if (dyingTicksRemaining <= 0) {
                dying = false;
            }
            return;
        }

        int newCol = col + direction.dx; // Beräknar nästa ruta
        int newRow = row + direction.dy;

        if (!maze.isWall(newCol, newRow)) {
            // Rörelsen kontrolleras mot Maze
            col = newCol;
            row = newRow;
        }

        if (poweredUp) {
            // Power up stängs automatiskt när timern är slut
            powerTicksRemaining--;
            if (powerTicksRemaining <= 0) {
                poweredUp = false;
            }
        }

        animationTick++; // Används i munanimationen
    }

    public void draw(Graphics2D g2d, int tileSize) {
        int px = col * tileSize;
        int py = row * tileSize;

        if (dying) {
            // Dödsanimationen
            g2d.setColor(Color.RED);
            g2d.fillOval(px + 2, py + 2, tileSize - 4, tileSize - 4);
            return;
        }

        double mouthAngle = 40 + 20 * Math.sin(animationTick * 1.5); // Munnen öppnas och stängs, använder sin för jämn animation
        double start;

        switch (direction) {
            // Anpassar startvinkel så munnen pekar i rörelseriktningen
            case RIGHT: start = mouthAngle / 2; break;
            case LEFT:  start = 180 + mouthAngle / 2; break;
            case UP:    start = 90 + mouthAngle / 2; break;
            case DOWN:  start = 270 + mouthAngle / 2; break;
            default:    start = mouthAngle / 2;
        }

        g2d.setColor(poweredUp ? Color.GREEN : Color.YELLOW); // Gul i normalläge, grön när powered up

        Arc2D.Double arc = new Arc2D.Double(px + 2, py + 2, // Arc för cirkelsektor till munnen
                tileSize - 4, tileSize - 4,
                start, 360 - mouthAngle, Arc2D.PIE);
        g2d.fill(arc);
    }
}