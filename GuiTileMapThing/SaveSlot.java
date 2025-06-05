package GuiTileMapThing;
import java.awt.*;
import javax.swing.ImageIcon;

public class SaveSlot {
    private int screenWidth;
    private int screenHeight;
    private int selectedSlot = 0;  // 0 = top, 1 = middle, 2 = bottom
    public boolean visible;
    private Image borderImage;
    private Image selectionArrow;
    private Font font;

    private final int borderTileSize = 16;
    private final int drawTileSize = 32;
    private final float fontSize = 28f;

    private String[] slotTexts = {
            "Save Slot 1 - Empty",
            "Save Slot 2 - Empty",
            "Save Slot 3 - Empty"
    };

    public SaveSlot(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Load assets
        borderImage = new ImageIcon("TileGambar/MenuBorder1.png").getImage();
        selectionArrow = new ImageIcon("TileGambar/Arrow_Selection_1.png").getImage();

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("Font/Pokemon_Jadul.ttf")).deriveFont(fontSize);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, 20);
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight); // fill background

        g2.setFont(font);
        g2.setColor(Color.WHITE);

        int slotWidth = screenWidth - 200;
        int slotHeight = 100;
        int slotX = 100;
        int startY = 100;
        int spacing = 150;

        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < 3; i++) {
            int slotY = startY + i * spacing;
            drawBorder(g2, slotX, slotY, slotWidth, slotHeight);
            String text = slotTexts[i];
            int textX = slotX + 40;
            int textY = slotY + (slotHeight + fm.getAscent()) / 2;
            g2.drawString(text, textX, textY);
            if (i == selectedSlot) {
                g2.drawImage(selectionArrow, slotX - 50, slotY + (slotHeight - 24) / 2, 24, 24, null);
            }
        }
    }
    private void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
        int b = borderTileSize;
        int d = drawTileSize;

        g2.drawImage(borderImage, x, y, x + d, y + d, 0, 0, b, b, null);
        g2.drawImage(borderImage, x + width - d, y, x + width, y + d, b * 2, 0, b * 3, b, null);
        g2.drawImage(borderImage, x, y + height - d, x + d, y + height, 0, b * 2, b, b * 3, null);
        g2.drawImage(borderImage, x + width - d, y + height - d, x + width, y + height, b * 2, b * 2, b * 3, b * 3, null);

        g2.drawImage(borderImage, x + d, y, x + width - d, y + d, b, 0, b * 2, b, null);
        g2.drawImage(borderImage, x + d, y + height - d, x + width - d, y + height, b, b * 2, b * 2, b * 3, null);
        g2.drawImage(borderImage, x, y + d, x + d, y + height - d, 0, b, b, b * 2, null);
        g2.drawImage(borderImage, x + width - d, y + d, x + width, y + height - d, b * 2, b, b * 3, b * 2, null);

        g2.drawImage(borderImage, x + d, y + d, x + width - d, y + height - d, b, b, b * 2, b * 2, null);
    }

    public void moveUp() {
        selectedSlot = (selectedSlot - 1 + 3) % 3;
    }

    public void moveDown() {
        selectedSlot = (selectedSlot + 1) % 3;
    }

    public int getSelectedSlot() {
        return selectedSlot;
    }

    public String getSelectedSlotText() {
        return slotTexts[selectedSlot];
    }
    public boolean checkSelectedSlot(int a) {
    String fileName = "saveSlot_" + a + ".txt";
    java.io.File file = new java.io.File(fileName);
    return file.exists() && file.isFile();
}
    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }

}

