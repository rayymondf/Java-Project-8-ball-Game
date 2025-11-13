//Raymond Fang
//June 10, 2025
//ICS4U
//GamePanel class for my pool game

package cpt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener, ActionListener {

    // Table dimensions - these constants define the size and position of the pool table
    private final int TABLE_WIDTH = 700;  // Width of the playable table area 
    private final int TABLE_HEIGHT = 350; // Height of the playable table area 
    private final int TABLE_X = 50;       // X-coordinate of table's top-left corner
    private final int TABLE_Y = 100;      // Y-coordinate of table's top-left corner

    // Ball properties - these control the visual and physical properties of balls
    private final int BALL_RADIUS = 10;   // Radius of each ball 
    private final int POCKET_RADIUS = 30; // Radius of each pocket 

    // Game state variables - these track the current state of the game
    private Ball cueBall;                 // The white cue ball that players hit
    private Timer timer;                  // Controls the game loop timing
    private boolean canShoot = true;      // indicator if player can take a shot
    private final double SHOOT_POWER = 30;// Base velocity applied when shooting
    private double aimAngle = 0;          // Current aiming angle in radians (0 = right)
    private ArrayList<Ball> balls = new ArrayList<>(); // List of all balls except cue ball

    // Player UI elements - these display game information
    JLabel player1 = new JLabel(); // Shows Player 1's remaining balls
    JLabel player2 = new JLabel(); // Shows Player 2's remaining balls
    
    // Player ball counts - track how many balls each player needs to pocket
    int player1Balls = 6; // Player 1 (red) starts with 6 balls
    int player2Balls = 6; // Player 2 (blue) starts with 6 balls

    // Constructor - sets up the initial game state
    public GamePanel() {
        setBackground(new Color(50, 100, 50)); // Set dark green background color
        addKeyListener(this);  // Enable keyboard input handling
        setFocusable(true);    // Allow this panel to receive keyboard focus
        resetGame();           // Initialize all game objects
        
        // Create game loop timer that ticks every 16ms (60fps)
        timer = new Timer(16, this); 
        timer.start(); // Start the game loop
    }

    //Purpose: handles all rendering of game objects
    //Pre: Graphics g
    //Post: none
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call parent class painting first
        Graphics2D g2d = (Graphics2D) g; // Get Graphics2D for advanced drawing

        // Draw table border (darker green area around table)
        g2d.setColor(new Color(20, 70, 20));
        g2d.fillRect(TABLE_X - 20, TABLE_Y - 20, TABLE_WIDTH + 40, TABLE_HEIGHT + 40);
        
        // Draw table surface (lighter green playing area)
        g2d.setColor(new Color(10, 100, 10));
        g2d.fillRect(TABLE_X, TABLE_Y, TABLE_WIDTH, TABLE_HEIGHT);

        // Draw all 6 pockets (black circles at table corners and sides)
        g2d.setColor(Color.BLACK);
        // Top-left pocket
        g2d.fillOval(TABLE_X-POCKET_RADIUS, TABLE_Y-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        // Top-middle pocket
        g2d.fillOval(TABLE_X + TABLE_WIDTH/2 -POCKET_RADIUS, TABLE_Y-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        // Top-right pocket
        g2d.fillOval(TABLE_X + TABLE_WIDTH -POCKET_RADIUS, TABLE_Y-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        // Bottom-left pocket
        g2d.fillOval(TABLE_X -POCKET_RADIUS, TABLE_Y + TABLE_HEIGHT-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        // Bottom-middle pocket
        g2d.fillOval(TABLE_X + TABLE_WIDTH/2 -POCKET_RADIUS, TABLE_Y + TABLE_HEIGHT-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        // Bottom-right pocket
        g2d.fillOval(TABLE_X + TABLE_WIDTH -POCKET_RADIUS, TABLE_Y + TABLE_HEIGHT-POCKET_RADIUS, POCKET_RADIUS * 2, POCKET_RADIUS * 2);
        
        // Draw aiming line when player can shoot (yellow line from cue ball)
        g2d.setColor(Color.yellow);
        if (canShoot) { // Only draw when player is allowed to shoot
            // Create line from cue ball center in direction of aimAngle
            Line2D.Double line = new Line2D.Double(
                cueBall.x + BALL_RADIUS, // Start X at cue ball center
                cueBall.y + BALL_RADIUS, // Start Y at cue ball center
                cueBall.x + BALL_RADIUS + 50 * Math.cos(aimAngle), // End X 50px in aim direction
                cueBall.y + BALL_RADIUS + 50 * Math.sin(aimAngle)); // End Y 50px in aim direction
            g2d.draw(line); // Actually draw the line
        }

        // Draw all balls - other balls first so cue ball appears on top
        for (Ball ball : balls) {
            ball.draw(g2d); // Draw each colored ball
        }
        cueBall.draw(g2d); // Draw cue ball last (on top)

        // Update and display player ball counts
        player1.setText("Player 1 (Red) Balls Left: " + player1Balls);
        player2.setText("Player 2 (Blue) Balls Left: " + player2Balls);
        player1.setBounds(0,0,200,30); // Position player1 label at top-left
        player2.setBounds(0,30,200,30); // Position player2 label below player1
        
        // Add labels if they haven't been added yet
        add(player1);
        add(player2);

        // Force label updates
        player1.repaint();
        player2.repaint();
    }

    //Purpose: initializes all game objects to starting positions
    //Pre:none
    //Post:none
    private void resetGame() {
        balls.clear(); // Remove all existing balls
        
        // Create cue ball at starting position (left side of table)
        cueBall = new Ball(TABLE_X + 150, TABLE_Y + TABLE_HEIGHT / 2, BALL_RADIUS, Color.WHITE);
        
        // Create rack of balls in triangle formation (standard 8-ball setup)
        // First column (1 ball)
        balls.add(new Ball(TABLE_X + 450, TABLE_Y + TABLE_HEIGHT/2, BALL_RADIUS, Color.RED));
        // Second column (2 balls)
        balls.add(new Ball(TABLE_X + 450 + 20, TABLE_Y + TABLE_HEIGHT/2 - 15, BALL_RADIUS, Color.BLUE));
        balls.add(new Ball(TABLE_X + 450 + 20, TABLE_Y + TABLE_HEIGHT/2 + 15, BALL_RADIUS, Color.RED));
        // Third column (3 balls)
        balls.add(new Ball(TABLE_X + 450 + 40, TABLE_Y + TABLE_HEIGHT/2 -30, BALL_RADIUS, Color.BLUE));
        balls.add(new Ball(TABLE_X + 450 + 40, TABLE_Y + TABLE_HEIGHT/2, BALL_RADIUS, Color.RED));
        balls.add(new Ball(TABLE_X + 450 + 40, TABLE_Y + TABLE_HEIGHT/2 + 30, BALL_RADIUS, Color.BLUE));
        // Fourth column (4 balls)
        balls.add(new Ball(TABLE_X + 450 + 60, TABLE_Y + TABLE_HEIGHT/2 -45, BALL_RADIUS, Color.RED));
        balls.add(new Ball(TABLE_X + 450 + 60, TABLE_Y + TABLE_HEIGHT/2 -15, BALL_RADIUS, Color.BLUE));
        balls.add(new Ball(TABLE_X + 450 + 60, TABLE_Y + TABLE_HEIGHT/2 +15, BALL_RADIUS, Color.RED));
        balls.add(new Ball(TABLE_X + 450 + 60, TABLE_Y + TABLE_HEIGHT/2 +45, BALL_RADIUS, Color.BLUE));
        // Fifth column (3 balls - partial row for 8-ball rack)
        balls.add(new Ball(TABLE_X + 450 + 80, TABLE_Y + TABLE_HEIGHT/2 -30, BALL_RADIUS, Color.RED));
        balls.add(new Ball(TABLE_X + 450 + 80, TABLE_Y + TABLE_HEIGHT/2 +30, BALL_RADIUS, Color.BLUE));
        balls.add(new Ball(TABLE_X + 450 + 80, TABLE_Y + TABLE_HEIGHT/2, BALL_RADIUS, Color.BLACK)); // 8-ball in center
    }

    //Purpose: game loop called by timer
    //Pre: ActionEven e
    //Post: None
    public void actionPerformed(ActionEvent e) {
        // Update physics for all balls
        updateBallPhysics(cueBall); // First update cue ball
        for (Ball ball : balls) {   // Then update all other balls
            updateBallPhysics(ball);
        }
        
        // Check for collisions between all pairs of balls
        checkAllCollisions();
        
        // Handle any balls that fell into pockets
        handlePocketedBall();
        
        // Check if all balls have stopped moving
        checkIfBallsStopped();
        
        repaint(); // Request Swing to redraw the game
    }

    //Purpose: handles movement and boundaries for a single ball
    //Pre: Ball ball
    //Post: none
    private void updateBallPhysics(Ball ball) {
        // Update position based on current velocity
        ball.x += ball.velocityX; // Move horizontally by velocityX pixels
        ball.y += ball.velocityY; // Move vertically by velocityY pixels

        // Apply friction (gradual slowdown)
        ball.velocityX *= ball.friction; // Reduce horizontal velocity by friction factor
        ball.velocityY *= ball.friction; // Reduce vertical velocity by friction factor

        // Check for collisions with table edges (with bounce and energy loss)
        if (ball.x < TABLE_X) { // Left edge collision
            ball.x = TABLE_X; // Prevent ball from going past edge
            ball.velocityX = -ball.velocityX * 0.8; // Reverse X velocity with 20% energy loss
        }
        if (ball.x + BALL_RADIUS*2 > TABLE_X + TABLE_WIDTH) { // Right edge collision
            ball.x = TABLE_X + TABLE_WIDTH - BALL_RADIUS*2; // Keep ball within bounds
            ball.velocityX = -ball.velocityX * 0.8; // Bounce with energy loss
        }
        if (ball.y < TABLE_Y) { // Top edge collision
            ball.y = TABLE_Y; // Keep ball within top boundary
            ball.velocityY = -ball.velocityY * 0.8; // Bounce with energy loss
        }
        if (ball.y + BALL_RADIUS*2 > TABLE_Y + TABLE_HEIGHT) { // Bottom edge collision
            ball.y = TABLE_Y + TABLE_HEIGHT - BALL_RADIUS*2; // Keep ball within bounds
            ball.velocityY = -ball.velocityY * 0.8; // Bounce with energy loss
        }
        
        // Stop very slow balls completely (to prevent tiny movements)
        if (Math.abs(ball.velocityX) < 0.05) { // If X velocity is very small
        	ball.velocityX = 0; // Set to exactly zero
        }
        if (Math.abs(ball.velocityY) < 0.05) { // If Y velocity is very small
        	ball.velocityY = 0; // Set to exactly zero
        }
    }

    //Purpose: checks for collisions between all ball pairs
    //Pre: none
    //Post: none
    private void checkAllCollisions() {
        // Check cue ball against all other balls
        for (Ball ball : balls) {
            checkCollision(cueBall, ball); // Test cue ball vs each colored ball
        }
        
        // Check all other balls against each other (avoid duplicate checks)
        for (int i = 0; i < balls.size(); i++) { // For each ball
            for (int j = i + 1; j < balls.size(); j++) { // Compare to remaining balls
                checkCollision(balls.get(i), balls.get(j)); // Check collision between pair
            }
        }
    }

    //Purpose: handles physics between two colliding balls
    //Pre: Ball ball1, Ball ball2
    //Post: none
    private void checkCollision(Ball ball1, Ball ball2) {
        // Calculate distance between ball centers
        double dx = ball2.x - ball1.x; // Horizontal distance
        double dy = ball2.y - ball1.y; // Vertical distance
        double distance = Math.sqrt(dx*dx + dy*dy); // Actual distance using Pythagorean theorem
        
        // If balls are overlapping (distance < sum of radii)
        if (distance < BALL_RADIUS * 2) {
            // Calculate angle of collision (direction from ball1 to ball2)
            double angle = Math.atan2(dy, dx);
            
            //sin/cos for rotation calculations
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            
            // Rotate velocities to align with collision angle
            double vx1 = ball1.velocityX * cos + ball1.velocityY * sin;
            double vy1 = ball1.velocityY * cos - ball1.velocityX * sin;
            double vx2 = ball2.velocityX * cos + ball2.velocityY * sin;
            double vy2 = ball2.velocityY * cos - ball2.velocityX * sin;
            
            // Swap X velocities (elastic collision for equal masses)
            double v1Final = vx2; // Ball1 gets ball2's X velocity
            double v2Final = vx1; // Ball2 gets ball1's X velocity
            
            // Rotate velocities back to original coordinate system
            ball1.velocityX = v1Final * cos - vy1 * sin;
            ball1.velocityY = vy1 * cos + v1Final * sin;
            ball2.velocityX = v2Final * cos - vy2 * sin;
            ball2.velocityY = vy2 * cos + v2Final * sin;
            
            // Separate balls to prevent sticking (resolve overlap)
            double overlap = (BALL_RADIUS * 2 - distance) / 2; // Calculate penetration depth
            ball1.x -= overlap * cos; // Move ball1 back along collision angle
            ball1.y -= overlap * sin;
            ball2.x += overlap * cos; // Move ball2 forward along collision angle
            ball2.y += overlap * sin;
        }
    }

    //Purpose: determines if all balls have stopped moving
    //Pre: none
    //Post: None
    private void checkIfBallsStopped() {
        boolean allStopped = true; // Assume stopped until proven otherwise
        
        // Check cue ball's velocity
        if (Math.abs(cueBall.velocityX) > 0.1 || Math.abs(cueBall.velocityY) > 0.1) {
            allStopped = false; // Cue ball is still moving
        }
        
        // Check other balls' velocities
        for (Ball ball : balls) {
            if (Math.abs(ball.velocityX) > 0.1 || Math.abs(ball.velocityY) > 0.1) {
                allStopped = false; // Found a moving ball
                break; // No need to check others
            }
        }
        
        canShoot = allStopped; // Update shooting availability
    }

    //Purpose: removes balls that fell into pockets
    //Pre: none
    //Post: none
    private void handlePocketedBall() {
        // Create copy to avoid concurrent modification during iteration
        ArrayList<Ball> ballsCopy = new ArrayList<>(balls);
        
        for(Ball ball : ballsCopy) {
            if (isBallInPocket(ball)) { // Check if this ball is in any pocket
                balls.remove(ball); // Remove from active balls
                
                // Update appropriate player's ball count
                if (ball.color.equals(Color.RED)) { // Player 1's ball
                    player1Balls--; // Decrement count
                } else if (ball.color.equals(Color.BLUE)) { // Player 2's ball
                    player2Balls--; // Decrement count
                }
                //8-ball (black) is currently not specially handled
            }
        }
    }

    //Purpose: checks if ball is in any of the 6 pockets
    //Pre: Ball ball
    //Post: returns true/false if the ball entered a pocket
    private boolean isBallInPocket(Ball ball) {
        // Calculate ball's center coordinates
        double ballCenterX = ball.x + BALL_RADIUS;
        double ballCenterY = ball.y + BALL_RADIUS;
        
        // Check each pocket location
        if (isInPocket(ballCenterX, ballCenterY, TABLE_X, TABLE_Y) || // top-left
        	isInPocket(ballCenterX, ballCenterY, TABLE_X + TABLE_WIDTH/2, TABLE_Y) || // top-middle
            isInPocket(ballCenterX, ballCenterY, TABLE_X + TABLE_WIDTH, TABLE_Y) || // top-right
            isInPocket(ballCenterX, ballCenterY, TABLE_X, TABLE_Y + TABLE_HEIGHT) || // bottom-left
            isInPocket(ballCenterX, ballCenterY, TABLE_X + TABLE_WIDTH/2, TABLE_Y + TABLE_HEIGHT) || // bottom-middle
            isInPocket(ballCenterX, ballCenterY, TABLE_X + TABLE_WIDTH, TABLE_Y + TABLE_HEIGHT)) { // bottom-right
            return true; // Ball is in at least one pocket
        }
        return false; // Ball not in any pocket
    }

    //Purpose: checks if ball is within a specific pocket
    //Pre: double ballX, double ballY, double pocketX, double pocketY
    //Post: returns true/false if the distance between pocket and ball is less than the pocket radius
    private boolean isInPocket(double ballX, double ballY, double pocketX, double pocketY) {
        // Calculate distance from ball center to pocket center
        double dx = ballX - pocketX;
        double dy = ballY - pocketY;
        double distance = Math.sqrt(dx*dx + dy*dy);
        
        // Ball is in pocket if within pocket's radius
        return distance < POCKET_RADIUS;
    }

    //Purpose: handles keyboard input
    //Pre: KeyEvent e
    //Post: none
    public void keyPressed(KeyEvent e) {  
        if (canShoot) { // Only process input when allowed to shoot
            // Left arrow decreases aim angle (rotates counter-clockwise)
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                aimAngle -= 0.1; // Adjust angle by 0.1 radians (~5.7 degrees)
            } 
            // Right arrow increases aim angle (rotates clockwise)
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                aimAngle += 0.1; // Adjust angle by 0.1 radians
            }
        }
        
        // Space bar shoots the cue ball when allowed
        if (e.getKeyCode() == KeyEvent.VK_SPACE && canShoot) {
            // Set cue ball velocity based on aim angle and shoot power
            cueBall.velocityX = SHOOT_POWER * Math.cos(aimAngle); // X component
            cueBall.velocityY = SHOOT_POWER * Math.sin(aimAngle); // Y component
            
            canShoot = false; // Disable shooting until balls stop
        }
    }

    // Unused key listener methods (required by KeyListener interface)
    @Override
    public void keyTyped(KeyEvent e) {} // Not used
    @Override
    public void keyReleased(KeyEvent e) {} // Not used
}