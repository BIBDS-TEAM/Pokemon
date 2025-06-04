package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class NPC extends Entity {
    protected GamePanel gp;
    protected String name;
    protected String spriteFolder;
    protected Pokemon[] pokemonList = new Pokemon[6];
    protected String dialogue;

    public NPC(GamePanel gp, String name, int worldX, int worldY, String direction, String spriteFolder) {
        this.gp = gp;
        this.name = name;
        this.worldX = worldX;
        this.worldY = worldY;
        this.direction = direction;
        this.spriteFolder = spriteFolder;
        this.solidArea = new Rectangle(4, 0, 28, 28);
        this.dialogue = "Hello player! This is the first part of the text on page 1. "
                      + "%%PAGEBREAK%% "
                      + "This text will start on a brand new page, page 2. "
                      + "Even if page 1 had more space. "
                      + "%%PAGEBREAK%% "
                      + "And this is page 3.";
        loadSprites();
        setDefaultArea();
    }
    public String getDialog(){
        return dialogue;
    }
    public boolean haveDialogue() {
        return dialogue != null;
    }

    protected void loadSprites() {
        try {
            up0 = ImageIO.read(new File("TileGambar/" + spriteFolder + "_up.png"));
            down0 = ImageIO.read(new File("TileGambar/" + spriteFolder + "_down.png"));
            left0 = ImageIO.read(new File("TileGambar/" + spriteFolder + "_left.png"));
            right0 = ImageIO.read(new File("TileGambar/" + spriteFolder + "_right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPokemonList(Pokemon[] pokemonList) {
        this.pokemonList = pokemonList;
    }

    public boolean isPlayerInRange() {
        if (gp.player == null) return false;

        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        return (Math.abs(worldX - playerX) == gp.tileSize && worldY == playerY) || 
               (Math.abs(worldY - playerY) == gp.tileSize && worldX == playerX);
    }

    public void interact() {
        if (isPlayerInRange()) {
            int dx = gp.player.worldX - worldX;
            int dy = gp.player.worldY - worldY;

            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "right" : "left";
            } else {
                direction = dy > 0 ? "down" : "up";
            }
            if (dialogue != null) {
                gp.textBox.setText(dialogue, gp.g2);
            }
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage spriteToDraw = null;

        switch (direction) {
            case "up":
                spriteToDraw = up0;
                break;
            case "down":
                spriteToDraw = down0;
                break;
            case "left":
                spriteToDraw = left0;
                break;
            case "right":
                spriteToDraw = right0;
                break;
            default:
                spriteToDraw = down0;
                break;
        }

        if (spriteToDraw != null) {
            g2.drawImage(spriteToDraw, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            g2.setColor(Color.RED);
            g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
        }
    }
}
