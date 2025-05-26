package GuiTileMapThing;
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

    private int x, y, width, height;
    private Image borderImage;
    private final int borderSize = 16;
    private final int drawSize = 32;

    private boolean visible = false;
    private Font font;
    private Image selectionArrow;

    // Constructor for 1D array
    public MenuWithSelection(String[] options, int x, int y, int width, int height) {
        this.options = options;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isGrid = false;
        initAssets();
    }

    // Constructor for 2D array (grid menu)
    public MenuWithSelection(String[][] gridOptions, int x, int y, int width, int height) {
        this.gridOptions = gridOptions;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isGrid = true;
        initAssets();
    }

    private void initAssets() {
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
        g2.setFont(font);
        g2.setColor(Color.BLACK);

        if (isGrid) {
            int rows = gridOptions.length;
            int cols = gridOptions[0].length;
            int cellHeight = 60;
            int padding = 40;

            FontMetrics fm = g2.getFontMetrics(font);
            int[] colWidths = new int[cols];
            for (int col = 0; col < cols; col++) {
                int maxWidth = 0;
                for (int row = 0; row < rows; row++) {
                    int strWidth = fm.stringWidth(gridOptions[row][col]);
                    if (strWidth > maxWidth) {
                        maxWidth = strWidth;
                    }
                }
                colWidths[col] = maxWidth + padding;
            }

            int totalMenuWidth = 0;
            for (int colWidth : colWidths) {
                totalMenuWidth += colWidth;
            }

            int totalHeight = rows * cellHeight + 20;
            drawBorder(g2, x, y, totalMenuWidth, totalHeight);

            int currentX = x + 40;
            for (int col = 0; col < cols; col++) {
                for (int row = 0; row < rows; row++) {
                    int textX = currentX;
                    int textY = y + 60 + row * cellHeight;
                    g2.drawString(gridOptions[row][col], textX, textY);
                    if (row == selectedRow && col == selectedCol && selectionArrow != null) {
                        int arrowY = textY - 24;
                        g2.drawImage(selectionArrow, textX - 30, arrowY, 24, 24, null);
                    }
                }
                currentX += colWidths[col];
            }

        } else {
            int cellHeight = 60;
            int menuHeight = cellHeight * options.length + 20;
            drawBorder(g2, x, y, width, menuHeight);

            for (int i = 0; i < options.length; i++) {
                int textY = y + 60 + i * cellHeight;
                g2.drawString(options[i], x + 40, textY);
                if (i == selectedRow && selectionArrow != null) {
                    int arrowY = textY - 24;
                    g2.drawImage(selectionArrow, x + 10, arrowY, 24, 24, null);
                }
            }
        }
    }

    protected void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
        int b = borderSize;
        int d = drawSize;

        int innerX = x + d;
        int innerY = y + d;
        int innerW = width - 2 * d;
        int innerH = height - 2 * d;

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
        if (isGrid) {
            selectedRow = (selectedRow - 1 + gridOptions.length) % gridOptions.length;
        } else {
            selectedRow = (selectedRow - 1 + options.length) % options.length;
        }
    }

    public void moveDown() {
        if (isGrid) {
            selectedRow = (selectedRow + 1) % gridOptions.length;
        } else {
            selectedRow = (selectedRow + 1) % options.length;
        }
    }

    public void moveLeft() {
        if (isGrid) {
            selectedCol = (selectedCol - 1 + gridOptions[0].length);
        }
    }

    public void moveRight() {
        if (isGrid) {
            selectedCol = (selectedCol + 1);
        }
    }

    public String select() {
        return isGrid ? gridOptions[selectedRow][selectedCol] : options[selectedRow];
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getSelectedIndex() {
        return selectedRow;
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

    protected Image getSelectionArrow() { return selectionArrow; }
    protected int getX() { return x; }
    protected int getY() { return y; }
    protected int getWidth() { return width; }
    protected int getHeight() { return height; }
    protected Font getFont() { return font; }
}