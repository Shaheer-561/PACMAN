package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

class Frame extends JFrame {
    ImageIcon logo = new ImageIcon(App.class.getResource("logo_1.png"));
    Map map;
   
    Frame() {
        //objects
        map = new Map();
       
        //frame setting
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Pac-Man");
        this.setIconImage(logo.getImage());
        //panels
        this.add(map);
        
        //panel setting
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}

class Position {
    Image image;
    int x;
    int y;
    int width;
    int height;
    int startX;
    int startY;
    
    Position(Image image, int x, int y, int width, int height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
    }
}

class Map extends JPanel implements KeyListener, ActionListener {
    //tile variables
    protected int blockSize = 32;
    protected int columns = 19;
    protected int rows = 21;
    protected int width = columns * blockSize;
    protected int height = rows * blockSize;
    
    // game variables
    protected boolean gameOver = false;
    protected int life = 3;
    protected int score = 0;
    
    // Movement variables
    private char currentDirection = 'd'; // Current moving direction
    private char nextDirection = 'd';    // Next attempted direction
    //blue ghost movement
    private char blueGhostDir = 'a';
private int blueGhostStepCount = 0;
//red ghost movement
 private char redGhostDir = 'a';
private int redGhostStepCount = 0;
//Pink ghost movement
 private char pinkGhostDir = 'a';
private int pinkGhostStepCount = 0;
//orange ghost movement
 private char orangeGhostDir = 'a';
private int orangeGhostStepCount = 0;


  
    
