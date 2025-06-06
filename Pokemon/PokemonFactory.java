package Pokemon;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonReader.*;
import java.util.Map;

public class PokemonFactory {
    private PokemonMoveFactory pmF;
    private Map<String, PokemonData> dataMap;
    // --- CHANGE THIS LINE ---
    private String path = "TileGambar/"; // REMOVED the "Pokemon/" prefix

    // --- The rest of your file stays exactly the same ---
    public PokemonFactory(Map<String, PokemonData> dataMap, PokemonMoveFactory moveFactory) {
        this.dataMap = dataMap;
        this.pmF = moveFactory;
    }

    public Pokemon createPokemon(String name, int level, PokemonMove[] moves) {
        PokemonData data = dataMap.get(name.toUpperCase());
        if (data == null) {
            System.err.println("No data found for Pokémon: " + name);
            return null;
        }

        String miniModelPath = path + "mini_" + data.id + ".png";
        String allyFightPath = path + "Ally_" + String.format("%03d", data.id) + ".png";
        String enemyFightPath = path + "Enemy_" + String.format("%03d", data.id) + ".png";

        return new Pokemon(
            data.name,
            data.types,
            level,
            data.maxHp,
            data.atk,
            data.def,
            data.spAtk,
            data.spDef,
            data.spd,
            miniModelPath,
            allyFightPath,
            enemyFightPath,
            moves
        );
    }
    
    // ... no other changes needed in the rest of the file ...
    public Pokemon createPokemonRandom() {
        int random = (int)(Math.random() * 100) + 1;
        for (PokemonData pokemon : dataMap.values()) {
            if (pokemon.id == random) {
                return new Pokemon(
                    pokemon.name,
                    pokemon.types,
                    (int)(Math.random() * 100) + 1,
                    pokemon.maxHp,
                    pokemon.atk,
                    pokemon.def,
                    pokemon.spAtk,
                    pokemon.spDef,
                    pokemon.spd,
                    (path + "mini_" + String.format("%d",pokemon.id) + ".png"),
                    (path + "Ally_" + String.format("%03d", pokemon.id) + ".png"),
                    (path + "Enemy_" + String.format("%03d", pokemon.id) + ".png"),
                    pmF.generateRandomMoveSet(pokemon.types)
                );
            }
        }
        return null;
    }

    public Pokemon createPokemonRandomPlayer() {
        int random = (int)(Math.random() * 151) + 1;
        int randomLev = ((int)(Math.random() * 100) + 1);
        int realLev = randomLev > 75 ? ((int)(Math.random() * 100) + 1) > randomLev ? randomLev : ((int)(Math.random() * 50) + 1) : randomLev;
        for (PokemonData pokemon : dataMap.values()) {
            if (pokemon.id == random) {
                return new Pokemon(
                    pokemon.name,
                    pokemon.types,
                    realLev,
                    pokemon.maxHp,
                    pokemon.atk,
                    pokemon.def,
                    pokemon.spAtk,
                    pokemon.spDef,
                    pokemon.spd,
                    (path + "mini_" + pokemon.id + ".png"),
                    (path + "Ally_" + String.format("%03d", pokemon.id) + ".png"),
                    (path + "Enemy_" + String.format("%03d", pokemon.id) + ".png"),
                    pmF.generateRandomMoveSet(pokemon.types)
                );
            }
        }
        return null;
    }
}