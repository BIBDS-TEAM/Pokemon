package GuiTileMapThing;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveCategory;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// It's good practice to have ImageIO import if you were to load images directly here,
// but Pokemon class seems to handle its own image loading.
// import javax.imageio.ImageIO;

public class Battle {
    protected Pokemon playerPokemons[];
    protected Pokemon enemyPokemon[]; // For NPC trainer battles
    protected Pokemon wildPokemon; // For wild Pokemon encounters
    private boolean isNpcBattle; // Distinguishes between NPC trainer and wild battle

    protected Font font;

    public MenuWithSelection optionBox;
    public MenuWithSelection movesBox;
    public MenuWithSelectionWithAdd itemBagBox;

    private String[][] menuSelectDecision = new String[][] { { "Fight", "PkMn" }, { "Bag", "Run" } };
    private String[] menuSelectMoves;

    private TextBox battleTextBox;

    private String currentDialogMessage = "";
    private boolean hasNewDialogMessage = false;
    private boolean decisionPromptDisplayedThisTurn = false;

    // Coordinates for UI elements
    private final int allyPokemonHpBarX = 290;
    private final int allyPokemonHpBarY = 320;
    private final int enemyPokemonHpBarX = 50;
    private final int enemyPokemonHpBarY = 100;

    // --- FIX START: Swapped the sprite coordinates ---

    // The ally (player's) sprite should be at the bottom-left.
    private int allyPokemonSpriteX = 60;
    private int allyPokemonSpriteY = 230;

    // The enemy's sprite should be at the top-right.
    private int enemyPokemonSpriteX = 350;
    private int enemyPokemonSpriteY = 30;

    // --- FIX END ---

    private int battleMenuSelectionX = 225;
    private int battleMenuSelectionY = 363;
    private int battleMoveSelectionX = 235;
    private int battleMoveSelectionY = 348;

    private int battleTextBoxX = 50;
    private int battleTextBoxY = 360;
    private int battleTextBoxWidth = 512;
    private int battleTextBoxHeight = 128;

    private int battleMoveSelectionWidth = 350;
    private int battleMoveSelectionHeight = 150;
    
    private Pokemon pokemonToFlicker = null;
    private long flickerStartTime = 0;
    private final long FLICKER_DURATION = 500; 
    private float currentFlickerOpacity = 1.0f;
    private int currentFlickerXOffset = 0;
    private long lastFlickerStateUpdateTime = 0;
    private final int FLICKER_STATE_UPDATE_INTERVAL = 100; // ms, how often shake/opacity changes
    private boolean flickerShakeToggle = false; // Toggles between -AMOUNT and +AMOUNT
    private boolean flickerOpacityToggle = false; // Toggles between LOW and HIGH opacity

    private final int FLICKER_SHAKE_AMOUNT = 20;
    private final float FLICKER_LOW_OPACITY = 0.4f; // 1.0f (normal) - 0.6f (60% reduction)
    private final float FLICKER_HIGH_OPACITY = 1.0f;

    private ArrayList<String> currentBattleMessages = new ArrayList<>();
    private boolean battleActionPlayerTurn = true; 

    // Constructor for NPC Trainer battles
    public Battle(Pokemon[] playerPokemons, Pokemon[] enemyPokemon, TextBox sharedTextBox) {
         if (playerPokemons == null || playerPokemons.length < 1 || playerPokemons[0] == null) {
            throw new IllegalArgumentException("playerPokemons array cannot be null, empty, or have a null first Pokemon.");
        }
        if (enemyPokemon == null || enemyPokemon.length < 1 || enemyPokemon[0] == null) {
            throw new IllegalArgumentException("enemyPokemon array cannot be null, empty, or have a null first Pokemon for NPC battles.");
        }
        this.playerPokemons = playerPokemons;
        this.enemyPokemon = enemyPokemon;
        this.isNpcBattle = true;
        this.wildPokemon = null;

        // --- KEY CHANGES ---
        this.battleTextBox = sharedTextBox; // Use the shared textbox
        initializeAssets(); // Call your asset initializer
    }

