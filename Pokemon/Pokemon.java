package Projek_BIBD_Pokemon.Pokemon;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

public abstract class Pokemon {
    protected String name;
    protected PokemonType type;
    protected int lvl;
    protected int hp;
    protected int mana;
    protected int atk;
    protected int def;
    protected int spAtk;
    protected int spDef;
    protected int spd;
    protected String soundFilePath;
    protected String imageFilePath;

    public void print() {
        System.out.println(("Name: " + name));
        System.out.println("Type: " + type);
        System.out.println("Level: " + lvl);
        System.out.println("HP: " + hp);
        System.out.println("Mana: " + mana);
        System.out.println("Attack: " + atk);
        System.out.println("Defense: " + def);
        System.out.println("Special Attack: " + spAtk);
        System.out.println("Special Defense: " + spDef);
        System.out.println("Speed: " + spd);
    }

    public Pokemon(String name, PokemonType type, int lvl, int hp, int mana, int atk, int def, int spAtk, int spDef, int spd, String soundFilePath) {
        this.name = name;
        this.type = type;
        this.lvl = lvl;
        this.hp = hp;
        this.mana = mana;
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.spd = spd;
        this.soundFilePath = soundFilePath;
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
        this.hp = hp;
    }

    public int getMana() {
        return mana;
    }
    public void setMana(int mana) {
        this.mana = mana;
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
    
    public String getSoundFilePath() {
        return soundFilePath;
    }
    public void setSoundFilePath(String soundFilePath) {
        this.soundFilePath = soundFilePath;
    }

    public abstract void move();

    public abstract void lvlUp();

    public abstract void attack(Pokemon pokemon);

    public abstract void specialAttack(Pokemon pokemon);

    public abstract void defend(Pokemon pokemon);

    public abstract void specialDefend(Pokemon pokemon); 

    public abstract void skipTurn(Pokemon pokemon);

    public abstract void takeDamage(int damage);

    public void playSound() {
        
    }
}   