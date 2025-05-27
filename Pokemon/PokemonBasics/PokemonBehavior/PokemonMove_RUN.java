package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.util.Map;
import java.util.HashMap;

public class PokemonMove_RUN extends PokemonMove {
    public PokemonMove_RUN(String moveName, int maxSp, String desc, int minLvl, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc, minLvl, moveType, moveCategory);
    }

    public Map<String, String> move() {
        throw new UnsupportedOperationException("This method isn't supported yet for this class'");
    }

    public Map<String, String> move(Pokemon attacker, Pokemon defender) {
        throw new UnsupportedOperationException("This method isn't supported yet for this class'");
    }

    public Map<String, String> move(Pokemon pokemon) {   
        Map<String, String> response = new HashMap<>();
        response.put("moveName", moveName);
        response.put("moveType", moveType.toString());
        response.put("desc", desc);
        response.put("message", pokemon.getName() + " used " + moveName + " and ran away!");
        response.put("flag", "true");
        return response;
    }
}