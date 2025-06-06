package GuiTileMapThing;

import PlayerNPCgitu.NPC;
import PlayerNPCgitu.Player;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonFactory;
import Pokemon.PokemonMoveFactory;
import Pokemon.PokemonReader.*;
import java.awt.*;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    // game screen
    private String tutorDiag ="Selamat Datang!" + "%%PAGEBREAK%%" + "Sayangnya... Game ini masin incomplete" + "%%PAGEBREAK%%" + "Jadi untuk sekarang anda akan memilih 6 Pokemon secara langsung." + "%%PAGEBREAK%%" + "Level dan Move Pokemon akan di Random, jadi semoga beruntung!";
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
    private SaveSlot saveSlotMenu = new SaveSlot(screenWidth,screenHeight);
    private int selectedSaveSlot;
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
    //Factory
    public PokemonFactory pf;
    public PokemonMoveFactory pmf;
    // other classes
    private static Clip clip;
    NpcSpawnManager nsm;
    KeyInput keyI = new KeyInput();
    Thread gameThread;
    Thread openAudio;
    TileManager tileManager = new TileManager(this);
    public Player player = new Player(this, keyI);
    public java.util.List<NPC> npcList = new ArrayList<>();
    NPC npc = new NPC(this, "Oak", 1080, 500, "down", "Oek");
    Battle battle;
    public Graphics2D g2;
    // gameSTATES
    private GameState currentState = GameState.SPLASH;
    BattleState battleState = BattleState.BATTLE_DECISION;
    BattleState nextBattleState;
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
    private MenuWithSelection confirmation;
    public TextBox textBox;
    private SaveSlot saveSlot;
    private boolean shouldShowInitialText = true;
    private boolean conf;
    private boolean isTutorial;
    private boolean isDoneTyping = false;
    //
    private boolean isFadingToBlack = true;
    private int fadeAlpha = 0;
    private final int FADE_SPEED = 8;
    private GameState stateBeforeTransition;
    private long lastInputTime = 0;
    private final long inputDelay = 150;
    private SaveSlotSubState currentSaveSlotSubState = SaveSlotSubState.SELECTING_SLOT;
    private StringBuilder currentName = new StringBuilder(); 
    private final int MAX_NAME_LENGTH = 7; 
    private boolean newGame;
    private boolean isOpeningPlayed;
    private PokemonMove selectedMove = null;
    private Map<String, String> selectedMoveInfo = new HashMap<>();
    private boolean isBattleMessage =false;
    private ArrayList<String> battleMessageQueue = new ArrayList<>();
    private boolean moveExecutedThisTurn = false;
    //options
    String[][] alphabetOptions = { { "a", "b", "c", "d", "e", "f" },
                { "g", "h", "i", "j", "k", "l" },
                { "m", "n", "o", "p", "q", "r" },
                { "s", "t", "u", "v", "w", "x" },
                { "y", "z", "1", "2", "3", "4" },
                { "5", "6", "7", "8", "9", "bck" },
                { "0", "?", "1" ,"!", "Up", "ok"},
        };
        String [][]ALPHABETOptions = { { "A", "B", "C", "D", "E", "F" },
            { "G", "H", "I", "J", "K", "L" },
            { "M", "N", "O", "P", "Q", "R" },
            { "S", "T", "U", "V", "W", "X" },
            { "Y", "Z", "1", "2", "3", "4" },
            { "5", "6", "7", "8", "9", "bck" },
            { "0", "?", "1" ,"!", "Lw", "ok"},
    };
        private MenuWithSelection pauseMenu;
        private String saveMessageText = "";
        private int saveMessageTimer = 0;
        private enum PauseSubState {
        MAIN,           
        CONFIRM_EXIT   
    }
    private PauseSubState pauseSubState = PauseSubState.MAIN;
    public GamePanel() {
        try {
            pmf = new PokemonMoveFactory(MovesetParser.loadMovesetFromTxt());
            pf= new PokemonFactory(PokemonListParser.loadPokemonFromTxt(),pmf);
            nsm = new NpcSpawnManager(this);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        npcList.add(npc);
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.DARK_GRAY);
        addKeyListener(keyI);
        setFocusable(true);
        requestFocusInWindow();
        splashLogo = new ImageIcon("pokemon_logo.png").getImage();
        mainMenuBG = new ImageIcon("bg_menu.png").getImage();
        titleScreenImage = new ImageIcon("TileGambar/FilkomEd_title2.png").getImage();
        String[] menuOptions = { "New Game", "Load Game", "Settings" };
        mainMenu = new MenuWithSelection(menuOptions, 140, 290, 28f);
        mainMenu.setVisible(false);
        nameSelect = new MenuWithSelection(alphabetOptions, 20, 160, 24f, 490.0, 340);
        String[] yesNo = {"Yes", "No"};
        confirmation = new MenuWithSelection(yesNo, 400, 300, 20f);
        textBox = new TextBox();
        String[] pauseOptions = { "Resume", "Save", "Exit to Main Menu" };
        pauseMenu = new MenuWithSelection(pauseOptions, 80, 200, 28f);
        pauseMenu.setVisible(false);
        try {
            pokemonFont = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(28f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pokemonFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            pokemonFont = new Font("Arial", Font.BOLD, 12);
        }
        renewNPC();
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
                    openAudio = new Thread(() -> playSound("../Pokemon/audioSave/Open.wav"));
                    openAudio.start();
                    isOpeningPlayed = true;
                }
                splashTimer++;
                if (splashTimer >= splashDuration) {
                    currentState = GameState.MAINMENU;
                    mainMenuFadeTimer = 0;
                }
                break; // Added break for consistency

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
                mainMenu.setVisible(false);
                saveSlotMenu.setVisible(true);

                if (currentSaveSlotSubState == SaveSlotSubState.SELECTING_SLOT) {
                    if (keyI.upPressed) {
                        saveSlotMenu.moveUp();
                        keyI.upPressed = false;
                    }
                    if (keyI.downPressed) {
                        saveSlotMenu.moveDown();
                        keyI.downPressed = false;
                    }
                    if (keyI.escPressed) {
                        keyI.escPressed = false;
                        currentSaveSlotSubState = SaveSlotSubState.SELECTING_SLOT;
                        saveSlotMenu.setVisible(false);
                        textBox.notVisible();
                        confirmation.setVisible(false);
                        currentState = GameState.MAINMENU;
                        mainMenu.setVisible(true);
                        break;
                    }
                    if (keyI.enterPressed) {
                        keyI.enterPressed = false;
                        selectedSaveSlot = saveSlotMenu.getSelectedSlot();

                        if (saveSlotMenu.checkSelectedSlot(selectedSaveSlot+1)) {
                            if (isNewGame) {
                                textBox.show();
                                textBox.setText("Overwrite data in Slot " + (selectedSaveSlot+1) + "?");
                                textBox.setVisible();
                                confirmation.setVisible(true);
                                currentSaveSlotSubState = SaveSlotSubState.CONFIRMING_OVERWRITE;
                            } else {
                                System.out.println("Loading game from slot " + (selectedSaveSlot));
                                stateBeforeTransition = GameState.SAVESLOT; // Remember where we came from
                                currentState = GameState.OVERWORLDTRANSITION;
                                nsm.spawnRandomNPCs(6);
                                player.loadGame(selectedSaveSlot+1);
                                isFadingToBlack = true;
                                fadeAlpha = 0;
                                saveSlotMenu.setVisible(false);
                                textBox.notVisible();
                                confirmation.setVisible(false);
                            }
                        } else {
                            if (isNewGame) {
                                System.out.println("Starting new game in empty slot " + (selectedSaveSlot));
                                currentState = GameState.NAMINGPLAYER;
                                saveSlotMenu.setVisible(false);
                                textBox.notVisible();
                                confirmation.setVisible(false);
                                player.generateRandomTeam();
                            } else {
                                textBox.setText("Slot " + (selectedSaveSlot+1) + " is empty. Cannot load.");
                                textBox.setVisible();
                            }
                        }
                    }
                } else if (currentSaveSlotSubState == SaveSlotSubState.CONFIRMING_OVERWRITE) {
                    if (keyI.upPressed) {
                        confirmation.moveUp();
                        keyI.upPressed = false;
                    }
                    if (keyI.downPressed) {
                        confirmation.moveDown();
                        keyI.downPressed = false;
                    }
                    if (keyI.escPressed) {
                        keyI.escPressed = false;
                        textBox.notVisible();
                        confirmation.setVisible(false);
                        currentSaveSlotSubState = SaveSlotSubState.SELECTING_SLOT; // Go back to selecting slot
                    }
                    if (keyI.enterPressed) {
                        keyI.enterPressed = false;
                        String choice = confirmation.select();

                        if (choice.equals("Yes")) {
                            System.out.println("Overwrite confirmed for slot " + (selectedSaveSlot));
                            currentState = GameState.NAMINGPLAYER;
                            saveSlotMenu.setVisible(false);
                        } else {
                            System.out.println("Overwrite cancelled for slot " + (selectedSaveSlot));
                            saveSlotMenu.setVisible(true);
                        }
                        textBox.notVisible();
                        confirmation.setVisible(false);
                        currentSaveSlotSubState = SaveSlotSubState.SELECTING_SLOT;
                    }
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
                    long now = System.currentTimeMillis();

                    if (now - lastInputTime >= inputDelay) {
                        String selected = nameSelect.getSelectedSelection();

                        if (selected.equals("Up")) {
                            nameSelect.setOptions(this.ALPHABETOptions);
                        }
                        else if (selected.equals("Lw")) {
                            nameSelect.setOptions(this.alphabetOptions);
                        }
                        else if (selected.equals("bck")) {
                            if (currentName.length() > 0) {
                                currentName.deleteCharAt(currentName.length() - 1);
                            }
                        }
                        else if(selected.equals("ok")){
                            currentState = GameState.TUTORIAL;
                            isTutorial = false;
                            textBox.reset();
                            textBox.setVisible();
                            player.setName(currentName.toString());
                            currentName.setLength(0);
                        }
                        else {
                            if (currentName.length() < MAX_NAME_LENGTH) {
                                currentName.append(selected);
                            }
                        }
                        lastInputTime = now;
                    }
                }
                break;
            case TUTORIAL:

                if (keyI.enterPressed) {
                    long now = System.currentTimeMillis();
                    if (now - lastInputTime >= inputDelay) {
                        handleTextBoxEnterLogic();
                        if(isDoneTyping){
                            stateBeforeTransition = GameState.TUTORIAL; // Remember where we came from
                            currentState = GameState.OVERWORLDTRANSITION;
                            isFadingToBlack = true;
                            fadeAlpha = 0;
                            isDoneTyping = false;
                            lastInputTime = now;
                        }
                    }
                }
                break;
            case OVERWORLD:
                switch (overworldState) {
                    case OVERWORLD_ROAM:
                        player.update();

                        if (keyI.spacePressed) { // Keep this for testing if you want
                            currentState = GameState.BATTLETRANSITION;
                            currentFPS = transitionFPS;
                            transitionStep = 0;
                            transitionTimer = 0;
                        }
                        if (keyI.escPressed) {
                            currentState = GameState.PAUSED;
                            pauseSubState = PauseSubState.MAIN;
                            pauseMenu.setVisible(true);
                            keyI.escPressed = false;
                        }
                        // --- FIX START ---
                        if (keyI.enterPressed) {
                            for (NPC npcs : npcList) {
                                // Check if player is facing and is close to the NPC
                                if (npcs.isPlayerInRange()) {
                                    System.out.println("SUCCESS: Player is in range of " + npc.name);
                                    // Make sure the NPC has pokemon to battle
                                    if(npc.getPokemonList() != null ) {
                                        this.battle = new Battle(player.getPokemonList(), npcs.getPokemonList(), textBox);
                                        currentState = GameState.BATTLETRANSITION;
                                        break; 
                                    }
                                }
                            }
                            // Reset the key press to prevent re-triggering
                            keyI.enterPressed = false;
                        }
                        // --- FIX END ---
                        break;

                    case OVERWORLD_INTERACTION:
                        player.update();
                        if (keyI.Fpressed) {
                            if (textBox.isVisible()) {
                                textBox.nextPage();
                                keyI.Fpressed = false;
                            }
                        }
                        break; // Added break for consistency
                }
                break;
            // --- FIX START ---
            case BATTLETRANSITION:
            currentFPS = transitionFPS;
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
                        // This is the correct way to transition to the battle
                        currentState = GameState.BATTLE;
                        battleState = BattleState.BATTLE_START; // Start with the intro messages
                        currentFPS = normalFPS;

                        // Reset all transition variables
                        isOpeningPhase = false;
                        transitionStep = 0;
                        transitionTimer = 0;
                        openingStep = 0;
                    }
                    currentFPS = normalFPS;
                }
                break;
            // --- FIX END ---
            case OVERWORLDTRANSITION:
                stopSound();
                if (isFadingToBlack) {
                    fadeAlpha += FADE_SPEED;
                    if (fadeAlpha >= 255) {
                        fadeAlpha = 255;
                        isFadingToBlack = false; // Switch to the fade-in phase
                    }
                } else {
                    fadeAlpha -= FADE_SPEED;
                    if (fadeAlpha <= 0) {
                        fadeAlpha = 0;
                        currentState = GameState.OVERWORLD; // Transition is complete
                    }
                }
                break;
            case BATTLE:
                if (battle == null) {
                    currentState = GameState.OVERWORLD;
                    return;
                }
                if (battle.isDisplayingDialog()) {
                    if (keyI.enterPressed) {
                        battle.acknowledgeDialog();
                        keyI.enterPressed = false;
                    }
                    return;
                }
                switch (battleState) {
                    case BATTLE_START:
                        Pokemon enemy = battle.getMainEnemyPokemon();
                        battleMessageQueue.add("Trainer " + npc.name + " wants to battle!"); // Or a generic wild message
                        battleMessageQueue.add(npc.name + " sent out " + enemy.getName() + "!");
                        battleMessageQueue.add("Go! " + battle.getMainPlayerPokemon().getName() + "!");
                        nextBattleState = BattleState.BATTLE_DECISION;
                        battleState = BattleState.BATTLE_MESSAGE;
                        break;
                    case BATTLE_MESSAGE:
                        if (!battleMessageQueue.isEmpty()) {
                            battle.setNewDialog(battleMessageQueue.remove(0));
                        } else {
                            if (nextBattleState == BattleState.BATTLE_DECISION && battle != null) {
                                battle.prepareForNewDecisionPrompt();
                            }
                            battleState = nextBattleState;
                        }
                        break;
                    case BATTLE_DECISION:
                        if (this.battle == null) {
                            System.err.println("Error: Battle object is null in BATTLE state. Reverting to OVERWORLD.");
                            currentState = GameState.OVERWORLD;
                            return;
                        }
                        if (this.battle.optionBox != null) {
                            if (keyI.upPressed) {
                                this.battle.menuOptionBoxMoveUp();
                                keyI.upPressed = false;
                            }
                            if (keyI.downPressed) {
                                this.battle.menuOptionBoxMoveDown();
                                keyI.downPressed = false;
                            }
                            if (keyI.leftPressed) {
                                this.battle.menuOptionBoxMoveLeft();
                                keyI.leftPressed = false;
                            }
                            if (keyI.rightPressed) {
                                this.battle.menuOptionBoxMoveRight();
                                keyI.rightPressed = false;
                            }

                            if (keyI.enterPressed) {
                                keyI.enterPressed = false;
                                String selectedOption = this.battle.optionBox.select();
                                System.out.println("Battle action selected: " + selectedOption);

                                this.battle.resetTextBoxStateForNewTurn();

                                if (selectedOption.equalsIgnoreCase("Fight")) {
                                    System.out.println("Fight selected - showing moves.");
                                    battleState = BattleState.BATTLE_SELECTMOVE;
                                    this.battle.optionBox.setVisible(false);
                                } else if (selectedOption.equalsIgnoreCase("Bag")) {
                                    System.out.println("Bag selected - This feature is not ready yet!");
                                    battleState = BattleState.BATTLE_ITEM;
                                    this.battle.optionBox.setVisible(false);
                                } else if (selectedOption.equalsIgnoreCase("Run")) {
                                    System.out.println("Run selected - Cannot flee a trainer battle!");
                                    // Optional: Add a message saying you can't run
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
                            if (keyI.escPressed) {
                                keyI.escPressed = false;
                                battleState = BattleState.BATTLE_DECISION;
                            }
                            if (keyI.enterPressed) {
                                keyI.enterPressed = false;
                                String selectedMoveName = this.battle.movesBox.select();
                                System.out.println("Selected move: " + selectedMoveName);
                                this.battle.resetTextBoxStateForNewTurn();

                                for (PokemonMove move : this.battle.getMainPlayerPokemon().getMoves()) {
                                    if (move.getMoveName().equalsIgnoreCase(selectedMoveName)) {
                                        selectedMove = move;
                                        break;
                                    }
                                }

                                if (selectedMove == null) {
                                    System.out.println("Error: Selected move not found in player's moves.");
                                    battleState = BattleState.BATTLE_DECISION;
                                    return;
                                }
                                battleState = BattleState.BATTLE_ALLYMOVE;
                            }
                        }
                        break;
                    case BATTLE_ALLYMOVE:
                        ArrayList<String> allyMoveMessages = battle.executeAttemptedMove(selectedMove,
                                this.battle.getMainPlayerPokemon(),
                                this.battle.getMainEnemyPokemon(),
                                battleState);
                        battleMessageQueue.addAll(allyMoveMessages);
                        nextBattleState = BattleState.BATTLE_ENEMYMOVE;

                        checkFaintAndBattleEndCondition();

                        battleState = BattleState.BATTLE_MESSAGE;
                        break;

                    case BATTLE_ENEMYMOVE:
                        ArrayList<String> message = battle.executeAttemptedMove(battle.rollEnemyMove(), this.battle.getMainEnemyPokemon(),
                                this.battle.getMainPlayerPokemon(), battleState);
                        battleMessageQueue.addAll(message);
                        nextBattleState = BattleState.BATTLE_DECISION;

                        checkFaintAndBattleEndCondition();

                        battleState = BattleState.BATTLE_MESSAGE;
                        break;
                    case BATTLE_SWITCH:
                        if (keyI.escPressed) {
                            keyI.escPressed = false;

                            battleState = BattleState.BATTLE_DECISION;
                        }
                        break;
                    case BATTLE_ITEM:
                        if (keyI.escPressed) {
                            keyI.escPressed = false;
                            battleState = BattleState.BATTLE_DECISION;
                        }
                        break;
                    case BATTLE_WIN:
                        battleMessageQueue.add("You defeated the opponent!");
                        battleMessageQueue.add("You gained some experience!");
                        nextBattleState = BattleState.BATTLE_END;
                        battleState = BattleState.BATTLE_MESSAGE;
                        break;
                    case BATTLE_LOSE:
                        battleMessageQueue.add("All your Pokémon have fainted!");
                        battleMessageQueue.add("You blacked out...");
                        nextBattleState = BattleState.BATTLE_END;
                        battleState = BattleState.BATTLE_MESSAGE;
                        break;
                    case BATTLE_END:
                        currentState = GameState.OVERWORLDTRANSITION;
                        break; // Added break for consistency
                }
                break; // BATTLE case break
            case PAUSED:
                pauseMenu.isVisible();
                if (saveMessageTimer > 0) {
                    saveMessageTimer--;
                }
                switch (pauseSubState) {
                    case MAIN:
                        if (keyI.upPressed) {
                            pauseMenu.moveUp();
                            keyI.upPressed = false;
                        }
                        if (keyI.downPressed) {
                            pauseMenu.moveDown();
                            keyI.downPressed = false;
                        }
                        if (keyI.enterPressed) {
                            String selected = pauseMenu.select();
                            if (selected.equals("Resume")) {
                                currentState = GameState.OVERWORLD;
                            } else if (selected.equals("Save")) {
                                player.saveGame(selectedSaveSlot+1);
                            } else if (selected.equals("Exit to Main Menu")) {
                                confirmation.setVisible(true);
                                pauseSubState = PauseSubState.CONFIRM_EXIT;
                            }
                            keyI.enterPressed = false;
                        }
                        break;

                    case CONFIRM_EXIT:
                        if (keyI.upPressed) {
                            confirmation.moveUp();
                            keyI.upPressed = false;
                        }
                        if (keyI.downPressed) {
                            confirmation.moveDown();
                            keyI.downPressed = false;
                        }
                        if (keyI.escPressed) {
                            pauseSubState = PauseSubState.MAIN;
                            keyI.escPressed = false;
                        }
                        if (keyI.enterPressed) {
                            String selected = confirmation.select();
                            if (selected.equals("Yes")) {
                                currentState = GameState.SPLASH;
                            } else {
                                pauseSubState = PauseSubState.MAIN;
                            }
                            keyI.enterPressed = false;
                        }
                        break;
                }
                break;
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
                saveSlotMenu.draw(g2);
                if (textBox.isVisible()) {
                    textBox.draw(g2, screenWidth, screenHeight);
                }
                if (confirmation.isVisible()) {
                    confirmation.draw(g2);
                }
                break;
            case NAMINGPLAYER:
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight()); 
                nameSelect.draw(g2);
                drawNamingScreen(g2);

                break;
             case TUTORIAL:
            if(!isTutorial){
                textBox.setText(tutorDiag);
                isTutorial = true;
            }
            if (textBox.isVisible()) {
                textBox.draw(g2, screenWidth, screenHeight);
            }
            break;
            case OVERWORLDTRANSITION:
                if (isFadingToBlack) {
                    if (stateBeforeTransition == GameState.SAVESLOT) {
                    saveSlotMenu.draw(g2);
                } else if (stateBeforeTransition == GameState.TUTORIAL) {
                if (textBox.isVisible()) {
                    textBox.draw(g2, screenWidth, screenHeight);
                }
            }
        } else {
                tileManager.draw(g2);
                for (NPC npc : npcList) {
                npc.draw(g2);
            }
        player.draw(g2);
    }
        originalComposite = g2.getComposite(); 
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) fadeAlpha / 255.0f));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setComposite(originalComposite); 
            break; 

            case OVERWORLD:
                tileManager.draw(g2);
            for (NPC npc : npcList) { 
                npc.draw(g2);
            }
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
            
                if (this.battle == null) {
                    g2.setColor(Color.RED);
                    this.battle.draw(g2, battleState, screenWidth, screenHeight);
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
                switch (battleState) {
                    case BATTLE_DECISION:
                        if (keyI.ePressed) {
                            switch (battle.optionBox.select()) {
                                case "Fight":
                                    battleState = BattleState.BATTLE_SELECTMOVE;
                                    break;
                                case "PkMn":
                                    battleState = BattleState.BATTLE_SWITCH;
                                    break;
                                case "Bag":
                                    battleState = BattleState.BATTLE_ITEM;
                                    break;
                                case "Run":
                                    battleState = BattleState.BATTLE_RUN;
                                    break;
                            }
                        }
                        break;
                    case BATTLE_ALLYMOVE:
                        break;
                    case BATTLE_ITEM:
                        break;
                    case BATTLE_SELECTMOVE:
                        if (keyI.ePressed) {
                            battle.optionBox.select();


                            battleState = BattleState.BATTLE_ALLYMOVE;
                        }
                        break;
                    case BATTLE_SWITCH:
                        break;
                    case BATTLE_RUN:
                        break;
                }
                break;
                case PAUSED:
                    tileManager.draw(g2);
                for (NPC npc : npcList) {
                    npc.draw(g2);
                }
                player.draw(g2);

                g2.setColor(new Color(0, 0, 0, 150)); // Black with ~60% opacity
                 g2.fillRect(0, 0, screenWidth, screenHeight);
                g2.setFont(pokemonFont.deriveFont(42f));
                g2.setColor(Color.WHITE);
    switch (pauseSubState) {
        case MAIN:
            g2.drawString("PAUSED", getWidth() / 2 - 80, 120);
            pauseMenu.draw(g2); 
            if (saveMessageTimer > 0) {
                g2.setFont(pokemonFont.deriveFont(24f));
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(saveMessageText);
                g2.drawString(saveMessageText, (getWidth() - textWidth) / 2, 350);
            }
            break;

        case CONFIRM_EXIT:
            g2.drawString("Are you sure?", getWidth() / 2 - 150, 180);
            confirmation.draw(g2);
            break;
    }
    break;
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
    public enum SaveSlotSubState {
        SELECTING_SLOT,
        CONFIRMING_OVERWRITE
    }
    private void drawNamingScreen(Graphics2D g2) {
    g2.setColor(Color.BLACK);
    g2.setFont(new Font("Monospaced", Font.BOLD, 24));
    g2.drawString("YOUR NAME?", 40, 50);

    g2.setFont(new Font("Monospaced", Font.PLAIN, 26));
    g2.drawString(currentName.toString(), 40, 90); 

    g2.setFont(new Font("Monospaced", Font.BOLD, 28));
    StringBuilder display = new StringBuilder();
    for (int i = 0; i < MAX_NAME_LENGTH; i++) {
        if (i < currentName.length()) {
            display.append(currentName.charAt(i)).append(" ");
        } else {
            display.append("_ ");
        }
    }
    g2.drawString(display.toString(), 40, 120); 
}
private void handleTextBoxEnterLogic() {
    if (!textBox.isDoneTyping()) {
        textBox.skipToFullPage();
    } else {
        textBox.nextPage();
    }
    if(textBox.isDoneTyping() && textBox.isLastPage() && !textBox.hasMorePages()){
        isDoneTyping = true;
    }
}
private boolean isTeamFainted(Pokemon[] team) {
    if (team == null) return true; 
    for (Pokemon p : team) {
        if (p != null && p.getHp() > 0) {
            return false; 
        }
    }
    return true;
}
private void checkFaintAndBattleEndCondition() {
    Pokemon playerPokemon = battle.getMainPlayerPokemon();
    Pokemon enemyPokemon = battle.getMainEnemyPokemon(); // The current active enemy

    // Check if the enemy fainted
    if (enemyPokemon != null && enemyPokemon.getHp() <= 0) {
        battleMessageQueue.add(enemyPokemon.getName() + " fainted!");
        
        // Check for victory
        if (isTeamFainted(battle.enemyPokemon)) {
            nextBattleState = BattleState.BATTLE_WIN;
        } else {
            nextBattleState = BattleState.BATTLE_DECISION;
        }
    }

    else if (playerPokemon != null && playerPokemon.getHp() <= 0) {
        battleMessageQueue.add(playerPokemon.getName() + " fainted!");

        if (isTeamFainted(battle.playerPokemons)) {
            nextBattleState = BattleState.BATTLE_LOSE;
        } else {

            battleMessageQueue.add("You must choose your next Pokemon!");
            nextBattleState = BattleState.BATTLE_SWITCH;
        }
    }
}
    public void renewNPC(){
        npcList.clear();
        nsm.spawnRandomNPCs(6);
        npcList.addAll(nsm.getnpcList());
    }
}
