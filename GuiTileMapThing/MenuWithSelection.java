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

    private int additionalXPadding = 0;
    private int additionalYPadding = 0;

    private int x, y;
    private Image borderImage;
    private final int borderTileSize = 16;
    private final int drawTileSize = 32; // Size of border corner/edge pieces when drawn
    private boolean visible = false;

    private float fontSize;
    protected Font font;
    private Image selectionArrow;
    private int selectionArrowWidth = 24;
    private int selectionArrowHeight = 24;
    private final int ARROW_HORIZONTAL_GAP = 5; // Gap between arrow and text
    private final int ARROW_AREA_WIDTH = (selectionArrowWidth > 0 ? selectionArrowWidth : 24) + ARROW_HORIZONTAL_GAP;


    private int fixedWidth = 0;
    private int fixedHeight = 0;

    // Constructors (chaining to simplify initialization)
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
    
    public MenuWithSelection(String[] options, int x, int y, float fontSize, double fixedWidth, int fixedHeight) {
        this(options, x, y, fontSize);
        this.fixedWidth = (int)fixedWidth;
        this.fixedHeight = fixedHeight;
    }

    public MenuWithSelection(String[][] gridOptions, int x, int y, float fontSize, double fixedWidth, int fixedHeight) {
        this(gridOptions, x, y, fontSize);
        this.fixedWidth = (int)fixedWidth;
        this.fixedHeight = fixedHeight;
    }

    public MenuWithSelection(String[] options, int x, int y, float fontSize, int additionalXPadding, int additionalYPadding) {
        this(options, x,y,fontSize);
        this.additionalXPadding = additionalXPadding;
        this.additionalYPadding = additionalYPadding;
    }

    public MenuWithSelection(String[][] gridOptions, int x, int y, float fontSize, int additionalXPadding, int additionalYPadding) {
        this(gridOptions,x,y,fontSize);
        this.additionalXPadding = additionalXPadding;
        this.additionalYPadding = additionalYPadding;
    }
    
    public MenuWithSelection(String[] options, int x, int y, float fontSize, int fixedWidth, int fixedHeight, int additionalXPadding, int additionalYPadding) {
        this(options, x, y, fontSize, fixedWidth, fixedHeight);
        // For fixed size, additional padding is usually for internal content distribution if needed,
        // but primarily fixedWidth/Height dictate the outer bounds. We'll let fixed size take precedence.
    }

    public MenuWithSelection(String[][] gridOptions, int x, int y, float fontSize, int fixedWidth, int fixedHeight, int additionalXPadding, int additionalYPadding) {
        this(gridOptions, x, y, fontSize, fixedWidth, fixedHeight);
    }


    protected void initAssets() {
        borderImage = new ImageIcon("TileGambar/MenuBorder1.png").getImage();
        selectionArrow = new ImageIcon("TileGambar/Arrow_Selection_1.png").getImage();
        if (selectionArrow != null) {
            selectionArrowWidth = selectionArrow.getWidth(null) > 0 ? selectionArrow.getWidth(null) : 24;
            selectionArrowHeight = selectionArrow.getHeight(null) > 0 ? selectionArrow.getHeight(null) : 24;
        }
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(fontSize);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, (int)fontSize);
        }
    }

    public void draw(Graphics2D g2) {
        if (!visible) return;

        g2.setFont(font);
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics(font);
        int textLineHeight = fm.getHeight(); // Total height for one line of text
        int ascent = fm.getAscent();
        
        // HIGHLIGHT: Define internal padding from border to text content area
        int internalPadding = textLineHeight / 2; 
        int lineSpacingGap = 10; // Gap between lines of text in a 1D list or rows in a grid

        int totalWidthToDraw, totalHeightToDraw;

        if (isGrid) {
            int rows = getRowCount();
            int cols = getColCount();
            if (rows == 0 || cols == 0) return;

            if (fixedWidth > 0 && fixedHeight > 0) {
                totalWidthToDraw = fixedWidth;
                totalHeightToDraw = fixedHeight;
            } else {
                int maxItemTextWidth = 0;
                for (String[] rowArray : gridOptions) {
                    for (String item : rowArray) {
                        if (item != null) maxItemTextWidth = Math.max(maxItemTextWidth, fm.stringWidth(item));
                    }
                }
                int contentWidthPerCell = maxItemTextWidth + ARROW_AREA_WIDTH;
                totalWidthToDraw = cols * contentWidthPerCell + internalPadding * (cols -1) + 2 * internalPadding + additionalXPadding;
                totalHeightToDraw = rows * textLineHeight + (rows > 1 ? (rows - 1) * lineSpacingGap : 0) + 2 * internalPadding + additionalYPadding;
            }
            drawBorder(g2, x, y, totalWidthToDraw, totalHeightToDraw);

            // Calculate usable content area *inside* the border and general internal padding
            int contentAreaX = x + internalPadding;
            int contentAreaY = y + internalPadding;
            int contentAreaWidth = totalWidthToDraw - 2 * internalPadding;
            int contentAreaHeight = totalHeightToDraw - 2 * internalPadding;

            int cellWidth = contentAreaWidth / cols;
            int cellHeight = contentAreaHeight / rows;


            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (gridOptions[r].length > c && gridOptions[r][c] != null) {
                        String text = gridOptions[r][c];
                        int textActualWidth = fm.stringWidth(text);
                        
                        int cellContentX = contentAreaX + c * cellWidth;
                        int cellContentY = contentAreaY + r * cellHeight;

                        // Center text within its allocated cell
                        int textDrawX = cellContentX + (cellWidth - textActualWidth) / 2;
                        int textDrawYBaseline = cellContentY + (cellHeight - textLineHeight) / 2 + ascent;

                        g2.drawString(text, textDrawX, textDrawYBaseline);

                        if (r == selectedRow && c == selectedCol && selectionArrow != null) {
                            int arrowX = textDrawX - ARROW_AREA_WIDTH + ARROW_HORIZONTAL_GAP; 
                            int arrowY = cellContentY + (cellHeight - selectionArrowHeight) / 2;
                            g2.drawImage(selectionArrow, arrowX, arrowY, selectionArrowWidth, selectionArrowHeight, null);
                        }
                    }
                }
            }

        } else { // 1D Menu
            int numOptions = getRowCount();
            if (numOptions == 0) return;

            if (fixedWidth > 0 && fixedHeight > 0) {
                totalWidthToDraw = fixedWidth;
                totalHeightToDraw = fixedHeight;
            } else {
                int maxTextWidth = 0;
                for (String option : options) {
                    if (option != null) maxTextWidth = Math.max(maxTextWidth, fm.stringWidth(option));
                }
                totalWidthToDraw = maxTextWidth + ARROW_AREA_WIDTH + 2 * internalPadding + additionalXPadding;
                totalHeightToDraw = numOptions * textLineHeight + (numOptions > 1 ? (numOptions - 1) * lineSpacingGap : 0) + 2 * internalPadding + additionalYPadding;
            }
            drawBorder(g2, x, y, totalWidthToDraw, totalHeightToDraw);
            
            // Calculate usable content area *inside* the border and general internal padding
            int contentAreaX = x + internalPadding;
            int contentAreaY = y + internalPadding;
            int contentAreaWidth = totalWidthToDraw - 2 * internalPadding;
            int contentAreaHeight = totalHeightToDraw - 2 * internalPadding;

            // HIGHLIGHT: Calculate starting Y to center the whole text block vertically within contentAreaHeight
            int totalTextBlockVisualHeight = numOptions * textLineHeight + (numOptions > 1 ? (numOptions - 1) * lineSpacingGap : 0);
            int startYForTextLines = contentAreaY + (contentAreaHeight - totalTextBlockVisualHeight) / 2;

            for (int i = 0; i < numOptions; i++) {
                if (options[i] == null) continue;
                String text = options[i];
                int textActualWidth = fm.stringWidth(text);
                
                // HIGHLIGHT: Center text horizontally within contentAreaWidth, considering space for arrow
                int textDrawX = contentAreaX + ARROW_AREA_WIDTH + (contentAreaWidth - ARROW_AREA_WIDTH - textActualWidth) / 2;
                int textDrawYBaseline = startYForTextLines + i * (textLineHeight + lineSpacingGap) + ascent;
                
                g2.drawString(text, textDrawX, textDrawYBaseline);

                if (i == selectedRow && selectionArrow != null) {
                    int arrowX = textDrawX - ARROW_AREA_WIDTH + ARROW_HORIZONTAL_GAP; // Place arrow to the left of text
                    // HIGHLIGHT: Vertically center arrow with the text line's full height
                    int arrowY = (textDrawYBaseline - ascent) + (textLineHeight - selectionArrowHeight) / 2;
                    g2.drawImage(selectionArrow, arrowX, arrowY, selectionArrowWidth, selectionArrowHeight, null);
                }
            }
        }
    }

    protected void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
        int b = borderTileSize; // source tile size from border image
        int d = drawTileSize;   // destination draw size for corners/edges

        if (borderImage == null || width < 2 * d || height < 2 * d) { // Fallback for no image or too small
            g2.setColor(new Color(230,230,230,200)); // Lighter gray, slightly transparent
            g2.fillRect(x,y,width,height);
            g2.setColor(Color.darkGray);
            g2.drawRect(x,y,width-1, height-1);
            return;
        }

        // Corners
        g2.drawImage(borderImage, x, y, x + d, y + d, 0, 0, b, b, null); // Top-left
        g2.drawImage(borderImage, x + width - d, y, x + width, y + d, b * 2, 0, b * 3, b, null); // Top-right
        g2.drawImage(borderImage, x, y + height - d, x + d, y + height, 0, b * 2, b, b * 3, null); // Bottom-left
        g2.drawImage(borderImage, x + width - d, y + height - d, x + width, y + height, b * 2, b * 2, b * 3, b * 3, null); // Bottom-right

        // Edges
        g2.drawImage(borderImage, x + d, y, x + width - d, y + d, b, 0, b * 2, b, null); // Top
        g2.drawImage(borderImage, x + d, y + height - d, x + width - d, y + height, b, b * 2, b * 2, b * 3, null); // Bottom
        g2.drawImage(borderImage, x, y + d, x + d, y + height - d, 0, b, b, b * 2, null); // Left
        g2.drawImage(borderImage, x + width - d, y + d, x + width, y + height - d, b * 2, b, b * 3, b * 2, null); // Right

        // Center
        g2.drawImage(borderImage, x + d, y + d, x + width - d, y + height - d, b, b, b * 2, b * 2, null); // Middle
    }

    public void moveUp() { selectedRow = (selectedRow - 1 + getRowCount()) % getRowCount(); }
    public void moveDown() { selectedRow = (selectedRow + 1) % getRowCount(); }
    public void moveLeft() { if (isGrid && getColCount() > 0) selectedCol = (selectedCol - 1 + getColCount()) % getColCount(); }
    public void moveRight() { if (isGrid && getColCount() > 0) selectedCol = (selectedCol + 1) % getColCount(); }

    public String select() {
        return getSelectedSelection();
    }

    public void setVisible(boolean visible) { this.visible = visible; }
    public boolean isVisible() { return visible; }
    public int getSelectedIndex() { return selectedRow; } 
    public int getSelectedRowIndex() { return selectedRow; } 
    public int getSelectedColIndex() { return selectedCol; } 


    public void setFont(Font font) { this.font = font; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOptions(String[] options) {
        this.options = options;
        this.isGrid = false;
        this.selectedRow = 0;
        this.selectedCol = 0;
    }

    public void setOptions(String[][] gridOptions) {
        this.gridOptions = gridOptions;
        this.isGrid = true;
        this.selectedRow = 0;
        this.selectedCol = 0;
    }

    private int getRowCount() {
        return isGrid ? (gridOptions != null ? gridOptions.length : 0) : (options != null ? options.length : 0);
    }

    private int getColCount() {
        return isGrid ? (gridOptions != null && gridOptions.length > 0 && gridOptions[0] != null ? gridOptions[0].length : 0) : 1; // Default to 1 for 1D array if not grid
    }
        
    protected String[] getOptions() { return options; }
    protected String[][] getGridOptions() { return gridOptions; }
    protected Image getSelectionArrow() { return selectionArrow; }
    protected int getX() { return x; }
    protected int getY() { return y; }
    protected int getSelectedRow() { return selectedRow; }
    protected int getSelectedCol() { return selectedCol; }

    public String getSelectedSelection(){
        if (isGrid) {
            if (gridOptions != null && selectedRow >=0 && selectedRow < gridOptions.length && 
                gridOptions[selectedRow] != null && selectedCol >=0 && selectedCol < gridOptions[selectedRow].length) {
                return gridOptions[selectedRow][selectedCol];
            }
        } else {
            if (options != null && selectedRow >=0 && selectedRow < options.length) {
                return options[selectedRow];
            }
        }
        return ""; 
    }
    protected Font getFont() { return font; }

    public void setFixedSize(int width, int height) {
        this.fixedWidth = width > 0 ? width : 0; // Ensure non-negative
        this.fixedHeight = height > 0 ? height : 0; // Ensure non-negative
    }
}
