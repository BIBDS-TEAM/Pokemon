package GuiTileMapThing;

import PlayerNPCgitu.Player;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{
    public final int oriTileSize = 16;
    public final int scale = 4;
    public final int tileSize = oriTileSize * scale;
    public final int maxScreenCol = 8;
    public final int maxScreenRow = 8;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public final int maxWorldC = 20;
    public final int maxWorldR = 20;
    public final int worldWidth = tileSize * maxWorldC;
    public final int worldHeight = tileSize * maxWorldR;
    public CollisionCheck cc = new CollisionCheck(this);
    int FPS = 60;
    KeyInput  keyI = new KeyInput();
    Thread gameThread;
    TileManager tileManager = new TileManager(this);
    Player player= new Player(this,keyI);
    private GameState currentState = GameState.SPLASH; 
    private int splashTimer = 0; 
    private final int splashDuration = 180; 
    private int mainMenuFadeTimer = 0;
    private final int mainMenuFadeDuration = 60;
    private Image splashLogo,mainMenuBG, titleScreenImage;
    private Font pokemonFont;
    
    
    public GamePanel(){
            setPreferredSize(new Dimension(screenWidth,screenHeight));
            setBackground(Color.DARK_GRAY);
            addKeyListener(keyI);
            setFocusable(true);
            requestFocusInWindow();
            splashLogo = new ImageIcon("pokemon_logo.png").getImage();
            mainMenuBG = new ImageIcon("bg_menu.png").getImage();
            titleScreenImage = new ImageIcon("TileGambar/FilkomEd_title2.png").getImage();
            try {
                pokemonFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(28f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(pokemonFont);
            } catch (IOException | FontFormatException e) {
                 e.printStackTrace();
                pokemonFont = new Font("Arial", Font.BOLD, 12); 
}
            startGameThread();
        }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run(){

        double interval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while(gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if(delta >= 1){
            update();
            repaint();
            delta--;
            drawCount++;
            }
            if(timer >= 1000000000){
                drawCount = 0;
                timer = 0;
            }
            
        }
    }

    public void update(){
         switch(currentState){
        case SPLASH:
            splashTimer++;
            if (splashTimer >= splashDuration) {
                currentState = GameState.MAINMENU;
                mainMenuFadeTimer = 0; 
            }

        case MAINMENU:
            if (mainMenuFadeTimer < mainMenuFadeDuration) {
                mainMenuFadeTimer++;
            }

            if (keyI.enterPressed) {
                System.out.println("Enter pressed on Main Menu!");
                currentState = GameState.OVERWORLD; 
                keyI.enterPressed = false; 
            }
            break;

        case OVERWORLD:
            player.update();
            break;

        case BATTLETRANSITION:
        case OVERWORLDTRANSITION:
        case BATTLE:
            break;
    }

    if (currentState == GameState.OVERWORLD) {
        player.update();
    }
}

   public void paintComponent(Graphics g){
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g; 

    switch(currentState){
        case SPLASH:
        float progress = Math.min(1.0f, (float) splashTimer / splashDuration);
        int brightness = (int) (255 * progress);
        g2.setColor(new Color(brightness, brightness, brightness));
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (splashLogo != null) {
            int x = (getWidth() - splashLogo.getWidth(null)) / 2;
            int y = (getHeight() - splashLogo.getHeight(null)) / 2;

        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, progress));
        g2.drawImage(splashLogo, x, y, null);
        g2.setComposite(originalComposite);
    }
    break;

        case MAINMENU:
            float menuProgress = Math.min(1.0f, (float) mainMenuFadeTimer / mainMenuFadeDuration);

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());

            Composite originalComposite = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuProgress));

            if (titleScreenImage != null) {
                int imgWidth = titleScreenImage.getWidth(null);
                int imgHeight = titleScreenImage.getHeight(null);
                int x = (getWidth() - imgWidth) / 2;
                int y = 40;
                g2.drawImage(titleScreenImage, x, y, null);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(pokemonFont);
            g2.drawString("New Game", 160, 300);
            g2.drawString("Load Game", 160, 350);
            g2.drawString("Settings", 160, 400);

            g2.setComposite(originalComposite);
            break;

        case OVERWORLD:
            tileManager.draw(g2);
            player.draw(g2);
            break;
    }

    g2.dispose();
}
}

