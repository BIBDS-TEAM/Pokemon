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
        tiles = new Tile[100];
        mapTileNum = new int[100][100];
        loadMap("dataSave/tileMap.txt"); 
        loadTileImages();
    }

    public void loadTileImages() {
        try {
            tiles[0] = new Tile(ImageIO.read(new File("TileGambar/000.png")), false);
            tiles[1] = new Tile(ImageIO.read(new File("TileGambar/001.png")), false);
            tiles[2] = new Tile(ImageIO.read(new File("TileGambar/002.png")), false);
            tiles[3] = new Tile(ImageIO.read(new File("TileGambar/003.png")), false);
            tiles[4] = new Tile(ImageIO.read(new File("TileGambar/004.png")), false);
            tiles[5] = new Tile(ImageIO.read(new File("TileGambar/005.png")), false);
            tiles[6] = new Tile(ImageIO.read(new File("TileGambar/006.png")), false);
            tiles[7] = new Tile(ImageIO.read(new File("TileGambar/007.png")), false);
            tiles[8] = new Tile(ImageIO.read(new File("TileGambar/008.png")), false);
            tiles[9] = new Tile(ImageIO.read(new File("TileGambar/009.png")), false);
            tiles[10] = new Tile(ImageIO.read(new File("TileGambar/010.png")), false);
            tiles[11] = new Tile(ImageIO.read(new File("TileGambar/011.png")), false);
            tiles[12] = new Tile(ImageIO.read(new File("TileGambar/012.png")), false);
            tiles[13] = new Tile(ImageIO.read(new File("TileGambar/013.png")), false);
            tiles[14] = new Tile(ImageIO.read(new File("TileGambar/014.png")), false);
            tiles[15] = new Tile(ImageIO.read(new File("TileGambar/015.png")), false);
            tiles[16] = new Tile(ImageIO.read(new File("TileGambar/016.png")), false);
            tiles[17] = new Tile(ImageIO.read(new File("TileGambar/017.png")), false);
            tiles[18] = new Tile(ImageIO.read(new File("TileGambar/018.png")), false);
            tiles[19] = new Tile(ImageIO.read(new File("TileGambar/019.png")), false);
            tiles[20] = new Tile(ImageIO.read(new File("TileGambar/020.png")), false);
            tiles[21] = new Tile(ImageIO.read(new File("TileGambar/021.png")), false);
            tiles[22] = new Tile(ImageIO.read(new File("TileGambar/022.png")), false);
            tiles[23] = new Tile(ImageIO.read(new File("TileGambar/023.png")), false);
            tiles[24] = new Tile(ImageIO.read(new File("TileGambar/024.png")), false);
            tiles[25] = new Tile(ImageIO.read(new File("TileGambar/025.png")), false);
            tiles[26] = new Tile(ImageIO.read(new File("TileGambar/026.png")), false);
            tiles[27] = new Tile(ImageIO.read(new File("TileGambar/027.png")), false);
            tiles[28] = new Tile(ImageIO.read(new File("TileGambar/028.png")), false);
            tiles[29] = new Tile(ImageIO.read(new File("TileGambar/029.png")), false);
            tiles[30] = new Tile(ImageIO.read(new File("TileGambar/030.png")), false);
            tiles[31] = new Tile(ImageIO.read(new File("TileGambar/031.png")), false);
            tiles[32] = new Tile(ImageIO.read(new File("TileGambar/032.png")), false);
            tiles[33] = new Tile(ImageIO.read(new File("TileGambar/033.png")), false);
            tiles[34] = new Tile(ImageIO.read(new File("TileGambar/034.png")), false);
            tiles[35] = new Tile(ImageIO.read(new File("TileGambar/035.png")), false);
            tiles[36] = new Tile(ImageIO.read(new File("TileGambar/036.png")), false);
            tiles[37] = new Tile(ImageIO.read(new File("TileGambar/037.png")), false);
            tiles[38] = new Tile(ImageIO.read(new File("TileGambar/038.png")), false);
            tiles[39] = new Tile(ImageIO.read(new File("TileGambar/039.png")), false);
            tiles[40] = new Tile(ImageIO.read(new File("TileGambar/040.png")), false);
            tiles[41] = new Tile(ImageIO.read(new File("TileGambar/041.png")), true);
            tiles[42] = new Tile(ImageIO.read(new File("TileGambar/042.png")), false);
            tiles[43] = new Tile(ImageIO.read(new File("TileGambar/043.png")), false);
            tiles[44] = new Tile(ImageIO.read(new File("TileGambar/044.png")), false);
            tiles[45] = new Tile(ImageIO.read(new File("TileGambar/045.png")), false);
            tiles[46] = new Tile(ImageIO.read(new File("TileGambar/046.png")), false);
            tiles[47] = new Tile(ImageIO.read(new File("TileGambar/047.png")), false);

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
    for (int row = 0; row < 50; row++) {
        for (int col = 0; col < 50; col++) {

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
}