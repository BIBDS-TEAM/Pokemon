package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PokemonMove_PHYSICAL_ATTACK extends PokemonMove {
    protected int power;
    protected double accuracy;

    public PokemonMove_PHYSICAL_ATTACK(String moveName, int maxSp, String desc, int minLvl, int power, double accuracy,
            PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc, minLvl, moveType, moveCategory);
        this.power = power;
        this.accuracy = accuracy;
    }

    public int getPower() {
        return power;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
        if (accuracy < 0)
            this.accuracy = 0;
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException("This method is not supported yet for this class.");
    }

    public Map<String, String> move(Pokemon pokemon) {
        throw new UnsupportedOperationException("This method is not supported yet for this class.");
    }

    // pokemon1 attack pokemon2
    public Map<String, String> move(Pokemon attacker, Pokemon defender) {
        Map<String, String> response = new HashMap<String, String>();

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("desc", desc);

        if (isSpZero()) {
            System.out.println("SP not enough");

            response.put("message", "SP not enough");
            response.put("flag", "false");
            return response;
        }
        useMove();

        Random rand = new Random();
        double roll = rand.nextDouble();
        if (roll > accuracy) {
            System.out.println("Attack missed");

            response.put("error", "Attack missed");
            response.put("flag", "false");
            return response;
        }

        int attackerAtk = attacker.getAtk();
        int defenderDef = defender.getDef();
        PokemonType[] attackerTypes = attacker.getType();
        PokemonType[] defenderTypes = defender.getType();

        double multiplier = 1.0;
        for (PokemonType attackerType : attackerTypes) {
            for (PokemonType defenderType : defenderTypes) {
                multiplier *= PokemonMultiplier.getAttackMultiplier(attackerType, defenderType);
            }
        }
        int damage = (int) ((2 * attacker.getLvl() / 5 + 2) * (attackerAtk * power)
                * multiplier
                / ((defenderDef > 0 ? defenderDef : 1) * 50.0 / 2 + 2.0));

        defender.setHp(defender.getHp() - damage);

        System.out.println(attacker.getName() + " used " + moveName + " and dealt " + damage + " damage to "
                + defender.getName() + "!");

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("desc", desc);
        response.put("message", attacker.getName() + " used " + moveName + " and dealt " + damage + " damage to "
                + defender.getName() + "!");
        response.put("flag", "true");
        return response;
    }
}
