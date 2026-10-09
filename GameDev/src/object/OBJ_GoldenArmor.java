package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_GoldenArmor extends Entity{

	public OBJ_GoldenArmor(GamePanel gp) {
		super(gp);
		type = type_armor;
		name = "Golden Armor";
		down1 = setup("/objects/Golden_armor", gp.tileSize, gp.tileSize);
		defenseValue = 3;
		description = "{"+name+"}\nshiny is the new sturdy \nbad";
		
	     collision = false;
	     isStatic = true;

	     solidArea.x = 0;
	     solidArea.y = 16;
	     solidArea.width = 48;
	     solidArea.height = 32;
	     solidAreaDefaultX = solidArea.x;
	     solidAreaDefaultY = solidArea.y;
	}
	
}
