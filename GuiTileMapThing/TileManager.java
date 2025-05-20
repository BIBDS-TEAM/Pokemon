package GuiTileMapThing;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    public Tile[] tiles;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol] [gp.maxScreenRow];
        loadMap("dataSave/tileMap.txt"); 
        loadTileImages();
    }

    public void loadTileImages() {
        try {
            tiles[0] = new Tile(ImageIO.read(new File("TileGambar/tile_0068.png")), true);
            tiles[1] = new Tile(ImageIO.read(new File("TileGambar/tile_0051.png")), false);
        } catch (IOException e) {
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
                for (int i = 0; i < numbers.length; i++) {
                    int num = Integer.parseInt(numbers[i]);
                    maxTileId = Math.max(maxTileId, num);
                }
                rows++;
            }

            br.close();
            br = new BufferedReader(new FileReader(filePath));

            mapTileNum = new int[rows][cols];

            for (int row = 0; row < rows; row++) {
                line = br.readLine();
                String[] numbers = line.trim().split(" ");
                for (int col = 0; col < numbers.length; col++) {
                    mapTileNum[row][col] = Integer.parseInt(numbers[col]);
                }
            }

            br.close();
            tiles = new Tile[maxTileId + 1];

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
    for (int row = 0; row < gp.maxWorldR; row++) {
        for (int col = 0; col < gp.maxWorldC; col++) {

            int tileNum = mapTileNum[row][col]; 

            int worldX = col * gp.tileSize;
            int worldY = row * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                g2.drawImage(tiles[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}