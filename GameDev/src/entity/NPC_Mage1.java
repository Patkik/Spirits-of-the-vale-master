package entity;

import java.util.Random;

import main.GamePanel;


public class NPC_Mage1 extends Entity{
	
	//replaced the original name NPC_Oldman
	public NPC_Mage1(GamePanel gp) {
	    super(gp);
	    direction = "left";
	    speed = 1;
	    getPlayerImage(); // Load NPC images
	    setDialogue();
	}
	public void getPlayerImage() {
        up1 = setup("/npc/npc_up1", gp.tileSize, gp.tileSize);
        up2 = setup("/npc/npc_up2", gp.tileSize, gp.tileSize);
        up3 = setup("/npc/npc_up3", gp.tileSize, gp.tileSize);
        down1 = setup("/npc/npc_down1", gp.tileSize, gp.tileSize);
        down2 = setup("/npc/npc_down2", gp.tileSize, gp.tileSize);
        down3 = setup("/npc/npc_down3", gp.tileSize, gp.tileSize);
        left1 = setup("/npc/npc_left1", gp.tileSize, gp.tileSize);
        left2 = setup("/npc/npc_left2", gp.tileSize, gp.tileSize);
        left3 = setup("/npc/npc_left3", gp.tileSize, gp.tileSize);
        right1 = setup("/npc/npc_right1", gp.tileSize, gp.tileSize);
        right2 = setup("/npc/npc_right2", gp.tileSize, gp.tileSize);
        right3 = setup("/npc/npc_right3", gp.tileSize, gp.tileSize);
    }
	public void setDialogue() {
		
		dialogue[0] = "Greetings, dear warrior!";
		dialogue[1] = "I'm the chief in this side of town\n";
		dialogue[2] = "Please help our village!!";

	}

	public void setAction(){
		actionLockCounter ++;
		if(actionLockCounter == 150) {
		Random random = new Random();
		int i = random.nextInt(100)+1;//pick up a number 1 - 100
		
		if(i <= 25) {
			direction = "up";
		}
		if( i > 25 && i <= 50) {
			direction = "down";
		}
		if( i > 50 && i <= 75) {
			direction = "left";
		}
		if( i > 75 && i <= 100) {
			direction = "right";
		}
		
		actionLockCounter = 0;
		}
	}
	public void speak() {
		super.speak();
	}
}
