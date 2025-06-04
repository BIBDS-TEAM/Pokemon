package GuiTileMapThing;

import java.awt.FontFormatException;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.awt.GraphicsEnvironment; // Required for font registration

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
    public MenuWithSelection MovesBox;
    public MenuWithSelectionWithAdd itemBagBox;

    private String[][] menuSelectDecision = new String[][] { { "Fight", "PkMn" }, { "Bag", "Run" } };

    private TextBox battleTextBox;

    // Coordinates for UI elements
    private final int allyPokemonHpBarX = 290;
    private final int allyPokemonHpBarY = 320;
    private final int enemyPokemonHpBarX = 50;
    private final int enemyPokemonHpBarY = 100;

    private int allyPokemonSpriteX = 350;
    private int allyPokemonSpriteY = 30;
    private int enemyPokemonSpriteX = 60;
    private int enemyPokemonSpriteY = 230;

    private int battleMenuSelectionX = 260;
    private int battleMenuSelectionY = 380;

    private int battleTextBoxX = 50;
    private int battleTextBoxY = 360;
    private int battleTextBoxWidth = 512;
    private int battleTextBoxHeight = 128;

    // Constructor for NPC Trainer battles
    public Battle(Pokemon[] playerPokemons, Pokemon[] enemyPokemon) {
        if (playerPokemons == null || playerPokemons.length < 1 || playerPokemons[0] == null) {
            throw new IllegalArgumentException(
                    "playerPokemons array cannot be null, empty, or have a null first Pokemon.");
        }
        if (enemyPokemon == null || enemyPokemon.length < 1 || enemyPokemon[0] == null) {
            throw new IllegalArgumentException(
                    "enemyPokemon array cannot be null, empty, or have a null first Pokemon for NPC battles.");
        }
        this.playerPokemons = playerPokemons;
        this.enemyPokemon = enemyPokemon;
        this.isNpcBattle = true; // This is an NPC trainer battle
        this.wildPokemon = null;
        initializeAssets();
    }

    // Constructor for Wild Pokemon battles
    public Battle(Pokemon playerPokemon, Pokemon wildPokemon) {
        if (playerPokemons == null) {
            throw new IllegalArgumentException("playerPokemon cannot be null Pokemon.");
        }
        if (wildPokemon == null) {
            throw new IllegalArgumentException("wildPokemon cannot be null for wild battles.");
        }
        this.playerPokemons = new Pokemon[1];
        this.playerPokemons[0] = playerPokemon;
        this.wildPokemon = wildPokemon;
        this.isNpcBattle = false; // This is a wild Pokemon battle
        this.enemyPokemon = null;
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
        optionBox = new MenuWithSelection(menuSelectDecision, battleMenuSelectionX, battleMenuSelectionY, 20f);
        // MovesBox and itemBagBox can be initialized when needed or here if always
        // shown initially.
        this.optionBox.setVisible(false); // GamePanel will control visibility

        // HIGHLIGHT: Initialize the battleTextBox
        this.battleTextBox = new TextBox();
    }

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
            if (this.enemyPokemon != null) {
                for (Pokemon p : this.enemyPokemon) {
                    if (p != null && p.getHp() > 0) {
                        return p; // Return the first active NPC Pokemon
                    }
                }
            }
            return null; // All NPC Pokemon fainted or array is null
        } else {
            // Wild battle
            return this.wildPokemon;
        }
    }

    public void update() {

    }

    public void draw(Graphics2D g2, BattleState currentBattleState, int panelWidth, int panelHeight) {
        Pokemon currentEnemyToDraw = getCurrentOpponentForDrawing();
        if (currentEnemyToDraw != null) {
            drawPokemonSpriteWithIndex(g2, currentEnemyToDraw, 2); // 2 for enemy
            drawEnemyPokemonHpBar(g2, 130, 20); // Uses internal logic to get the Pokemon
        } else {
            // Optionally draw a placeholder if no enemy
            if (font != null)
                g2.setFont(font.deriveFont(Font.PLAIN, 12f));
            else
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(Color.GRAY);
            g2.drawString("No Opponent", enemyPokemonSpriteX + 10, enemyPokemonSpriteY + 30);
        }

        Pokemon currentPlayerPkmn = getMainPlayerPokemon();
        if (currentPlayerPkmn != null) {
            drawPokemonSpriteWithIndex(g2, currentPlayerPkmn, 1); // 1 for ally
            drawAllyPokemonHpBar(g2, 130, 20); // Uses internal logic for player Pokemon
        } else {
            // Optionally draw a placeholder if no player Pokemon
            if (font != null)
                g2.setFont(font.deriveFont(Font.PLAIN, 12f));
            else
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(Color.GRAY);
            g2.drawString("No Ally", allyPokemonSpriteX + 10, allyPokemonSpriteY + 30);
        }

        drawBattleMenuSelection(g2, battleMenuSelectionX, battleMenuSelectionY);
        drawBattleTextBox(g2, currentBattleState, panelWidth, panelHeight);
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
    public void drawEnemyPokemonHpBar(Graphics2D g2, int width, int height) {
        // Determine the Pokemon whose HP bar needs to be drawn.
        // This is the critical part for the VerifyError.
        Pokemon pokemonToDraw; // This will be locals[4] for this method
        if (this.isNpcBattle) {
            Pokemon firstActiveNPCPokemon = null;
            if (this.enemyPokemon != null && this.enemyPokemon.length > 0) {
                for (Pokemon p : this.enemyPokemon) {
                    if (p != null && p.getHp() > 0) {
                        firstActiveNPCPokemon = p;
                        break;
                    }
                }
            }
            pokemonToDraw = firstActiveNPCPokemon;
        } else {
            pokemonToDraw = this.wildPokemon;
        }

        // If pokemonToDraw is null, we can't draw its HP bar.
        if (pokemonToDraw == null) {
            // System.err.println("drawEnemyPokemonHpBar: pokemonToDraw is null.");
            // Optionally draw a placeholder or message indicating no opponent
            if (font != null)
                g2.setFont(font.deriveFont(Font.PLAIN, 12f));
            else
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(Color.RED);
            g2.drawString("NO ENEMY TARGET", enemyPokemonHpBarX, enemyPokemonHpBarY + 10);
            return;
        }

        // Rest of the drawing logic using 'pokemonToDraw'
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

    // Reverted signature
    public void drawAllyPokemonHpBar(Graphics2D g2, int width, int height) {
        Pokemon pokemonToDraw = getMainPlayerPokemon(); // Get the current player's Pokemon

        if (pokemonToDraw == null) {
            // System.err.println("drawAllyPokemonHpBar: pokemonToDraw is null (player's
            // main Pokemon).");
            if (font != null)
                g2.setFont(font.deriveFont(Font.PLAIN, 12f));
            else
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.setColor(Color.RED);
            g2.drawString("NO ALLY TARGET", allyPokemonHpBarX, allyPokemonHpBarY + 10);
            return;
        }

        // Rest of the drawing logic using 'pokemonToDraw'
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

    // Other methods like flickeringSprite, drawBattleMenuSelection, etc. remain
    // largely the same
    // but ensure they use the correctly determined Pokemon objects.
    public void flickeringSprite(Graphics2D g2, Pokemon pokemon, String pokemonAlliance) {
        if (g2 == null)
            throw new NullPointerException("Graphics2D g2 cannot be null");
        if (pokemon == null) {
            return;
        }
        if (pokemonAlliance == null
                || (!pokemonAlliance.equalsIgnoreCase("ally") && !pokemonAlliance.equalsIgnoreCase("enemy"))) {
            throw new IllegalArgumentException(
                    "Invalid pokemon alliance for flickering: " + pokemonAlliance + ". Must be 'ally' or 'enemy'.");
        }

        int spriteIndex = pokemonAlliance.equalsIgnoreCase("ally") ? 1 : 2;

        long millis = System.currentTimeMillis() % 1000;
        if (millis < 500) {
            drawPokemonSpriteWithIndex(g2, pokemon, spriteIndex);
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

    public void drawBattleTextBox(Graphics2D g2, BattleState battleState, int panelWidth, int panelHeight) {
        if (this.battleTextBox == null) {
            this.battleTextBox = new TextBox(); // Should be initialized in constructor
        }

        String textToShow = "";
        Pokemon activePlayerPokemon = getMainPlayerPokemon(); // Get active Pokemon for the prompt

        if (battleState == BattleState.BATTLE_DECISION) {
            if (activePlayerPokemon != null && activePlayerPokemon.getName() != null) {
                textToShow = "What will " + activePlayerPokemon.getName().toUpperCase() + " do?";
            } else {
                textToShow = "Select an action."; // Fallback if Pokemon is null
            }
        } else {
            // Placeholder for other battle states (e.g., after a move is used)
            // This part will need to be updated with actual dialogue from battle events.
            // For example: if (lastEventMessage != null) textToShow = lastEventMessage;
            textToShow = "..."; // Default for non-decision states
        }

        this.battleTextBox.setText(textToShow, g2); // g2 is needed for FontMetrics by TextBox
        this.battleTextBox.show(); // Make sure it's marked as visible
        this.battleTextBox.draw(g2, panelWidth, panelHeight);
    }

    public void drawBattleMenuSelection(Graphics2D g2, int x, int y) {
        if (optionBox == null) {
            optionBox = new MenuWithSelection(menuSelectDecision, x, y, 20f);
        }
        
        optionBox.setPosition(x, y);
        optionBox.setVisible(true);
        optionBox.draw(g2);
    }

    public void drawBattleMovesSelection(Graphics2D g2, String[][] gridOptions, int x, int y) {
        if (MovesBox == null) {
            MovesBox = new MenuWithSelection(gridOptions, x, y, 20f);
        } else {
            MovesBox.setOptions(gridOptions);
        }
        MovesBox.setPosition(x, y);
        MovesBox.setVisible(true);
        MovesBox.draw(g2);
    }

    public void executePokemonMove(Graphics2D g2, PokemonMove move, Pokemon pokemon, Pokemon target) {
        // ... logic for move execution...
        String moveType, message = "";
        Map<String, String> moveInfo;
        // If move causes damage: message += " It dealt X damage.";
        if (battleTextBox != null) { // Ensure battleTextBox is initialized
            battleTextBox.setText(message, g2); // g2_for_textbox is needed by current TextBox.setText
            battleTextBox.show(); // Ensure it's visible
        }
    }
    
    public void drawBattleBagItemSelection(Graphics2D g2, String[] itemNames, String[] itemQuantities) {
        if (itemBagBox == null) {
            itemBagBox = new MenuWithSelectionWithAdd(itemNames, itemQuantities, battleMenuSelectionX, battleMenuSelectionY, 16f);
        } else {
            itemBagBox.setOptions(itemNames);
            itemBagBox.setAdditionalInfo(itemQuantities);
        }
        itemBagBox.setPosition(battleMenuSelectionX, battleMenuSelectionY);
        itemBagBox.setVisible(true);
        itemBagBox.draw(g2);
    }

    public void menuSelectionMoveUp() { if (optionBox != null) optionBox.moveUp(); }
    public void menuSelectionMoveDown() { if (optionBox != null) optionBox.moveDown(); }
    public void menuSelectionMoveLeft() { if (optionBox != null) optionBox.moveLeft(); }
    public void menuSelectionMoveRight() { if (optionBox != null) optionBox.moveRight(); }
}
