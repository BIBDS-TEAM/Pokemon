package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class PokemonMove_SPECIAL_SPATTACK extends PokemonMove {
    protected int power;
    protected double accuracy;
    protected PokemonMoveType type;

    public PokemonMove_SPECIAL_SPATTACK(String moveName, int maxSp, String desc, int power, double accuracy,
            PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc, moveType, moveCategory);
        this.power = power;
        this.accuracy = accuracy;
    }

    public int getPower() {
        return power;
    }
    
    public void setPower(int power) {
        if (power > 0)
            this.power = power;
        else
            this.power = 0;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        if (accuracy > 0)
            this.accuracy = accuracy;
        else
            this.accuracy = 0;
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException("This method isn't supported yet for this class'");
    }

    public Map<String, String> move(Pokemon pokemon) {
        throw new UnsupportedOperationException("This method isn't supported yet for this class'");
    }

    public Map<String, String> move(Pokemon attacker, Pokemon defender) {
        Map<String, String> response = new HashMap<>();
        String message;

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("Desc", desc);

        if (isSpZero()) {
            message = "SP is not enough";

            System.out.println(message);
            response.put("error", message);
            response.put("flag", "false");
            return response;
        } else {
            Random rand = new Random();
            double accuracy = rand.nextInt(100) + 1;
            int value = 0;

            if (this.accuracy <= accuracy) {
                useMove();
                message = attacker.getName() + " used " + moveName + " and hit " + defender.getName() + " with ";

                int spAtk = attacker.getSpAtk();
                int spDef = defender.getSpDef();
                // int attackerMultiplierIndex = -1;
                // PokemonType[] types = PokemonMultiplier.types;
                // for (int i = 0; i < types.length; i++) {
                // if (types[i] == attacker.getType()) {
                // attackerMultiplierIndex = i;
                // break;
                // }
                // }

                // if (attackerMultiplierIndex == -1) {
                // message = "Attacker multiplier index not found!";
                // System.out.println("Error: " + message);
                // response.put("error", message);
                // response.put("flag", "false");
                // return response;
                // }
                PokemonType[] attackerTypes = attacker.getType();
                PokemonType[] defenderTypes = defender.getType();

                double multiplier = 1.0;
                for (PokemonType attackerType : attackerTypes) {
                    for (PokemonType defenderType : defenderTypes) {
                        multiplier *= PokemonMultiplier.getAttackMultiplier(attackerType, defenderType);
                    }
                }
                int atkLvl = attacker.getLvl();

                value = (int) ((2 * atkLvl / 5 + 2) * (spAtk * (power * multiplier)) / (spDef * 50.0 + 2.0) + 2.0);

                defender.setHp(defender.getHp() - value);

                message += value + " damage!";
                System.out.println(message);

                response.put("message", message);
                response.put("flag", "true");
            } else {
                message = attacker.getName() + " used " + moveName + " and missed!";
                System.out.println(message);

                response.put("error", message);
                response.put("flag", "false");
            }
        }
        return response;
    }
}