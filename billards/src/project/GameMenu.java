//GameMenu class for my pool game

package project;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class GameMenu extends JPanel implements ActionListener {
    // UI components
    JButton rules = new JButton("RULES"); // Button to show/hide rules
    JButton start = new JButton("START"); // Button to start game
    JTextArea txtArea = new JTextArea(100, 100); // Displays game rules
    int current = 0; // Tracks rules visibility state (0=hidden, 1=visible)
     
    //Purpose: Constructor sets up menu UI
    //Pre: none
    //Post none
    public GameMenu() {
        setBackground(new Color(50, 100, 50)); // Dark green background
        
        setLayout(null); // Use absolute positioning for components
        
        // Configure rules button
        rules.setBounds(200,40,150,60); // Position and size
        rules.addActionListener(this);   // Register click handler
        
        // Configure start button
        start.setBounds(400,40,150,60); // Position and size
        start.addActionListener(this);   // Register click handler
        
        // Configure rules text area
        txtArea.setBounds(10,120,750,200); // Position and size
        txtArea.setEditable(false);     // Prevent editing
        txtArea.setVisible(false);      // Start hidden
        txtArea.setLineWrap(true);      // Enable line wrapping
        txtArea.setWrapStyleWord(true); // Wrap at word boundaries
        
        // Add components to panel
        add(txtArea);
        add(rules);
        add(start);
    }


    //Purpose: Handles button clicks
    //Pre: ActionEvent e
    //Post: none
    public void actionPerformed(ActionEvent e) {
        // Rules button toggle behavior
        if(e.getSource()==rules && current ==0) { // If rules button clicked and rules hidden
        	
        	//Showing the rules:
        	txtArea.setText (" RULES: The game is a two-player digital version of 8-ball billiards, developed in Java. It will simulate a standard pool table with corner pockets and a full set of balls in the colours of red, blue, and black (8-ball). Each player will be assigned either red or blue and must aim to sink all of their designated balls into the pockets before pocketing the 8-ball to win. Players will take turns controlling the cue ball, using the arrow keys to adjust the angle of the shot and the space bar to strike the cue ball. If a player makes the 8-ball before pocketing all their designated balls, they will automatically lose. ");
            txtArea.setVisible(true); // Show rules
            current=1; // Update state to "visible"
        }
        else if(e.getSource()==rules && current ==1) { // If rules button clicked and rules visible
            txtArea.setVisible(false); // Hide rules
            current=0; // Update state to "hidden"
        }
        // Start button behavior
        else if (e.getSource()==start) { // If start button clicked
            GamePanel gamePanel = new GamePanel(); // Create new game panel
            
            // Switch to game panel using card layout
            Main.centerPanel.add(gamePanel,"gamePanel");
            Main.navigation.show(Main.centerPanel, "gamePanel");
            gamePanel.requestFocusInWindow(); // Ensure game panel receives keyboard input
        }
    }
}