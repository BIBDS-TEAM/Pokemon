package GuiTileMapThing;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class TextBox {
    private Image frameImage;
    private String fullText = "";
    private String displayedText = "";
    private final int padding = 16;

    private long lastCharTime = 0;
    private final int charDelay = 30; // ms antar karakter
    private int charIndex = 0;

    public TextBox() {
        try {
            frameImage = new ImageIcon("TileGambar/MenuBorder.png").getImage();
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar frame textbox.");
            e.printStackTrace();
        }
    }

    public void setText(String newText) {
        this.fullText = newText;
        this.displayedText = "";
        this.charIndex = 0;
        this.lastCharTime = System.currentTimeMillis();
    }

    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        if (frameImage == null)
            return;

        int boxWidth = panelWidth - 40;
        int boxHeight = 100;
        int x = 20;
        int y = panelHeight - boxHeight - 20;

        g2.drawImage(frameImage, x, y, boxWidth, boxHeight, null);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));

        // Update animasi ketik
        long currentTime = System.currentTimeMillis();
        if (charIndex < fullText.length() && currentTime - lastCharTime >= charDelay) {
            displayedText += fullText.charAt(charIndex);
            charIndex++;
            lastCharTime = currentTime;
        }

        // Bungkus teks
        FontMetrics fm = g2.getFontMetrics();
        int maxWidth = boxWidth - 2 * padding;

        String[] words = displayedText.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y + padding + 20;

        for (String word : words) {
            String testLine = line + word + " ";
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x + padding, lineY);
                line = new StringBuilder(word + " ");
                lineY += fm.getHeight();

                // Hindari keluar dari frame
                if (lineY > y + boxHeight - padding)
                    break;
            } else {
                line.append(word).append(" ");
            }
        }
        g2.drawString(line.toString(), x + padding, lineY);
    }

    public void draw(Graphics2D g2, int x, int y, int panelWidth, int panelHeight) {
        if (frameImage == null)
            return;

        int boxWidth = panelWidth - 40;
        int boxHeight = 100;

        g2.drawImage(frameImage, x, y, boxWidth, boxHeight, null);

        try {
            g2.setColor(Color.BLACK);
            Font pokemonFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(28f);
            g2.setFont(pokemonFont);
        } catch (IOException | FontFormatException e) {
            System.err.println("Gagal memuat font.");
            e.printStackTrace();
        }

        // Update animasi ketik
        long currentTime = System.currentTimeMillis();
        if (charIndex < fullText.length() && currentTime - lastCharTime >= charDelay) {
            displayedText += fullText.charAt(charIndex);
            charIndex++;
            lastCharTime = currentTime;
        }

        // Bungkus teks
        FontMetrics fm = g2.getFontMetrics();
        int maxWidth = boxWidth - 2 * padding;

        String[] words = displayedText.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y + padding + 20;

        for (String word : words) {
            String testLine = line + word + " ";
            if (fm.stringWidth(testLine) > maxWidth) {
                g2.drawString(line.toString(), x + padding, lineY);
                line = new StringBuilder(word + " ");
                lineY += fm.getHeight();

                // Hindari keluar dari frame
                if (lineY > y + boxHeight - padding)
                    break;
            } else {
                line.append(word).append(" ");
            }
        }
        g2.drawString(line.toString(), x + padding, lineY);
    }

    public boolean isDoneTyping() {
        return charIndex >= fullText.length();
    }
}
