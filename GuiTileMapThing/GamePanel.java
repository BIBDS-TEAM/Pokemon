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
    int normalFPS = 60;
    int transitionFPS = 180;
    int currentFPS = normalFPS;
    private int transitionStep = 0;
    private int transitionTimer = 0;
    private final int transitionSpeed = 4; 
    private boolean isOpeningPhase = false;
    private int openingStep = 0;
    private final int openingSpeed = 2;
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
    private MenuWithSelection mainMenu;
    private TextBox textBox;
    
    public GamePanel(){
            setPreferredSize(new Dimension(screenWidth,screenHeight));
            setBackground(Color.DARK_GRAY);
            addKeyListener(keyI);
            setFocusable(true);
            requestFocusInWindow();
            splashLogo = new ImageIcon("pokemon_logo.png").getImage();
            mainMenuBG = new ImageIcon("bg_menu.png").getImage();
            titleScreenImage = new ImageIcon("TileGambar/FilkomEd_title2.png").getImage();
            String[][] menuOptions = {{ "New Game", "Load Game"},{"Settings","Test"}};
            mainMenu = new MenuWithSelection(menuOptions, 140, 290, 300, 150);
            mainMenu.setVisible(false);
            textBox = new TextBox();
            textBox.setText("Selamat datang di dunia petualangan!sssssssssssssssssssssssssssssssssssssssss");
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

        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while(gameThread != null){
            double interval = 1000000000/currentFPS;
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
            mainMenu.setVisible(true);

        if (mainMenuFadeTimer < mainMenuFadeDuration) {
            mainMenuFadeTimer++;
        }

        if (keyI.upPressed) {
            mainMenu.moveUp();
            keyI.upPressed = false;
        }
        if (keyI.downPressed) {
            mainMenu.moveDown();
            keyI.downPressed = false;
        }
        if (keyI.enterPressed) {
            String selected = mainMenu.select();
            System.out.println("Selected: " + selected);
        if (selected.equals("New Game")) {
            currentState = GameState.OVERWORLD;
        }
        keyI.enterPressed = false;
    }
    break;

        case OVERWORLD:
            player.update();
            if (keyI.spacePressed) {
                    keyI.spacePressed = false;
                    currentState = GameState.BATTLETRANSITION;
                    currentFPS = transitionFPS;
                    transitionStep = 0;
                    transitionTimer = 0;
                }
                break;

        case BATTLETRANSITION:
            if (!isOpeningPhase) {
                transitionTimer++;
                if (transitionTimer >= transitionSpeed) {
                    transitionTimer = 0;
                    transitionStep++;
                if (transitionStep > (getWidth() / 2 / transitionSpeed + getHeight() / 2 / transitionSpeed)) {
                    isOpeningPhase = true;
                    openingStep = 0;
                }
            }
        } else {
            openingStep++;
            if (openingStep > getHeight() / 2 / openingSpeed) {
                currentState = GameState.BATTLE;
                currentFPS = normalFPS;
            }
        }
            break;
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
            mainMenu.draw(g2);
            g2.setComposite(originalComposite);
            break;

        case OVERWORLD:
            tileManager.draw(g2);
            player.draw(g2);
            textBox.draw(g2, getWidth(), getHeight());
            if (keyI.spacePressed) {
                keyI.spacePressed = false;
                textBox.setText("Hello world,text goes here");
            }

            break;
        
        case BATTLETRANSITION:
            tileManager.draw(g2);
            player.draw(g2);
            g2.setColor(Color.BLACK);

            if (!isOpeningPhase) {
                int stepPixels = transitionStep * transitionSpeed;
                g2.fillRect(getWidth() - stepPixels, 0, stepPixels, getHeight());
                g2.fillRect(0, 0, getWidth(), stepPixels);
                g2.fillRect(0, 0, stepPixels, getHeight());
                g2.fillRect(0, getHeight() - stepPixels, getWidth(), stepPixels);
            } else {
                int openPixels = openingStep * openingSpeed;
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.clearRect(0, getHeight() / 2 - openPixels, getWidth(), openPixels);
                g2.clearRect(0, getHeight() / 2, getWidth(), openPixels);
            }
            break;

            case BATTLE:
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.drawString("Battle Start!", 100, 100);
                break;
    }
    g2.dispose();
}
}

