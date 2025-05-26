package Pokemon.PokemonBasics.PokemonBehavior;

import Pokemon.PokemonBasics.PokemonAllType.PokemonType;

public class PokemonMultiplier {
    protected final static PokemonType[] types = {PokemonType.NORMAL, PokemonType.FIRE, PokemonType.WATER, PokemonType.GRASS, PokemonType.ELECTRIC, PokemonType.ICE, PokemonType.ROCK};
    // 0 for Normal, 1 for Fire, 2 for Water, 3 for Grass, 4 for Electric, 5 for Ice, 6 for Rock
    protected final static double[][] attackMultiplier = {
        {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5}, // Normal attacking Fire, Water, Grass, Electric, Ice, Rock
        {1.0, 0.5, 0.5, 2.0, 1.0, 2.0, 1.0}, // Fire attacking Normal, Water, Grass, Electric, Ice, Rock
        {1.0, 2.0, 0.5, 0.5, 1.0, 1.0, 1.0}, // Water attacking Normal, Fire, Grass, Electric, Ice, Rock
        {1.0, 0.5, 2.0, 0.5, 1.0, 2.0, 1.0}, // Grass attacking Normal, Fire, Water, Electric, Ice, Rock
        {1.0, 1.0, 2.0, 0.5, 1.0, 1.0, 1.0}, // Electric attacking Normal, Fire, Water, Grass, Ice, Rock
        {1.0, 0.5, 1.0, 2.0, 0.5, 1.0, 1.0}, // Ice attacking Normal, Fire, Water, Grass, Electric, Rock
        {1.0, 2.0, 1.0, 1.0, 2.0, 0.5, 1.0}, // Rock attacking Normal, Fire, Water, Grass, Electric, Ice
    };

    public static double getAttackMultiplier(PokemonType attacker, PokemonType defender) {
        return attackMultiplier[attacker.ordinal()][defender.ordinal()];
    }
}
