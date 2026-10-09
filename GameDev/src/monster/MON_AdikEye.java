package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Eyeatk;
import object.OBJ_HealingPotion;

public class MON_AdikEye extends Entity {
    GamePanel gp;

    public MON_AdikEye(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_monster; // Assuming a new type for AdikEye
        name = "Adik Eye";
        speed = 1; // Slightly faster than Red Slime
        maxLife = 7; // More life than Red Slime
        life = maxLife;
        exp = 2;
        projectile = new OBJ_Eyeatk(gp);

        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.width = 40;
        solidArea.height = 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        up1 = setup("/monster/eye1", gp.tileSize, gp.tileSize);
        up2 = setup("/monster/eye2", gp.tileSize, gp.tileSize);
        up3 = setup("/monster/eye3", gp.tileSize, gp.tileSize);
        down1 = setup("/monster/eye1", gp.tileSize, gp.tileSize);
        down2 = setup("/monster/eye2", gp.tileSize, gp.tileSize);
        down3 = setup("/monster/eye3", gp.tileSize, gp.tileSize);
        left1 = setup("/monster/eye1", gp.tileSize, gp.tileSize);
        left2 = setup("/monster/eye2", gp.tileSize, gp.tileSize);
        left3 = setup("/monster/eye3", gp.tileSize, gp.tileSize);
        right1 = setup("/monster/eye1", gp.tileSize, gp.tileSize);
        right2 = setup("/monster/eye2", gp.tileSize, gp.tileSize);
        right3 = setup("/monster/eye3", gp.tileSize, gp.tileSize);
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 100) { // Randomize direction every 100 updates
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Generate 1-100

            if (i <= 25) {
                direction = "up";
            } else if (i > 25 && i <= 50) {
                direction = "down";
            } else if (i > 50 && i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }
            actionLockCounter = 0;
            
        }
        
        int i = new Random().nextInt(100)+1;
        if (i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
        	projectile.set(worldX, worldY, direction, true, this);
        	gp.projectileList.add(projectile);
        	shotAvailableCounter = 0;
        }
    }
    
    public void checkDrop() {
    	int i = new Random().nextInt(150)+1;
    	
    	//SET MONSTER DROP
    	if (i < 75) {
    		dropItem(new OBJ_HealingPotion(gp));
    	}
    }
}