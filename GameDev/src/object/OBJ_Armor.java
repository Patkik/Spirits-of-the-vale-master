package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Armor extends Entity {
	public OBJ_Armor (GamePanel gp) {
		super(gp);
		type = type_armor;
		name = "Armor";
		down1 = setup("/objects/armor", gp.tileSize, gp.tileSize);
		defenseValue = 1;
		description = "{"+name+"}\nA bit worn Armor but not \nbad";
		
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
