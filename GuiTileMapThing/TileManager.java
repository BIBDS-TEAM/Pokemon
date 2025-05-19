package GuiTileMapThing;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    Tile[] tiles;
    int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tiles = new Tile[10]; 
        mapTileNum = new int[30][30];

        loadTileImages();
    }

    public void loadTileImages() {
        try {
            tiles[1] = new Tile(ImageIO.read(new File("TileGambar/tile_0051.png")), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void loadMap(String filePath) {
        // try {
        //     BufferedReader br = new BufferedReader(new FileReader(filePath));
        //     int row = 0;

    //         while (row < 30) {
    //             String line = br.readLine();
    //             String[] numbers = line.split(" ");

    //             for (int col = 0; col < 30; col++) {
    //                 int num = Integer.parseInt(numbers[col]);
    //                 mapTileNum[row][col] = num;
    //             }
    //             row++;
    //         }
    //         br.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < 30 ; row++) {
            for (int col = 0; col < 30; col++) {
                int tileNum = mapTileNum[row][col];
                int x = col * 16;
                int y = row * 16;
                g2.drawImage(tiles[1].image, x, y, 16, 16, null);
            }
        }
    }
}
