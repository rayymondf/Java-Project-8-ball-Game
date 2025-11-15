//Main class for my pool game

package project;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Main {
    // Static navigation components for screen management
    public static final CardLayout navigation = new CardLayout(); // Manages screen transitions
    public static JFrame frame = new JFrame(); // Main application window
    public static JPanel centerPanel = new JPanel(navigation); // Container for screens
    
    //Purpose: Constructor sets up the main window
    //Pre: none
    //Post: none
    public Main() {
        frame.setTitle("8-Ball Pool"); // Window title
        frame.setSize(800, 600);      // Window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close behavior
        frame.setResizable(false);    // Prevent window resizing
        
        GameMenu menu = new GameMenu(); // Create main menu
        
        // Set up card layout with menu screen
        centerPanel.add(menu,"menu"); // Add menu screen with identifier "menu"
        frame.add(centerPanel);      // Add card panel to frame
        navigation.show(centerPanel, "menu"); // Show menu screen first
        
        frame.setVisible(true); // Make window visible
    }

    public static void main(String[] args) {
        new Main(); // Create and show the game
    }
}