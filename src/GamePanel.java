import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List; // För styrning med tangenter
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    
    private static final int TILE_SIZE = 24;
    private static final int UPDATE_DELAY_MS = 150; // Spelets "hastighet" i spel-loopen

    private static final String[][] LEVELS = {
        // Två stycken nivåer med samma layout
        {
            "####################",
            "#o.......##.......o#",
            "#.####...##...####.#",
            "#.#  #...##...#  #.#",
            "#.####...##...####.#",
            "#..................#",
            "#.####.######.####.#",
            "#......#    #......#",
            "######.#    #.######",
            "#o.......##.......o#",
            "####################"
        },
        {
            "####################",
            "#o.......##.......o#",
            "#.####...##...####.#",
            "#.#  #...##...#  #.#",
            "#.####...##...####.#",
            "#..................#",
            "#.####.######.####.#",
            "#......#    #......#",
            "######.#    #.######",
            "#o.......##.......o#",
            "####################"
        }
    };

    private static final int[] GHOST_MOVE_DELAYS = { 3, 2 }; // Spöken rör sig var tredje respektive varannan tick

    private static final int PAC_START_COL = 1; // Startposition för PacMan
    private static final int PAC_START_ROW = 1;

    private static final int[][] GHOST_START_POSITIONS = {
        // Fyra startpositioner för spöken
        {10,5},
        {11,5},
        {10,6},
        {11,6}
    };

    private Maze maze;
    private PacMan pacMan;
    private Ghost[] ghosts;

    private BonusItem pointItem = new BonusItem();
    private BonusItem freezeItem = new BonusItem();
    private int freezeTimer = 0;
    private Random random = new Random();

    private int score;
    private int lives = 3;
    private int levelIndex = 0;

    private boolean gameOver;
    private boolean allLevelsCleared;
    private boolean paused;

    //private boolean gameWon;

    private Timer timer;
    private int ghostMoveDelay;
    private int ghostMoveCounter;

    private HighScoreManager highScoreManager = new HighScoreManager();

    public GamePanel() {
        loadLevel(0); // Laddar in nivå 0

        int width = maze.getCols() * TILE_SIZE; // Sätter panelstorlek
        int height = maze.getRows() * TILE_SIZE +80;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);

        setFocusable(true); // Slår på fokus
        addKeyListener(this); // Slår på tangentlyssning
 
        timer = new Timer(UPDATE_DELAY_MS, this); // Startar timern
        timer.start();
    }

    private void loadLevel(int index) {
        // Skapar en ny labyrint, ny PacMan, nya spöken,
        // sätter delay för spöken, resettar bonus
        levelIndex = index;
        maze = new Maze(LEVELS[index]);
        pacMan = new PacMan(PAC_START_COL, PAC_START_ROW);

        ghosts = new Ghost[] {
            new Ghost(GHOST_START_POSITIONS[0][0], GHOST_START_POSITIONS[0][1], Color.RED),
            new Ghost(GHOST_START_POSITIONS[1][0], GHOST_START_POSITIONS[1][1], Color.PINK),
            new Ghost(GHOST_START_POSITIONS[2][0], GHOST_START_POSITIONS[2][1], Color.ORANGE),
            new Ghost(GHOST_START_POSITIONS[3][0], GHOST_START_POSITIONS[3][1], Color.MAGENTA)
        };

        ghostMoveDelay = GHOST_MOVE_DELAYS[index];
        ghostMoveCounter = 0;
        pointItem = new BonusItem();
    }

    private void resetGame() {
        // Återställer score, lives, laddar in första nivån igen
        score = 0;
        lives = 3;
        gameOver = false;
        allLevelsCleared = false;
        paused = false;
        loadLevel(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Huvudloopen
        if (gameOver || paused) {
            // Stoppar vid game over eller paus
            repaint();
            return;
        }

        pacMan.update(maze); // Uppdatera PacMan

        if (maze.eatPellet(pacMan.getCol(), pacMan.getRow())) {
            // +10 poäng för pellet
            score += 10;
            SoundManager.play(SoundManager.Sound.PELLET);
        }
        if (maze.eatPowerPellet(pacMan.getCol(), pacMan.getRow())) {
            // +50 poäng för power pellet
            score += 50;
            pacMan.startPowerMode();
            SoundManager.play(SoundManager.Sound.POWER);
        }

        maybeSpawnBonusItem(); // Försöker spawna bonusföremål
        pointItem.update();

        if (pointItem.isActive() &&
            pointItem.getCol() == pacMan.getCol() &&
            pointItem.getRow() == pacMan.getRow()) {

            score += pointItem.getPoints();
            pointItem.collect(); // Samlar in bonus om det finns
        }

        maybeSpawnFreezeItem(); // Försöker spawna freezeföremål
        freezeItem.update();
        if (freezeItem.isActive() &&
        freezeItem.getCol() == pacMan.getCol() && 
        freezeItem.getRow() == pacMan.getRow()) {
            freezeItem.collect();
            freezeTimer = 100;
        }
        if (freezeTimer > 0) {
            freezeTimer--;
        } else {
            ghostMoveCounter++;
            boolean frightened = pacMan.isPoweredUp();
            if (ghostMoveCounter % ghostMoveDelay == 0) {
                for (Ghost ghost : ghosts) {
                    ghost.update(maze, pacMan, frightened);
                }
            }
        }

        handleCollisions(); // Hanterar kollisioner

        checkLevelCompletion();

        repaint();
    }

    private void maybeSpawnBonusItem() {
        if (pointItem.isActive()) {
            return;
        }

        if (random.nextInt(200) == 0) {
            // 1/200 chans att spawna bonus för varje tick
            int col, row;
            do {
                col = random.nextInt(maze.getCols());
                row = random.nextInt(maze.getRows());
            } while (maze.isWall(col, row));

            int lifetime = 40;
            int points = 500;
            pointItem.activate(col, row, lifetime, points, Color.MAGENTA);
        }
    }

    private void maybeSpawnFreezeItem() {
        if (freezeItem.isActive()) return;

        if (random.nextInt(400) == 0) {
            int col, row;
            do {
                col = random.nextInt(maze.getCols());
                row = random.nextInt(maze.getRows());
            } while (maze.isWall(col, row));

            freezeItem.activate(col, row, 60, 0, Color.CYAN);
        }
    }

    private void handleCollisions() {
        // Hanterar kollisioner
        if (pacMan.isDying()) return; // Ignorera kollisioner om PacMan håller på att dö

        for (Ghost ghost : ghosts) {
            if (ghost.isCollidingWith(pacMan)) {
                if (pacMan.isPoweredUp()) {
                    // Om powered up, ät spöke och lägg till poäng
                    score += 200;
                    SoundManager.play(SoundManager.Sound.GHOST_EAT);
                    ghost.resetPosition();
                } else {
                    // Om inte powered up, förlora ett liv
                    loseLife();
                    break;
                }
            }
        }
    }

    private void loseLife() {
        lives--;
        pacMan.startDyingAnimation();
        SoundManager.play(SoundManager.Sound.PAC_DEATH);

        if (lives <= 0) {
            // Om liven är slut
            gameOver = true;
            allLevelsCleared = false;
            highScoreManager.addScore(score);
        } else {
            // Vänta 800ms och resetta positionerna för spelet
            new Thread(() -> {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException ignored) { }
            pacMan.resetPosition();
            pacMan.clearPowerMode();
            for (Ghost ghost : ghosts) {
                ghost.resetPosition();
            }
        }).start();
    }
}

    private void checkLevelCompletion() {
        // Kollar om nivån är klar
        if (maze.getPelletCount() == 0) {
            if (levelIndex + 1 < LEVELS.length) {
                loadLevel(levelIndex + 1);
            } else {
                gameOver = true;
                allLevelsCleared = true;
                highScoreManager.addScore(score);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        maze.draw(g2d, TILE_SIZE); // Ritar upp labyrinten
        pacMan.draw(g2d, TILE_SIZE); // Ritar upp PacMAn

        boolean frightened = pacMan.isPoweredUp();
        for (Ghost ghost : ghosts) {
            // Ritar upp alla spöken
            ghost.draw(g2d, TILE_SIZE, frightened);
        }

        pointItem.draw(g2d, TILE_SIZE); // Ritar upp bonusföremål
        freezeItem.draw(g2d, TILE_SIZE); // Ritar upp frysföremål

        // Text-rad 1: poäng, liv, nivå, paus
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        int textY1 = maze.getRows() * TILE_SIZE + 20;
        String pauseText = paused ? " (PAUSAD)" : "";
        g2d.drawString("Poäng: " + score + "   Liv: " + lives +
                       "   Nivå: " + (levelIndex + 1) + pauseText, 10, textY1);

        // Topplista under
        List<Integer> highs = highScoreManager.getHighScores();
        int textY2 = textY1 + 20;
        g2d.drawString("Topplista:", 10, textY2);
        for (int i = 0; i < highs.size(); i++) {
            g2d.drawString((i + 1) + ". " + highs.get(i), 10, textY2 + 20 * (i + 1));
        }

        if (gameOver) {
            String msg = allLevelsCleared
                    ? "Du klarade alla nivåer! Tryck R för att spela igen."
                    : "Game Over! Tryck R för att spela igen.";
            g2d.drawString(msg, 260, textY1);
        } else {
            g2d.drawString("P = pausa/fortsätt", 300, textY1);
        }
    }


    // KeyListener

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch(key) {
            // Piltangenterna styr riktningen
            case KeyEvent.VK_UP:
                pacMan.setDirection(Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                pacMan.setDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                pacMan.setDirection(Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                pacMan.setDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_R:
                // R resettar spelet
                if (gameOver) {
                    resetGame();
                }
                break;
            case KeyEvent.VK_P:
                // P pausar spelet
                paused = !paused;
                break;
            default:
        
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
