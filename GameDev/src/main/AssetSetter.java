package main;

import entity.NPC_Mage1;
import monster.MON_AdikEye;
import monster.MON_FinalBoss;
import monster.MON_RedSlime;
import object.OBJ_Armor;
import object.OBJ_GoldenArmor;
import object.OBJ_HealingPotion;
import object.OBJ_Katana;


public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

    	int i = 0;
    	gp.obj[i] = new OBJ_Armor(gp);
    	gp.obj[i].worldX = gp.tileSize*90;
    	gp.obj[i].worldY = gp.tileSize*26;
    	i++;
    	gp.obj[i] = new OBJ_Katana(gp);
    	gp.obj[i].worldX = gp.tileSize*37;
    	gp.obj[i].worldY = gp.tileSize*39;
    	i++;
    	gp.obj[i] = new OBJ_GoldenArmor(gp);
    	gp.obj[i].worldX = gp.tileSize*89;
    	gp.obj[i].worldY = gp.tileSize*26;
    	i++;
    	gp.obj[i] = new OBJ_HealingPotion(gp);
    	gp.obj[i].worldX = gp.tileSize*18;
    	gp.obj[i].worldY = gp.tileSize*2;
    	i++;
    	
    }

    public void setNPC() {
        gp.npc[0] = new NPC_Mage1(gp);
        gp.npc[0].worldX = gp.tileSize * 43;
        gp.npc[0].worldY = gp.tileSize * 36;
        
        

    }
    public void setMonster() {
    	int i = 0;
    	gp.monster[i] = new MON_FinalBoss(gp);
        gp.monster[i].worldX = gp.tileSize * 21;
        gp.monster[i].worldY = gp.tileSize * 21;
        i++;
    	gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 66;
        gp.monster[i].worldY = gp.tileSize * 31;
        i++;
    	gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 60;
        gp.monster[i].worldY = gp.tileSize * 31;
        i++;
    	gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 60;
        gp.monster[i].worldY = gp.tileSize * 32;
        i++;
    	gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 57;
        gp.monster[i].worldY = gp.tileSize * 30;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 10;
        gp.monster[i].worldY = gp.tileSize * 29;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 10;
        gp.monster[i].worldY = gp.tileSize * 27;
        i++;
        gp.monster[i] = new MON_AdikEye(gp);
        gp.monster[i].worldX = gp.tileSize * 66;
        gp.monster[i].worldY = gp.tileSize * 44;
        i++;
        gp.monster[i] = new MON_AdikEye(gp);
        gp.monster[i].worldX = gp.tileSize * 66;
        gp.monster[i].worldY = gp.tileSize * 67;
        i++;
        gp.monster[i] = new MON_AdikEye(gp);
        gp.monster[i].worldX = gp.tileSize * 66;
        gp.monster[i].worldY = gp.tileSize * 65;
        i++;
        gp.monster[i] = new MON_AdikEye(gp);
        gp.monster[i].worldX = gp.tileSize * 66;
        gp.monster[i].worldY = gp.tileSize * 63;
        i++;
       
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 28;
        gp.monster[i].worldY = gp.tileSize * 38;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 27;
        gp.monster[i].worldY = gp.tileSize * 38;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 33;
        gp.monster[i].worldY = gp.tileSize * 74;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 37;
        gp.monster[i].worldY = gp.tileSize * 74;
        i++;
        gp.monster[i] = new MON_RedSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 35;
        gp.monster[i].worldY = gp.tileSize * 74;
        i++;
        gp.monster[i] = new MON_AdikEye(gp);
        gp.monster[i].worldX = gp.tileSize * 29;
        gp.monster[i].worldY = gp.tileSize * 60;
        i++;

    }

}