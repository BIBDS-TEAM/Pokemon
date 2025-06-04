package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class PokemonMove_STATUS_DEBUFF extends PokemonMove {
    protected String debuffType;
    protected boolean isDebuffInPercent; // true = percentage, false = flat
    protected double debuffValue;
    protected double accuracy;

    public PokemonMove_STATUS_DEBUFF(String moveName, int maxSp, String desc, String debuffType, boolean isDebuffInPercent, double debuffValue, double accuracy) {
        super(moveName, maxSp, desc);
        this.debuffType = debuffType.toUpperCase();
        this.isDebuffInPercent = isDebuffInPercent;
        setDebuffQ(debuffValue);
        setAccuracy(accuracy);
    }

    public String getDebuffedStats() {
        return debuffType;
    }

    public void setDebuffedStats(String debuffType) {
        this.debuffType = debuffType.toUpperCase();
    }

    public boolean isDebuffType() {
        return isDebuffInPercent;
    }

    public void setDebuffType(boolean isDebuffInPercent) {
        this.isDebuffInPercent = isDebuffInPercent;
    }

    public double getDebuffQ() {
        return debuffValue;
    }

    public void setDebuffQ(double debuffValue) {
        if (debuffValue < 0) debuffValue = 0;
        else this.debuffValue = debuffValue;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        if (accuracy < 0) accuracy = 0;
        else this.accuracy = accuracy;
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException("This method is not supported yet for this class.");
    }

    public Map<String, String> move(Pokemon pokemon) {
        throw new UnsupportedOperationException("This method is not supported yet for this class.");
    }

    public Map<String, String> move(Pokemon attacker, Pokemon defender) {
        Random rand = new Random();
        int accuracy = rand.nextInt(100) + 1;
        String message = attacker.getName() + " used " + moveName + " on " + defender.getName() + " and set " + debuffType + " to ";
        int debuffSum = 0;
        Map<String, String> response = new HashMap<String, String>();

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("targetName", defender.getName());
        response.put("desc", desc);

        if (!isSpZero()) {
            response.put("error", "SP not enough");
            response.put("flag", "false");
            return response;
        }

        if (accuracy <= this.accuracy) {
            switch (debuffType) {
                case "HP":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getHp() * (1 - debuffValue)));
                    } else {
                        debuffSum = (defender.getHp() - (int)debuffValue);
                    }
                    defender.setHp(debuffSum);
                    break;
                case "ATK":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getAtk() * (1 - debuffValue)));
                    } else {
                        debuffSum = (defender.getAtk() - (int)debuffValue);
                    }
                    defender.setAtk(debuffSum);
                    break;
                case "DEF":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getDef() * (1 - debuffValue)));
                    } else {
                        
                        debuffSum = ((int)(defender.getDef() * (1 - debuffValue)));
                    }
                    defender.setDef(debuffSum);
                    break;
                case "SPATK":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getDef() * (1 - debuffValue)));
                    } else {
                        debuffSum = (defender.getSpAtk() - (int)debuffValue);
                    }
                    defender.setSpAtk(debuffSum);
                    break;
                case "SPDEF":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getSpDef() * (1 - debuffValue)));
                    } else {
                        debuffSum = (defender.getSpDef() - (int)debuffValue);
                    }
                    defender.setSpDef(debuffSum);
                    break;
                case "SPD":
                    if (isDebuffInPercent) {
                        debuffSum = ((int)(defender.getSpd() * (1 - debuffValue)));
                    } else {
                        debuffSum = (defender.getSpd() - (int)debuffValue);
                    }
                    defender.setSpd(debuffSum);
                    break;
                default:
                    System.out.println("Debuff type not found");
                    response.put("error", "Debuff type not found");
                    response.put("flag", "false");
                    return response;
                }
                
            message += debuffSum + "!";

            response.put("message", message);
            response.put("flag", "true");

            System.out.println(message);

            return response;
        } else {
            response.put("error", "Attack missed!");
            response.put("flag", "false");

            System.out.println("Attack missed!");
        }

        return response;
    }
}