    // Constructor for Wild Pokemon battles
    public Battle(Pokemon[] playerPokemon, Pokemon wildPokemon,TextBox sharedTextBox) {
        if (playerPokemons == null) {
            throw new IllegalArgumentException("playerPokemon cannot be null Pokemon.");
        }
        if (wildPokemon == null) {
            throw new IllegalArgumentException("wildPokemon cannot be null for wild battles.");
        }
        this.playerPokemons = playerPokemon;
        this.wildPokemon = wildPokemon;
        this.isNpcBattle = false;
        this.enemyPokemon = null;

        this.battleTextBox = sharedTextBox;
        initializeAssets();
    }

    public TextBox getBattleTextBox() {
        return battleTextBox;
    } // Getter method for battleTextBox

    public void setBattleTextBox(TextBox textBox) {
        this.battleTextBox = textBox;
    }

    private void initializeAssets() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("Font/Pokemon_Jadul.ttf")).deriveFont(16f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            font = new Font("Arial", Font.PLAIN, 16); // Fallback font
        }

        menuSelectMoves = new String[4];
        Pokemon mainPlayerPoke = getMainPlayerPokemon();
        if (mainPlayerPoke != null) {
            for (int i = 0; i < 4; i++) {
                 if(mainPlayerPoke.getMove(i) != null){
                    menuSelectMoves[i] = mainPlayerPoke.getMove(i).getMoveName();
                 } else {
                    menuSelectMoves[i] = "-"; // Placeholder for empty move slot
                 }
            }
        } else {
             for (int i = 0; i < 4; i++) menuSelectMoves[i] = "-"; // All placeholders if no Pokemon
        }


        this.optionBox = new MenuWithSelection(menuSelectDecision, battleMenuSelectionX, battleMenuSelectionY, 20f, 45, 50);
        this.movesBox = new MenuWithSelection(menuSelectMoves, battleMoveSelectionX, battleMoveSelectionY, 14f, 285.0, 145);
        // MovesBox and itemBagBox can be initialized when needed or here if always
        // shown initially.
        this.optionBox.setVisible(false); // GamePanel will control visibility
        this.movesBox.setVisible(false);
    }

    public Pokemon getMainEnemyPokemon() { 
        if (enemyPokemon != null && enemyPokemon.length > 0 && enemyPokemon[0] != null) {
            return enemyPokemon[0];
        }
        // Return a dummy or handle this case to avoid NullPointerExceptions
        if (wildPokemon != null) {
            return wildPokemon;
        }
        System.err.println("Warning: getMainEnemyPokemon() returning null.");
        return null;
    } // Getter method for EnemyPok

    public Pokemon getMainPlayerPokemon() {
        if (playerPokemons != null && playerPokemons.length > 0 && playerPokemons[0] != null) {
            return playerPokemons[0];
        }
        // Return a dummy or handle this case to avoid NullPointerExceptions
        System.err.println("Warning: getMainPlayerPokemon() returning null.");
        return null;
    }

    // This method determines the current opponent to draw.
    private Pokemon getCurrentOpponentForDrawing() {
        if (this.isNpcBattle) {
        return (this.enemyPokemon != null && this.enemyPokemon[0].getHp() > 0) ? this.enemyPokemon[0] : null;
    } else {
        return this.wildPokemon;
    }
}


    public void setMainEnemyPokemon(Pokemon pokemon) {
        if (enemyPokemon[0] != null && !isNpcBattle) {
            enemyPokemon[0] = pokemon;
        }
        if (wildPokemon != null) {
            wildPokemon = pokemon;
        }
        System.err.println("Warning: There are no main Enemy pokemon to set");
    }

    public void setMainPlayerPokemon(Pokemon pokemon) {
        if (playerPokemons[0] != null) {
            playerPokemons[0] = pokemon;
        }
        System.err.println("Warning: There are no main Player pokemon to set");
    }

    public void update() {

    }

    public void draw(Graphics2D g2, BattleState currentBattleState, int panelWidth, int panelHeight) {
    // --- ENEMY DRAWING LOGIC ---
    Pokemon currentEnemyToDraw = getCurrentOpponentForDrawing();
    if (currentEnemyToDraw != null) {
        drawPokemonSpriteWithIndex(g2, currentEnemyToDraw, 2); // 2 for enemy
        // CHANGE: Pass the 'currentEnemyToDraw' object, which is the active, non-fainted one.
        drawEnemyPokemonHpBar(g2, currentEnemyToDraw, 130, 20);
    } else {
        // This part for when all enemies are fainted is fine.
        if (font != null)
            g2.setFont(font.deriveFont(Font.PLAIN, 12f));
        else
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.GRAY);
        g2.drawString("No Opponent", enemyPokemonSpriteX + 10, enemyPokemonSpriteY + 30);
    }

    // --- ALLY DRAWING LOGIC ---
    Pokemon currentPlayerPkmn = getMainPlayerPokemon();
    // ADDED: A check to ensure we don't draw the UI for a fainted player Pokémon.
    if (currentPlayerPkmn != null && currentPlayerPkmn.getHp() > 0) {
        drawPokemonSpriteWithIndex(g2, currentPlayerPkmn, 1); // 1 for ally
        // CHANGE: Pass the 'currentPlayerPkmn' object to the HP bar method.
        drawAllyPokemonHpBar(g2, currentPlayerPkmn, 130, 20);
    } else {
        // This part for when the player's Pokémon is fainted is fine.
        if (font != null)
            g2.setFont(font.deriveFont(Font.PLAIN, 12f));
        else
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.GRAY);
        g2.drawString("No Ally", allyPokemonSpriteX + 10, allyPokemonSpriteY + 30);
    }

    // --- FLICKER AND OTHER UI LOGIC (This part is unchanged) ---
    if (pokemonToFlicker != null) {
        long elapsedTime = System.currentTimeMillis() - flickerStartTime;
        if (elapsedTime < FLICKER_DURATION) {
            String alliance = (pokemonToFlicker == getMainPlayerPokemon()) ? "ally" : "enemy";
            flickeringSprite(g2, pokemonToFlicker, alliance);
        } else {
            pokemonToFlicker = null;
        }
    }
    if (pokemonToFlicker != getMainEnemyPokemon()) {
        drawPokemonSpriteWithIndex(g2, getCurrentOpponentForDrawing(), 2);
    }
    if (pokemonToFlicker != getMainPlayerPokemon()) {
        drawPokemonSpriteWithIndex(g2, getMainPlayerPokemon(), 1);
    }
    drawBattleTextBox(g2, currentBattleState, panelWidth, panelHeight);
    if (currentBattleState == BattleState.BATTLE_DECISION) {
        movesBox.setVisible(false);
        if (optionBox != null) {
            optionBox.setVisible(true);
            drawBattleMenuSelection(g2, battleMenuSelectionX, battleMenuSelectionY);
        }
    } else if (currentBattleState == BattleState.BATTLE_SELECTMOVE) {
        optionBox.setVisible(false);
        if (movesBox != null) {
            movesBox.setVisible(true);
            drawBattleMovesBox(g2, battleMoveSelectionX, battleMoveSelectionY);
        }
    } else {
        if (optionBox != null) {
            optionBox.setVisible(false); // Hide optionBox in other states
        }
    }
}

    public void drawPokemonSpriteWithIndex(Graphics2D g2, Pokemon pokemon, int index) {
        int pokemonPosX, pokemonPosY;
        BufferedImage sprite = null;
        if (index == 1) { // Ally
            sprite = pokemon.getAllyFightModel();
            pokemonPosX = allyPokemonSpriteX;
            pokemonPosY = allyPokemonSpriteY;
        } else if (index == 2) { // Enemy
            sprite = pokemon.getEnemyFightModel();
            pokemonPosX = enemyPokemonSpriteX;
            pokemonPosY = enemyPokemonSpriteY;
        } else {
            System.err.println("Invalid sprite index for drawing: " + index);
            return;
        }

        if (sprite != null) {
            // Consider if scaling is needed and where it's best applied (once vs. every
            // frame)
            g2.drawImage(sprite, pokemonPosX, pokemonPosY, null);
        } else {
            System.err.println("Warning: Sprite for " + pokemon.getName() + " (index " + index + ") is null.");
            g2.setColor(Color.GRAY);
            g2.fillRect(pokemonPosX, pokemonPosY, 64, 64); // Placeholder
            g2.setColor(Color.BLACK);
            if (font != null)
                g2.setFont(font.deriveFont(Font.BOLD, 20f));
            else
                g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.drawString("?", pokemonPosX + 28, pokemonPosY + 38);
        }
    }

    // Reverted signature to (Graphics2D, int, int) as implied by error
    public void drawEnemyPokemonHpBar(Graphics2D g2, Pokemon pokemonToDraw, int width, int height) {
    // CHANGE: This method now accepts a Pokemon object directly.
    // REMOVED: The old logic that tried to find 'this.enemyPokemon[0]'.

    // If pokemonToDraw is null, we can't draw its HP bar.
    if (pokemonToDraw == null) {
        if (font != null)
            g2.setFont(font.deriveFont(Font.PLAIN, 12f));
        else
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.RED);
        g2.drawString("NO ENEMY TARGET", enemyPokemonHpBarX, enemyPokemonHpBarY + 10);
        return;
    }

    // Rest of the drawing logic using 'pokemonToDraw' (This part is unchanged)
    float hpRatio = (pokemonToDraw.getMaxHp() > 0) ? (float) pokemonToDraw.getHp() / pokemonToDraw.getMaxHp() : 0;
    hpRatio = Math.max(0, Math.min(1, hpRatio));

    Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

    int paddingX = enemyPokemonHpBarX - 14;
    int paddingY = enemyPokemonHpBarY - 18;
    int borderWidth = width + 36;
    int borderHeight = height + 16;

    g2.setColor(Color.BLACK);
    g2.fillRect(paddingX, paddingY, 6, borderHeight);
    g2.fillRect(paddingX + 2, paddingY + borderHeight - 1, borderWidth, 6);
    g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 7, 4, 3);
    g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 4, 10, 3);
    g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 1, 15, 3);
    g2.fillRect(paddingX + borderWidth, paddingY + borderHeight + 2, 19, 3);

    g2.setColor(Color.BLACK);
    if (font != null)
        g2.setFont(font.deriveFont(Font.BOLD, 12f));
    else
        g2.setFont(new Font("Arial", Font.BOLD, 12));
    g2.drawString("HP:", enemyPokemonHpBarX, enemyPokemonHpBarY);

    int hpBarActualWidth = borderWidth - 52;
    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(enemyPokemonHpBarX + 36, enemyPokemonHpBarY - 10, hpBarActualWidth, 12);

    g2.setColor(barColor);
    g2.fillRect(enemyPokemonHpBarX + 37, enemyPokemonHpBarY - 9, (int) ((hpBarActualWidth - 2) * hpRatio), 10);

    g2.setColor(Color.BLACK);
    g2.drawRect(enemyPokemonHpBarX + 36, enemyPokemonHpBarY - 10, hpBarActualWidth, 12);

    int nameAndLvlPadding = 24;
    String pokemonName = pokemonToDraw.getName();
    String pokemonLvl = "Lv." + pokemonToDraw.getLvl();
    g2.setColor(Color.BLACK);
    if (font != null)
        g2.setFont(font.deriveFont(Font.PLAIN, 18f));
    else
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
    g2.drawString(pokemonName, enemyPokemonHpBarX, enemyPokemonHpBarY - (nameAndLvlPadding * 9 / 5) - 4);

    if (font != null)
        g2.setFont(font.deriveFont(Font.BOLD, 14f));
    else
        g2.setFont(new Font("Arial", Font.BOLD, 14));
    g2.drawString(pokemonLvl, enemyPokemonHpBarX + 10, enemyPokemonHpBarY - (nameAndLvlPadding * 7 / 8) - 4);
}

    public void drawAllyPokemonHpBar(Graphics2D g2, Pokemon pokemonToDraw, int width, int height) {
    // CHANGE: This method now accepts a Pokemon object directly.
    // REMOVED: The line 'Pokemon pokemonToDraw = getMainPlayerPokemon();'

    if (pokemonToDraw == null) {
        if (font != null)
            g2.setFont(font.deriveFont(Font.PLAIN, 12f));
        else
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.RED);
        g2.drawString("NO ALLY TARGET", allyPokemonHpBarX, allyPokemonHpBarY + 10);
        return;
    }

    // The rest of this method's code is unchanged.
    float hpRatio = (pokemonToDraw.getMaxHp() > 0) ? (float) pokemonToDraw.getHp() / pokemonToDraw.getMaxHp() : 0;
    hpRatio = Math.max(0, Math.min(1, hpRatio));

    Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

    int borderWidth = width + 36;
    int borderHeight = height + 26;

    g2.setColor(Color.BLACK);
    g2.fillRect(allyPokemonHpBarX + 15, allyPokemonHpBarY - 7, 4, 3);
    g2.fillRect(allyPokemonHpBarX + 9, allyPokemonHpBarY - 4, 10, 3);
    g2.fillRect(allyPokemonHpBarX + 4, allyPokemonHpBarY - 1, 15, 3);
    g2.fillRect(allyPokemonHpBarX, allyPokemonHpBarY + 2, 19, 3);
    g2.fillRect(allyPokemonHpBarX + 19, allyPokemonHpBarY - 1, borderWidth, 6);
    g2.fillRect(allyPokemonHpBarX + borderWidth + 16, allyPokemonHpBarY - borderHeight + 1, 6, borderHeight);

    int barPosX = allyPokemonHpBarX + 52;
    int barPosY = allyPokemonHpBarY - 38;

    g2.setColor(Color.BLACK);
    if (font != null)
        g2.setFont(font.deriveFont(Font.BOLD, 12f));
    else
        g2.setFont(new Font("Arial", Font.BOLD, 12));
    g2.drawString("HP:", barPosX - 36, barPosY + 9);

    int hpBarActualWidth = borderWidth - 52;
    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(barPosX, barPosY - 2, hpBarActualWidth, 12);

    g2.setColor(barColor);
    g2.fillRect(barPosX + 1, barPosY - 1, (int) ((hpBarActualWidth - 2) * hpRatio), 10);

    g2.setColor(Color.BLACK);
    g2.drawRect(barPosX, barPosY - 2, hpBarActualWidth, 12);

    String hpStats = pokemonToDraw.getHp() + "/" + pokemonToDraw.getMaxHp();
    if (font != null)
        g2.setFont(font.deriveFont(Font.BOLD, 14f));
    else
        g2.setFont(new Font("Arial", Font.BOLD, 14));
    int hpStatsWidth = g2.getFontMetrics().stringWidth(hpStats);
    g2.drawString(hpStats, barPosX + hpBarActualWidth - hpStatsWidth - 5, barPosY + 25);

    int nameAndLvlPadding = 10;
    g2.setColor(Color.BLACK);
    if (font != null)
        g2.setFont(font.deriveFont(Font.PLAIN, 18f));
    else
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
    g2.drawString(pokemonToDraw.getName(), allyPokemonHpBarX + 20, barPosY - (nameAndLvlPadding * 3) - 4);

    if (font != null)
        g2.setFont(font.deriveFont(Font.BOLD, 14f));
    else
        g2.setFont(new Font("Arial", Font.BOLD, 14));
    g2.drawString("Lv." + pokemonToDraw.getLvl(), allyPokemonHpBarX + width - 30, barPosY - nameAndLvlPadding - 4);
}

    public void flickeringSprite(Graphics2D g2, Pokemon pokemon, String pokemonAlliance) {
        if (g2 == null) throw new NullPointerException("Graphics2D g2 cannot be null");
        if (pokemon == null) {
            return;
        }
        if (pokemonAlliance == null
                || (!pokemonAlliance.equalsIgnoreCase("ally") && !pokemonAlliance.equalsIgnoreCase("enemy"))) {
            throw new IllegalArgumentException(
                    "Invalid pokemon alliance for flickering: " + pokemonAlliance + ". Must be 'ally' or 'enemy'.");
        }

        long currentTime = System.currentTimeMillis();

        // Update flicker state (shake and opacity) periodically
        if (currentTime - lastFlickerStateUpdateTime > FLICKER_STATE_UPDATE_INTERVAL) {
            flickerShakeToggle = !flickerShakeToggle;
            flickerOpacityToggle = !flickerOpacityToggle;

            currentFlickerXOffset = flickerShakeToggle ? FLICKER_SHAKE_AMOUNT : -FLICKER_SHAKE_AMOUNT;
            currentFlickerOpacity = flickerOpacityToggle ? FLICKER_HIGH_OPACITY : FLICKER_LOW_OPACITY;

            lastFlickerStateUpdateTime = currentTime;
        }

        // Determine overall visibility (the "blink" effect)
        // Sprite is visible for 250ms, then invisible for 250ms (500ms cycle)
        boolean isSpriteCurrentlyVisible = (currentTime % 500) < 250;

        if (!isSpriteCurrentlyVisible) {
            return; // Don't draw if in the "off" part of the blink
        }

        // Determine sprite image and base position
        int basePosX, basePosY;
        BufferedImage sprite = null;
        if (pokemonAlliance.equalsIgnoreCase("ally")) {
            sprite = pokemon.getAllyFightModel();
            basePosX = allyPokemonSpriteX;
            basePosY = allyPokemonSpriteY;
        } else { // enemy
            sprite = pokemon.getEnemyFightModel();
            basePosX = enemyPokemonSpriteX;
            basePosY = enemyPokemonSpriteY;
        }

        if (sprite != null) {
            Graphics2D g2dCopy = (Graphics2D) g2.create();

            // Apply opacity
            g2dCopy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentFlickerOpacity));

            // Apply X offset for shaking and draw
            g2dCopy.drawImage(sprite, basePosX + currentFlickerXOffset, basePosY, null);

            g2dCopy.dispose();
        } else {
            // Fallback for null sprite
            System.err.println("Warning: Sprite for " + pokemon.getName() + " (" + pokemonAlliance + ") is null in flickeringSprite.");
            // Optional: Draw a placeholder like in drawPokemonSpriteWithIndex if sprite is null
            g2.setColor(Color.GRAY);
            int placeholderX = pokemonAlliance.equalsIgnoreCase("ally") ? allyPokemonSpriteX : enemyPokemonSpriteX;
            int placeholderY = pokemonAlliance.equalsIgnoreCase("ally") ? allyPokemonSpriteY : enemyPokemonSpriteY;
            // Also apply flicker offset to placeholder
            g2.fillRect(placeholderX + currentFlickerXOffset, placeholderY, 64, 64);
            g2.setColor(Color.BLACK);
            Font placeholderFont = (font != null) ? font.deriveFont(Font.BOLD, 20f) : new Font("Arial", Font.BOLD, 20);
            g2.setFont(placeholderFont);
            g2.drawString("?", placeholderX + currentFlickerXOffset + 28, placeholderY + 38);
        }
    }

    public static BufferedImage scaleImage(BufferedImage originalImage, double scale) {
        if (originalImage == null || scale <= 0) {
            throw new IllegalArgumentException("Original image cannot be null.");
        }

        int scaledWidth = (int) (originalImage.getWidth() * scale);
        int scaledHeight = (int) (originalImage.getHeight() * scale);

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, originalImage.getType());

        Graphics2D g2 = scaledImage.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g2.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);

        g2.dispose();

        return scaledImage;
    }

     public void setNewDialog(String message) {
        this.currentDialogMessage = message;
        this.hasNewDialogMessage = true;
        if (this.battleTextBox != null) {
            this.battleTextBox.setText(message); 
            this.battleTextBox.show();
        }
    }
    public void acknowledgeDialog() {
    if (this.hasNewDialogMessage) {
        this.hasNewDialogMessage = false;
        this.battleTextBox.setText(""); 
    }
}

