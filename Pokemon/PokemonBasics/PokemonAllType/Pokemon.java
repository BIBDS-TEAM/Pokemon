package Pokemon.PokemonBasics.PokemonAllType;

import Pokemon.PokemonBasics.PokemonBehavior.*;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class Pokemon {
    protected String name;
    protected PokemonType type;
    protected int lvl;
    protected int maxHp;
    protected int hp;
    protected int atk;
    protected int def;
    protected int spAtk;
    protected int spDef;
    protected int spd;
    // index 0 for miniModel and 1 for fightModel
    protected BufferedImage[] model = new BufferedImage[2];
    protected String miniModelPath;
    protected String fightModelPath;
    // index 0 for ATTACK, 1 for DEFEND, 2 for SKIPPING_TURN, 3 for TAKE_DAMAGE, 4
    // for HEAL, 5 for SPECIAL_ATTACK, 6 for SPECIAL_DEFEND, 7 for LVL_UP, 8 for
    // MOVE, 9 for RESIST, 10 for DODGE
    protected PokemonSound[] sound = new PokemonSound[11];
    protected PokemonMove[] move;

    public void print() {
        System.out.println(("Name: " + name));
        System.out.println("Type: " + type);
        System.out.println("Level: " + lvl);
        System.out.println("Max HP: " + maxHp);
        System.out.println("HP: " + hp);
        System.out.println("Attack: " + atk);
        System.out.println("Defense: " + def);
        System.out.println("Special Attack: " + spAtk);
        System.out.println("Special Defense: " + spDef);
        System.out.println("Speed: " + spd);
    }

    public Map<String, String> getPokemonInfo() {
        Map<String, String> pokemonInfo = new HashMap<>();
        pokemonInfo.put("name", name);
        pokemonInfo.put("type", type.toString());
        pokemonInfo.put("level", String.valueOf(lvl));
        pokemonInfo.put("maxHP", String.valueOf(maxHp));
        pokemonInfo.put("HP", String.valueOf(hp));
        pokemonInfo.put("attack", String.valueOf(atk));
        pokemonInfo.put("defense", String.valueOf(def));
        pokemonInfo.put("specialAttack", String.valueOf(spAtk));
        pokemonInfo.put("specialDefense", String.valueOf(spDef));
        pokemonInfo.put("speed", String.valueOf(spd));
        pokemonInfo.put("miniModelPath", miniModelPath);
        pokemonInfo.put("fightModelPath", fightModelPath);
        return pokemonInfo;
    }

    public void paint(Graphics g, int modelIndex) {
        g.drawImage(model[modelIndex], 0, 0, null);
    }

    public Pokemon(String name, PokemonType type, int lvl, int maxHp, int atk, int def, int spAtk, int spDef, int spd) {
        this.name = name;
        this.type = type;
        this.lvl = lvl;
        this.maxHp = maxHp;
        setHp(maxHp);
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.spd = spd;
    }

    public Pokemon(String name, PokemonType type, int lvl, int maxHp, int atk, int def, int spAtk, int spDef, int spd,
            String miniModelPath, String fightModelPath) {
        this.name = name;
        this.type = type;
        this.lvl = lvl;
        this.maxHp = maxHp;
        setHp(maxHp);
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.spd = spd;
        try {
            model[0] = ImageIO.read(new File(miniModelPath));
            model[1] = ImageIO.read(new File(fightModelPath));
            System.out.println("Image loaded successfully!");
        } catch (IOException e) {
            System.err.println("Failed to load pokemon's model, error: " + e.getMessage());
        }
    }

    // setter & getter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonType getType() {
        return type;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        if (hp > maxHp)
            this.hp = maxHp;
        else if (hp < 0)
            this.hp = 0;
        else
            this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getSpAtk() {
        return spAtk;
    }

    public void setSpAtk(int spAtk) {
        this.spAtk = spAtk;
    }

    public int getSpDef() {
        return spDef;
    }

    public void setSpDef(int spDef) {
        this.spDef = spDef;
    }

    public int getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public void setModel(String filePath, int modelIndex) {
        if (modelIndex < 0 || modelIndex >= this.model.length) {
            System.err.println("Invalid model index (0 - 1): " + modelIndex);
            return;
        }
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            this.model[modelIndex] = image;
        } catch (IOException e) {
            System.err.println("Failed to load pokemon's model, error: " + e.getMessage());
        }
    }

    public BufferedImage getMiniModel() {
        return model[0];
    }

    public BufferedImage getFightModel() {
        return model[1];
    }

    public PokemonSound getSound(int soundIndex) {
        if (soundIndex < 0 || soundIndex >= sound.length) {
            System.err.println("Invalid sound index (0 - 9): " + soundIndex);
            return null;
        }
        return sound[soundIndex];
    }

    public Dimension getPreferredSize(int modelIndex) {
        if (modelIndex < 0 || modelIndex >= model.length)
            throw new IndexOutOfBoundsException();
        if (model[modelIndex] == null) {
            return new Dimension(100, 100);
        } else {
            return new Dimension(model[modelIndex].getWidth(null), model[modelIndex].getHeight(null));
        }
    }

    public int hpStatsUp() {
        return (int)(maxHp + (lvl * 1.5) + 15);
    }
    public int statsUp(int stat)  {
        int statsUp = (int)(stat + (lvl * 1.5) + 5);
        return statsUp;
    }

    public void lvlUp()  {
        lvl++;
        hpStatsUp();
        atk = statsUp(atk);
        def = statsUp(def);
        spAtk = statsUp(spAtk);
        spDef = statsUp(spDef);
        spd = statsUp(spd);
    }

    public void attack(PokemonMove move, Pokemon defender) {
        move.move(this, defender);
    }

    public void specialAttack(Pokemon pokemon);

    public void defend(Pokemon pokemon);

    public void specialDefend(Pokemon pokemon);

    public void skipTurn(Pokemon pokemon);

    public void takeDamage(int damage);

    public void playSound() {

    }
}