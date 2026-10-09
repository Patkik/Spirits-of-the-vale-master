package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_HealingPotion extends Entity {
    GamePanel gp;
    int value = 5;

    public OBJ_HealingPotion(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_consumable;
        name = "Healing Potion";
        down1 = setup("/objects/healingPotion", gp.tileSize, gp.tileSize);
        description = "[Healing Potion]\nIt can heal " + value + ".\nBut it can never heal a \nbroken heart.";
        collision = false;
        isStatic = true;
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void use(Entity entity) {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drank the " + name + "!\n"
                + "Your life has been recovered by " + value + ".";
        
        entity.life += value;
        if (entity.life > entity.maxLife) { // Assuming entity has maxLife
            entity.life = entity.maxLife;
        }

        gp.playSE(11);
    }
}
