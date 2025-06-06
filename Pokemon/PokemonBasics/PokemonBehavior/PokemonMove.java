package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonReader.MoveData;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;

public class PokemonMove {
    private Clip clip;
    protected String moveName;
    protected int power;
    protected double accuracy;

    protected double buffOrDebuffValue;
    protected final double BUFF_PERCENTAGE_PER_STACK = 0.25;
    protected final double DEBUFF_PERCENTAGE_PER_STACK = 0.15;

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
        this.sp = moveData.sp;
        String moveCategory = String.valueOf(moveData.category).toUpperCase();
        if (moveCategory.equals("PHYSICAL") || moveCategory.equals("SPECIAL")) {
            if(moveData.power == null){
                setPower(0);
            }
            else {
                setPower(moveData.power);
            }
            setAccuracy(moveData.accuracy);
        } 
        setDesc(moveData.description);
        setEffect(moveData.effect);
    }

    public PokemonMove(MoveData moveData) {
        loadPokemonMoveByType(moveData);
    }

    public PokemonMove(String moveName, MoveData moveData) {
        loadPokemonMoveByType(moveData);
        this.moveName = moveName;
    }

    public PokemonMove(String moveName, PokemonMoveCategory moveCategory, String pokemonMoveSoundPath) {
        this.moveName = moveName;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = pokemonMoveSoundPath;
    }

    protected String Effect;

    public PokemonMove(String moveName, int maxSp, String desc, PokemonMoveType MoveType,
            PokemonMoveCategory moveCategory) {
        this.moveName = moveName;
        this.maxSp = maxSp;
        this.sp = maxSp;
        this.desc = desc;
        this.moveType = MoveType;
        this.moveCategory = moveCategory;
        this.pokemonMoveSoundPath = "../Pokemon/audioSave/" + moveName + ".wav";
    }

    public void playPokemonMoveSound() {
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
            if (power > 0)
                return power;
            else
                throw new NullPointerException("Power cannot be negative");
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
        if (accuracy > 0)
            return accuracy;
        else
            throw new NullPointerException("Accuracy cannot be negative");
    }

    public void setAccuracy(double accuracy) {
        if (moveCategory == PokemonMoveCategory.RUN) {
            throw new NullPointerException("Accuracy is not applicable for this move category");
        }
        this.accuracy = accuracy;
    }

    public PokemonMoveCategory getMoveCategory() {
        return moveCategory;
    }

    public void setMoveCategory(PokemonMoveCategory moveCategory) {
        this.moveCategory = moveCategory;
    }

    public double getBuffOrDebuffValue() {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (buffOrDebuffValue > 0)
                return buffOrDebuffValue;
            else
                throw new NullPointerException("Buff or debuff value cannot be negative");
        }
        throw new NullPointerException("Buff or debuff value is not applicable for this move category");
    }

    public void setBuffOrDebuffValue(double buffOrDebuffValue) {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (buffOrDebuffValue > 0)
                this.buffOrDebuffValue = buffOrDebuffValue;
            else
                throw new NullPointerException("Buff or debuff value cannot be negative");
        } else {
            throw new NullPointerException("Buff or debuff value is not applicable for this move category");
        }
    }

    public double getBuffOrDebuffPercentage(int stack) {
        if (moveCategory == PokemonMoveCategory.STATUS) {
            if (stack >= 0)
                return BUFF_PERCENTAGE_PER_STACK * stack;
            if (stack < 0)
                return DEBUFF_PERCENTAGE_PER_STACK * stack;
        }
        throw new NullPointerException("Buff or debuff percentage is not applicable for this move category");
    }

    public void setEffect(String effect) {
        if (effect != null){
            this.effect = effect;
            this.effect = effect.toUpperCase();
    }
        else
            this.effect = "";
    }

    public PokemonMoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(PokemonMoveType moveType) {
        this.moveType = moveType;
    }

    public int getSp() {
        return sp;
    }
    public void setSp(int sp){
        this.sp = sp;
    }
    public boolean isSpZero() {
        return sp <= 0;
    }

    public void useMove() {
        if (isSpZero()) {
            System.out.println("SP not enough");
            return;
        }
        sp--;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Pokemon[] move(Pokemon user, Pokemon target) {
        System.out.println(this.sp);
        if (isSpZero()) {
            System.out.println("SP not enough");
            return new Pokemon[] { user, target };
        }
        useMove();
        String moveCategory = String.valueOf(this.moveCategory);
        if (moveCategory == "PHYSICAL" || moveCategory == "SPECIAL" || moveCategory == "STATUS") {
            Random rand = new Random();
            double roll = rand.nextDouble();
            if (roll > accuracy) {
                System.out.println("Attack missed");
                return new Pokemon[] { user, target };
            }

            if (moveCategory == "PHYSICAL" || moveCategory == "SPECIAL") {
                System.out.println(moveCategory + " attack");
                double pntMultiplier = PokemonMultiplier.getAttackMultiplier(moveType, target.getType()[0]);
                if(target.getType()[1] != null){
                    pntMultiplier *= PokemonMultiplier.getAttackMultiplier(moveType, target.getType()[1]);
                }

                int attackerAtk, defenderDef, damage;
                if (moveCategory == "PHYSICAL") {
                    attackerAtk = user.getAtk();
                    defenderDef = target.getDef();
                } else {
                    attackerAtk = user.getSpAtk();
                    defenderDef = target.getSpDef();
                }
                damage = (int)((((((10.0 * user.getLvl()) / 5 + 2) * attackerAtk * power) / defenderDef / 4.0 + 2)) * pntMultiplier);
                System.out.println("DefenderDef: " + defenderDef);
                System.out.println("Damage: " + damage);
                System.out.println("effect" + this.effect);
                roll = rand.nextDouble();
                if (effect.equals("LIFESTEAL")) {
                    user.setHp((int) (user.getHp() + damage * 0.5));
                }
                if (roll <= 0.1) {
                    String[] effectAnh = effect.toUpperCase().split("\s,");
                    PokemonMoveEffect effectType = PokemonMoveEffect.valueOf(effectAnh[0]);
                    switch (effectType) {
                        case BURN:
                            target.setMoveEffectStack(2);
                            target.setDotDmg((int) (damage * 0.3));
                            break;
                        case POISON:
                            target.setMoveEffectStack(4);
                            target.setDotDmg((int) (damage * 0.2));
                            break;
                        case FLINCH:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case LIFESTEAL:
                            System.out.println("Skipping Life Steal");
                            break;
                        case FREEZE:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case MULTIPLE:
                            int intRoll = rand.nextInt(Integer.parseInt(effectAnh[1]), Integer.parseInt(effectAnh[2]));
                            for (int i = 0; i < intRoll; i++) {
                                target.setHp(target.getHp() - damage);
                            }
                            break;
                        case INVINCIBLE:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case CONFUSE:
                            user.setHp((int)(user.getHp() - damage * 0.3));
                            return new Pokemon[] { user, target };
                        case CRIT:
                            damage *= 1.7;
                            break;
                        case INFLICT:
                            user.setHp((int)(user.getHp() - damage * 0.4));
                            break;
                        case HEAL:
                            user.setHp((int)(target.getHp() + damage * 0.3));
                            break;
                        case ONEHIT:
                            damage = target.getHp();
                            break;
                        default:
                            System.out.println("No effect");
                            break;
                    }
                }
                target.setHp(target.getHp() - damage);
                System.out.println("Target HP: " + target.getHp());
            } else if (moveCategory == "STATUS") {
                String[] effectAnh = effect.toUpperCase().split("\s,");
                    PokemonMoveEffect effectType = PokemonMoveEffect.valueOf(effectAnh[0]);
                    int targetEffectStack = target.getMoveEffectStack();
                    switch (effectType) {
                        case ATTACK:
                            switch(effectAnh[2]) {
                                case "+":
                                    target.setMoveEffectStack(targetEffectStack + Integer.parseInt(effectAnh[3]));
                                    target.setAtk((int)(target.getAtk() * (1 + (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                                case "-":
                                    target.setMoveEffectStack(targetEffectStack - Integer.parseInt(effectAnh[3]));
                                    target.setAtk((int)(target.getAtk() * (1 - (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                            }
                            break;
                        case DEFENCE:
                            switch(effectAnh[2]) {
                                case "+":
                                    target.setMoveEffectStack(targetEffectStack + Integer.parseInt(effectAnh[3]));
                                    target.setDef((int)(target.getDef() * (1 + (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                                case "-":
                                    target.setMoveEffectStack(targetEffectStack - Integer.parseInt(effectAnh[3]));
                                    target.setDef((int)(target.getDef() * (1 - (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                            }
                            break;
                        case SPEED:
                            switch(effectAnh[2]) {
                                case "+":
                                    target.setMoveEffectStack(targetEffectStack + Integer.parseInt(effectAnh[3]));
                                    target.setSpd((int)(target.getSpd() * (1 + (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                                case "-":
                                    target.setMoveEffectStack(targetEffectStack - Integer.parseInt(effectAnh[3]));
                                    target.setSpd((int)(target.getSpd() * (1 - (target.getMoveEffectStack() * BUFF_PERCENTAGE_PER_STACK))));
                                    break;
                            }
                            break;
                        case POISON:
                            target.setMoveEffectStack(targetEffectStack + 2);
                            target.setDotDmg((int) (target.getMoveEffectStack() * 0.2 * 0.5 * target.getHp()));
                            break;
                        case FLINCH:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case INVINCIBLE:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case CONFUSE:
                            target.setMoveEffect(effectType);
                            target.setMoveEffectStack(2);
                            break;
                        case SLEEP:
                            target.setMoveEffectStack(1);
                            target.setMoveEffect(effectType);
                            break;
                        case HEAL:
                            user.setHp((int)(target.getHp() * 1.3));
                            break;
                        default:
                            System.out.println("No effect");
                            break;
                    }
            }
            System.out.println(user.getName() + " used " + moveName);
        }
        return new Pokemon[] { user, target };
    }

    public void playSound() {

    }

    public String toString() {
        return moveName + " " + moveCategory + " " + maxSp + " " + moveType + " " + sp + " " + desc;
    }
}