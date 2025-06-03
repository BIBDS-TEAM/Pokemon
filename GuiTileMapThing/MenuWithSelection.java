package GuiTileMapThing;

import java.awt.FontFormatException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;

public class MenuWithSelection {
    private String[] options;
    private String[][] gridOptions;
    private boolean isGrid = false;

    private int selectedRow = 0;
    private int selectedCol = 0;

    private int x, y;
    private Image borderImage;
    private final int borderTileSize = 16;
    private final int drawTileSize = 32;
    private boolean visible = false;

    private float fontSize;
    protected Font font;
    private Image selectionArrow;

    // Constructor for 1D menu
    public MenuWithSelection(String[] options, int x, int y, float fontSize) {
        this.options = options;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.isGrid = false;
        initAssets();
    }


    public MenuWithSelection(String[][] gridOptions, int x, int y, float fontSize) {
        this.gridOptions = gridOptions;
        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
        this.isGrid = true;
        initAssets();
    }

    protected void initAssets() {
        borderImage = new ImageIcon("TileGambar/MenuBorder1.png").getImage();
        selectionArrow = new ImageIcon("TileGambar/Arrow_Selection_1.png").getImage();
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(fontSize);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, 12);
        }
    }

    public void draw(Graphics2D g2) {
        if (!visible) return;

        g2.setFont(font);
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics(font);

        int textHeight = fm.getHeight();
        int padding = textHeight;
        int lineSpacing = textHeight + 10;
        int extraSpaceX = (int)(20 * (fontSize/28f));
        if (isGrid) {
            int rows = gridOptions.length;
            int cols = gridOptions[0].length;
            int colSpacing = 40;

            int[] colWidths = new int[cols];
            for (int col = 0; col < cols; col++) {
                int maxWidth = 0;
                for (int row = 0; row < rows; row++) {
                    if (gridOptions[row].length > col) {
                        maxWidth = Math.max(maxWidth, fm.stringWidth(gridOptions[row][col]));
                    }
                }
                colWidths[col] = maxWidth;
            }

            int contentWidth = colSpacing * (cols - 1);
            for (int w : colWidths) contentWidth += w;

            int totalWidth = contentWidth + 2 * padding + extraSpaceX;
            int totalHeight = rows * lineSpacing + 2 * padding;

            drawBorder(g2, x, y, totalWidth, totalHeight);

            int startX = x + padding + extraSpaceX;
            for (int col = 0; col < cols; col++) {
                for (int row = 0; row < rows; row++) {
                    if (gridOptions[row].length > col) {
                        String text = gridOptions[row][col];
                        int textX = startX;
                        int textY = y + padding + row * lineSpacing + fm.getAscent();

                        g2.drawString(text, textX, textY);
                        if (row == selectedRow && col == selectedCol && selectionArrow != null) {
                            g2.drawImage(selectionArrow, textX - 30, textY - 24, 24, 24, null);
                        }
                    }
                }
                startX += colWidths[col] + colSpacing;
            }

        } else {
            int maxWidth = 0;
            for (String option : options) {
                maxWidth = Math.max(maxWidth, fm.stringWidth(option));
            }

            int totalWidth = maxWidth + 2 * padding + extraSpaceX ;
            int totalHeight = options.length * lineSpacing + 2 * padding;

            drawBorder(g2, x, y, totalWidth, totalHeight);

            for (int i = 0; i < options.length; i++) {
                int textX = x + padding + extraSpaceX;
                int textY = y + padding + i * lineSpacing + fm.getAscent();
                g2.drawString(options[i], textX, textY);

                if (i == selectedRow && selectionArrow != null) {
                    g2.drawImage(selectionArrow, textX - 30, textY - 24, 24, 24, null);
                }
            }
        }
    }

    protected void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
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

    public void moveUp() { selectedRow = (selectedRow - 1 + getRowCount()) % getRowCount(); }
    public void moveDown() { selectedRow = (selectedRow + 1) % getRowCount(); }
    public void moveLeft() { if (isGrid) selectedCol = (selectedCol - 1 + getColCount()) % getColCount(); }
    public void moveRight() { if (isGrid) selectedCol = (selectedCol + 1) % getColCount(); }

    public String select() {
        return isGrid ? gridOptions[selectedRow][selectedCol] : options[selectedRow];
    }

    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }
    public int getSelectedIndex() { return selectedRow; }

    public void setFont(Font font) { this.font = font; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOptions(String[] options) {
        this.options = options;
        this.isGrid = false;
        selectedRow = 0;
    }

    public void setOptions(String[][] gridOptions) {
        this.gridOptions = gridOptions;
        this.isGrid = true;
        selectedRow = 0;
        selectedCol = 0;
    }

    private int getRowCount() {
        return isGrid ? gridOptions.length : options.length;
    }

    private int getColCount() {
        return isGrid ? gridOptions[0].length : 1;
    }

    // Protected Getters
    protected String[] getOptions() { return options; }
    protected String[][] getGridOptions() { return gridOptions; }
    protected Image getSelectionArrow() { return selectionArrow; }
    protected int getX() { return x; }
    protected int getY() { return y; }
    protected int getSelectedRow() { return selectedRow; }
    protected int getSelectedCol() { return selectedCol; }
    protected Font getFont() { return font; }
}
