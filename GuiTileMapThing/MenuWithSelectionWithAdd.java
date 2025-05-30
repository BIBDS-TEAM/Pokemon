package GuiTileMapThing;

import java.awt.*;

public class MenuWithSelectionWithAdd extends MenuWithSelection {
    private String[] additionalInfo;

    public MenuWithSelectionWithAdd(String[] options, String[] additionalInfo, int x, int y, float fontSize) {
        super(options, x, y, fontSize);
        this.additionalInfo = additionalInfo;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!isVisible()) return;

        g2.setFont(getFont());
        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics(getFont());

        int textHeight = fm.getHeight();
        int padding = textHeight;
        int lineSpacing = textHeight * 2;  

        int extraSpaceX = (int)(20 * (getFont().getSize2D()/28f));
        String[] options = getOptions();
        int maxWidth = 0;
        for (String option : options) {
            maxWidth = Math.max(maxWidth, fm.stringWidth(option));
        }

        int totalWidth = maxWidth + 2 * padding + extraSpaceX;
        int totalHeight = options.length * lineSpacing + 2 * padding;

        drawBorder(g2, getX(), getY(), totalWidth, totalHeight);

        for (int i = 0; i < options.length; i++) {
            int textX = getX() + padding + extraSpaceX;
            int textY = getY() + padding + i * lineSpacing + fm.getAscent();

            g2.setFont(getFont());
            g2.drawString(options[i], textX, textY);

            if (additionalInfo != null && additionalInfo.length > i && additionalInfo[i] != null && !additionalInfo[i].isEmpty()) {
                Font smallFont = getFont().deriveFont(getFont().getSize2D() * 0.7f);
                g2.setFont(smallFont);
                g2.drawString(additionalInfo[i], textX, textY + fm.getHeight() / 2);
            }

            if (i == getSelectedRow() && getSelectionArrow() != null) {
                g2.drawImage(getSelectionArrow(), textX - 30, textY - 24, 24, 24, null);
            }
        }
    }
    public void setAdditionalInfo(String[] additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
