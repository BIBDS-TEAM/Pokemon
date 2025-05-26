package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import java.util.Map;
import java.util.HashMap;

public class PokemonMove_SKIP extends PokemonMove {
    public PokemonMove_SKIP(String moveName, int maxSp, String desc, int minLvl, PokemonMoveType moveType, PokemonMoveCategory moveCategory) {
        super(moveName, maxSp, desc, minLvl, moveType, moveCategory);
    }

    public void move(Pokemon pokemon) {
        
    }
}