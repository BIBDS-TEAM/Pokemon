package GuiTileMapThing;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Battle{
    protected Pokemon playerPokemons[] = new Pokemon[6];
    protected Pokemon enemyPokemon[] = new Pokemon[6];
    protected Pokemon wildPokemon;
    private boolean isNpcBattle;
    
    protected Font font;
   
    public MenuWithSelection optionBox;
    public MenuWithSelection MovesBox;
    public MenuWithSelectionWithAdd itemBagBox;
    private String[][] menuSelectDecision = new String[][]{{"Fight","PkMn"}, {"Bag","Run"}};

    private final int allyPokemonHpBarX = 290;
    private final int allyPokemonHpBarY = 320;
    private final int enemyPokemonHpBarX = 50;
    private final int enemyPokemonHpBarY = 100;

    private int allyPokemonSpriteX = 350;
    private int allyPokemonSpriteY = 30;
    private int enemyPokemonSpriteX = 60;
    private int enemyPokemonSpriteY = 230;

    private int battleMenuSelectionX = 260;
    private int battleMenuSelectionY = 260;

    public Battle(Pokemon[] playerPokemons, Pokemon[] enemyPokemon) {
        if (playerPokemons.length != 6) throw new IllegalArgumentException("playerPokemons must have 6 elements");
        if (enemyPokemon == null) throw new IllegalArgumentException("enemyPokemon cannot be null");
        isNpcBattle = false;
        this.playerPokemons = playerPokemons;
        this.enemyPokemon = enemyPokemon;
    }

    public Battle(Pokemon playerPokemons, Pokemon wildPokemon) {
        if (playerPokemons == null) throw new IllegalArgumentException("playerPokemons cannot be null");
        if (wildPokemon == null) throw new IllegalArgumentException("enemyPokemon cannot be null");
        this.playerPokemons[0] = playerPokemons;
        this.wildPokemon = wildPokemon;
        isNpcBattle = true;
    }

    public Pokemon[] getPlayerPokemons() {
        return playerPokemons;
    }

    public void setPlayerPokemons(Pokemon[] pokemons) {
        this.playerPokemons = pokemons;
    }

    public Pokemon[] getEnemyPokemon() {
        return enemyPokemon;
    }

    public void setEnemyPokemon(Pokemon[] pokemon) {
        this.enemyPokemon = pokemon;
    }

    public Pokemon getMainPokemon() {
        return playerPokemons[0];
    }

    public Pokemon getWildPokemon() {
        return wildPokemon;
    }

    public void setWildPokemon(Pokemon wildPokemon) {
        this.wildPokemon = wildPokemon;
    }

    public int[] getAllyPokemonHpBarPos() {
        return new int[]{allyPokemonHpBarX, allyPokemonHpBarY};
    }

    public int[] getEnemyPokemonHpBarPos() {
        return new int[]{enemyPokemonHpBarX, enemyPokemonHpBarY};
    }

    public void changeMainPokemon(int index) {
        Pokemon tmp = playerPokemons[0];
        playerPokemons[0] = playerPokemons[index];
        playerPokemons[index] = tmp;
    }

    public void draw(Graphics2D g2) {
        // Implementation for drawing the entire battle scene would go here.
        // For example, drawing background, sprites, HP bars, menus etc.
        // This method needs to be called from your game loop.

        // Example (order matters for layering):
        // drawBackground(g2);

        // Draw enemy Pokemon and its HP bar
        Pokemon currentEnemy = isNpcBattle ? wildPokemon : (enemyPokemon != null && enemyPokemon[0] != null ? enemyPokemon[0] : null);
        if (currentEnemy != null) {
            drawPokemonSpriteWithIndex(g2, currentEnemy, 2); // 2 for enemy
            drawEnemyPokemonHpBar(g2, currentEnemy, 100, 10); // Example width/height
        }

        // Draw player's Pokemon and its HP bar
        Pokemon currentPlayerPkmn = getMainPokemon();
        if (currentPlayerPkmn != null) {
            drawPokemonSpriteWithIndex(g2, currentPlayerPkmn, 1); // 1 for ally
            drawAllyPokemonHpBar(g2, currentPlayerPkmn, 100, 10); // Example width/height
        }
        
        drawBattleTextBox(g2);
        drawBattleMenuSelection(g2, battleMenuSelectionX, battleMenuSelectionY); // x, y would be screen coordinates for the menu
    }
    public void drawPokemonSpriteWithIndex(Graphics2D g2, Pokemon pokemon, int index) {
        // 1 for ally, 2 for enemy
        int pokemonPosX, pokemonPosY;
        BufferedImage sprite = null;
        if (index == 1) {
            sprite = pokemon.getAllyFightModel();
            pokemonPosX = allyPokemonSpriteX;
            pokemonPosY = allyPokemonSpriteY;
        } else if (index == 2)  {
            sprite = pokemon.getEnemyFightModel(); 
            pokemonPosX = enemyPokemonSpriteX;
            pokemonPosY = enemyPokemonSpriteY;
        } else throw new IllegalArgumentException("Invalid index: " + index);

        if (sprite != null) {
            try {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Interpolation hint not supported.");
            }

            g2.drawImage(scaleImage(sprite, 0.5), pokemonPosX, pokemonPosY, null);
        } else {
            System.out.println("Failed to load " + pokemon.getName() + "'s model");
        }
    }

    public BufferedImage scaleImage(BufferedImage originalImage, double scale) {
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

    /**
     * Draw the HP bar of a Pokemon
     * @param g2 Graphics2D to draw on
     * @param pokemon Pokemon to draw HP bar for
     * @param x x-coordinate of the HP bar
     * @param y y-coordinate of the HP bar
     * @param width width of the HP bar
     * @param height height of the HP bar
     */
    public void drawEnemyPokemonHpBar(Graphics2D g2, Pokemon pokemon, int width, int height) {
        if (pokemon == null) {
            throw new NullPointerException("pokemon cannot be null");
        }

        float hpRatio = (float) pokemon.getHp() / pokemon.getMaxHp();

        Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

        int paddingX = enemyPokemonHpBarX - 14;
        int paddingY = enemyPokemonHpBarY - 18;
        int borderWidth = width + 36;
        int borderHeight = height + 16;

        if (g2 == null) {
            throw new NullPointerException("Graphics2D g2 cannot be null");
        }

        g2.setColor(Color.BLACK);

        g2.fillRect(paddingX, paddingY, 6, borderHeight);

        g2.fillRect(paddingX + 2, paddingY + borderHeight - 1, borderWidth, 6);

        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 7, 4, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 4, 10, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 1, 15, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight + 2, 19, 3);

        // hp bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.BOLD, 12f));
        g2.drawString("HP: ", enemyPokemonHpBarX, enemyPokemonHpBarY);

        g2.setColor(Color.BLACK);
        // hp bar border
        g2.fillRect(enemyPokemonHpBarX + 36, enemyPokemonHpBarY - 10, borderWidth - 52, 12);
        g2.fillRect(enemyPokemonHpBarX + 34, enemyPokemonHpBarY - 8, 2, 8);
        g2.fillRect(enemyPokemonHpBarX + 150, enemyPokemonHpBarY - 8, 2, 8);

        g2.setColor(barColor);
        g2.fillRect(enemyPokemonHpBarX + 36, enemyPokemonHpBarY - 8, (int) ((borderWidth - 52) * hpRatio), 8);

        // pokemon name
        int nameAndLvlPadding = 24;
        String pokemonName = pokemon.getName();
        String pokemonLvl = "Lv. " + pokemon.getLvl();
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.PLAIN, 18f));
        int nameWidth = g2.getFontMetrics().stringWidth(pokemonName);
        g2.drawString(pokemon.getName(), enemyPokemonHpBarX + nameWidth / 7, enemyPokemonHpBarY - nameAndLvlPadding - 20);
        g2.setFont(font.deriveFont(font.BOLD, 14f));
        int lvlWidth = g2.getFontMetrics().stringWidth(pokemonLvl);
        g2.drawString("Lv. " + pokemon.getLvl(), enemyPokemonHpBarX + nameWidth / 2 - lvlWidth / 3, enemyPokemonHpBarY - nameAndLvlPadding);
    }

    /**
     * Draws the HP bar for a Pokemon on the ally side.
     * @param g2 the Graphics2D object to draw on
     * @param pokemon the Pokemon to draw the HP bar for
     * @param x the x-coordinate of the HP bar
     * @param y the y-coordinate of the HP bar
     * @param width the width of the HP bar
     * @param height the height of the HP bar
     */
    public void drawAllyPokemonHpBar(Graphics2D g2, Pokemon pokemon, int width, int height) {
        float hpRatio = (float) pokemon.getHp() / pokemon.getMaxHp();

        Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

        int paddingX = allyPokemonHpBarX + 24;
        int paddingY = allyPokemonHpBarY - 10;
        int borderWidth = width + 36;
        int borderHeight = height + 26;

        g2.setColor(Color.BLACK);

        // semi pyramid shape
        g2.fillRect(allyPokemonHpBarX + 15, allyPokemonHpBarY - 7, 4, 3);
        g2.fillRect(allyPokemonHpBarX + 9, allyPokemonHpBarY - 4, 10, 3);
        g2.fillRect(allyPokemonHpBarX + 4, allyPokemonHpBarY - 1, 15, 3);
        g2.fillRect(allyPokemonHpBarX, allyPokemonHpBarY + 2, 19, 3);

        // thick bottom horizontal bar
        g2.fillRect(allyPokemonHpBarX + 19, allyPokemonHpBarY - 1, borderWidth, 6);

        // thick right vertical bar
        g2.fillRect(allyPokemonHpBarX + borderWidth + 16, allyPokemonHpBarY - borderHeight + 1, 6, borderHeight);

        // hp stats
        String hpStats = pokemon.getHp() + " / " + pokemon.getMaxHp();
        int hpStatsWidth = (int) g2.getFontMetrics().getStringBounds(hpStats, g2).getWidth();
        g2.setFont(font.deriveFont(font.BOLD, 14f));
        g2.drawString(hpStats, paddingX + borderWidth / 7 * 6 - hpStatsWidth * 2, paddingY);

        // hp bar
        int barPosX = allyPokemonHpBarX + 52;
        int barPosY = allyPokemonHpBarY - 38;

        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.BOLD, 12f));
        g2.drawString("HP: ", barPosX - 36, barPosY + 9);

        g2.setColor(Color.BLACK);
        // hp bar border
        int hpBarWidth = borderWidth - 52;
        g2.fillRect(barPosX, barPosY - 2, hpBarWidth, 12);
        g2.fillRect(barPosX - 2, barPosY, 2, 8);
        g2.fillRect(barPosX + 114, barPosY, 2, 8);

        // hp bar
        g2.setColor(barColor);
        g2.fillRect(barPosX, barPosY, (int) ((hpBarWidth) * hpRatio), 8);

        // pokemon name
        int nameAndLvlPadding = 10;
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.BOLD, 14f));
        String pokemonName = pokemon.getName();
        int pokemonNameWidth = (int) g2.getFontMetrics().getStringBounds(pokemonName, g2).getWidth();
        int pokemonLvlWidth = (int) g2.getFontMetrics().getStringBounds("Lv. " + pokemon.getLvl(), g2).getWidth();
        g2.drawString("Lv. " + pokemon.getLvl(), allyPokemonHpBarX + 15 + hpBarWidth - pokemonNameWidth * 4 / 5 + pokemonLvlWidth / 2,
                barPosY - nameAndLvlPadding);
        g2.setFont(font.deriveFont(font.PLAIN, 18f));
        g2.drawString(pokemon.getName(), allyPokemonHpBarX + 15 + hpBarWidth - pokemonNameWidth, barPosY - nameAndLvlPadding - 20);
    }

    public void flickeringSprite(Graphics2D g2, Pokemon pokemon, String pokemonAlliance) {
        if (g2 == null) throw new NullPointerException("Graphics2D g2 cannot be null");
        if (pokemon == null) {
            // System.out.println("Warning: Attempted to flicker a null Pokemon.");
            return;
        }
        if (pokemonAlliance == null) throw new IllegalArgumentException("pokemonAlliance cannot be null");

        // FIX: Determine the correct index for drawPokemonSpriteWithIndex
        int spriteIndex;
        if (pokemonAlliance.equalsIgnoreCase("ally")) {
            spriteIndex = 1; // Ally Pokemon
        } else if (pokemonAlliance.equalsIgnoreCase("enemy")) {
            spriteIndex = 2; // Enemy Pokemon
        } else {
            throw new IllegalArgumentException("Invalid pokemon alliance for flickering: " + pokemonAlliance + ". Must be 'ally' or 'enemy'.");
        }

        // Flicker logic: show sprite for 500ms, hide for 500ms
        long millis = System.currentTimeMillis() % 1000;
        if (millis < 500) {
            // FIX: Call drawPokemonSpriteWithIndex with the correct signature and determined index
            drawPokemonSpriteWithIndex(g2, pokemon, spriteIndex);
        }
        // Else (millis >= 500), do nothing to achieve the "off" state of the flicker.
    }

    public void drawBattleTextBox(Graphics2D g2){
        TextBox chatBox = new TextBox();
        chatBox.show();
        chatBox.draw(g2, 512, 512);
    }
    public void drawBattleMenuSelection(Graphics2D g2, int x, int y) {
        optionBox = new MenuWithSelection(menuSelectDecision, x, y,20f);
        optionBox.setVisible(true);
        optionBox.draw(g2);
    }
    public void drawBattleMovesSelection(Graphics2D g2, String[][] gridOptions, int x, int y){
        MovesBox = new MenuWithSelection(gridOptions, x, y,20f);
        MovesBox.setVisible(true);
        MovesBox.draw(g2);
    }
    public void drawBattleBagItemSelection(Graphics2D g2, String[][] itemBagList, int x, int y){
        itemBagBox = new MenuWithSelectionWithAdd(itemBagList[0], itemBagList[1], x, y, 16f);
        itemBagBox.setVisible(true);
        itemBagBox.draw(g2);
    }
}

    
