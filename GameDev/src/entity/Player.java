package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Armor;
import object.OBJ_Eyeatk;
import object.OBJ_Key;
import object.OBJ_Sword;

public class Player extends Entity {

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;  // X offset for collision area
        solidArea.y = 16; // Y offset for collision area
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;  // Width of collision area
        solidArea.height = 30;  // Height of collision area

        // ATTACK RANGE
        attackArea.width = 45;
        attackArea.height = 45;
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 27; // Starting X position
        worldY = gp.tileSize * 30; // Starting Y position
        speed = 6;                 // Movement speed
        direction = "down";        // Default direction
        
        
        // PLAYER STATUS
        level = 1;
        maxLife = 6;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 8;
        currentWeapon = new OBJ_Sword(gp); 
        currentArmor = new OBJ_Armor(gp);
        projectile = new OBJ_Eyeatk(gp);
        attack = getAttack();
        defense = getDefense();
    }
    public void setDefaultPositions()
    {
        worldX = gp.tileSize * 27;
        worldY = gp.tileSize * 30;
        direction = "down";
    }
    public void restoreLife() {
    	life = maxLife;
    	invincible = false;
    }
    	public void setItems() {
    		inventory.clear();
    		inventory.add(currentWeapon);
    		inventory.add(currentArmor);
    		inventory.add(new OBJ_Key(gp));
    	}
    	
        public int getAttack() {
        	return attack = strength * currentWeapon.attackValue;
        }
        
        public int getDefense() {
        	return defense = dexterity * currentArmor.defenseValue;
        }
       
    
    public void getPlayerImage() {
        up1 = setup("/player/up1", gp.tileSize, gp.tileSize);
        up2 = setup("/player/up2", gp.tileSize, gp.tileSize);
        up3 = setup("/player/up3", gp.tileSize, gp.tileSize);
        down1 = setup("/player/down1", gp.tileSize, gp.tileSize);
        down2 = setup("/player/down2", gp.tileSize, gp.tileSize);
        down3 = setup("/player/down3", gp.tileSize, gp.tileSize);
        left1 = setup("/player/left1", gp.tileSize, gp.tileSize);
        left2 = setup("/player/left2", gp.tileSize, gp.tileSize);
        left3 = setup("/player/left3", gp.tileSize, gp.tileSize);
        right1 = setup("/player/right1", gp.tileSize, gp.tileSize);
        right2 = setup("/player/right2", gp.tileSize, gp.tileSize);
        right3 = setup("/player/right3", gp.tileSize, gp.tileSize);
    }
    
    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/up_attack0", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("/player/up_attack1", gp.tileSize, gp.tileSize * 2);
        attackUp3 = setup("/player/up_attack2", gp.tileSize, gp.tileSize * 2);

        attackDown1 = setup("/player/down_attack0", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("/player/down_attack1", gp.tileSize, gp.tileSize * 2);
        attackDown3 = setup("/player/down_attack2", gp.tileSize, gp.tileSize * 2);

        attackLeft1 = setup("/player/left_attack0", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("/player/left_attack1", gp.tileSize * 2, gp.tileSize);
        attackLeft3 = setup("/player/left_attack2", gp.tileSize * 2, gp.tileSize);

        attackRight1 = setup("/player/right_attack0", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("/player/right_attack1", gp.tileSize * 2, gp.tileSize);
        attackRight3 = setup("/player/right_attack2", gp.tileSize * 2, gp.tileSize);
    }

    public void update() {
        if (attacking) {
            attacking(); // Handle attack animation
            return; // Skip the rest of the update if attacking
        }

        boolean moving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        if (moving) {
            if (keyH.upPressed) direction = "up";
            if (keyH.downPressed) direction = "down";
            if (keyH.leftPressed) direction = "left";
            if (keyH.rightPressed) direction = "right";

            collisionOn = false;

            // Check collisions
            gp.cChecker.checkTile(this);
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);
            gp.eHandler.checkEvent();

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }
            
            if (keyH.enterPressed == true && attackCanceled == false) {
            	gp.playSE(6);
            	attacking = true;
            	spriteCounter = 0;
            }
            
            attackCanceled = false;
            gp.keyH.enterPressed = false;

            // Update sprite for animation
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum % 3) + 1; // Loop between 1, 2, 3
                spriteCounter = 0;
            }
        } else if (keyH.enterPressed) {
            // If standing still, allow interactions
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            gp.eHandler.checkEvent();
            keyH.enterPressed = false;
        }

        // Handle invincibility duration
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        
        if(gp.keyH.shotKeyPressed == true && projectile.alive == false && shotAvailableCounter == 30) {
        	// SET DEFAULT COORDINATES
            projectile.set(worldX, worldY, direction, true, this);
            
            gp.projectileList.add(projectile);
        }
        
        
        if (shotAvailableCounter < 30) {	
        	shotAvailableCounter++;
        }
        
        if(life <= 0)
        {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum =- 1; //for if you die while pressing enter
            gp.stopMusic();
            gp.playSE(11);
        }

        System.out.println("Player update called. Game state: " + gp.gameState);
    }
    
    public void attacking() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1; // First frame
        }
        if (spriteCounter > 5 && spriteCounter <= 15) {
            spriteNum = 2; // Second frame
            
            // save the current worldX, worldY, soliArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            // Adjust player's worldX/Y for the attackArea
            switch(direction) {
            case "up" : worldY -= attackArea.height; break;
            case "down" : worldY += attackArea.height; break;
            case "left" : worldX -= attackArea.width; break;
            case "right" : worldX += attackArea.width; break;
            }
            
            // attack area becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            
            // Check monster collision with the updated worldX, worldY, and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);
            
            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
            
        }
        if (spriteCounter > 15 && spriteCounter <= 25) {
            spriteNum = 3; // Third frame
        }
        if (spriteCounter > 25) {
            spriteNum = 1; // Reset to the first frame
            spriteCounter = 0;
            attacking = false; // Attack animation ends
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
String text;
        	
            if (inventory.size()!= maxInventorySize) {
            	
            	inventory.add(gp.obj[i]);
            	text = "Obtained " + gp.obj[i].name + "!";
            }
            else {
            	text = "You cannot carry any more items";
            }
            gp.ui.addMessage(text);
            gp.obj[i] = null;
        }
    }

    public void interactNPC(int i) {
        if (gp.keyH.enterPressed == true) {
            if (i != 999) {
            	attackCanceled = true;
                gp.gameState = gp.dialogueState; // Change game state to dialogue
                gp.npc[i].speak(); // Call the speak method on the NPC
            } else {
            	gp.playSE(6);
            	attacking = true;
            }
        }
        gp.keyH.enterPressed = false;
    }
        
    public void contactMonster(int i) {
        if (i != 999) {
            if (invincible == false && gp.monster[i].dying == false) {

            	int damage = gp.monster[i].attack - defense;
    			if (damage < 0 ) {
    				damage = 0;
    			}
                life -= damage;
                invincible = true;
            }
        }
    }
    
    public void damageMonster(int i) {
    	if(i != 999) {
    		if(gp.monster[i].invincible == false) {
    			
    			gp.playSE(7);
    			
    			int damage = attack - gp.monster[i].defense;
    			if (damage < 0 ) {
    				damage = 0;
    			}
    			gp.monster[i].life -= damage;
    			gp.monster[i].invincible = true;
    			gp.monster[i].damageReaction();
    			
    			
    			if(gp.monster[i].life <= 0) {
    				gp.monster[i].dying = true;
    				gp.ui.addMessage("killed the " + gp.monster[i].name + "!");
    				gp.ui.addMessage("Exp +" + gp.monster[i].exp );
    				exp += gp.monster[i].exp;
    				checkLevelUp();
    			}
    		}
    	}
    	
    }
    public void checkLevelUp() {
    	if (exp >= nextLevelExp) {
    		level++;
    		nextLevelExp = nextLevelExp*2;
    		maxLife += 1;
    		strength++;
    		dexterity++;
    		attack = getAttack();
    		defense = getDefense();
    		
    		gp.gameState = gp.dialogueState;
    		gp.ui.currentDialogue = "You are level " + level + " now!\n"
    					+ "You feel stronger!";
    		
    	}
    }
    
    public void selectItem() {
    	int itemIndex = gp.ui.getItemIndexOnSlot();
    	
    	if(itemIndex < inventory.size()) {
    		
    		Entity selectedItem = inventory.get(itemIndex);
    		
    		if(selectedItem.type == type_sword || selectedItem.type == type_katana) {
    			
    			currentWeapon = selectedItem;
    			attack = getAttack();
    		}
    		if(selectedItem.type == type_armor) {
    			currentArmor = selectedItem;
    			defense = getDefense();
    		}
    		if(selectedItem.type == type_consumable) {
    			selectedItem.use(this);
    			inventory.remove(itemIndex);
    		}
    	}
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Select attack sprites based on direction
        if (attacking) {
            switch (direction) {
                case "up":
                    image = getSpriteImage(attackUp1, attackUp2, attackUp3);
                    g2.drawImage(image, screenX, screenY - gp.tileSize, gp.tileSize, gp.tileSize * 2, null); // Adjust for height
                    break;
                case "down":
                    image = getSpriteImage(attackDown1, attackDown2, attackDown3);
                    g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize * 2, null); // Adjust for height
                    break;
                case "left":
                    image = getSpriteImage(attackLeft1, attackLeft2, attackLeft3);
                    g2.drawImage(image, screenX - gp.tileSize, screenY, gp.tileSize * 2, gp.tileSize, null); // Adjust for width
                    break;
                case "right":
                    image = getSpriteImage(attackRight1, attackRight2, attackRight3);
                    g2.drawImage(image, screenX, screenY, gp.tileSize * 2, gp.tileSize, null); // Adjust for width
                    break;
            }
        } else {
            // Normal movement drawing logic
            switch (direction) {
                case "up":
                    image = getSpriteImage(up1, up2, up3);
                    break;
                case "down":
                    image = getSpriteImage(down1, down2, down3);
                    break;
                case "left":
                    image = getSpriteImage(left1, left2, left3);
                    break;
                case "right":
                    image = getSpriteImage(right1, right2, right3);
                    break;
            }
            g2.drawImage(image, tempScreenX, tempScreenY, gp.tileSize, gp.tileSize, null);
        }
    }

    private BufferedImage getSpriteImage(BufferedImage sprite1, BufferedImage sprite2, BufferedImage sprite3) {
        switch (spriteNum) {
            case 1:
                return sprite1;
            case 2:
                return sprite2;
            case 3:
                return sprite3;
            default:
                return sprite1; // Fallback
        }
    }
} 
