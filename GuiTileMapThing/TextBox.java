package GuiTileMapThing;

import java.awt.FontFormatException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.*;

public class TextBox {
    private final int boxWidth = 512;
    private final int boxHeight = 128;
    private final int padding = 16;

    private Image borderImage;
    private final int borderTileSize = 16;
    private final int drawTileSize = 32;

    private Font font;
    private int charIndex = 0;
    private long lastCharTime = 0;
    private final int charDelay = 30;

    protected ArrayList<String> pages = new ArrayList<>();
    private int currentPageIndex = 0;
    private String displayedText = "";

    private boolean visible = false;

    private static final String MANUAL_PAGE_BREAK_DELIMITER = "%%PAGEBREAK%%";

    public TextBox() {
        try {
            InputStream borderStream = getClass().getResourceAsStream("/TileGambar/MenuBorder1.png");
            if (borderStream != null) {
                borderImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(borderStream.readAllBytes())).getImage();
            } else {
                System.err.println("TextBox Error: Could not load border image resource /TileGambar/MenuBorder1.png");
            }

            InputStream fontStream = getClass().getResourceAsStream("/Font/Pokemon_Jadul.ttf");
            if (fontStream != null) {
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
            } else {
                System.err.println("TextBox Error: Could not load font resource /Font/Pokemon_Jadul.ttf. Using fallback.");
                font = new Font("Arial", Font.PLAIN, 16);
            }
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);

        } catch (IOException | FontFormatException e) {
            System.err.println("Failed to load assets for TextBox. Using fallback font.");
            e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, 16);
        }

        if (borderImage == null) {
            System.err.println("TextBox border image is null. Drawing will use a simple fallback border.");
        }
    }

    public TextBox(Font font) {
        this.font = font;
        try {
            InputStream borderStream = getClass().getResourceAsStream("/TileGambar/MenuBorder1.png");
            if (borderStream != null) {
                borderImage = new ImageIcon(Toolkit.getDefaultToolkit().createImage(borderStream.readAllBytes())).getImage();
            } else {
                System.err.println("TextBox Error: Could not load border image resource /TileGambar/MenuBorder1.png");
            }

            if (font == null) {
                InputStream fontStream = getClass().getResourceAsStream("/Font/Pokemon_Jadul.ttf");
            if (fontStream != null) {
                font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
            } else {
                System.err.println("TextBox Error: Could not load font resource /Font/Pokemon_Jadul.ttf. Using fallback.");
                font = new Font("Arial", Font.PLAIN, 16);
            }
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
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

    public void setVisible(){
        visible = true;
    }
    public void setText(String fullText, Graphics2D g2) {
        pages.clear();
        currentPageIndex = 0;
        displayedText = "";
        charIndex = 0;
        lastCharTime = System.currentTimeMillis();

        if (fullText == null || fullText.isEmpty()) {
            pages.add("");
            visible = true;
            return;
        }

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int textDisplayAreaWidth = boxWidth - 2 * padding;
        int textDisplayAreaHeight = boxHeight - 2 * padding;
        int lineHeight = fm.getHeight();

        if (lineHeight <= 0) {
            System.err.println("TextBox Error: Invalid lineHeight (" + lineHeight + "). Cannot paginate text. Displaying raw.");
            pages.add(fullText.replace(MANUAL_PAGE_BREAK_DELIMITER, "\n--- Page Break ---\n"));
            visible = true;
            return;
        }
        int maxLinesPerPage = Math.max(1, textDisplayAreaHeight / lineHeight);

        String[] manualSegments = fullText.split(MANUAL_PAGE_BREAK_DELIMITER, -1);

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
                            currentLineBuffer = new StringBuilder();
                            break;
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
                    pageBuffer = new StringBuilder();
                    lineCountOnCurrentPage = 0;
                }
                if (lineCountOnCurrentPage > 0) {
                    pageBuffer.append("\n");
                }
                pageBuffer.append(lineToAdd);
                lineCountOnCurrentPage++;
            }

            if (pageBuffer.length() > 0) {
                pages.add(pageBuffer.toString());
            } else if (linesForThisSegment.isEmpty() && segment.isEmpty()) {
                pages.add("");
            }
        }

        if (pages.isEmpty()) {
            pages.add("");
        }
        visible = true;
    }

    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        if (!visible || pages.isEmpty() || currentPageIndex >= pages.size()) {
            return;
        }

        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();

        int actualBoxWidth = this.boxWidth;
        int actualBoxHeight = this.boxHeight;
        int x = (panelWidth - actualBoxWidth) / 2;
        int y = panelHeight - actualBoxHeight - 20;

        drawBorder(g2, x, y, actualBoxWidth, actualBoxHeight);

        g2.setColor(Color.BLACK);

        String fullPageText = pages.get(currentPageIndex);

        long currentTime = System.currentTimeMillis();
        if (charIndex < fullPageText.length() && currentTime - lastCharTime >= charDelay) {
            displayedText += fullPageText.charAt(charIndex);
            charIndex++;
            lastCharTime = currentTime;
        } else if (charIndex >= fullPageText.length() && !displayedText.equals(fullPageText)) {
            displayedText = fullPageText;
        }


        String[] linesToDraw = displayedText.split("\n");
        int lineY = y + padding + fm.getAscent();

        for (String line : linesToDraw) {
            g2.drawString(line, x + padding, lineY);
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

        int b_src = borderTileSize;
        int d_draw = drawTileSize;

        g2.drawImage(borderImage, x, y, x + d_draw, y + d_draw, 0 * b_src, 0 * b_src, 1 * b_src, 1 * b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y, x + width, y + d_draw, 2 * b_src, 0 * b_src, 3 * b_src, 1 * b_src, null);
        g2.drawImage(borderImage, x, y + height - d_draw, x + d_draw, y + height, 0 * b_src, 2 * b_src, 1 * b_src, 3 * b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y + height - d_draw, x + width, y + height, 2 * b_src, 2 * b_src, 3 * b_src, 3 * b_src, null);

        g2.drawImage(borderImage, x + d_draw, y, x + width - d_draw, y + d_draw, 1 * b_src, 0 * b_src, 2 * b_src, 1 * b_src, null);
        g2.drawImage(borderImage, x + d_draw, y + height - d_draw, x + width - d_draw, y + height, 1 * b_src, 2 * b_src, 2 * b_src, 3 * b_src, null);
        g2.drawImage(borderImage, x, y + d_draw, x + d_draw, y + height - d_draw, 0 * b_src, 1 * b_src, 1 * b_src, 2 * b_src, null);
        g2.drawImage(borderImage, x + width - d_draw, y + d_draw, x + width, y + height - d_draw, 2 * b_src, 1 * b_src, 3 * b_src, 2 * b_src, null);

        g2.drawImage(borderImage, x + d_draw, y + d_draw, x + width - d_draw, y + height - d_draw, 1 * b_src, 1 * b_src, 2 * b_src, 2 * b_src, null);
    }

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
        if(!isDoneTyping()){
            skipToFullPage();
        }
        else {
            if (pages.isEmpty()) {
            hide();
            return;
        }
        if (!isLastPage()) {
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
        if (pages.isEmpty()) {
            return false;
        }
        return currentPageIndex < pages.size() - 1;
    }

    public void reset() {
        if (!pages.isEmpty()) {
            currentPageIndex = 0;
            displayedText = "";
            charIndex = 0;
            lastCharTime = System.currentTimeMillis();
        } else {
            pages.add("");
            currentPageIndex = 0;
            displayedText = "";
            charIndex = 0;
        }
    }

    public void show() {
        visible = true;
        if (!pages.isEmpty() && currentPageIndex < pages.size()) {
            lastCharTime = System.currentTimeMillis();
        } else if (pages.isEmpty()){
            setText("", null);
        }
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
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
}