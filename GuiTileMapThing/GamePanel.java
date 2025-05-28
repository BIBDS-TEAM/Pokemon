package GuiTileMapThing;

import PlayerNPCgitu.Player;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonAllType.PokemonType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

class BattlePanel extends JPanel {

}

public class GamePanel extends JPanel implements Runnable {
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
    private boolean isNewGame;
    private int transitionStep = 0;
    private int transitionTimer = 0;
    private final int transitionSpeed = 4;
    private boolean isOpeningPhase = false;
    private int openingStep = 0;
    private final int openingSpeed = 2;
    KeyInput keyI = new KeyInput();
    Thread gameThread;
    TileManager tileManager = new TileManager(this);
    Player player = new Player(this, keyI);
    private GameState currentState = GameState.SPLASH;
    private int splashTimer = 0;
    private final int splashDuration = 180;
    private int mainMenuFadeTimer = 0;
    private final int mainMenuFadeDuration = 60;
    private Image splashLogo, mainMenuBG, titleScreenImage;
    private Font pokemonFont;
    private MenuWithSelection mainMenu;
    private MenuWithSelection nameSelect;
    private TextBox textBox;
    private SaveSlot saveSlot;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.DARK_GRAY);
        addKeyListener(keyI);
        setFocusable(true);
        requestFocusInWindow();
        saveSlot = new SaveSlot();
        splashLogo = new ImageIcon("pokemon_logo.png").getImage();
        mainMenuBG = new ImageIcon("bg_menu.png").getImage();
        titleScreenImage = new ImageIcon("TileGambar/FilkomEd_title2.png").getImage();
        String[] menuOptions = { "New Game", "Load Game" ,"Settings" };
        mainMenu = new MenuWithSelection(menuOptions, 140,290,28f);
        mainMenu.setVisible(false);
        String [][] alphabetOptions = 
        {{"a", "b", "c", "d", "e", "f"},
         {"g", "h", "i", "j", "k", "l"},
         {"m", "n", "o", "p", "q", "r"},
         {"s", "t", "u", "v", "w", "x"},
         {"y", "z", "1", "2", "3", "4"},
         {"5", "6", "7", "8", "9", "?"}};
        nameSelect = new MenuWithSelection(alphabetOptions, 40, 100,28f);
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

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while (gameThread != null) {
            double interval = 1000000000 / currentFPS;
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update() {
        switch (currentState) {
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
                    if (selected.equals("New Game") || selected.equals("Load Game")) {
                    isNewGame = selected.equals("New Game"); 
                    currentState = GameState.SAVESLOT;
                    }
                    keyI.enterPressed = false;
                }
                break;
            case SAVESLOT :
            if (keyI.enterPressed) {
                currentState = GameState.NAMINGPLAYER;
            }
                break;
            case NAMINGPLAYER :
            nameSelect.setVisible(true);
                 if (keyI.upPressed) {
                    nameSelect.moveUp();
                    keyI.upPressed = false;
                }
                if (keyI.downPressed) {
                    nameSelect.moveDown();
                    keyI.downPressed = false;
                }
                if(keyI.leftPressed) {
                    nameSelect.moveLeft();
                    keyI.leftPressed = false;
                }
                if(keyI.rightPressed) {
                    nameSelect.moveRight();
                    keyI.rightPressed = false;
                }
                if(keyI.spacePressed){
                    currentState = GameState.OVERWORLD;
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (currentState) {
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
            case SAVESLOT:
                saveSlot.draw(g2);
                break;
            case NAMINGPLAYER:
                nameSelect.draw(g2);
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
                String[][] gridOptions = {{"FIGHT", "PKMN"}, {"ITEM", "RUN"}};
                String snorlaxEnemyFightModelPath = "Pokemon\\PokemonAssets\\SNORLAX_FIGHTMODEL_ENEMY.png";
                String snorlaxAllyFightModelPath = "Pokemon\\PokemonAssets\\SNORLAX_FIGHTMODEL_ALLY.png";
                String snorlaxMiniModelPath = "Pokemon\\PokemonAssets\\SNORLAX_FIGHTMODEL_ALLY.png";

                Pokemon pokemon = new Pokemon("SNORLAX", PokemonType.NORMAL, 1, 80, 50, 45, 35, 60, 60, snorlaxMiniModelPath, snorlaxAllyFightModelPath, snorlaxEnemyFightModelPath);
                Pokemon[] pokemons = {pokemon, pokemon, pokemon, pokemon};
                Battle battle = new Battle(gridOptions, 20, 20, 200, 100, pokemons, pokemon);
                
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // g2.setColor(Color.BLACK);
                // g2.setFont(new Font("Arial", Font.BOLD, 20));
                // g2.drawString("Battle Start!", 100, 100);
                BufferedImage tempAllyFightModel = battle.scaleImage(pokemon.getAllyFightModel(), 2.0);
                BufferedImage tempEnemyFightModel = battle.scaleImage(pokemon.getEnemyFightModel(), 2.0);
                pokemon.setAllyFightModel(tempAllyFightModel);
                pokemon.setEnemyFightModel(tempEnemyFightModel);
                battle.drawAllyPokemonHpBar(g2, pokemon, 290, 320, 130, 20);
                battle.drawEnemyPokemonHpBar(g2, pokemon, 50, 100, 130, 20);
                battle.drawPokemonSpriteWithIndex(g2, pokemon, 2,  350,  30); // 300 20
                battle.drawPokemonSpriteWithIndex(g2, pokemon, 1, 60,  230); // 60 180
                break;
        }
        g2.dispose();
    }

    public void startBattle(Graphics g) {
        currentState = GameState.BATTLETRANSITION;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        

    }
}
