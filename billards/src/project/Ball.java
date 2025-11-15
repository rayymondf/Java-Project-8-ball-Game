//Ball class for my pool game
package project;

import javax.swing.*;
import java.awt.*;


class Ball {
    // Position and visual properties
    double x, y;        // Top-left corner coordinates (Java2D draws from top-left)
    int radius;         // Radius of the ball
    Color color;        // Color of the ball
    
    // Physics properties
    double velocityX = 0; // Horizontal velocity (pixels per frame)
    double velocityY = 0; // Vertical velocity (pixels per frame)
    double friction = 0.98; // Friction coefficient (2% speed loss per frame)
    
    
    //Purpose: Constructor creates a new ball with specified properties
    //Pre: double x, double y, int radius, Color color
    //Post: none
    public Ball(double x, double y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }
    

    //Purpose: Draws the ball on the specified graphics context
    //Pre: Graphics2D g2d
    //Post: none
    public void draw(Graphics2D g2d) {
        g2d.setColor(color); // Set the drawing color
        // Draw oval with diameter = radius*2 (Swing draws from top-left with width/height)
        g2d.fillOval((int)(x), (int)(y), radius*2, radius*2);
    }
}