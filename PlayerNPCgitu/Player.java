package PlayerNPCgitu;

import GuiTileMapThing.GamePanel;
import GuiTileMapThing.KeyInput;
import Pokemon.PokemonBasics.PokemonAllType.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Player extends Entity {
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
        if(direction == null){
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
        gp.cc.checkEntity(this,gp.npcList);
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
        if (isWalkingSoundPlaying) return;

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
    public String getName(){
        return playerName;
    }
    public void setName(String name){
        playerName = name;
    }
}

