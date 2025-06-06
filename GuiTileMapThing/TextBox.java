package GuiTileMapThing;

import java.awt.*;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Represents a text box for displaying paginated text with a custom border and font.
 */
public class TextBox {

    // --- Constants ---
    private static final int DEFAULT_BOX_WIDTH = 512;
    private static final int DEFAULT_BOX_HEIGHT = 128;
    private static final int BORDER_TILE_SIZE = 16;
    private static final int DRAW_TILE_SIZE = 32;
    private static final int PADDING = 16;
    private static final int CHAR_DELAY = 30; // Milliseconds
    private static final String MANUAL_PAGE_BREAK_DELIMITER = "%%PAGEBREAK%%";
    private static final String BORDER_IMAGE_PATH = "/TileGambar/MenuBorder1.png";
    private static final String FONT_PATH = "/Font/Pokemon_Jadul.ttf";

    // --- UI and State Properties ---
    private int additionalWidth = 0;
    private int additionalHeight = 0;
    private Image borderImage;
    private Font font;
    private boolean visible = false;

    // --- Text and Pagination Properties ---
    private final ArrayList<String> pages = new ArrayList<>();
    private int currentPageIndex = 0;
    private String displayedText = "";
    private String rawText;
    private boolean needsPagination = true;
    private int charIndex = 0;
    private long lastCharTime = 0;

    public TextBox() {
        this(null); // Delegate to the other constructor
    }
    public TextBox(Font font) {
        this.font = font;
        loadResources();
        if (pages.isEmpty()) {
            pages.add("");
        }
    }
    private void loadResources() {
        try {
            // Load border image
            InputStream borderStream = getClass().getResourceAsStream(BORDER_IMAGE_PATH);
            if (borderStream != null) {
                borderImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(borderStream.readAllBytes())).getImage();
            } else {
                System.err.println("TextBox Error: Could not load border image resource " + BORDER_IMAGE_PATH);
            }

            // Load font if not already provided
            if (font == null) {
                InputStream fontStream = getClass().getResourceAsStream(FONT_PATH);
                if (fontStream != null) {
                    font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                } else {
                    System.err.println("TextBox Error: Could not load font resource " + FONT_PATH + ". Using fallback.");
                    font = new Font("Arial", Font.PLAIN, 16);
                }
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Failed to load assets for TextBox. Using fallback font.");
            e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, 16);
        }

