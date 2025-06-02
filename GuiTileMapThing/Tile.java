package GuiTileMapThing;
import java.awt.image.BufferedImage;
public class Tile {
    public BufferedImage image;
    public boolean collision;
    public boolean encounterable;

    public Tile(BufferedImage image, boolean collision) {
        this.image = image;
        this.collision = collision;
    }
    public void setCollision(boolean a){
        collision = a;
    }
    public void setEncounterable(boolean a){
        encounterable =a;
    }
}

