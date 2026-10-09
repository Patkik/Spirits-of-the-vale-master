package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

    // Flag to check if the entity is static (non-animated)
    public boolean isStatic = false; 

    GamePanel gp;

    // Sprite images for different directions and actions
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp1, attackUp2, attackUp3, attackDown1, attackDown2, attackDown3;
    public BufferedImage attackRight1, attackRight2, attackRight3, attackLeft1, attackLeft2, attackLeft3;
    public BufferedImage image, image2, image3;

    // Collision areas for the entity
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;

    // Dialogue array to hold entity conversations
    public String[] dialogue = new String[20];

    // Flags and states
    public boolean collision = false;
    public boolean attackCanceled = false;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;

    // Entity position and direction
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public int dialogueIndex = 0;

    // Counters for various states
    public int spriteCounter = 0;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int dyingCounter = 0;
    public int hpBarCounter = 0;
    public int shotAvailableCounter = 0;

    // Character attributes
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int attack;
    public int defense;
    public int nextLevelExp;
    public int exp;
    public int strength;
    public int dexterity;
    public Entity currentWeapon;
    public Entity currentArmor;
    public Projectile projectile;

    // Item attributes
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;

    // Entity types
    public int type;
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_katana = 4;
    public final int type_armor = 5;
    public final int type_consumable = 6;

    // Constructor
    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    // Define actions for the entity (to be overridden by subclasses)
    public void setAction() {
    }

    // Define reaction to damage (to be overridden in specific entity classes)
    public void damageReaction() {
    }

    // Handles entity dialogue and direction changes
    public void speak() {
        if (dialogue[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogue[dialogueIndex];
        dialogueIndex++;

        // Change direction based on player's direction
        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    // Use an item or ability (to be implemented in specific cases)
    public void use(Entity entity) {
    }

    // Check for item drops (to be implemented in specific cases)
    public void checkDrop() {
    }

    // Drop an item at the entity's position
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] == null) {
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX;
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }

    // Update the entity's state
    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkPlayer(this);

        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        // Handle contact with player for monsters
        if (this.type == type_monster && contactPlayer) {
            damagePlayer(attack);
        }

        // Move entity if no collision and it's not static
        if (!collisionOn && !isStatic) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
    }

    // Damage the player
    public void damagePlayer(int attack) {
        if (!gp.player.invincible) {
            gp.player.life -= 1;
            gp.player.invincible = true;
        }
    }

    // Draw the entity on the screen
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // Calculate screen position relative to player
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Check if the entity is within view
        if (isInView()) {
            // Choose sprite image based on entity state
            if (isStatic) {
                image = down1;
            } else {
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
            }

            try {
                // Adjust position and size for sprites
                int drawWidth = image.getWidth();
                int drawHeight = image.getHeight();
                int drawX = screenX - (drawWidth - gp.tileSize) / 2;
                int drawY = screenY - (drawHeight - gp.tileSize);

                // Draw monster HP bar if applicable
                if (type == 2 && hpBarOn == true) {
                    
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;
                	
                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize+2, 12);
                
                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
                
                hpBarCounter++;
                if(hpBarCounter > 600) {
                	hpBarCounter = 0;
                	hpBarOn = false;
                	}
                }
                
                if (invincible == true) {
                	hpBarOn = true;
        			hpBarCounter = 0;
                }
                if (dying == true) {
                	dyingAnimation(g2);
                }
                // Draw the image
                g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
                
                changeAlpha(g2, 1f);
            
            }catch(Exception e) {
         	   System.out.println("image null");
            }
            }
        
        
        
    }

    // Change the transparency of the graphics context. Encap
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    // Check if the entity is within the player's view
    private boolean isInView() {
        return worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY;
    }

    // Handle the entity's dying animation. Encap
    private boolean soundPlayed = false; // Flag to ensure sound plays once
    
 

    public void dyingAnimation(Graphics2D g2) {
        dyingCounter++;
        int i = 5;

        if (dyingCounter <= i) { changeAlpha(g2, 0f); }
        if (dyingCounter > i && dyingCounter <= i * 2) { changeAlpha(g2, 1f); }
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) { changeAlpha(g2, 0f); }
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) { changeAlpha(g2, 1f); }
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) { changeAlpha(g2, 0f); }
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) { changeAlpha(g2, 1f); }
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) { changeAlpha(g2, 0f); }
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) { changeAlpha(g2, 1f); }

        if (dyingCounter > i * 8) {
            alive = false;
        }

        // Play death sound once
        if (!soundPlayed) {
            gp.playSE(8);
            soundPlayed = true;
        }
    }

    // Load and scale images for sprites
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    // Manage sprite animations
    private BufferedImage getSpriteImage(BufferedImage img1, BufferedImage img2, BufferedImage img3) {
        // Return the static image if the entity doesn't animate
        if (isStatic) {
            return img1;
        }

        // Increment sprite counter and cycle through frames
        spriteCounter++;
        if (spriteCounter > 25) {
            spriteCounter = 0;
            spriteNum++;
            if (spriteNum > 3) {
                spriteNum = 1;
            }
        }

        // Reset invincibility after a duration
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Increase cooldown for projectile shots
        if (shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }

        // Return the current frame based on spriteNum
        switch (spriteNum) {
            case 1:
                return img1;
            case 2:
                return img2;
            case 3:
                return img3;
            default:
                return img1;
        }
    }
}
