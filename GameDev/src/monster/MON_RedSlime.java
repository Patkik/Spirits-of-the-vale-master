package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_HealingPotion;
	
public class MON_RedSlime extends Entity {
	GamePanel gp;
    public MON_RedSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_monster;
        name = "Red Slime";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 3;
        defense = 0;
        exp = 1;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
    }

    
    public void getImage() {

            up1 = setup("/monster/rSlime1", gp.tileSize, gp.tileSize);
            up3 = setup("/monster/rSlime2", gp.tileSize, gp.tileSize);
            up2 = setup("/monster/rSlime3", gp.tileSize, gp.tileSize);
            down1 = setup("/monster/rSlime1", gp.tileSize, gp.tileSize);
            down2 = setup("/monster/rSlime2", gp.tileSize, gp.tileSize);
            down3 = setup("/monster/rSlime3", gp.tileSize, gp.tileSize);
            left1 = setup("/monster/rSlime1", gp.tileSize, gp.tileSize);
            left2 = setup("/monster/rSlime2", gp.tileSize, gp.tileSize);
            left3 = setup("/monster/rSlime3", gp.tileSize, gp.tileSize);
            right1 = setup("/monster/rSlime1", gp.tileSize, gp.tileSize);
            right2 = setup("/monster/rSlime2", gp.tileSize, gp.tileSize);
            right3 = setup("/monster/rSlime3", gp.tileSize, gp.tileSize);
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 150) { // Randomize direction every 150 updates
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
    }
    public void damageReaction() {
    	
    	actionLockCounter = 0;
    	direction = gp.player.direction;
    	
    }
    public void checkDrop() {
    	int i = new Random().nextInt(150)+1;
    	
    	//SET MONSTER DROP
    	if (i < 50) {
    		dropItem(new OBJ_HealingPotion(gp));
    	}
    }
}