// Also add a getter to check the flag from your input handler
public boolean isDisplayingDialog() {
    return this.hasNewDialogMessage;
}

    public void prepareForNewDecisionPrompt() {
        this.decisionPromptDisplayedThisTurn = false;
        if (this.optionBox != null) {
            this.optionBox.setVisible(true); // Make sure menu is ready
        }
         if (this.battleTextBox != null) {
            
        }
    }

    public ArrayList<String> executeAttemptedMove(PokemonMove move, Pokemon user, Pokemon target, BattleState battleState) {
    if (target == null) {
        ArrayList<String> messages = new ArrayList<>();
        messages.add(user.getName() + "'s attack missed!");
        // Or handle it in another way that makes sense for your game.
        return messages;
    }

    // Create temporary copies to calculate move effects without immediately changing the originals
    Pokemon[] pokemonTemp = { new Pokemon(user), new Pokemon(target) };
    
    ArrayList<String> messages = new ArrayList<String>();

    int dmgToUser, dmgToTarget, buffToUser = 0;
    String buffedStats = "";

    PokemonMoveCategory moveType = move.getMoveCategory();

    // The move method calculates the result and returns the modified temporary Pokemon
    pokemonTemp = move.move(pokemonTemp[0], pokemonTemp[1]);

    // Calculate damage based on the difference in HP before and after the move
    dmgToUser = user.getHp() - pokemonTemp[0].getHp();
    dmgToTarget = target.getHp() - pokemonTemp[1].getHp();
    
    System.out.println(dmgToUser);

    if (moveType == PokemonMoveCategory.PHYSICAL || moveType == PokemonMoveCategory.SPECIAL) {
        if (dmgToTarget > 0) {
            messages.add(user.getName() + " dealt " + dmgToTarget + " damage to " + target.getName());
        } else {
            messages.add(user.getName() + "'s attack failed and dealt no damage to " + target.getName());
        }
        if (dmgToUser > 0) {
            messages.add(user.getName() + " took " + dmgToUser + " recoil damage!");
        }
    } else if (moveType == PokemonMoveCategory.STATUS) {
        if (dmgToTarget > 0) {
            messages.add(user.getName() + " used " + move.getMoveName() + " on " + target.getName() + " and inflicted " + dmgToTarget + " damage");
        }
        if (dmgToUser > 0) {
            messages.add(user.getName() + " self damage " + dmgToUser);
        }

        if (pokemonTemp[0].getHp() - user.getHp() > 0) {
            buffToUser = (pokemonTemp[0].getHp() - user.getHp());
            buffedStats = "hp";
        } else if (pokemonTemp[0].getAtk() - user.getAtk() > 0) {
            buffToUser = (pokemonTemp[0].getAtk() - user.getAtk());
            buffedStats = "atk";
        } else if (pokemonTemp[0].getDef() - user.getDef() > 0) {
            buffToUser = (pokemonTemp[0].getDef() - user.getDef());
            buffedStats = "def";
        } else if (pokemonTemp[0].getSpd() - user.getSpd() > 0) {
            buffToUser = (pokemonTemp[0].getSpd() - user.getSpd());
            buffedStats = "spd";
        }

        switch (buffedStats) {
            case "hp" -> messages.add(user.getName() + " recovered " + buffToUser + " hp");
            case "atk" -> messages.add(user.getName() + "'s attack increased by " + buffToUser);
            case "def" -> messages.add(user.getName() + "'s defence increased by " + buffToUser);
            case "spd" -> messages.add(user.getName() + "'s speed increased by " + buffToUser);
            default -> {
                System.out.println("No buff");
            }
        }
    }

    // Apply the changes from the temporary Pokemon to the actual Pokemon
    user.setHp(pokemonTemp[0].getHp());
    target.setHp(pokemonTemp[1].getHp());
    user.setAtk(pokemonTemp[0].getAtk());
    user.setDef(pokemonTemp[0].getDef());
    user.setSpd(pokemonTemp[0].getSpd());
    target.setAtk(pokemonTemp[1].getAtk());
    target.setDef(pokemonTemp[1].getDef());
    target.setSpd(pokemonTemp[1].getSpd());

    // --- START: ADDED LOGIC TO HANDLE FAINTING ---
    // Check if the target Pokemon has fainted
    if (target.getHp() <= 0) {
        messages.add(target.getName() + " fainted!");
        // If the fainted Pokemon is the main enemy, handle the switch
        if (target == getMainEnemyPokemon()) {
            handleFaintedEnemy(); 
        }
        // Note: You may also want to add similar logic for when the player's Pokemon faints.
    }
    // --- END: ADDED LOGIC TO HANDLE FAINTING ---
    
    // Trigger the flickering animation
    if (dmgToTarget > 0) {
        this.pokemonToFlicker = target; 
        this.flickerStartTime = System.currentTimeMillis();
    }
    if (dmgToUser > 0) {
        this.pokemonToFlicker = user; 
        this.flickerStartTime = System.currentTimeMillis();
    }

    System.out.println("t" + dmgToTarget);
    return messages;
}

     public void drawBattleTextBox(Graphics2D g2, BattleState battleState, int panelWidth, int panelHeight) {
        if (this.battleTextBox == null) {
            return; 
        }

    if (hasNewDialogMessage) {
            // The text is already set, just need to make sure it's visible.
            if (!this.battleTextBox.isVisible()) {
                this.battleTextBox.show();
            }
        } else if (battleState == BattleState.BATTLE_DECISION) {
            Pokemon activePlayerPokemon = getMainPlayerPokemon();
            String decisionPrompt = "What will " +
                    (activePlayerPokemon != null ? activePlayerPokemon.getName().toUpperCase() : "POKEMON") +
                    " do?";

            if (!this.battleTextBox.getCurrentText().equals(decisionPrompt)) {
                 this.battleTextBox.setText(decisionPrompt);
            }
            if (!this.battleTextBox.isVisible()) {
                this.battleTextBox.show();
            }
        }
        // ... other else-if conditions for different prompts (like selecting a move)
        else {
             this.battleTextBox.hide();
        }

        if (this.battleTextBox.isVisible()) {
            this.battleTextBox.draw(g2, panelWidth, panelHeight);
        }
    }
    public void resetTextBoxStateForNewTurn() {
        this.decisionPromptDisplayedThisTurn = false;
        this.hasNewDialogMessage = false; // Clear any pending dialog flags too
        // if (this.battleTextBox != null) this.battleTextBox.hide(); // Optionally hide until explicitly shown
    }

    public void drawBattleMenuSelection(Graphics2D g2, int x, int y) {
        if (optionBox == null) {
            optionBox = new MenuWithSelection(menuSelectDecision, x, y, 20f);
        }

        optionBox.setPosition(x, y);
        optionBox.setVisible(true);
        optionBox.draw(g2);
    }

    public void drawBattleMovesBox(Graphics2D g2, int x, int y) {
        if (movesBox == null) {
            movesBox = new MenuWithSelection(menuSelectMoves, x, y, 14f);
        }
        movesBox.setPosition(x, y);
        movesBox.setVisible(true);
        movesBox.draw(g2);
    }

    public void drawBattleBagItemSelection(Graphics2D g2, String[] itemNames, String[] itemQuantities) {
        if (itemBagBox == null) {
            itemBagBox = new MenuWithSelectionWithAdd(itemNames, itemQuantities, battleMenuSelectionX,
                    battleMenuSelectionY, 16f);
        } else {
            itemBagBox.setOptions(itemNames);
            itemBagBox.setAdditionalInfo(itemQuantities);
        }
        itemBagBox.setPosition(battleMenuSelectionX, battleMenuSelectionY);
        itemBagBox.setVisible(true);
        itemBagBox.draw(g2);
    }

    public void menuOptionBoxMoveUp() {
        if (optionBox != null)
            optionBox.moveUp();
    }

    public void menuOptionBoxMoveDown() {
        if (optionBox != null)
            optionBox.moveDown();
    }

    public void menuOptionBoxMoveLeft() {
        if (optionBox != null)
            optionBox.moveLeft();
    }

    public void menuOptionBoxMoveRight() {
        if (optionBox != null)
            optionBox.moveRight();
    }

    public PokemonMove rollEnemyMove() {
    Pokemon enemy = getMainEnemyPokemon();
    ArrayList<PokemonMove> availableMoves = new ArrayList<>();
    for (PokemonMove move : enemy.getMoves()) {
        if (move != null && !move.getMoveName().equals("-")) {
            availableMoves.add(move);
        }
    }
    Random random = new Random();
    PokemonMove chosenMove = availableMoves.get(random.nextInt(availableMoves.size()));
    return chosenMove;
    }
    public void handleFaintedEnemy() {
    if (!isNpcBattle) {
        // No need to switch for wild battles
        return;
    }

    // Check if the current main enemy has fainted
    if (enemyPokemon != null && enemyPokemon.length > 0 && enemyPokemon[0].getHp() <= 0) {
        Pokemon faintedPokemon = enemyPokemon[0];
        boolean foundNewPokemon = false;

        // Find the next non-fainted Pokemon and shift the array
        for (int i = 1; i < enemyPokemon.length; i++) {
            if (enemyPokemon[i] != null && enemyPokemon[i].getHp() > 0) {
                // Set the new main enemy
                enemyPokemon[0] = enemyPokemon[i];
                enemyPokemon[i] = faintedPokemon; // Move the fainted one to the swapped position
                foundNewPokemon = true;
                setNewDialog("Your opponent is about to send in " + enemyPokemon[0].getName() + "!");
                break;
            }
        }

        // If no new Pokemon was found, the battle may be over
        if (!foundNewPokemon) {
            // Handle battle end logic here
            System.out.println("All enemy Pokemon have fainted!");
        }
    }
}
}
