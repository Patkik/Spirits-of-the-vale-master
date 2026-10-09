package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;


public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[165]; // Increased to 165 to include tile 164
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        loadMap("/maps/map1.txt");
    }

    public void getTileImage() {
        for (int i = 0; i < 165; i++) { // Updated to 165
            String imagePath = String.format("%03d", i);
            boolean collision = (i >= 1 && i <= 39) || 
                                (i >= 59 && i <= 69) || 
                                (i == 71) || 
                                (i >= 73 && i <= 75) || 
                                (i >= 77 && i <= 78) || 
                                (i >= 80 && i <= 83) || 
                                (i == 92) || 
                                (i >= 93 && i <= 94) || 
                                (i >= 99 && i <= 106) || 
                                (i == 109) || 
                                (i >= 111 && i <= 133) || 
                                (i == 142) || 
                                (i == 151) ||
                                (i >= 155 && i <= 163); // Include new tile 164 in collision check

            setup(i, imagePath, collision);
        }
    }

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName + ".png"));
            
            if (tile[index].image == null) {
                System.err.println("CRITICAL: Failed to load tile image for index " + index);
                // Optionally, load a default/placeholder image
                // tile[index].image = defaultImage;
            } else {
                tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
                tile[index].collision = collision;
            }
        } catch (IOException e) {
            System.err.println("Error loading tile image for index " + index + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            int row = 0;

            while (row < gp.maxWorldRow) {
                String line = br.readLine();

                if (line == null) break;

                String numbers[] = line.split(" ");

                for (int col = 0; col < gp.maxWorldCol && col < numbers.length; col++) {
                    int num = Integer.parseInt(numbers[col]);

                    if (num < 0 || num >= tile.length) {
                        System.out.println("Invalid tile number: " + num + " at row: " + row + " col: " + col);
                        mapTileNum[col][row] = 0; // Use tile index 0 as a default
                    } else {
                        mapTileNum[col][row] = num;
                    }
                }

                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing tile number in map file: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                worldX - gp.tileSize < gp.player.worldX + gp.player .screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            
            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}