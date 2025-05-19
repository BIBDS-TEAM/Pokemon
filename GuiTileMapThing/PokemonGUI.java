package GuiTileMapThing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokemonGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen());
    }
}

class SplashScreen extends JFrame {
    private int stage = 0;
    private float fadeValue = 0f;  // untuk animasi fade in

    private Timer fadeTimer;
    private JPanel panel;

    public SplashScreen() {
        setUndecorated(true);
        setSize(480, 480);
        setResizable(false);
        setLocationRelativeTo(null); // muncul di tengah
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                if (stage == 0) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else if (stage == 1) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                } else if (stage == 2) {
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    ImageIcon icon = new ImageIcon("pokemon_logo.png");
                    g2d.drawImage(icon.getImage(),
                            (getWidth() - icon.getIconWidth()) / 2,
                            (getHeight() - icon.getIconHeight()) / 2, null);
                }
            }
        };

        add(panel);
        setVisible(true);

        // Timer untuk transisi: hitam → putih → loading → menu
        Timer stageTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stage++;
                panel.repaint();

                if (stage == 3) {
                    ((Timer) e.getSource()).stop();
                    dispose();
                    new MainMenu();
                }
            }
        });
        stageTimer.setInitialDelay(0);
        stageTimer.start();
    }
}

class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("Pokémon Menu");
        setSize(480, 480);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setContentPane(new JLabel(new ImageIcon("bg_menu.png")));
        setLayout(null);

        JButton btnNewGame = createButton("New Game", 180);
        JButton btnLoadGame = createButton("Load Game", 240);
        JButton btnSettings = createButton("Settings", 300);

        add(btnNewGame);
        add(btnLoadGame);
        add(btnSettings);

        btnNewGame.addActionListener(e -> JOptionPane.showMessageDialog(this, "New Game Clicked!"));
        btnLoadGame.addActionListener(e -> JOptionPane.showMessageDialog(this, "Load Game Clicked!"));
        btnSettings.addActionListener(e -> JOptionPane.showMessageDialog(this, "Settings Clicked!"));

        setVisible(true);
    }

    private JButton createButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(140, y, 200, 40);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.RED);
        button.setFocusPainted(false);
        return button;
    }
}
