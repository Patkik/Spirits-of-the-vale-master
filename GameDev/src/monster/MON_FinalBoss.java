package monster;

import java.util.Random;
import entity.Entity;
import main.GamePanel;

public class MON_FinalBoss extends Entity {
    GamePanel gp;
    private int attackCounter = 0;
    private final int attackInterval = 60; // Frames (1 second at 60 FPS)
    private boolean attacking = false;

    public MON_FinalBoss(GamePanel gp) {
        super(gp);
        this.gp = gp;
        type = type_monster;
        name = "Adik Eye";
        speed = 2;
        attack = 4;
        maxLife = 50;
        life = maxLife;
        exp = 2;

        solidArea.x = 5;
        solidArea.y = 5;
        solidArea.width = 40;
        solidArea.height = 40;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
        getAttackImage();
    }

    public void getImage() {
        int i = 5;
        up1 = setup("/monster/FinalBoss_up1", gp.tileSize * i, gp.tileSize * i);
        up2 = setup("/monster/FinalBoss_up2", gp.tileSize * i, gp.tileSize * i);
        up3 = setup("/monster/FinalBoss_up3", gp.tileSize * i, gp.tileSize * i);
        down1 = setup("/monster/FinalBoss_down1", gp.tileSize * i, gp.tileSize * i);
        down2 = setup("/monster/FinalBoss_down2", gp.tileSize * i, gp.tileSize * i);
        down3 = setup("/monster/FinalBoss_down3", gp.tileSize * i, gp.tileSize * i);
        left1 = setup("/monster/FinalBoss_left1", gp.tileSize * i, gp.tileSize * i);
        left2 = setup("/monster/FinalBoss_left2", gp.tileSize * i, gp.tileSize * i);
        left3 = setup("/monster/FinalBoss_left3", gp.tileSize * i, gp.tileSize * i);
        right1 = setup("/monster/FinalBoss_right1", gp.tileSize * i, gp.tileSize * i);
        right2 = setup("/monster/FinalBoss_right2", gp.tileSize * i, gp.tileSize * i);
        right3 = setup("/monster/FinalBoss_right3", gp.tileSize * i, gp.tileSize * i);
    }

    public void getAttackImage() {
        int i = 5;
        attackUp1 = setup("/monster/FinalBoss_attack_up", gp.tileSize * i, gp.tileSize * i);
        attackUp2 = setup("/monster/FinalBoss_attack_up2", gp.tileSize * i, gp.tileSize * i);
        attackUp3 = setup("/monster/FinalBoss_attack_up3", gp.tileSize * i, gp.tileSize * i);
        attackDown1 = setup("/monster/FinalBoss_attack_down", gp.tileSize * i, gp.tileSize * i);
        attackDown2 = setup("/monster/FinalBoss_attack_down2", gp.tileSize * i, gp.tileSize * i);
        attackDown3 = setup("/monster/FinalBoss_attack_down3", gp.tileSize * i, gp.tileSize * i);
        attackLeft1 = setup("/monster/FinalBoss_attack_left", gp.tileSize * i, gp.tileSize * i);
        attackLeft2 = setup("/monster/FinalBoss_attack_left2", gp.tileSize * i, gp.tileSize * i);
        attackLeft3 = setup("/monster/FinalBoss_attack_left3", gp.tileSize * i, gp.tileSize * i);
        attackRight1 = setup("/monster/FinalBoss_attack_right", gp.tileSize * i, gp.tileSize * i);
        attackRight2 = setup("/monster/FinalBoss_attack_right2", gp.tileSize * i, gp.tileSize * i);
        attackRight3 = setup("/monster/FinalBoss_attack_right3", gp.tileSize * i, gp.tileSize * i);
    }

    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter >= 100) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;

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

        attackCounter++;
        if (attackCounter >= attackInterval && !attacking) {
            Random random = new Random();
            if (random.nextInt(100) < 50) { // 50% chance to attack
                attacking = true;
                attackCounter = 0;
            }
        }

        // Movement logic: Update the boss's position based on the direction
        if (!attacking) {
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

}
