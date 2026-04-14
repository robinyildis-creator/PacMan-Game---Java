public enum Direction { 
    // Skapar en "enum" som heter Direction
    UP(0, -1), // Minskar y med 1
    DOWN(0, 1), // Ökar y med 1
    LEFT(-1, 0), // Minskar x med 1
    RIGHT(1, 0); // Ökar x med 1

    public final int dx; // Förändring i x-led
    public final int dy; // Förändring i y-led

    Direction(int dx, int dy) {
        // Konstruktor för varje enum-värde
        this.dx = dx;
        this.dy = dy;
    }
}