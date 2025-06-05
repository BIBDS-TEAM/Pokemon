package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PokemonMove_PHYSICAL_ATTACK extends PokemonMove {
    protected int power;
    protected double accuracy;
    protected PokemonMoveType type;

    public PokemonMove_PHYSICAL_ATTACK(String moveName, int maxSp, String desc, int power, double accuracy,
            PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc, moveType, moveCategory);
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
