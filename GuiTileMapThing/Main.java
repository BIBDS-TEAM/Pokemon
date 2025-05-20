package GuiTileMapThing;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String argz[]) {
        JFrame frame = new JFrame("pukemon");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setMinimumSize(new Dimension(800,800));
            frame.getContentPane().setBackground(Color.BLACK);
            JPanel outerPanel = new JPanel(new GridBagLayout());
            outerPanel.setBackground(Color.BLACK);
            GamePanel gamePanel = new GamePanel();
            outerPanel.add(gamePanel);
            frame.add(outerPanel, BorderLayout.CENTER);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
    }
}
