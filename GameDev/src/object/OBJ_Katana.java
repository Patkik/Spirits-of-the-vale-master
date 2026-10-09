package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Katana extends Entity{

	public OBJ_Katana(GamePanel gp) {
		super(gp);
		type = type_katana;
		name = "Katana";
        down1 = setup("/objects/katana", gp.tileSize, gp.tileSize);
        attackValue = 2;
        description = "{" + name + "}\nFinally something sharper!\nDAMAGE: 3";
        collision = false;
        isStatic = true;
        //range
        attackArea.width = 50;
        attackArea.height = 50;
        
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
	}

}
