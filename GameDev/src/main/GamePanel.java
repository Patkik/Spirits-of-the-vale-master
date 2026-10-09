
package main;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    final int originalTileSize = 16;
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 100;
    public final int maxWorldRow = 100;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    // FULLSCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;
    
    // FPS
    int FPS = 60;
    
    // SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    Thread gameThread;
    
    // ENTITY and OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[15]; // Initialize obj array
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public ArrayList<Entity> projectileList = new ArrayList<>(); //DSa
    ArrayList<Entity> entityList = new ArrayList<>();
    
    
    // GAME STATE    
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;
    public final int gameOverState = 5;
    public final int optionState = 6;
    
    
    
    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 6;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                requestFocusInWindow();
                handleMouseClick(e);
            }
        });

        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                handleMouseMove(e);
            }
        });
    }

    private void handleMouseClick(java.awt.event.MouseEvent e) {
        if (gameState == titleState && ui.titleScreenState == 0) {
            int my = e.getY();
            int baseY = tileSize * 9;
            if (my >= baseY - 40 && my < baseY + 10) {
                ui.commandNum = 0;
                gameState = playState;
                playMusic(0);
            } else if (my >= baseY + tileSize - 40 && my < baseY + tileSize + 10) {
                ui.commandNum = 1;
            } else if (my >= baseY + tileSize * 2 - 40 && my < baseY + tileSize * 2 + 10) {
                ui.commandNum = 2;
                System.exit(0);
            }
        }
    }

    private void handleMouseMove(java.awt.event.MouseEvent e) {
        if (gameState == titleState && ui.titleScreenState == 0) {
            int my = e.getY();
            int baseY = tileSize * 9;
            if (my >= baseY - 40 && my < baseY + 10) {
                if (ui.commandNum != 0) {
                    ui.commandNum = 0;
                    playSE(10);
                }
            } else if (my >= baseY + tileSize - 40 && my < baseY + tileSize + 10) {
                if (ui.commandNum != 1) {
                    ui.commandNum = 1;
                    playSE(10);
                }
            } else if (my >= baseY + tileSize * 2 - 40 && my < baseY + tileSize * 2 + 10) {
                if (ui.commandNum != 2) {
                    ui.commandNum = 2;
                    playSE(10);
                }
            }
        }
    }

    public void setUpGame() {
        aSetter.setObject(); // Ensure objects are initialized
        aSetter.setNPC(); // Ensure NPCs are initialized
        aSetter.setMonster();
        gameState = titleState; // Ensure the title state is set initially
        
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
        
        if(fullScreenOn == true) {
        setFullScreen();
        }
    }
    public void retry() {
    	player.setDefaultPositions();
    	player.restoreLife();
    	aSetter.setNPC();
    	aSetter.setMonster();
    }
    public void restart() {
    	player.setDefaultPositions();
    	player.setDefaultValues();
    	player.restoreLife();
    	player.setItems();
    	aSetter.setObject();
    	aSetter.setMonster();
    	aSetter.setNPC();
    }
    
    public void setFullScreen() {
    	//GET LOCAL SCREEN DEVICE
    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    	GraphicsDevice gd = ge.getDefaultScreenDevice();
    	gd.setFullScreenWindow(Main.window);
    	
    	//GET FULL SCREEN HEIGHT AND WIDTH
    	screenWidth2 = Main.window.getWidth();
    	screenHeight2 = Main.window.getHeight();
    	
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                try {
                    update();
                    drawtoTempScreen(); // DRAW EVERYTHING TO BUFFERED IMAGE
                    drawToScreen(); // DRAW BUFFERED IMAGE TO SCREEN
                } catch (Exception e) {
                    System.err.println("Render error: " + e.getMessage());
                    e.printStackTrace();
                }
                delta--;
                drawCount++;
            }

            // Cap delta if the system hitched to prevent spiral of death / freezing
            if (delta > 3) {
                delta = 1;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }

            // Sleep briefly to yield CPU and keep OS / AWT event queue responsive
            try {
                double remainingTime = (lastTime + drawInterval - System.nanoTime()) / 1000000.0;
                if (remainingTime > 1) {
                    Thread.sleep((long) remainingTime);
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                // Thread interrupted
            }
        }
    }

    public void update() {
        if (gameState == playState) {
            // player
            player.update();
            // npc
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                	if(monster[i].alive == true && monster[i].dying ==false) {
                		monster[i].update();
                	}
                	if(monster[i].alive == false) {
                		monster[i].checkDrop();
                		monster[i] = null;
                	}
                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                	if(projectileList.get(i).alive == true) {
                		projectileList.get(i).update();
                	}
                	if(projectileList.get(i).alive == false) {
                		projectileList.remove(i);
                	}
                }
            }
            
            eHandler.checkEvent(); // Use the existing EventHandler instance
        }
    }
    
    public void drawtoTempScreen() {
    	 if (gameState == titleState) {
             ui.draw(g2); // Draw title screen
         } else {
             
         	tileM.draw(g2);
         	
         	entityList.add(player);
         	
         	//add entities to the list
         	for(int i = 0; i < npc.length; i++) {
         		if(npc[i] != null) {
         			entityList.add(npc[i]);
         			}
         		}
         	
         
 	        for(int i = 0; i < obj.length; i++) {
 	    		if(obj[i] != null) {
 	    			entityList.add(obj[i]);
 	    			}
 	    		}	
 	        for(int i = 0; i < monster.length; i++) {
 	    		if(monster[i] != null) {
 	    			entityList.add(monster[i]);
 	    			}
 	    		}
 	        for(int i = 0; i < projectileList.size(); i++) {
 	    		if(projectileList.get(i) != null) {
 	    			entityList.add(projectileList.get(i));
 	    			}
 	    		}
 	        //sort
 	        Collections.sort(entityList, new Comparator<Entity>() {

 				@Override
 				public int compare(Entity e1, Entity e2) {
 					int result = Integer.compare(e1.worldY, e2.worldY);
 					return result;
 				}
 	        	
 	        });
 	        try {
 	        //draw entities
 	        for(int i = 0; i < entityList.size(); i ++) {
 	        	entityList.get(i).draw(g2);
 	        }
 	        }catch(Exception e) {
 	        	System.out.println("image null");
 	        }
 	        
 	        // Draw entities
 	        entityList.clear();

 	        // Clear entity list after rendering
 	        entityList.clear();

 	        // Draw UI
 	        ui.draw(g2);

         
     }
    }

    public void drawToScreen() {
    	Graphics g = getGraphics();
    	if (g != null) {
    	    g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
    	    g.dispose();
    	}
    }
    
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
