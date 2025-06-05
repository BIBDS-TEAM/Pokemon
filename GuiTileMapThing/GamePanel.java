package GuiTileMapThing;

import PlayerNPCgitu.NPC;
import PlayerNPCgitu.Player;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonAllType.PokemonType;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonReader.*;import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveType;

import java.awt.*;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    // game screen
    public final int oriTileSize = 16;
    public final int scale = 2;
    public final int tileSize = oriTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 16;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public final int maxWorldC = 20;
    public final int maxWorldR = 20;
    public final int worldWidth = tileSize * maxWorldC;
    public final int worldHeight = tileSize * maxWorldR;
    // tile checks
    public CollisionCheck cc = new CollisionCheck(this);
    public EncounterCheck eCheck = new EncounterCheck(this);
    // fps game loop
    int normalFPS = 60;
    int transitionFPS = 180;
    int currentFPS = normalFPS;
    // default time for transitions etc
    private boolean isNewGame;
    private int transitionStep = 0;
    private int transitionTimer = 0;
    private final int transitionSpeed = 4;
    private boolean isOpeningPhase = false;
    private int openingStep = 0;
    private final int openingSpeed = 2;
    // other classes
    private static Clip clip;
    KeyInput keyI = new KeyInput();
    Thread gameThread;
    TileManager tileManager = new TileManager(this);
    public Player player = new Player(this, keyI);
    public java.util.List<NPC> npcList = new ArrayList<>();
    NPC npc = new NPC(this, "Oak", 1080, 700, "down", "Oek");
    Battle battle;
    public Graphics2D g2;
    // gameSTATES
    private GameState currentState = GameState.SPLASH;
    BattleState battleState = BattleState.BATTLE_DECISION;
    public OverworldState overworldState = OverworldState.OVERWORLD_ROAM;
    // opening transition ???
    private int splashTimer = 0;
    private final int splashDuration = 180;
    private int mainMenuFadeTimer = 0;
    private final int mainMenuFadeDuration = 60;
    // import + gambar + grafik ?
    private Image splashLogo, mainMenuBG, titleScreenImage;
    private Font pokemonFont;
    private MenuWithSelection mainMenu;
    private MenuWithSelection nameSelect;
    public TextBox textBox;
    private SaveSlot saveSlot;
    private boolean shouldShowInitialText = true;
    //
    private boolean isOpeningPlayed;
    private PokemonMove selectedMove = null;
    private Map<String, String> selectedMoveInfo = new HashMap<>();

    private Map<String, String> allyMoveResult = new HashMap<>();
    private Map<String, String> enemyMoveResult = new HashMap<>();
    private boolean allyMoveExecuted = false;
    private boolean enemyMoveExecuted = false;

    public GamePanel() {
        npcList.add(npc);
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.DARK_GRAY);
        addKeyListener(keyI);
        setFocusable(true);
        requestFocusInWindow();
        saveSlot = new SaveSlot();
        splashLogo = new ImageIcon("pokemon_logo.png").getImage();
        mainMenuBG = new ImageIcon("bg_menu.png").getImage();
        titleScreenImage = new ImageIcon("TileGambar/FilkomEd_title2.png").getImage();
        String[] menuOptions = { "New Game", "Load Game", "Settings" };
        mainMenu = new MenuWithSelection(menuOptions, 140, 290, 28f);
        mainMenu.setVisible(false);
        String[][] alphabetOptions = { { "a", "b", "c", "d", "e", "f" },
                { "g", "h", "i", "j", "k", "l" },
                { "m", "n", "o", "p", "q", "r" },
                { "s", "t", "u", "v", "w", "x" },
                { "y", "z", "1", "2", "3", "4" },
                { "5", "6", "7", "8", "9", "?" } };
        nameSelect = new MenuWithSelection(alphabetOptions, 40, 100, 28f);
        textBox = new TextBox();
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
                if (!isOpeningPlayed) {
                    Thread audioThread = new Thread(() -> playSound("../Pokemon/audioSave/Open.wav"));
                    audioThread.start();
                    isOpeningPlayed = true;
                }
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
            case SAVESLOT:
                if (keyI.enterPressed) {
                    currentState = GameState.NAMINGPLAYER;
                }
                break;
            case NAMINGPLAYER:
                nameSelect.setVisible(true);
                if (keyI.upPressed) {
                    nameSelect.moveUp();
                    keyI.upPressed = false;
                }
                if (keyI.downPressed) {
                    nameSelect.moveDown();
                    keyI.downPressed = false;
                }
                if (keyI.leftPressed) {
                    nameSelect.moveLeft();
                    keyI.leftPressed = false;
                }
                if (keyI.rightPressed) {
                    nameSelect.moveRight();
                    keyI.rightPressed = false;
                }
                if (keyI.enterPressed) {
                    currentState = GameState.OVERWORLD;
                    stopSound();
                    isOpeningPlayed = false;

                }
                break;
            case OVERWORLD:
                switch (overworldState) {
                    case OVERWORLD_ROAM:
                        player.update();
                        if (keyI.spacePressed) {
                            currentState = GameState.BATTLETRANSITION;
                            currentFPS = transitionFPS;
                            transitionStep = 0;
                            transitionTimer = 0;
                        }
                        if (keyI.enterPressed) {
                            for (NPC npc : npcList) {
                                if (npc.isPlayerInRange() && npc.haveDialogue()) {
                                    String dialog = npc.getDialog();
                                    textBox.setText(dialog, g2);
                                    textBox.setVisible();
                                    npc.interact();
                                    break;  
                                }
                            }
                        }
                        break;
                    case OVERWORLD_INTERACTION:
                        player.update();
                        if (keyI.Ppressed) {
                            if (textBox.isVisible()) {
                                textBox.nextPage();
                                keyI.Ppressed = false;
                            }
                        }
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
                        // ... (Pokemon creation logic from previous solution) ...
                        // Example: this.battle = new Battle(playerCards, enemyCards);

                        battleState = BattleState.BATTLE_DECISION;
                        if (this.battle != null) {
                            // HIGHLIGHT: Prepare for the initial decision prompt
                            this.battle.prepareForNewDecisionPrompt();
                        }
                        currentState = GameState.BATTLE;
                        currentFPS = normalFPS;
                        isOpeningPhase = false;
                        transitionStep = 0;
                        transitionTimer = 0;
                        openingStep = 0;
                    }
                }

                String snorlaxEnemyFightModelPath = "Pokemon/PokemonAssets/SNORLAX_FIGHTMODEL_ENEMY.png";
                String snorlaxAllyFightModelPath = "Pokemon/PokemonAssets/SNORLAX_FIGHTMODEL_ALLY.png";
                String snorlaxMiniModelPath = "Pokemon/PokemonAssets/SNORLAX_FIGHTMODEL_ALLY.png"; // Or actual mini

                MovesetParser movesetReader = new MovesetParser();

                Map<String, MoveData> moveset = new HashMap<>();

                try {
                    moveset = movesetReader.loadMovesetFromTxt("Pokemon/PokemonReader/PokemonMovesetList.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> movesetNames = new ArrayList<>(moveset.keySet());

                // Inside GamePanel.java, update() method, BATTLETRANSITION case

                PokemonMove[] moves = new PokemonMove[4];
                for (int j = 0; j < moves.length; j++) {
                    // Correctly assign the returned PokemonMove object
                    moves[j] = PokemonMove.loadPokemonMoveByType(moveset.get(movesetNames.get(j))); // Changed line
                    System.out.println("moves[" + j + "]: "
                            + (moves[j] != null ? moves[j].toString() : "null -> " + moveset.get(movesetNames.get(j)))); // For
                                                                                                                         // debugging
                    if (moves[j] == null) {
                        System.err.println("Warning: Pokemon move at index " + j
                                + " is null after loading. Check MovesetList.txt and PokemonMove.java. MoveData: "
                                + moveset.get(movesetNames.get(j)));
                        // Consider assigning a default move like "Struggle" or skipping
                        // For example, to prevent nulls, you could assign a basic move:
                        // moves[j] = new PokemonMove_PHYSICAL_ATTACK("Struggle", 1, "A desperate
                        // attack.", 50, 1.0, PokemonMoveType.ATTACK, PokemonMoveCategory.PHYSICAL);
                    }
                }

                PokemonType[] snorlaxType = { PokemonType.NORMAL, PokemonType.FIRE }; // Pokemon constructor takes
                                                                                      // PokemonType[]
                // Make sure PokemonType.FIRE is defined if you meant to use it, or remove if
                // Snorlax is only Normal.
                // PokemonType[] snorlaxType = { PokemonType.NORMAL, PokemonType.FIRE };

                Pokemon playerPokemon = new Pokemon("SNORLAX", snorlaxType, 1, 80, 50, 45, 35, 60, 60,
                        snorlaxMiniModelPath, snorlaxAllyFightModelPath, snorlaxEnemyFightModelPath, moves);
                // Scale models if necessary (consider doing this once when Pokemon is loaded,
                // not every battle start)
                playerPokemon.setAllyFightModel(Battle.scaleImage(playerPokemon.getAllyFightModel(), 2.0));
                playerPokemon.setEnemyFightModel(Battle.scaleImage(playerPokemon.getEnemyFightModel(), 2.0));

                Pokemon enemyPokemon = new Pokemon("SNORLAX", snorlaxType, 1, 80, 50, 45, 35, 60, 60,
                        snorlaxMiniModelPath, snorlaxAllyFightModelPath, snorlaxEnemyFightModelPath);
                enemyPokemon.setAllyFightModel(Battle.scaleImage(enemyPokemon.getAllyFightModel(), 2.0));
                enemyPokemon.setEnemyFightModel(Battle.scaleImage(enemyPokemon.getEnemyFightModel(), 2.0));

                Pokemon[] playerCards = { playerPokemon }; // Example for wild battle constructor
                // Pokemon[] enemyCards = { enemyPokemon }; // Example for trainer battle

                // Assuming a wild battle for this Snorlax example:
                this.battle = new Battle(playerCards, playerCards);
                // Or for a trainer battle:
                // this.battle = new Battle(playerCards, enemyCards);

                battleState = BattleState.BATTLE_DECISION;
                // HIGHLIGHT: Reset textbox state when battle starts and it's decision time
                if (this.battle != null) {
                    this.battle.resetTextBoxStateForNewTurn();
                }
                currentState = GameState.BATTLE;
                currentFPS = normalFPS;
                // Reset transition flags
                isOpeningPhase = false;
                transitionStep = 0;
                transitionTimer = 0;
                openingStep = 0;

                break;
            case OVERWORLDTRANSITION:
                break;
            case BATTLE:
                if (this.battle == null) { // Safety check
                    System.err.println("Error: Battle object is null in BATTLE state. Reverting to OVERWORLD.");
                    currentState = GameState.OVERWORLD;
                    return;
                }
                battle.update();
                switch (battleState) {
                    case BATTLE_DECISION:

                        if (this.battle.optionBox != null) { //
                            if (keyI.upPressed) {
                                this.battle.menuOptionBoxMoveUp();
                                keyI.upPressed = false;
                            } //
                            if (keyI.downPressed) {
                                this.battle.menuOptionBoxMoveDown();
                                keyI.downPressed = false;
                            } //
                            if (keyI.leftPressed) {
                                this.battle.menuOptionBoxMoveLeft();
                                keyI.leftPressed = false;
                            } //
                            if (keyI.rightPressed) {
                                this.battle.menuOptionBoxMoveRight();
                                keyI.rightPressed = false;
                            } //

                            if (keyI.enterPressed) { //
                                keyI.enterPressed = false;
                                String selectedOption = this.battle.optionBox.select(); //
                                System.out.println("Battle action selected: " + selectedOption);

                                this.battle.resetTextBoxStateForNewTurn();

                                if (selectedOption.equalsIgnoreCase("Fight")) {
                                    System.out.println("Fight selected - Starting battle!");
                                    battleState = BattleState.BATTLE_SELECTMOVE;
                                    this.battle.optionBox.setVisible(false);
                                    // Potentially show moves selection menu here, which might use the textbox or
                                    // another UI
                                } else if (selectedOption.equalsIgnoreCase("Bag")) {
                                    System.out.println("Bag selected - This feature is not ready yet!");
                                    battleState = BattleState.BATTLE_ITEM;
                                    this.battle.optionBox.setVisible(false);
                                } else if (selectedOption.equalsIgnoreCase("Run")) {
                                    System.out.println("Run selected - Attempting to flee!");
                                    battle.getMainPlayerPokemon().decrementDefenseBoostTurns(); // Clear any active
                                                                                                // buffs
                                    battle.getMainEnemyPokemon().decrementDefenseBoostTurns();
                                    currentState = GameState.OVERWORLD;
                                    // Potentially stop battle music, etc.
                                } else if (selectedOption.equalsIgnoreCase("PkMn")) {
                                    System.out.println("Pokemon selected - Switch Pokemon logic needed.");
                                    battleState = BattleState.BATTLE_SWITCH;
                                    this.battle.optionBox.setVisible(false);
                                }
                            }
                        }
                        break;

                    case BATTLE_SELECTMOVE:
                        if (this.battle.movesBox != null) {
                            if (keyI.upPressed) {
                                this.battle.movesBox.moveUp();
                                keyI.upPressed = false;
                            }
                            if (keyI.downPressed) {
                                this.battle.movesBox.moveDown();
                                keyI.downPressed = false;
                            }
                            if (keyI.leftPressed) {
                                this.battle.movesBox.moveLeft();
                                keyI.leftPressed = false;
                            }
                            if (keyI.rightPressed) {
                                this.battle.movesBox.moveRight();
                                keyI.rightPressed = false;
                            }
                            if (keyI.enterPressed) {
                                keyI.enterPressed = false;
                                this.battle.movesBox.select();

                                System.out.println("Selected move: " + this.battle.movesBox.select());

                                this.battle.resetTextBoxStateForNewTurn();

                                for (PokemonMove move : this.battle.getMainPlayerPokemon().getMoves()) {
                                    if (move.getMoveName().equalsIgnoreCase(this.battle.movesBox.select())) {
                                        selectedMove = move;
                                        break;
                                    }
                                }

                                selectedMoveInfo = battle.executeAttemptedMove(selectedMove);

                                if (selectedMoveInfo.containsKey("error")) {
                                    System.out.println("Error: " + selectedMoveInfo.get("error"));
                                    battleState = BattleState.BATTLE_DECISION;
                                }

                                battleState = BattleState.BATTLE_ALLYMOVE;
                            }
                        }
                        break;
                    case BATTLE_ALLYMOVE:
                        // Display result of ally move, wait for player to press Enter/Confirm
                        if (keyI.enterPressed && allyMoveExecuted) {
                            keyI.enterPressed = false;
                            allyMoveExecuted = false;

                            // HIGHLIGHT: Decrement player's temporary defense buff after their turn if it
                            // was a defense move that turn.
                            // More accurately, any active buff should tick down.
                            battle.getMainPlayerPokemon().decrementDefenseBoostTurns();

                            if (battle.getMainEnemyPokemon().getHp() <= 0) {
                                battle.setNewDialog(battle.getMainEnemyPokemon().getName().toUpperCase() + " fainted!",
                                        g2);
                                // Add logic for battle end, EXP gain, etc.
                                // For now, transition back to overworld
                                currentState = GameState.OVERWORLDTRANSITION; // Placeholder for proper win state
                                break;
                            }

                            // Enemy's turn
                            PokemonMove enemySelectedMove = battle.getMainEnemyPokemon().getMove(0); // Simple: enemy
                                                                                                     // always uses
                                                                                                     // first move
                            if (enemySelectedMove != null) {
                                enemyMoveResult = battle.executeAttemptedMove(enemySelectedMove); // This needs to be
                                                                                                  // adapted for enemy
                                // The executeAttemptedMove is player-centric. We need a version for enemy or
                                // adapt it.
                                // For now, let's assume a similar structure for enemy's move execution for
                                // simplicity
                                // This would ideally be:
                                // enemyMoveResult = enemySelectedMove.move(battle.getMainEnemyPokemon(),
                                // battle.getMainPlayerPokemon());
                                // And then process the result.
                                // Simplified for now:
                                Pokemon enemy = battle.getMainEnemyPokemon();
                                Pokemon playerTarget = battle.getMainPlayerPokemon();
                                if (enemySelectedMove.getMoveType() == PokemonMoveType.ATTACK
                                        || enemySelectedMove.getMoveType() == PokemonMoveType.SPECIAL_ATTACK
                                        || enemySelectedMove.getMoveType() == PokemonMoveType.DEBUFF) {
                                    enemyMoveResult = enemySelectedMove.move(enemy, playerTarget);
                                } else if (enemySelectedMove.getMoveType() == PokemonMoveType.BUFF
                                        || enemySelectedMove.getMoveType() == PokemonMoveType.DEFENSE) {
                                    enemyMoveResult = enemySelectedMove.move(enemy);
                                }

                                battle.setNewDialog(
                                        enemyMoveResult.getOrDefault("message", battle.getMainEnemyPokemon().getName()
                                                + " used " + enemySelectedMove.getMoveName() + "!"),
                                        g2);
                            } else {
                                battle.setNewDialog(battle.getMainEnemyPokemon().getName() + " has no moves!", g2);
                            }
                            enemyMoveExecuted = true;
                            battleState = BattleState.BATTLE_ENEMYMOVE;
                        }
                        break;

                    case BATTLE_ENEMYMOVE:
                        // Display result of enemy move, wait for player to press Enter/Confirm
                        if (keyI.enterPressed && enemyMoveExecuted) {
                            keyI.enterPressed = false;
                            enemyMoveExecuted = false;

                            // HIGHLIGHT: Decrement enemy's temporary defense buff after their turn
                            battle.getMainEnemyPokemon().decrementDefenseBoostTurns();

                            if (battle.getMainPlayerPokemon().getHp() <= 0) {
                                battle.setNewDialog(
                                        battle.getMainPlayerPokemon().getName().toUpperCase() + " fainted! Game Over!",
                                        g2);
                                // Add logic for game over
                                currentState = GameState.MAINMENU; // Placeholder for game over state
                                break;
                            }
                            battle.prepareForNewDecisionPrompt(); // Prepare for player's next decision
                            battleState = BattleState.BATTLE_DECISION;
                        }
                        break;
                    // Add cases for BATTLE_ITEM, BATTLE_SWITCH, BATTLE_RUN
                }
                break;
            // Add logic for other battle states (BATTLE_SELECTMOVE, BATTLE_ALLYMOVE, etc.)
            // e.g., if (battleState == BattleState.BATTLE_SELECTMOVE &&
            // this.battle.MovesBox != null) { ... }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
                npc.draw(g2);
                player.draw(g2);
                if (textBox.isVisible()) {
                    textBox.draw(g2, screenWidth, screenHeight);
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
                if (this.battle == null) { // Safety check
                    g2.setColor(Color.RED);
                    if (pokemonFont != null)
                        g2.setFont(pokemonFont.deriveFont(20f));
                    else
                        g2.setFont(new Font("Arial", Font.BOLD, 20));
                    g2.drawString("Error: Battle not initialized!", 50, getHeight() / 2);
                    return;
                }

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                this.battle.draw(g2, battleState, screenWidth, screenHeight);
                // g2.setColor(Color.BLACK);
                // g2.setFont(new Font("Arial", Font.BOLD, 20));
                // g2.drawString("Battle Start!", 100, 100);
                break;
        }
        if (g2 != null) { // Ensure g2 is not null before disposing
           g2.dispose(); // Dispose should be handled by Swing's repaint mechanism, not manually here.
        }
    }

    public void startBattle(Graphics g) {
        currentState = GameState.BATTLETRANSITION;
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());

    }

    public static void playSound(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
