package GuiTileMapThing;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
    
public class MenuWithSelection {
    private String[] options;
    private int selectedIndex = 0;
    private int x, y, width, height;
    private Image borderImage;
    private final int borderSize = 16;
    private final int drawSize = 32;  

    private boolean visible = false;
    private Font font ;
    private Image selectionArrow;
    

    public MenuWithSelection(String[] options, int x, int y, int width, int height) {
        this.options = options;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        borderImage = new ImageIcon("TileGambar/MenuBorder1.png").getImage();
        selectionArrow = new ImageIcon("TileGambar/Arrow_Selection_1.png").getImage();
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(28f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, 12); 
        }
    }

    public void draw(Graphics2D g2) {
    if (!visible) return;

    int menuHeight = 60 * options.length + 20;

    drawBorder(g2, x, y, width, menuHeight);

    g2.setFont(font);
    g2.setColor(Color.BLACK);
    for (int i = 0; i < options.length; i++) {
        int textY = y + 60 + i * 60;
        g2.drawString(options[i], x + 40, textY);
        if (i == selectedIndex && selectionArrow != null) {
            int arrowY = textY - 24;
            g2.drawImage(selectionArrow, x + 10, arrowY, 24, 24, null);
        }
    }
}
    private void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
    int b = borderSize;
    int d = drawSize;

    // Adjust width/height to accommodate scaled border
    int innerX = x + d;
    int innerY = y + d;
    int innerW = width - 2 * d;
    int innerH = height - 2 * d;

    // Corners
    g2.drawImage(borderImage, x, y, x + d, y + d, 0, 0, b, b, null); // top-left
    g2.drawImage(borderImage, x + width - d, y, x + width, y + d, b * 2, 0, b * 3, b, null); // top-right
    g2.drawImage(borderImage, x, y + height - d, x + d, y + height, 0, b * 2, b, b * 3, null); // bottom-left
    g2.drawImage(borderImage, x + width - d, y + height - d, x + width, y + height, b * 2, b * 2, b * 3, b * 3, null); // bottom-right

    // Sides
    g2.drawImage(borderImage, x + d, y, x + width - d, y + d, b, 0, b * 2, b, null); // top
    g2.drawImage(borderImage, x + d, y + height - d, x + width - d, y + height, b, b * 2, b * 2, b * 3, null); // bottom
    g2.drawImage(borderImage, x, y + d, x + d, y + height - d, 0, b, b, b * 2, null); // left
    g2.drawImage(borderImage, x + width - d, y + d, x + width, y + height - d, b * 2, b, b * 3, b * 2, null); // right

    // Center fill
    g2.drawImage(borderImage, x + d, y + d, x + width - d, y + height - d, b, b, b * 2, b * 2, null);
}


    public void moveUp() {
        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = options.length - 1; 
        }
    }

    public void moveDown() {
        selectedIndex++;
        if (selectedIndex >= options.length) {
            selectedIndex = 0; 
        }
    }

    public String select() {
        return options[selectedIndex];
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setOptions(String[] options) {
        this.options = options;
        selectedIndex = 0;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}