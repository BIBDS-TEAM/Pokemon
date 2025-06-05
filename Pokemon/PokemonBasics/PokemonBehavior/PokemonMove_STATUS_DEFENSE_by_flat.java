package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.*;
import java.util.Map;
import java.util.HashMap;

public class PokemonMove_STATUS_DEFENSE_by_flat extends PokemonMove {
    protected int defIncFlat;

    public PokemonMove_STATUS_DEFENSE_by_flat(String moveName, int maxSp, String desc, int defIncFlat) {
        super(moveName, maxSp, desc);
        this.defIncFlat = defIncFlat;
        moveCategory = PokemonMoveCategory.STATUS;
        moveType = PokemonMoveType.DEFENSE;
    }

    public int getDefIncFlat() {
        return defIncFlat;
    }
    public void setDefIncFlat(int defIncFlat) {
        this.defIncFlat = defIncFlat;
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException();
    }

    public Map<String, String> move(Pokemon pokemon) {
        Map<String, String> response = new HashMap<String, String>();
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
        // Apply temporary boost for 1 turn
        pokemon.applyTemporaryDefenseBoost(defIncFlat, 1); 
        
        response.put("message", pokemon.getName() + " used " + moveName + " and greatly increased its DEF for 1 turn!");
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
        target.setDef((int)(target.getDef() + defIncFlat));
        System.out.println(user.getName() + " used " + moveName + " and increased " + target.getName() + "'s DEF by " + defIncFlat + "!");

        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("sp", String.valueOf(sp));
        response.put("targetName", target.getName());
        response.put("desc", desc);
        response.put("message", user.getName() + " used " + moveName + " and increased " + target.getName() + "'s DEF by " + defIncFlat + "!");
        response.put("flag", "true");
        return response;
    }
}
