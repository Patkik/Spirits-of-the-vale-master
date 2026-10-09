package main;

public class EventHandler {

    private GamePanel gp;
    EventRect eventRect[][];
    
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    
    private boolean fountainEventTriggered = false;
    
    private boolean rockBlockCooldown = false;
    private int rockBlockCooldownCounter = 0;
    private static final int ROCK_BLOCK_COOLDOWN_TIME = 240; // 4 seconds at 60 FPS
    private boolean doorCooldown = false;
    private int doorCooldownCounter = 0;
    private static final int DOOR_COOLDOWN_TIME = 120; // 2 seconds at 60 FPS
    private boolean orbCooldown = false;
    private int orbCooldownCounter = 0;
    private static final int ORB_COOLDOWN_TIME = 120; // 2 seconds at 60 FPS

    // Bridge Variables
    private boolean bridgeCooldown = false;
    private int bridgeCooldownCounter = 0;
    private static final int BRIDGE_COOLDOWN_TIME = 120; // 2 seconds at 60 FPS
    

    // Damage handling variables
    private boolean canTakeDamage = true;
    private int damageCooldown = 0;

    public EventHandler(GamePanel gp) {
        this.gp = gp;
        
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
        
        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
        	
        	eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
        	col++;
        	
        	
        	if(col == gp.maxWorldCol) {
        		col = 0;
        		row++;
        	}
        	
        }
        
    }

    public void checkEvent() {
    	
    	//check if player character is more than 1 tile away from the last event
    	int xDistance = Math.abs(gp.player.worldX - previousEventX);
    	int yDistance = Math.abs(gp.player.worldY - previousEventY);
    	int distance = Math.max(xDistance, yDistance);
    	
    	if(distance > gp.tileSize) {
    		canTouchEvent = true;
    	}
    	
    	if(canTouchEvent == true) {
//    		// Check for pit damage event
//            if (hit(4, 1, "any")) {
//                if (canTakeDamage) {
//                    damagePit(gp.dialogueState);
//                    canTakeDamage = false;
//                    damageCooldown = 60; // 1 second at 60 FPS
//                    canTouchEvent = false;
//                }
//            }
            
            // Proximity to water fountain
            if (isNear(45, 4, 2)) { // Tile (45, 4) with a proximity range of 2
                fountainProximityEvent();
            }

            // Check for healing pool event
            if (hit(70, 31, "any")) {
                healingPool(gp.dialogueState);
            }	
            
            
            
            //rock dialogue
            if (rockBlockCooldown) {
                rockBlockCooldownCounter++;
                if (rockBlockCooldownCounter >= ROCK_BLOCK_COOLDOWN_TIME) {
                    rockBlockCooldown = false;
                    rockBlockCooldownCounter = 0;
                }
            }

            // Rock block event with cooldown check
            if (hit(31, 39, "left") || hit(41, 40, "left")) {
                if (!rockBlockCooldown) {
                    rockBlock(gp.dialogueState);
                    rockBlockCooldown = true;
                    rockBlockCooldownCounter = 0;
                }
            }
            
         // Existing cooldown logic for door
            if (doorCooldown) {
                doorCooldownCounter++;
                if (doorCooldownCounter >= DOOR_COOLDOWN_TIME) {
                    doorCooldown = false;
                    doorCooldownCounter = 0;
                }
            }

            // Modify door event checks to include cooldown
            if (hit(47, 35, "up")) {
                if (!doorCooldown) {
                    doorEvent(gp.dialogueState);
                    doorCooldown = true;
                    doorCooldownCounter = 0;
                }
            }
            if (hit(35, 68, "up")) {
                if (!doorCooldown) {
                    doorEvent(gp.dialogueState);
                    doorCooldown = true;
                    doorCooldownCounter = 0;
                }
            }
            if (hit(44, 71, "up")) {
                if (!doorCooldown) {
                    doorEvent(gp.dialogueState);
                    doorCooldown = true;
                    doorCooldownCounter = 0;
                }
            }
            // Teleportation Orb Event
            if (hit(71, 73, "any")) {
                if (!orbCooldown) {
                    teleportationOrbEvent1(gp.dialogueState);
                    orbCooldown = true;
                    orbCooldownCounter = 0;
                }
            }
            // Teleportation Orb Event return
            if (hit(35, 40, "any")) {
                if (!orbCooldown) {
                    teleportationOrbEvent(gp.dialogueState);
                    orbCooldown = true;
                    orbCooldownCounter = 0;
                }
            }
            // Teleportation Orb Event
            if (hit(39, 75, "any")) {
                if (!orbCooldown) {
                    teleportationOrbEvent2(gp.dialogueState);
                    orbCooldown = true;
                    orbCooldownCounter = 0;
                }
            }
            
    	}
        
        // Cooldown logic
        if (!canTakeDamage) {
            damageCooldown--;
            if (damageCooldown <= 0) {
                canTakeDamage = true;
            }
        }
     // Orb Cooldown Logic
        if (orbCooldown) {
            orbCooldownCounter++;
            if (orbCooldownCounter >= ORB_COOLDOWN_TIME) {
                orbCooldown = false;
                orbCooldownCounter = 0;
            }
        }

        // Bridge Cooldown Logic
        if (bridgeCooldown) {
            bridgeCooldownCounter++;
            if (bridgeCooldownCounter >= BRIDGE_COOLDOWN_TIME) {
                bridgeCooldown = false;
                bridgeCooldownCounter = 0;
            }
        }
    }

    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        // Null checks
        if (gp == null || gp.player == null) {
            return false;
        }

        // Save original positions
        int playerSolidX = gp.player.solidArea.x;
        int playerSolidY = gp.player.solidArea.y;
        int eventRectX = eventRect[col][row].x;
        int eventRectY = eventRect[col][row].y;

        // Update player solid area to world coordinates
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Update event rectangle to world coordinates
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].eventRectDefaultY;

        // Check for intersection
        if (gp.player.solidArea.intersects(eventRect[col][row])) {
            if (gp.player.direction.equals(reqDirection) || reqDirection.equals("any")) {
                hit = true;

                // Update previous event position
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;

                // Debug output to check collision results
                System.out.println("Collision detected at: " + previousEventX + ", " + previousEventY);
            }
        }

        // Restore original positions
        gp.player.solidArea.x = playerSolidX;
        gp.player.solidArea.y = playerSolidY;
        eventRect[col][row].x = eventRectX;
        eventRect[col][row].y = eventRectY;

        return hit;
    }
    public void fountainProximityEvent() {
        if (gp == null || gp.ui == null || gp.player == null || gp.keyH == null) {
            return;
        }

        // Trigger only if the event has not already been triggered
        if (!fountainEventTriggered) {
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You hear the soothing sound of water...\n" +
                                    "The fountain seems to beckon you.\n";
                                   
            fountainEventTriggered = true; // Mark the event as triggered
        }

        // Handle Enter key press to trigger the healing pool event
        if (gp.keyH.enterPressed) {
            // Only allow entering if the player is pressing enter to trigger healing
            if (isNear(69, 33, 6)) {
                healingPool(gp.dialogueState);
            }
            gp.keyH.enterPressed = false; // Reset enterPressed flag
            canTouchEvent = false; // Prevent immediate retrigger
        }
    }



 // Checks if the player is within range of a given point
    private boolean isNear(int targetCol, int targetRow, int range) {
        if (gp == null || gp.player == null) {
            return false;
        }

        int playerCol = gp.player.worldX / gp.tileSize;
        int playerRow = gp.player.worldY / gp.tileSize;

        int colDistance = Math.abs(playerCol - targetCol);
        int rowDistance = Math.abs(playerRow - targetRow);

        return colDistance <= range && rowDistance <= range;
    }

    public void doorEvent(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set game state to dialogue
        gp.gameState = gameState;

        // Set door dialogue
        gp.ui.currentDialogue = "The door is locked \n(I should just explore more)";
    }
    
    public void damagePit(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set dialogue state
        gp.gameState = gameState;
        gp.ui.currentDialogue = "You fell into a pit!";

        // Apply damage to player
        gp.player.life -= 1;

        // Play damage sound
        gp.playSE(3);

        // Check for game over
        if (gp.player.life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.currentDialogue = "You have perished...";
            gp.playSE(4);
        }
    }
    //healing pool
    public void healingPool(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null || gp.keyH == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Check if player is near and Enter is pressed
        
        	// Set the game state to dialogue state
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.playSE(9);
            gp.ui.currentDialogue = "You have taken a sip from this\n mysterious fountain. Fortunately, it \nfills you with vitality.";
            // Heal the player
            int maxLife = gp.player.maxLife;
            gp.player.life = Math.min(gp.player.life + maxLife, maxLife);
            gp.aSetter.setMonster();

            // Play healing sound
            gp.playSE(2); // Assuming sound effect index 2 is for healing
            // Reset Enter key flag
            gp.keyH.enterPressed = false;
            canTouchEvent = false; // Prevent immediate retrigger
        }
    


    public void rockBlock(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null || gp.keyH == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set the game state to dialogue state
        gp.gameState = gp.dialogueState;
        
        // Set dialogue message
        gp.ui.currentDialogue = "Rocks are blocking the way.\n" +
                                 "You cannot pass through here.";

        // Reset enter pressed to prevent repeated triggers
        gp.keyH.enterPressed = false;
    }
    //teleport 
    public void teleportationOrbEvent1(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set game state to dialogue
        gp.gameState = gameState;

        // Teleportation dialogue
        gp.ui.currentDialogue = "A mystical orb pulses with energy.\n" +
                                 "As you touch it, you feel a sudden\n" +
                                 "shift in reality...";

        // Teleport player to a specific location
        gp.player.worldX = gp.tileSize * 35; // Example X coordinate
        gp.player.worldY = gp.tileSize * 40; // Example Y coordinate

        // Optional: Play teleportation sound
        gp.playSE(5); // Assuming sound effect index 5 is for teleportation
    }
    //teleport back
    public void teleportationOrbEvent(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set game state to dialogue
        gp.gameState = gameState;

        // Teleportation dialogue
        gp.ui.currentDialogue = "A mystical orb pulses with energy.\n" +
                                 "As you touch it, you feel a sudden\n" +
                                 "shift in reality...";

        // Teleport player to a specific location
        gp.player.worldX = gp.tileSize * 71; // Example X coordinate
        gp.player.worldY = gp.tileSize * 73; // Example Y coordinate

        // Optional: Play teleportation sound
        gp.playSE(5); // Assuming sound effect index 5 is for teleportation
    }
    public void teleportationOrbEvent2(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set game state to dialogue
        gp.gameState = gameState;

        // Teleportation dialogue
        gp.ui.currentDialogue = "A mystical orb pulses with energy.\n" +
                                 "As you touch it, you feel a sudden\n" +
                                 "shift in reality...";

        // Teleport player to a specific location
        gp.player.worldX = gp.tileSize * 89; // Example X coordinate
        gp.player.worldY = gp.tileSize * 29; // Example Y coordinate

        // Optional: Play teleportation sound
        gp.playSE(5); // Assuming sound effect index 5 is for teleportation
    }
    public void bridgeEvent(int gameState) {
        // Null checks
        if (gp == null || gp.ui == null || gp.player == null) {
            System.err.println("Critical error: Game components are null");
            return;
        }

        // Set game state to dialogue
        gp.gameState = gameState;

        // Bridge interaction dialogue
        gp.ui.currentDialogue = "This bridge looks dangerous enough.";


        }
    }
