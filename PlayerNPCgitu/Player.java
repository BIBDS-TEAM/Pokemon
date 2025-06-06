package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import GuiTileMapThing.KeyInput;
import Pokemon.PokemonBasics.PokemonAllType.*;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Player extends Entity {
    private int currentSaveFile;
    private String playerName;
    private String lastDirection;
    private Clip bumpClip;
    private Clip footstepClip;
    private boolean isWalkingSoundPlaying = false;
    private boolean isInteracting = false;
    private long lastBumpSoundTime = 0;
    private final long bumpCooldown = 300;
    GamePanel gp;
    KeyInput keyI;
    public final int screenX, screenY;
    BufferedImage playerImage;
    int lastLocationX, lastLocationY;
    Pokemon[] pokemon = new Pokemon[6];

    public Player(GamePanel gp, KeyInput keyI) {
        this.gp = gp;
        this.keyI = keyI;
        screenX = 512 / 2 - 16;
        screenY = 512 / 2 - 16;

        solidArea = new Rectangle(4, 4, 24, 24);

        setDefaultValues();
        loadPlayer();
        setDefaultArea();
    }

    public void setDefaultValues() {
        worldX = 900;
        worldY = 680;
        speed = 3;
        lastDirection = "down";
        playerImage = down0;
    }

    public void update() {
        direction = keyI.getCurrentDirection();
        if (direction == null) {
            spriteNum = 0;
        }
        boolean isMoving = direction != null &&
                (keyI.upPressed || keyI.downPressed || keyI.leftPressed || keyI.rightPressed) &&
                !isInteracting;

        if (!isMoving) {
            spriteNum = 0;
            stopFootstepSound();
            return;
        }

        gp.eCheck.cekTile(this);
        collisionOn = false;
        gp.cc.cekTile(this);
        gp.cc.checkEntity(this, gp.npcList);
        spriteCounter++;
        if (spriteCounter > 10) {
            spriteCounter = 0;
            if (direction.equals("up") || direction.equals("down")) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else {
                    spriteNum = 1;
                }
            } else if (direction.equals("left") || direction.equals("right")) {
                spriteNum = (spriteNum + 1) % 2;
            }
        }

        if (!collisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    lastDirection = direction;
                    break;
                case "down":
                    worldY += speed;
                    lastDirection = direction;
                    break;
                case "left":
                    worldX -= speed;
                    lastDirection = direction;
                    break;
                case "right":
                    worldX += speed;
                    lastDirection = direction;
                    break;
            }
            startFootstepSound();
        } else {
            stopFootstepSound();
            playBumpSound();
        }
    }

    public void draw(Graphics2D g2) {
        direction = (direction != null) ? direction : lastDirection;
        switch (direction) {
            case "up":
                playerImage = (spriteNum == 0) ? up0 : (spriteNum == 1 ? up1 : up2);
                break;
            case "down":
                playerImage = (spriteNum == 0) ? down0 : (spriteNum == 1 ? down1 : down2);
                break;
            case "left":
                playerImage = (spriteNum % 2 == 0) ? left0 : left1;
                break;
            case "right":
                playerImage = (spriteNum % 2 == 0) ? right0 : right1;
                break;
            default:
                playerImage = down0;
                break;
        }

        if (playerImage != null) {
            g2.drawImage(playerImage, screenX, screenY, 32, 32, null);
        }

        // Draw the Pokémon party UI
        drawPokemonPartyUI(g2);
    }

    /**
     * Draws the player's current Pokémon party as mini-sprites on the top-left of the screen.
     * @param g2 The Graphics2D context to draw on.
     */
    private void drawPokemonPartyUI(Graphics2D g2) {
        // Define the area for the party UI on the top-left
        final int padding = 8;
        final int iconSize = 32; // The size of each Pokémon icon
        final int uiAreaHeight = gp.screenHeight / 2; // Use top 25% of the screen
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRect(padding / 2, padding / 2, iconSize + padding, uiAreaHeight);

        // Calculate the vertical spacing between icons to fit them in the UI area
        int verticalSpacing = (uiAreaHeight - padding) / pokemon.length;

        for (int i = 0; i < pokemon.length; i++) {
            Pokemon p = pokemon[i];
            if (p != null) {
                // This assumes your Pokemon class has a getMiniModel() method that returns its mini-sprite.
                BufferedImage pokemonIcon = p.getMiniModel();

                if (pokemonIcon != null) {
                    // Calculate the position for each icon
                    int x = padding;
                    // Distribute icons evenly within the allocated 25% height
                    int y = padding + (i * verticalSpacing);

                    // Draw the icon
                    g2.drawImage(pokemonIcon, x, y, iconSize, iconSize, null);
                }
            }
        }
    }

    public void loadPlayer() {
        try {
            up0 = ImageIO.read(new File("TileGambar/Plyup_0.png"));
            up1 = ImageIO.read(new File("TileGambar/Plyup_1.png"));
            up2 = ImageIO.read(new File("TileGambar/Plyup_2.png"));
            down0 = ImageIO.read(new File("TileGambar/Plydown_0.png"));
            down1 = ImageIO.read(new File("TileGambar/Plydown_1.png"));
            down2 = ImageIO.read(new File("TileGambar/Plydown_2.png"));
            left0 = ImageIO.read(new File("TileGambar/Plyleft_0.png"));
            left1 = ImageIO.read(new File("TileGambar/Plyleft_1.png"));
            right0 = ImageIO.read(new File("TileGambar/Plyright_0.png"));
            right1 = ImageIO.read(new File("TileGambar/Plyright_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPokemonToBag(Pokemon pokemon) {
        for (int i = 0; i < this.pokemon.length; i++) {
            if (this.pokemon[i] == null) {
                this.pokemon[i] = pokemon;
                break;
            }
        }
    }

    private void startFootstepSound() {
        if (isWalkingSoundPlaying)
            return;

        try {
            File audioFile = new File("../Pokemon/audioSave/footsteps.wav");
            if (!audioFile.exists()) {
                System.err.println("Footstep audio file not found: " + audioFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            footstepClip = AudioSystem.getClip();
            footstepClip.open(audioStream);
            footstepClip.loop(Clip.LOOP_CONTINUOUSLY);
            footstepClip.start();
            isWalkingSoundPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopFootstepSound() {
        if (footstepClip != null && footstepClip.isRunning()) {
            footstepClip.stop();
            footstepClip.flush();
            footstepClip.close();
            footstepClip = null;
        }
        isWalkingSoundPlaying = false;
    }

    private void playBumpSound() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBumpSoundTime < bumpCooldown) {
            return;
        }

        lastBumpSoundTime = currentTime;

        try {
            File bumpFile = new File("../Pokemon/audioSave/bumpToWall.wav");
            if (!bumpFile.exists()) {
                System.err.println("Bump audio file not found: " + bumpFile.getAbsolutePath());
                return;
            }

            AudioInputStream bumpStream = AudioSystem.getAudioInputStream(bumpFile);
            bumpClip = AudioSystem.getClip();
            bumpClip.open(bumpStream);
            bumpClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return playerName;
    }

    public void setName(String name) {
        playerName = name;
    }

    public void saveGame(int slotNumber) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("saveSlot_" + slotNumber + ".txt"))) {
            writer.println("name:" + this.getName());
            writer.println("x:" + this.worldX);
            writer.println("y:" + this.worldY);
            for (Pokemon p : pokemon) {
                writer.println("[POKEMON]");
                writer.println("p_name:" + p.getName());
                writer.println("p_level:" + p.getLvl());
                writer.println("p_hp:" + p.getHp());

                for (PokemonMove move : p.getMoves()) {
                    if (move != null) {
                        writer.println("p_move:" + move.getMoveName() + "," + move.getSp());
                    }
                }
            }

            writer.println("[END]");
            System.out.println("Game saved successfully to slot " + slotNumber);

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(int slotNumber) {
        List<Pokemon> loadedTeamList = new ArrayList<>();
        String tempPokemonName = null;
        int tempPokemonLevel = 0;
        int tempPokemonHP = 0;
        List<PokemonMove> tempMoves = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("saveSlot_" + slotNumber + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.equals("[POKEMON]")) {

                    if (tempPokemonName != null) {
                        PokemonMove[] moveArray = tempMoves.toArray(new PokemonMove[0]);
                        Pokemon loadedPokemon = gp.pf.createPokemon(tempPokemonName, tempPokemonLevel, moveArray);
                        loadedPokemon.setHp(tempPokemonHP);
                        loadedTeamList.add(loadedPokemon);
                    }

                    tempPokemonName = null;
                    tempPokemonLevel = 0;
                    tempPokemonHP = 0;
                    tempMoves.clear();
                    continue;
                }

                // Check for the [END] marker
                if (line.equals("[END]")) {
                    // Finalize the very last Pokemon in the file
                    if (tempPokemonName != null) {
                        PokemonMove[] moveArray = tempMoves.toArray(new PokemonMove[0]);
                        Pokemon loadedPokemon = gp.pf.createPokemon(tempPokemonName, tempPokemonLevel, moveArray);
                        loadedPokemon.setHp(tempPokemonHP);
                        loadedTeamList.add(loadedPokemon);
                    }
                    break; 
                }

                String[] parts = line.split(":", 2);
                if (parts.length < 2)
                    continue;
                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "name":
                        this.setName(value);
                        break;
                    case "x":
                        this.worldX = Integer.parseInt(value);
                        break;
                    case "y":
                        this.worldY = Integer.parseInt(value);
                        break;
                    case "p_name":
                        tempPokemonName = value;
                        break;
                    case "p_level":
                        tempPokemonLevel = Integer.parseInt(value); 
                        break;
                    case "p_hp":
                        tempPokemonHP = Integer.parseInt(value);
                        break;
                    case "p_move":
                        String[] moveParts = value.split(",");
                        String moveName = moveParts[0];
                        int currentPP = Integer.parseInt(moveParts[1]);

                        PokemonMove move = gp.pmf.createMove(moveName);
                        move.setSp(currentPP);

                        tempMoves.add(move);
                        break;
                }
            }
            Pokemon[] finalTeamArray = loadedTeamList.toArray(new Pokemon[0]);
            this.setPokemonTeam(finalTeamArray);

            System.out.println("Game loaded successfully!");

        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
        }
    }

    public void setPokemonTeam(Pokemon[] team) {
        this.pokemon = team;
    }

    public void generateRandomTeam() {
        for (int i = 0; i < 6; i++) {
            pokemon[i] = gp.pf.createPokemonRandomPlayer();
        }
    }
    public Pokemon[] getPokemonList(){
        return pokemon;
    }
}