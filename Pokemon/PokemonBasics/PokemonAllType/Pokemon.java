package Pokemon.PokemonBasics.PokemonAllType;

import Pokemon.PokemonBasics.PokemonBehavior.*;
import Pokemon.PokemonReader.*;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;

public class Pokemon {
    protected PokemonData pokemonData;
    protected String name;
    protected PokemonType[] type = new PokemonType[2];
    protected int lvl;
    protected int maxHp;
    protected int hp;
    protected int atk;
    protected int def;
    protected int spAtk;
    protected int spDef;
    protected int spd;
    protected MajorStatus majorStatus;
    protected BufferedImage[] model = new BufferedImage[3];
    protected String miniModelPath;
    protected String AllyFightModelPath;
    protected String EnemyFightModelPath;
    protected PokemonSound sound;
    protected PokemonMove[] moves = new PokemonMove[4];

    protected final int MAX_BUFF_STACK = 5;
    protected final int MAX_DEBUFF_STACK = 4;
    // if less than 0 debuff, if more than 0 buff
    protected int hpStack = 0;
    protected int atkStack = 0;
    protected int defStack = 0;
    protected int spAtkStack = 0;
    protected int spdStack = 0;

    protected int dotDamage = 0;
    protected PokemonMoveEffect moveEffect = null;
    protected int moveEffectStack = 0;

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

    public void paint(Graphics g, int modelIndex) {
        g.drawImage(model[modelIndex], 0, 0, null);
    }

    public Pokemon(Pokemon other) {
        this.name = other.name;
        this.type = other.type;
        this.lvl = other.lvl;
        this.maxHp = other.maxHp;
        this.hp = other.hp;
        this.atk = other.atk;
        this.def = other.def;
        this.spAtk = other.spAtk;
        this.spDef = other.spDef;
        this.spd = other.spd;
        this.moves = other.moves;
    }

    public Pokemon(String name, PokemonType[] type, int lvl, int maxHp, int atk, int def, int spAtk, int spDef, int spd,
                   String miniModelPath, String AllyFightModelPath, String EnemyFightModelPath, PokemonMove[] moves) {
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
        this.moves = moves;

        // --- START OF DEBUGGING CODE ---
        System.out.println("------------------------------------------");
        System.out.println("Attempting to load assets for: " + this.name);
        try {
            // Debugging Mini Model
            File miniFile = new File(miniModelPath);
            System.out.println("Trying to read mini sprite from: " + miniFile.getAbsolutePath());
            if(miniFile.exists()) {
                System.out.println("SUCCESS: Mini sprite file found!");
                model[0] = ImageIO.read(miniFile);
            } else {
                System.err.println("FAILURE: Mini sprite file NOT found!");
            }

            // Debugging Ally Model
            File allyFile = new File(AllyFightModelPath);
            System.out.println("Trying to read ally sprite from: " + allyFile.getAbsolutePath());
            if(allyFile.exists()) {
                System.out.println("SUCCESS: Ally sprite file found!");
                model[1] = ImageIO.read(allyFile);
            } else {
                System.err.println("FAILURE: Ally sprite file NOT found!");
            }

            // Debugging Enemy Model
            File enemyFile = new File(EnemyFightModelPath);
            System.out.println("Trying to read enemy sprite from: " + enemyFile.getAbsolutePath());
            if(enemyFile.exists()) {
                System.out.println("SUCCESS: Enemy sprite file found!");
                model[2] = ImageIO.read(enemyFile);
            } else {
                System.err.println("FAILURE: Enemy sprite file NOT found!");
            }
            System.out.println("------------------------------------------");

        } catch (IOException e) {
            System.err.println("An IOException occurred. This usually means the file exists but cannot be read.");
            e.printStackTrace();
        }
        // --- END OF DEBUGGING CODE ---
    }
    
    // --- Rest of the file is unchanged ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PokemonType[] getType() {
        return type;
    }

    public void setType(PokemonType[] type) {
        this.type = type;
    }

    public void setMoveEffectStack(int moveEffectStack) {
        this.moveEffectStack = moveEffectStack;
    }

    public void decreaseMoveEffectStack() {
        if (moveEffectStack <= 0)
            return;
        this.moveEffectStack -= 1;
    }

    public int getMoveEffectStack() {
        return moveEffectStack;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
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

    public void setDotDmg(int dotDmg) {
        if (dotDmg > maxHp)
            this.dotDamage = maxHp;
        else if (dotDmg < 0)
            this.dotDamage = 0;
        else
            this.dotDamage = dotDmg;
    }

    public int getDotDmg() {
        if (dotDamage > maxHp) return maxHp;
        else if (dotDamage < 0) return 0;
        else return dotDamage;
    }

    public PokemonMoveEffect getMoveEffect() {
        return moveEffect;
    }

    public void resetStatusEffect() {
        moveEffect = null;
    }

    public void setMoveEffect(PokemonMoveEffect moveEffect) {
        this.moveEffect = moveEffect;
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

    public BufferedImage getAllyFightModel() {
        return model[1];
    }

    public BufferedImage getEnemyFightModel() {
        return model[2];
    }

    public PokemonMove getMove(int moveIndex) {
        if (moveIndex < 0 && moveIndex >= moves.length) {
            System.err.println("Invalid move index (0 - 3): " + moveIndex);
            return null;
        }
        return moves[moveIndex];
    }

    public PokemonMove[] getMoves() {
        return moves;
    }

    public void setMiniModel(BufferedImage miniModel) {
        if (miniModel != null)
            this.model[0] = miniModel;
        else System.err.println("Mini model cannot be null.");
    }

    public void setAllyFightModel(BufferedImage AllyFightModel) {
        if (AllyFightModel != null)
            this.model[1] = AllyFightModel;
        else System.err.println("Fight model cannot be null.");
    }

    public void setEnemyFightModel(BufferedImage EnemyFightModel) {
        if (EnemyFightModel != null)
            this.model[2] = EnemyFightModel;
        else System.err.println("Fight model cannot be null.");
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

    public int statsUp(int stat) {
        int statsUp = (int)(stat + (lvl * 1.5) + 5);
        return statsUp;
    }

    public void lvlUp() {
        lvl++;
        hpStatsUp();
        atk = statsUp(atk);
        def = statsUp(def);
        spAtk = statsUp(spAtk);
        spDef = statsUp(spDef);
        spd = statsUp(spd);
    }

    public PokemonMoveCategory getMoveCategory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMoveCategory'");
    }
}