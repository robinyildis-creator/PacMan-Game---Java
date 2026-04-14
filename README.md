# 🎮 Pac-Man (Java Game from Scratch)

This project is a complete implementation of the classic **Pac-Man game**, built from scratch in Java using object-oriented design principles and real-time game logic.

The project was developed as part of a programming course, with a strong focus on **clean architecture, modular design, and game mechanics**.

---

## 🚀 Features

* 🟡 Fully playable Pac-Man game
* 👻 Intelligent ghost behavior (chasing + random movement when frightened)
* ⚡ Power pellets with temporary power mode
* 🎁 Bonus items (extra points & freeze ability)
* ❤️ Life system and scoring
* 🏆 Persistent high score system (saved to file)
* ⏸ Pause and restart functionality
* 🎨 Custom rendering using Java Swing

---

## 🧠 Architecture

The game is built using a **modular object-oriented design**, where each component has a clear responsibility:

### Core Components

* `Game` – Entry point, initializes the game window 
* `GamePanel` – Main game loop, rendering, input handling, and game state management 
* `Maze` – Handles the grid, walls, pellets, and map structure 
* `PacMan` – Player logic, movement, animation, and power mode 
* `Ghost` – Enemy AI with pathfinding and behavior switching 

### Supporting Components

* `BonusItem` – Dynamic bonus spawning and effects 
* `SoundManager` – Sound playback system 
* `HighScoreManager` – Persistent leaderboard system 
* `Direction` & `TileType` – Enums for movement and map representation  

---

## ⚙️ Game Mechanics

### 🎯 Movement

* Grid-based movement system
* Collision detection with walls via the `Maze` class

### 👻 Ghost AI

* Uses a simple heuristic:

  * Moves toward Pac-Man by minimizing distance
  * Switches to random movement when Pac-Man is powered up

### ⚡ Power Mode

* Triggered by power pellets
* Temporarily allows Pac-Man to eat ghosts

### 🎁 Bonus System

* Random spawning of:

  * Point bonuses
  * Freeze items (temporarily stop ghost movement)

### 🏆 High Scores

* Top scores are saved locally to a file
* Automatically sorted and persisted between sessions

---

## 🛠️ Technologies Used

* Java
* Java Swing (GUI & rendering)
* Object-Oriented Programming (OOP)
* Event-driven programming (game loop via Timer)

---

## ▶️ How to Run

1. Compile all `.java` files:

```bash
javac *.java
```

2. Run the game:

```bash
java Game
```

---

## 🎮 Controls

* Arrow keys → Move Pac-Man
* `P` → Pause / Resume
* `R` → Restart (after game over)

---

## 📁 Project Structure

```id="r2x81a"
pacman-java/
├── Game.java
├── GamePanel.java
├── Maze.java
├── PacMan.java
├── Ghost.java
├── BonusItem.java
├── SoundManager.java
├── HighScoreManager.java
├── Direction.java
├── TileType.java
└── sounds/
```

---

## 🎯 Key Takeaways

* Designed and implemented a complete game system from scratch
* Applied object-oriented principles to separate responsibilities
* Built a real-time game loop with collision handling and AI behavior
* Implemented persistent data storage (high scores)
* Demonstrated understanding of game architecture and state management

---

## 📬 Contact

Feel free to reach out if you have questions or want to collaborate!
