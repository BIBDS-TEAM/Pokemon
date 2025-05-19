package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import GuiTileMapThing.KeyInput;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Player extends Entity{
    GamePanel gp;
    KeyInput keyI;
    int lastLocationX,lastLocationY;
    BufferedImage playerImage_down,playerImage_up,playerImage_right,playerImage_left,playerImage;
    public Player(GamePanel gp, KeyInput keyI) {
        this.gp = gp;
        this.keyI = keyI;
        setDefaultValues(); 
        loadPlayer();
        playerImage = playerImage_down;
    }
    public void setDefaultValues(){
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update() {
    lastLocationX = x;
    lastLocationY = y;

    String direction = keyI.getCurrentDirection();

    if(direction == null){
        return;
    }

    if(direction.equals("up")){
        if(!atBorder(y - speed)){
            y -= speed;
            playerImage = playerImage_up;
        }
    }
    else if(direction.equals("down")){
        if(!atBorder(y + speed)){
            y += speed;
            playerImage = playerImage_down;
        }
    }
    else if(direction.equals("left")){
        if(!atBorder(x - speed)){
            x -= speed;
            playerImage = playerImage_left;
        }
    }
    else if(direction.equals("right")){
        if(!atBorder(x + speed)){
            x += speed;
            playerImage = playerImage_right;
        }
    }
}

public boolean atBorder(int nextLocation) {
    return nextLocation < 0 || nextLocation > 448;
}
    public void loadPlayer(){
     try {
            playerImage_down = ImageIO.read(new File("C:\\Users\\pf43e\\OneDrive\\Documents\\Visual Studio 2022\\Java\\PROJEKPEMLANAKHIR\\TileGambar\\tile_chara000.png"));
            playerImage_up = ImageIO.read(new File("C:\\Users\\pf43e\\OneDrive\\Documents\\Visual Studio 2022\\Java\\PROJEKPEMLANAKHIR\\TileGambar\\tile_chara012.png"));
            playerImage_left = ImageIO.read(new File("C:\\Users\\pf43e\\OneDrive\\Documents\\Visual Studio 2022\\Java\\PROJEKPEMLANAKHIR\\TileGambar\\tile_chara004.png"));
            playerImage_right = ImageIO.read(new File("C:\\Users\\pf43e\\OneDrive\\Documents\\Visual Studio 2022\\Java\\PROJEKPEMLANAKHIR\\TileGambar\\tile_chara008.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
         if (playerImage != null) {
            g2.drawImage(playerImage, x, y, 32, 32, null); // draws the image at (x,y), scaled to 32x32
        }
    }
}

