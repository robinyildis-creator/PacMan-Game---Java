import java.awt.Color;
import java.awt.Graphics2D;

public class Maze {

    // Klass som representerar spelplanen
    // Hanterar rutnät, väggar, pellets, power pellets
    // Känner inte till PacMan eller spöken
    
    private TileType[][] tiles; // Lagrar typen av varje ruta i labyrinten
    private int rows;
    private int cols;
    private int pelletCount;

    public Maze(String[] layout) {
        // Skapar labyrint från textbaserad layout
        this.rows = layout.length;
        this.cols = layout[0].length();
        this.tiles = new TileType[rows][cols]; // Skapar rutnätet

        for (int y = 0; y < rows; y++) {
            String row = layout[y];
            for (int x = 0; x < cols; x++) {
                char c = row.charAt(x);
                switch (c) {
                    case '#': // Vägg
                        tiles[y][x] = TileType.WALL;
                        break;
                    case '.': // Pellet
                        tiles[y][x] = TileType.PELLET;
                        pelletCount++;
                        break;
                    case 'o': // Power Pellet
                    case 'O':
                        tiles[y][x] = TileType.POWER_PELLET;
                        pelletCount++;
                        break;
                        
                    default: // Alla andra tecken betyder tom ruta
                        tiles[y][x] = TileType.EMPTY;
                        break;
                }
            }
        }
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public int getPelletCount() { return pelletCount; } // Används för att se om nivån är klar

    public boolean isWall(int col, int row) {
        // Används för att se om en ruta är en vägg
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return true; // Koordinater utanför väggar behandlas som väggar
        }
        return tiles[row][col] == TileType.WALL;
    }

    public boolean hasPellet(int col, int row) {
        // Används för att se om en ruta har en pellet
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return tiles[row][col] == TileType.PELLET;
    }

    public boolean hasPowerPellet(int col, int row) {
        // Används för att se om en ruta har en power pellet
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return tiles[row][col] == TileType.POWER_PELLET;
    }

    public boolean eatPellet(int col, int row) {
        // Anropas när PacMan rör sig
        if (hasPellet(col, row)) {
            tiles[row][col] = TileType.EMPTY; // Tar bort pellet från rutan om det finns
            pelletCount--;
            return true;
        }
        return false;
    }

    public boolean eatPowerPellet(int col, int row) {
        // Samma som ovan, fast för Power Pellets
        if (hasPowerPellet(col, row)) {
            tiles[row][col] = TileType.EMPTY;
            pelletCount--;
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g2d, int tileSize) {
        // Ritar upp labyrinten ruta för ruta
        
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                TileType type = tiles[y][x];

                int px = x*tileSize; // Omvandlar rutkoordinater till pixelkoordinater
                int py = y*tileSize;

                g2d.setColor(Color.BLACK); // Svart bakgrund
                g2d.fillRect(px, py, tileSize, tileSize);

                if (type == TileType.WALL) {
                    // Ritar upp väggar
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(px, py, tileSize, tileSize);

                } else if (type == TileType.PELLET) {
                    // Ritar upp pellets
                    g2d.setColor(Color.WHITE);
                    int r = tileSize / 4;
                    int cx = px + tileSize / 2 - r / 2;
                    int cy = py + tileSize / 2 - r / 2;
                    g2d.fillOval(cx, cy, r, r);

                } else if (type == TileType.POWER_PELLET) {
                    // Ritar upp power pellets
                    g2d.setColor(Color.ORANGE);
                    int r = tileSize / 2;
                    int cx = px + tileSize / 2 - r / 2;
                    int cy = py + tileSize / 2 - r / 2;
                    g2d.fillOval(cx, cy, r, r);
                }
            }
        }
    }
}
