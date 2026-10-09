package main;


import javax.swing.JFrame;

public class Main {
	
public static JFrame window;

public static void main(String[] args) {
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Spirits of the Vale");

		
		GamePanel gamePanel = new GamePanel ();
		window.add(gamePanel);
		
		gamePanel.config.loadConfig();
		if(gamePanel.fullScreenOn == true) {
			window.setUndecorated(true);
		}
		
		window.pack();
			
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.toFront();
		
		gamePanel.setUpGame();
		gamePanel.startGameThread();
		gamePanel.requestFocusInWindow();
		
		window.addWindowFocusListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowGainedFocus(java.awt.event.WindowEvent e) {
				gamePanel.requestFocusInWindow();
			}
		});
		
		
		
	
		
		
		
	}

}