        if (borderImage == null) {
            System.err.println("TextBox border image is null. Drawing will use a simple fallback border.");
        }
    }

    public void setText(String fullText) {
    this.rawText = (fullText == null) ? "" : fullText; 
    this.needsPagination = true; 

    pages.clear();
    currentPageIndex = 0;
    displayedText = "";
    charIndex = 0;
    lastCharTime = System.currentTimeMillis();
    visible = true; 
}

    private void paginate(Graphics2D g2) {
        if (!needsPagination || rawText == null) {
            return;
        }

        pages.clear();

        if (rawText.isEmpty()) {
            pages.add("");
            needsPagination = false;
            return;
        }

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int textDisplayAreaWidth = (DEFAULT_BOX_WIDTH + additionalWidth) - 2 * PADDING;
        int textDisplayAreaHeight = (DEFAULT_BOX_HEIGHT + additionalHeight) - 2 * PADDING;
        int lineHeight = fm.getHeight();

        if (lineHeight <= 0) {
            System.err.println("TextBox Error: Invalid lineHeight. Displaying raw text.");
            pages.add(rawText);
            needsPagination = false;
            return;
        }
        int maxLinesPerPage = Math.max(1, textDisplayAreaHeight / lineHeight);

        String[] manualSegments = rawText.split(MANUAL_PAGE_BREAK_DELIMITER, -1);

        for (String segment : manualSegments) {
            ArrayList<String> linesForThisSegment = new ArrayList<>();
            String[] rawLinesInSegment = segment.split("\n", -1);

            for (String rawLine : rawLinesInSegment) {
                if (rawLine.isEmpty()) {
                    linesForThisSegment.add("");
                    continue;
                }

                String[] words = rawLine.split(" ");
                StringBuilder currentLineBuffer = new StringBuilder();

                for (String word : words) {
                    boolean isFirstWordInLine = currentLineBuffer.length() == 0;
                    String testLine = isFirstWordInLine ? word : currentLineBuffer.toString() + " " + word;

                    if (fm.stringWidth(testLine) > textDisplayAreaWidth) {
                        if (!isFirstWordInLine) {
                            linesForThisSegment.add(currentLineBuffer.toString());
                            currentLineBuffer = new StringBuilder(word);
                        } else {
                            linesForThisSegment.add(word);
                            currentLineBuffer.setLength(0); // Clear buffer
                        }
                    } else {
                        if (!isFirstWordInLine) {
                            currentLineBuffer.append(" ");
                        }
                        currentLineBuffer.append(word);
                    }
                }
                if (currentLineBuffer.length() > 0) {
                    linesForThisSegment.add(currentLineBuffer.toString());
                }
            }

            StringBuilder pageBuffer = new StringBuilder();
            int lineCountOnCurrentPage = 0;
            for (String lineToAdd : linesForThisSegment) {
                if (lineCountOnCurrentPage >= maxLinesPerPage) {
                    pages.add(pageBuffer.toString());
                    pageBuffer.setLength(0);
                    lineCountOnCurrentPage = 0;
                }
                if (lineCountOnCurrentPage > 0) {
                    pageBuffer.append("\n");
                }
                pageBuffer.append(lineToAdd);
                lineCountOnCurrentPage++;
            }

            if (pageBuffer.length() > 0 || linesForThisSegment.isEmpty()) {
                pages.add(pageBuffer.toString());
            }
        }

        if (pages.isEmpty()) {
            pages.add("");
        }

        needsPagination = false;
    }

    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
    // 1. First, check only for visibility. If it's not visible, we're done.
    if (!visible) {
        return;
    }
    
    // 2. Second, check if the text needs to be processed. This is where
    //    the pages list gets created. This MUST happen before any checks
    //    on the pages list itself.
    if (needsPagination) {
        paginate(g2);
    }

    // 3. Now that we are SURE the pages list has been created, we can safely
    //    check if the current page index is valid.
    if (currentPageIndex >= pages.size()) {
        return;
    }

    // --- The rest of your drawing logic remains the same ---
    g2.setFont(font);
    FontMetrics fm = g2.getFontMetrics();

    int actualBoxWidth = DEFAULT_BOX_WIDTH + additionalWidth;
    int actualBoxHeight = DEFAULT_BOX_HEIGHT + additionalHeight;
    int x = (panelWidth - actualBoxWidth) / 2;
    int y = panelHeight - actualBoxHeight - 20;

    drawBorder(g2, x, y, actualBoxWidth, actualBoxHeight);

    g2.setColor(Color.BLACK);

    String fullPageText = pages.get(currentPageIndex);

    long currentTime = System.currentTimeMillis();
    if (charIndex < fullPageText.length() && currentTime - lastCharTime >= CHAR_DELAY) {
        charIndex++;
        displayedText = fullPageText.substring(0, charIndex);
        lastCharTime = currentTime;
    } else if (charIndex >= fullPageText.length()) {
        displayedText = fullPageText;
    }
    String[] linesToDraw = displayedText.split("\n");
    int lineY = y + PADDING + fm.getAscent();

    for (String line : linesToDraw) {
        g2.drawString(line, x + PADDING, lineY);
        lineY += fm.getHeight();
    }
}
    private void drawBorder(Graphics2D g2, int x, int y, int width, int height) {
        if (borderImage == null) {
            g2.setColor(new Color(200, 200, 200, 200));
            g2.fillRect(x, y, width, height);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, width - 1, height - 1);
            return;
        }

        int b_src = BORDER_TILE_SIZE;
        int d_draw = DRAW_TILE_SIZE;

        // Corners
        g2.drawImage(borderImage, x, y, x + d_draw, y + d_draw, 0, 0, b_src, b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y, x + width, y + d_draw, 2 * b_src, 0, 3 * b_src, b_src, null);
        g2.drawImage(borderImage, x, y + height - d_draw, x + d_draw, y + height, 0, 2 * b_src, b_src, 3 * b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y + height - d_draw, x + width, y + height, 2 * b_src, 2 * b_src, 3 * b_src, 3 * b_src, null);

        // Edges
        g2.drawImage(borderImage, x + d_draw, y, x + width - d_draw, y + d_draw, b_src, 0, 2 * b_src, b_src, null);
        g2.drawImage(borderImage, x + d_draw, y + height - d_draw, x + width - d_draw, y + height, b_src, 2 * b_src, 2 * b_src, 3 * b_src, null);
        g2.drawImage(borderImage, x, y + d_draw, x + d_draw, y + height - d_draw, 0, b_src, b_src, 2 * b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y + d_draw, x + width, y + height - d_draw, 2 * b_src, b_src, 3 * b_src, 2 * b_src, null);

        // Center
        g2.drawImage(borderImage, x + d_draw, y + d_draw, x + width - d_draw, y + height - d_draw, b_src, b_src, 2 * b_src, 2 * b_src, null);
    }
    
    // --- Getters and Setters ---

    public int getDefaultWidth() {
        return DEFAULT_BOX_WIDTH;
    }

    public int getDefaultHeight() {
        return DEFAULT_BOX_HEIGHT;
    }

    public int getAdditionalWidth() {
        return additionalWidth;
    }

    public void setAdditionalWidth(int additionalWidth) {
        this.additionalWidth = additionalWidth;
        this.needsPagination = true;
    }

    public int getAdditionalHeight() {
        return additionalHeight;
    }

    public void setAdditionalHeight(int additionalHeight) {
        this.additionalHeight = additionalHeight;
        this.needsPagination = true;
    }
    
    // --- Visibility Control ---
    
    public void setVisible() {
        this.visible = true;
        this.lastCharTime = System.currentTimeMillis();
    }

    public void notVisible() {
        this.visible = false;
    }
    
    public void hide() {
        this.visible = false;
    }
    
    public void show() {
        this.visible = true;
        reset();
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    // --- Page and Text Control ---

    public boolean isDoneTyping() {
        if (pages.isEmpty() || currentPageIndex >= pages.size()) {
            return true;
        }
        return charIndex >= pages.get(currentPageIndex).length();
    }

    public boolean isLastPage() {
        if (pages.isEmpty()) {
            return true;
        }
        return currentPageIndex >= pages.size() - 1;
    }

    public void nextPage() {
        if (!isDoneTyping()) {
            skipToFullPage();
        } else {
            if (hasMorePages()) {
                currentPageIndex++;
                displayedText = "";
                charIndex = 0;
                lastCharTime = System.currentTimeMillis();
            } else {
                hide();
            }
        }
    }

    public boolean hasMorePages() {
        return !pages.isEmpty() && currentPageIndex < pages.size() - 1;
    }

    public void reset() {
        currentPageIndex = 0;
        displayedText = "";
        charIndex = 0;
        lastCharTime = System.currentTimeMillis();
    }

    public void skipToFullPage() {
        if (!pages.isEmpty() && currentPageIndex < pages.size()) {
            String fullPageText = pages.get(currentPageIndex);
            if (charIndex < fullPageText.length()) {
                displayedText = fullPageText;
                charIndex = fullPageText.length();
            }
        }
    }

    public String getCurrentText() {
        if (!pages.isEmpty() && currentPageIndex < pages.size()) {
            return pages.get(currentPageIndex);
        }
        return ""; // Return empty string to avoid NullPointerExceptions
    }
}