package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class NPC extends Entity {
    GamePanel gp;

    public NPC(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(4, 4, 24, 24);
        setDefaultValues();
        loadNPCImages();
    }

    public void setDefaultValues() {
        worldX = 1000;
        worldY = 680;
        direction = "down";
    }

    private void loadNPCImages() {
        try {
            up0 = ImageIO.read(new File("TileGambar/NPC_up.png"));
            down0 = ImageIO.read(new File("TileGambar/NPC_down.png"));
            left0 = ImageIO.read(new File("TileGambar/NPC_left.png"));
            right0 = ImageIO.read(new File("TileGambar/NPC_right.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Placeholder for future NPC AI or animation
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage img = switch (direction) {
            case "up" -> up0;
            case "down" -> down0;
            case "left" -> left0;
            case "right" -> right0;
        };

        if (img != null) {
            g2.drawImage(img, screenX, screenY, 32, 32, null);
        }
    }

    public boolean isPlayerInRange() {
        int playerX = gp.player.worldX;
        int playerY = gp.player.worldY;

        return (Math.abs(worldX - playerX) == 32 && worldY == playerY) || // left/right
               (Math.abs(worldY - playerY) == 32 && worldX == playerX);   // up/down
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

            System.out.println("NPC: Hello, traveler!");
        }
    }
}
