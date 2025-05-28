package GuiTileMapThing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[71];
        mapTileNum = new int[100][100];
        loadMap("dataSave/tileMap.txt"); 
        loadTileImages();
    }

    public void loadTileImages() {
        try {
        boolean[] specialTiles = loadSpecialTiles("dataSave/tileCollision.txt");

        for (int i = 0; i < tiles.length; i++) {
            String fileName = String.format("TileGambar/%03d.png", i);
            BufferedImage tileImage = ImageIO.read(new File(fileName));
            boolean isSpecialTile = (i < specialTiles.length) ? specialTiles[i] : false;

            tiles[i] = new Tile(tileImage, isSpecialTile);
        }
    } catch (IOException e) {
        System.err.println("Error loading tile image: " + e.getMessage());
        e.printStackTrace();
    }
}

   public void loadMap(String filePath) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        int rows = 0;
        int cols = 0;
        int maxTileId = 0;

        while ((line = br.readLine()) != null) {
            String[] numbers = line.trim().split("\\s+");
            cols = Math.max(cols, numbers.length);
            for (String numStr : numbers) {
                int num = Integer.parseInt(numStr); 
                maxTileId = Math.max(maxTileId, num);
            }
            rows++;
        }

        br.close();
        br = new BufferedReader(new FileReader(filePath));

        for (int row = 0; row < rows; row++) {
            line = br.readLine();
            String[] numbers = line.trim().split("\\s+");
            for (int col = 0; col < numbers.length; col++) {
                mapTileNum[row][col] = Integer.parseInt(numbers[col]); 
            }
        }

        br.close();


    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public void draw(Graphics2D g2) {
    for (int row = 0; row < 100; row++) {
        for (int col = 0; col < 100; col++) {

            int tileNum = mapTileNum[row][col]; 

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                if (tileNum >= 0 && tileNum < tiles.length && tiles[tileNum] != null) {
    g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
            }
        }
    }
    private boolean[] loadSpecialTiles(String filePath) {
    try {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        int index = 0;

        boolean[] specialTiles = new boolean[100];

        while ((line = br.readLine()) != null) {
            if (index % 2 == 1) { 
                specialTiles[index / 2] = Boolean.parseBoolean(line.trim());
            }
            index++;
        }

        br.close();
        return specialTiles;
    } catch (IOException e) {
        e.printStackTrace();
        return new boolean[0];
    }
}
}