    HashSet<Position> walls;
    HashSet<Position> foods;
    Position blueGhost;
    Position redGhost;
    Position pinkGhost;
    Position orangeGhost;
    Position Pacman;
    
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    Map() {
        
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        

        // Load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();
        
        loadMap();
        
        // Game loop 
        Timer gameLoop = new Timer(5, this);
        gameLoop.start();
    }

    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "        bpo        ",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    protected void loadMap() {
        walls = new HashSet<>(); 
        foods = new HashSet<>(); 
        
        for(int r = 0; r < rows; r++) {
            String currentRow = tileMap[r];
            for(int c = 0; c < columns; c++) {
                char currentTile = currentRow.charAt(c);
                int x = c * blockSize;
                int y = r * blockSize;
                
                switch(currentTile) {
                    case 'X':
                        walls.add(new Position(wallImage, x, y, blockSize, blockSize));
                        break;
                    case ' ':
                        foods.add(new Position(null, x + 14, y + 14, 4, 4));
                        break;
                    case 'P':
                        Pacman = new Position(pacmanRightImage, x, y, blockSize, blockSize);
                        break;
                    case 'b':
                        blueGhost = new Position(blueGhostImage, x, y, blockSize, blockSize);
                        break;
                    case 'o':
                        orangeGhost = new Position(orangeGhostImage, x, y, blockSize, blockSize);
                        break;
                    case 'r':
                        redGhost = new Position(redGhostImage, x, y, blockSize, blockSize);
                        break;
                    case 'p':
                        pinkGhost = new Position(pinkGhostImage, x, y, blockSize, blockSize);
                        break;
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        
    }

    public void draw(Graphics g) {
        if (Pacman != null) {
            g.drawImage(Pacman.image, Pacman.x, Pacman.y, Pacman.width, Pacman.height, null);
        }

       if(blueGhost != null){
            g.drawImage(blueGhost.image, blueGhost.x, blueGhost.y, blueGhost.width, blueGhost.height, null);
       }
        if(redGhost != null){
            g.drawImage(redGhost.image, redGhost.x, redGhost.y, redGhost.width, redGhost.height, null);
       }
         if(orangeGhost != null){
            g.drawImage(orangeGhost.image, orangeGhost.x, orangeGhost.y, orangeGhost.width, orangeGhost.height, null);
       }
          if(pinkGhost != null){
            g.drawImage(pinkGhost.image, pinkGhost.x, pinkGhost.y, pinkGhost.width, pinkGhost.height, null);
       }

        for (Position wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.ORANGE);
        for (Position food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), blockSize/2, blockSize/2);
        }
        else {
            g.drawString("x" + String.valueOf(life) + " Score: " + String.valueOf(score), blockSize/2, blockSize/2);
        }
    }

    public void move() {
        // pehley check kero k new button say collusion to nai horha
        if (tryMove(nextDirection)) {
            currentDirection = nextDirection;
            
         
        
            return;
        }
        
        // If next direction fails, keep the current direction
        tryMove(currentDirection);
        
   

    }

   private boolean tryMove(char direction) {
    int nextX = Pacman.x;
    int nextY = Pacman.y;

    switch(direction) {
        case 'a':  
            nextX -= blockSize/12;
            Pacman.image = pacmanLeftImage;
            break;
        case 'd':
            nextX += blockSize/12;
            Pacman.image = pacmanRightImage;
            break;
        case 'w':
            nextY -= blockSize/12;
            Pacman.image = pacmanUpImage;
            break;
        case 's':
            nextY += blockSize/12;
            Pacman.image = pacmanDownImage;
            break;
    }

 
    // Check wall collision first (if wall, movement fails)
    Position nextPacman = new Position(null, nextX, nextY, Pacman.width, Pacman.height);
    for (Position wall : walls) {
        if (collision(nextPacman, wall)) {
            return false; // Wall collision → movement blocked
        }
    }

    // If no wall collision, check if next position has food
    Position foodToRemove = null;
    for (Position food : foods) {
        // Check if food is at (nextX, nextY) before Pacman moves
        if (food.x + food.width/2 >= nextX && 
            food.x <= nextX + Pacman.width &&
            food.y + food.height/2 >= nextY && 
            food.y <= nextY + Pacman.height) {
            foodToRemove = food;
            score += 10;
            break; 
        }
    }

    // Remove food before moving
    if (foodToRemove != null) {
        foods.remove(foodToRemove);
    }
       if(nextX == 0 && Pacman.image == pacmanLeftImage){
        Pacman.x = 608;   
    }
       else if(nextX == 608 && Pacman.image == pacmanRightImage){
           Pacman.x = 0;
       }

    // move Pacman to the new position
       else{
    Pacman.x = nextX;}
    Pacman.y = nextY;
    repaint();
    return true;
}
//main collision formula
    public boolean collision(Position a, Position b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

 public void ghostMovementBlue() {
    int nextX = blueGhost.x;
    int nextY = blueGhost.y;

    switch(blueGhostDir) {
        case 'a': nextX -= blockSize / 12; break;
        case 'd': nextX += blockSize / 12; break;
        case 'w': nextY -= blockSize / 12; break;
        case 's': nextY += blockSize / 12; break;
    }

    Position nextBlue = new Position(null, nextX, nextY, blueGhost.width, blueGhost.height);

    boolean collided = false;
    for (Position wall : walls) {
        if (collision(nextBlue, wall)) {
            collided = true;
            break;
        }
    }

    if (collided || blueGhostStepCount >= 5 * blockSize) {
        // change direction on collision or after 5 tiles
        Random rand = new Random();
        char[] keys = {'w','a','s','d'};
        char newDir;

        do {
            newDir = keys[rand.nextInt(keys.length)];
        } while (newDir == blueGhostDir);

        blueGhostDir = newDir;
        blueGhostStepCount = 0;
    } else {
       
        blueGhost.x = nextX;
        blueGhost.y = nextY;
        blueGhostStepCount += blockSize / 12;
    }
    if(nextX == 0 ){
        blueGhost.x = 608;   
    }
       else if(nextX == 608 ){
           blueGhost.x = 0;
       }
}//ghostMovementBlue ends here
public void ghostMovementPink() {
    int nextX = pinkGhost.x;
    int nextY = pinkGhost.y;

    switch(pinkGhostDir) {
        case 'a': nextX -= blockSize / 12; break;
        case 'd': nextX += blockSize / 12; break;
        case 'w': nextY -= blockSize / 12; break;
        case 's': nextY += blockSize / 12; break;
    }

    Position nextPink = new Position(null, nextX, nextY, pinkGhost.width, pinkGhost.height);

    boolean collided = false;
    for (Position wall : walls) {
        if (collision(nextPink, wall)) {
            collided = true;
            break;
        }
    }

    if (collided || pinkGhostStepCount >= 5 * blockSize) {
        // change direction on collision or after 5 tiles
        Random rand = new Random();
        char[] keys = {'w','a','s','d'};
        char newDir;

        do {
            newDir = keys[rand.nextInt(keys.length)];
        } while (newDir == pinkGhostDir);

        pinkGhostDir = newDir;
        pinkGhostStepCount = 0;
    } else {
       
        pinkGhost.x = nextX;
        pinkGhost.y = nextY;
        pinkGhostStepCount += blockSize / 12;
    }
    if(nextX == 0 ){
        pinkGhost.x = 608;   
    }
       else if(nextX == 608 ){
           pinkGhost.x = 0;
       }
}
public void ghostMovementRed() {
    int nextX = redGhost.x;
    int nextY = redGhost.y;

    switch(redGhostDir) {
        case 'a': nextX -= blockSize / 12; break;
        case 'd': nextX += blockSize / 12; break;
        case 'w': nextY -= blockSize / 12; break;
        case 's': nextY += blockSize / 12; break;
    }

    Position nextRed = new Position(null, nextX, nextY, redGhost.width, redGhost.height);

    boolean collided = false;
    for (Position wall : walls) {
        if (collision(nextRed, wall)) {
            collided = true;
            break;
        }
    }

    if (collided || redGhostStepCount >= 7 * blockSize) {
        // change direction on collision or after 7 tiles
        Random rand = new Random();
        char[] keys = {'w','a','s','d'};
        char newDir;

        do {
            newDir = keys[rand.nextInt(keys.length)];
        } while (newDir == redGhostDir);

        redGhostDir = newDir;
        redGhostStepCount = 0;
    } else {
       
        redGhost.x = nextX;
        redGhost.y = nextY;
        redGhostStepCount += blockSize / 12;
    }
    if(nextX == 0 ){
        redGhost.x = 608;   
    }
       else if(nextX == 608 ){
           redGhost.x = 0;
       }
}
public void ghostMovementOrange() {
    int nextX = orangeGhost.x;
    int nextY = orangeGhost.y;

    switch(orangeGhostDir) {
        case 'a': nextX -= blockSize / 12; break;
        case 'd': nextX += blockSize / 12; break;
        case 'w': nextY -= blockSize / 12; break;
        case 's': nextY += blockSize / 12; break;
    }

    Position nextOrange = new Position(null, nextX, nextY, orangeGhost.width, orangeGhost.height);

    boolean collided = false;
    for (Position wall : walls) {
        if (collision(nextOrange, wall)) {
            collided = true;
            break;
        }
    }

    if (collided || orangeGhostStepCount >= 4 * blockSize) {
        // change direction on collision or after 5 tiles
        Random rand = new Random();
        char[] keys = {'w','a','s','d'};
        char newDir;

        do {
            newDir = keys[rand.nextInt(keys.length)];
        } while (newDir == orangeGhostDir);

        orangeGhostDir = newDir;
        orangeGhostStepCount = 0;
    } else {
       
        orangeGhost.x = nextX;
        orangeGhost.y = nextY;
        orangeGhostStepCount += blockSize / 12;
    }
    if(nextX == 0 ){
        orangeGhost.x = 608;   
    }
       else if(nextX == 608 ){
           orangeGhost.x = 0;
       }
}
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        char key = Character.toLowerCase(e.getKeyChar());
        if (key == 'w' || key == 'a' || key == 's' || key == 'd') {
            nextDirection = key; 
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
//jab bhi koi action listener milay move will be applied
    @Override
    public void actionPerformed(ActionEvent e) {
       
            move();
            ghostMovementBlue();
ghostMovementRed();
ghostMovementPink();
ghostMovementOrange();
repaint();

//now for collusion between pacman and ghost
if (collision(Pacman, blueGhost) || collision(Pacman, redGhost) ||
        collision(Pacman, pinkGhost) || collision(Pacman, orangeGhost)) {
        
        life--;

        // Reset all to starting positions
        Pacman.x = Pacman.startX;
        Pacman.y = Pacman.startY;

        blueGhost.x = blueGhost.startX;
        blueGhost.y = blueGhost.startY;

        redGhost.x = redGhost.startX;
        redGhost.y = redGhost.startY;

        pinkGhost.x = pinkGhost.startX;
        pinkGhost.y = pinkGhost.startY;

        orangeGhost.x = orangeGhost.startX;
        orangeGhost.y = orangeGhost.startY;

        if (life == 0) {
            gameOver = true;
        }
    }

    repaint();
        
    }
}
//pause menu
//class Pause extends JPanel implements KeyListener{
//private int width = 608;
//private int height = 672;
//
//    @Override
//    public void keyTyped(KeyEvent e) {      }
//
//    @Override
//    public void keyPressed(KeyEvent e) {    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//        System.out.println("Spacebar pressed!");
//        // Add your game logic (e.g., pause, shoot, etc.)
//    }
//    }
//    
//}
public class App {
    public static void main(String[] args) {
    new Frame();
    }
}