package Font;

import java.awt.*;
import java.io.IOException;
import java.io.File;

public class FontLoader {
    public static Font loadFont(String path, int fontSize) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(fontSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, fontSize);
        }
    }
}
