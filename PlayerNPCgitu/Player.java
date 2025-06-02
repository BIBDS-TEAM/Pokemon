package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import GuiTileMapThing.KeyInput;
import Pokemon.PokemonBasics.PokemonAllType.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Entity {
    GamePanel gp;
    KeyInput keyI;
    public final int screenX, screenY;
    int lastLocationX, lastLocationY;
    BufferedImage playerImage_down, playerImage_up, playerImage_right, playerImage_left, playerImage;
    Pokemon[] pokemon = new Pokemon[6];

    public Player(GamePanel gp, KeyInput keyI) {
        this.gp = gp;
        this.keyI = keyI;
        screenX = 512 / 2 - 32;
        screenY = 512 / 2 - 32;
        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 30;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        loadPlayer();
        playerImage = playerImage_down;
    }

    public void setDefaultValues() {
        worldX = 900;
        worldY = 680;
        speed = 4;
    }

    public void update(boolean walk) {

        direction = keyI.getCurrentDirection();

        if (direction == null) {
            return;
        }
        if (walk == true) {
            gp.eCheck.cekTile(this);
        }
        collisionOn = false;
        gp.cc.cekTile(this);
        if (!collisionOn && walk) {
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
                default:
                    break;
            }
        }
    }

    public void loadPlayer() {
        try {
            playerImage_down = ImageIO.read(new File("TileGambar/tile_chara000.png"));
            playerImage_up = ImageIO.read(new File("TileGambar/tile_chara012.png"));
            playerImage_left = ImageIO.read(new File("TileGambar/tile_chara004.png"));
            playerImage_right = ImageIO.read(new File("TileGambar/tile_chara008.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (direction != null) {
            switch (direction) {
                case "up":
                    playerImage = playerImage_up;
                    break;
                case "down":
                    playerImage = playerImage_down;
                    break;
                case "left":
                    playerImage = playerImage_left;
                    break;
                case "right":
                    playerImage = playerImage_right;
                    break;
            }
        }
        g2.drawImage(playerImage, screenX, screenY, 48, 48, null); // draws the image at (x,y), scaled to 32x32
    }

    public void addPokemonToBag(Pokemon pokemon) {
        for (int i = 0; i < this.pokemon.length; i++) {
            if (this.pokemon[i] == null) {
                this.pokemon[i] = pokemon;
                break;
            }
        }
    }
}