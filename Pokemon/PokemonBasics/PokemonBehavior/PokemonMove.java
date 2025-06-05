package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.io.File;
import javax.sound.sampled.*;
import Pokemon.PokemonReader.MoveData;

import java.util.Map;
import java.util.HashMap;

public class PokemonMove {
    private Clip clip;
    protected String moveName;
    protected int power;
    protected double accuracy;

    protected double buffOrDebuffValue;
    protected final double BUFF_PERCENTAGE_PER_STACK = 0.25;
    protected final double DEBUFF_PERCENTAGE_PER_STACK = 0.15;
    protected final int MAX_BUFF_STACK = 5;
    protected final int MAX_DEBUFF_STACK = 4;
    // if less than 0 debuff, if more than 0 buff
    protected int stack = 0;

    protected PokemonMoveType moveType;
    protected PokemonMoveCategory moveCategory;
    protected int maxSp;
    protected int sp;

    protected String effect;
    protected String desc;
    protected String pokemonMoveSoundPath;

    public void loadPokemonMoveByType(MoveData moveData) {
        this.moveName = moveData.moveName;
        this.moveCategory = moveData.category;
        this.moveType = moveData.type;
        String moveCategory = String.valueOf(moveData.category);
        if (moveCategory.equals("Physical") || moveCategory.equals("Special")) {
            setPower(moveData.power);
            setAccuracy(moveData.accuracy);
        }
        setDesc(moveData.description);
        setEffect(moveData.effect);
    }

    public PokemonMove(String moveName, PokemonMoveCategory moveCategory, String pokemonMoveSoundPath) {
        this.moveName = moveName;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = pokemonMoveSoundPath;
    }

    protected String Effect;

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType MoveType, PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = "../Pokemon/audioSave/"+ moveName +".wav";
        loadStatusEffect();
    }
    public void playPokemonMoveSound(){
        new Thread(() -> {
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(pokemonMoveSoundPath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            } catch (Exception e) {
                System.err.println("Error playing sound: " + pokemonMoveSoundPath);
                e.printStackTrace();
            }
        }).start();
    }
    public String getMoveName() {
        return moveName;
    }
    public void setmoveName(String moveName) {
        this.moveName = moveName;
    }

    public int getPower() {
        if (moveCategory == PokemonMoveCategory.PHYSICAL || moveCategory == PokemonMoveCategory.SPECIAL) {
           if (power > 0) return power;
           else throw new NullPointerException("Power cannot be negative");
        }
        throw new NullPointerException("Power is not applicable for this move category");
    }

    public void setPower(int power) {
        if (moveCategory == PokemonMoveCategory.PHYSICAL || moveCategory == PokemonMoveCategory.SPECIAL) {
            this.power = power;
        } else {
            throw new NullPointerException("Power is not applicable for this move category");
        }
    }

    public double getAccuracy() {
        if (moveCategory == PokemonMoveCategory.RUN) {
            throw new NullPointerException("Accuracy is not applicable for this move category");
        }
        if (accuracy > 0) return accuracy;
        else throw new NullPointerException("Accuracy cannot be negative");
    }

    public void setAccuracy(double accuracy) {
        if (moveCategory == PokemonMoveCategory.RUN) {
            throw new NullPointerException("Accuracy is not applicable for this move category");
        }
        this.accuracy = accuracy;
    }

    public double getBuffOrDebuffValue() {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (buffOrDebuffValue > 0) return buffOrDebuffValue;
            else throw new NullPointerException("Buff or debuff value cannot be negative");
        }
        throw new NullPointerException("Buff or debuff value is not applicable for this move category");
    }

    public void setBuffOrDebuffValue(double buffOrDebuffValue) {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (buffOrDebuffValue > 0) this.buffOrDebuffValue = buffOrDebuffValue;
            else throw new NullPointerException("Buff or debuff value cannot be negative");
        } else {
            throw new NullPointerException("Buff or debuff value is not applicable for this move category");
        }
    }

    public int getStack() {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (stack > MAX_BUFF_STACK) return MAX_BUFF_STACK;
            if (stack <= 0) return 0;
            return stack;
        }
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (stack < MAX_DEBUFF_STACK) return MAX_DEBUFF_STACK;
            if (stack >= 0) return 0;
            return stack;
        }
        throw new NullPointerException("Stack is not applicable for this move category");
    }

    public void stackedStack() {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (stack < MAX_BUFF_STACK) stack++;
        }
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (stack > MAX_DEBUFF_STACK) stack--;
        }
        throw new NullPointerException("Stack is not applicable for this move category");
    }

    public double getBuffOrDebuffPercentage() {
        if (moveCategory == PokemonMoveCategory.BUFF) return BUFF_PERCENTAGE_PER_STACK * stack;
        if (moveCategory == PokemonMoveCategory.DEBUFF) return DEBUFF_PERCENTAGE_PER_STACK * stack;
        throw new NullPointerException("Buff or debuff percentage is not applicable for this move category");
    }

    public void setEffect(String effect) {
        if (effect != null) this.effect = effect;
        else this.effect = "";
    }

    public PokemonMoveType getMoveType() {
        return moveType;
    }
    public void setMoveType(PokemonMoveType moveType) {
        this.moveType = moveType;
    }

    public int getSp() {
        if (sp < 0) sp = 0;
        return sp;
    }
    public boolean isSpZero() {
        return sp > 0;
    }

    public void useMove(Pokemon user, Pokemon target) {
        if (!isSpZero()) {
            System.out.println("SP not enough");
            return;
        }
        else {

        }
        sp--;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public Map<String, String> useMoveInfo() {
            Map<String, String> response = new HashMap<String, String>();
        if (!isSpZero()) {
            System.out.println("SP not enough");
            response.put("error", "SP not enough");
            return response;
        } else {
            response.put("MoveName", moveName);
            response.put("MoveType", moveType.toString());
            response.put("sp", String.valueOf(sp));
            response.put("desc", desc);
            return response;
        }
    }

    public void playSound() {

    }

    public String toString() {
        return moveName + " " + moveCategory + " "  + maxSp + " " + moveType + " " + sp + " " + desc;
    }
}