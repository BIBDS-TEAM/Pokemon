package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.util.Map;
import java.util.HashMap;

public class PokemonMove_STATUS_DEFENSE_by_percentage extends PokemonMove {
    protected double defIncPercent;
    
    public PokemonMove_STATUS_DEFENSE_by_percentage(String moveName, int maxSp, String desc, double defIncPercent) {
        super(moveName, maxSp, desc);
        moveCategory = PokemonMoveCategory.STATUS;
        moveType = PokemonMoveType.DEFENSE;
        this.defIncPercent = defIncPercent;
    }

    public double getDefIncPercent() {
        return defIncPercent;
    }
    public void setDefIncPercent(double defIncPercent) {
        this.defIncPercent = defIncPercent;
        if (defIncPercent < 0) this.defIncPercent = 0;
    }
    
    public Map<String, String> move() {
        throw new UnsupportedOperationException();
    }

    public Map<String, String> move(Pokemon pokemon) {
        Map<String, String> response = new HashMap<>();
        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("desc", desc);

        if (!isSpZero()) {
            response.put("message", "SP not enough");
            response.put("flag", "false");
            return response;
        }
        useMove();
        // Calculate boost based on base defense (pokemon.def, not pokemon.getDef())
        int boostAmount = (int)(pokemon.getDef() * (defIncPercent / 100.0)); 
        pokemon.applyTemporaryDefenseBoost(boostAmount, 1); // Apply temporary boost for 1 turn
        
        response.put("message", pokemon.getName() + " used " + moveName + " and greatly increased its DEF by " + defIncPercent + "% for 1 turn!");
        response.put("flag", "true");
        return response;
    }

    public Map<String, String> move(Pokemon user, Pokemon target) {
        Map<String, String> response = new HashMap<>();
        if (isSpZero()) {
            System.out.println("SP not enough");

            response.put("moveName", moveName);
            response.put("moveType", moveType.toString());
            response.put("sp", String.valueOf(sp));
            response.put("desc", desc);
            response.put("message", "SP not enough");
            response.put("flag", "false");
            return response;
        }
        useMove();
        target.setDef((int)(target.getDef() * (1 + defIncPercent / 100.0)));
        System.out.println(user.getName() + " used " + moveName + " and increased " + target.getName() + "'s DEF by " + defIncPercent + "%!");

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("targetName", target.getName());
        response.put("desc", desc);
        response.put("message", user.getName() + " used " + moveName + " and increased " + target.getName() + "'s DEF by " + defIncPercent + "%!");
        response.put("flag", "true");
        return response;
    }
}