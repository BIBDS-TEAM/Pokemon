package GuiTileMapThing;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Battle extends MenuWithSelection {
    protected Pokemon playerPokemons[] = new Pokemon[4];
    protected Pokemon enemyPokemon;
    public Battle(String[][] gridOptions, int x, int y, int width, int height, Pokemon[] playerPokemons, Pokemon enemyPokemon) {
        super(gridOptions, x, y, width, height,28f);
        if (playerPokemons.length != 4) throw new IllegalArgumentException("playerPokemons must have 4 elements");
        if (enemyPokemon == null) throw new IllegalArgumentException("enemyPokemon cannot be null");
        this.playerPokemons = playerPokemons;
        this.enemyPokemon = enemyPokemon;
        initAssets();
    }

    public Pokemon[] getPlayerPokemons() {
        return playerPokemons;
    }

    public void setPlayerPokemons(Pokemon[] pokemons) {
        this.playerPokemons = pokemons;
    }

    public Pokemon getEnemyPokemon() {
        return enemyPokemon;
    }

    public void setEnemyPokemon(Pokemon pokemon) {
        this.enemyPokemon = pokemon;
    }

    public Pokemon getMainPokemon() {
        return playerPokemons[0];
    }

    public void changeMainPokemon(int index) {
        Pokemon tmp = playerPokemons[0];
        playerPokemons[0] = playerPokemons[index];
        playerPokemons[index] = tmp;
    }

    public void draw() {
        int defaultPokemonHpBarWidth = 130;
        int defaultPokemonHpBarHeight = 20;
        int AllyPokemonHpBarXPos = 290;
        int AllyPokemonHpBarYPos = 320;
        int EnemyPokemonHpBarXPos = 50;
        int EnemyPokemonHpBarYPos = 100;
        // make all components in 1 draw so it's easier to maintain
    }

    public void draw(Graphics2D g2) {
        String[][] gridOptions = getGridOptions();
        int rows = gridOptions.length;
        int cols = gridOptions[0].length;

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        int padding = 40;

        drawBorder(g2, getX(), getY(), getWidth() + padding, getHeight() + padding);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String text = gridOptions[row][col];
                int x = col * cellWidth + padding + getX();
                int y = row * cellHeight + padding + getY();

                g2.drawString(text, x, y);

                if (row == getSelectedRow() && col == getSelectedCol() && getSelectionArrow() != null) {
                    int arrowY = y - 24;
                    g2.drawImage(getSelectionArrow(), x - 30, arrowY, 24, 24, null);
                }
            }
        }
    }

    public void drawPokemonSpriteWithIndex(Graphics2D g2, Pokemon pokemon, int index, int x, int y) {
        BufferedImage sprite = null;
        if (index == 0) sprite = pokemon.getMiniModel();
        if (index == 1) sprite = pokemon.getAllyFightModel();
        if (index == 2) sprite = pokemon.getEnemyFightModel();
        if (index > 2) throw new IllegalArgumentException("Invalid index: " + index);

        if (sprite != null) {
            // Optional: Set rendering hints only if you're scaling or transforming.
            try {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Interpolation hint not supported.");
            }

            g2.drawImage(sprite, x, y, null); // draw without scaling
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
    public void drawEnemyPokemonHpBar(Graphics2D g2, Pokemon pokemon, int x, int y, int width, int height) {
        if (pokemon == null) {
            throw new NullPointerException("pokemon cannot be null");
        }

        float hpRatio = (float) pokemon.getHp() / pokemon.getMaxHp();

        Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

        int paddingX = x - 14;
        int paddingY = y - 18;
        int borderWidth = width + 36;
        int borderHeight = height + 16;

        if (g2 == null) {
            throw new NullPointerException("Graphics2D g2 cannot be null");
        }

        g2.setColor(Color.BLACK);

        // Thicker left vertical bar
        g2.fillRect(paddingX, paddingY, 6, borderHeight);

        // Thicker bottom horizontal bar
        g2.fillRect(paddingX + 2, paddingY + borderHeight - 1, borderWidth, 6);

        // Thicker right vertical bar
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 7, 4, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 4, 10, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight - 1, 15, 3);
        g2.fillRect(paddingX + borderWidth, paddingY + borderHeight + 2, 19, 3);

        // hp bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.BOLD, 12f));
        g2.drawString("HP: ", x, y);

        g2.setColor(Color.BLACK);
        // hp bar border
        g2.fillRect(x + 36, y - 10, borderWidth - 52, 12);
        g2.fillRect(x + 34, y - 8, 2, 8);
        g2.fillRect(x + 150, y - 8, 2, 8);

        g2.setColor(barColor);
        g2.fillRect(x + 36, y - 8, (int) ((borderWidth - 52) * hpRatio), 8);

        // pokemon name
        int nameAndLvlPadding = 24;
        String pokemonName = pokemon.getName();
        String pokemonLvl = "Lv. " + pokemon.getLvl();
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(font.PLAIN, 18f));
        int nameWidth = g2.getFontMetrics().stringWidth(pokemonName);
        g2.drawString(pokemon.getName(), x + nameWidth / 7, y - nameAndLvlPadding - 20);
        g2.setFont(font.deriveFont(font.BOLD, 14f));
        int lvlWidth = g2.getFontMetrics().stringWidth(pokemonLvl);
        g2.drawString("Lv. " + pokemon.getLvl(), x + nameWidth / 2 - lvlWidth / 3, y - nameAndLvlPadding);
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
    public void drawAllyPokemonHpBar(Graphics2D g2, Pokemon pokemon, int x, int y, int width, int height) {
        float hpRatio = (float) pokemon.getHp() / pokemon.getMaxHp();

        Color barColor = hpRatio > 0.5 ? Color.GREEN : hpRatio > 0.2 ? Color.YELLOW : Color.RED;

        int paddingX = x + 24;
        int paddingY = y - 10;
        int borderWidth = width + 36;
        int borderHeight = height + 26;

        g2.setColor(Color.BLACK);

        // semi pyramid shape
        g2.fillRect(x + 15, y - 7, 4, 3);
        g2.fillRect(x + 9, y - 4, 10, 3);
        g2.fillRect(x + 4, y - 1, 15, 3);
        g2.fillRect(x, y + 2, 19, 3);

        // thick bottom horizontal bar
        g2.fillRect(x + 19, y - 1, borderWidth, 6);

        // thick right vertical bar
        g2.fillRect(x + borderWidth + 16, y - borderHeight + 1, 6, borderHeight);

        // hp stats
        String hpStats = pokemon.getHp() + " / " + pokemon.getMaxHp();
        int hpStatsWidth = (int) g2.getFontMetrics().getStringBounds(hpStats, g2).getWidth();
        g2.setFont(font.deriveFont(font.BOLD, 14f));
        g2.drawString(hpStats, paddingX + borderWidth / 7 * 6 - hpStatsWidth * 2, paddingY);

        // hp bar
        int barPosX = x + 52;
        int barPosY = y - 38;

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
        g2.drawString("Lv. " + pokemon.getLvl(), x + 15 + hpBarWidth - pokemonNameWidth * 4 / 5 + pokemonLvlWidth / 2,
                barPosY - nameAndLvlPadding);
        g2.setFont(font.deriveFont(font.PLAIN, 18f));
        g2.drawString(pokemon.getName(), x + 15 + hpBarWidth - pokemonNameWidth, barPosY - nameAndLvlPadding - 20);
    }

    public void drawBattleMenuSelection(Graphics2D g2, String[][] gridOptions, String chatText, int x, int y) {
        TextBox chatBox = new TextBox();
        MenuWithSelection optionBox = new MenuWithSelection(gridOptions, x, y, 400, 534,28f);
        
        optionBox.setVisible(true);

        optionBox.draw(g2);
        chatBox.setText(chatText);
        chatBox.draw(g2, x, y, 400, 534);
        optionBox.drawBorder(g2, x, y, optionBox.getWidth(), optionBox.getHeight());
    }
}
