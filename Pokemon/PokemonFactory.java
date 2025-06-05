package Pokemon;

import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonBehavior.*;
import Pokemon.PokemonReader.*;

import java.util.Map;

public class PokemonFactory {
    private Map<String, PokemonData> dataMap;

    public PokemonFactory(Map<String, PokemonData> dataMap) {
        this.dataMap = dataMap;
    }

    public Pokemon createPokemon(String name, int level, PokemonMove[] moves) {
        PokemonData data = dataMap.get(name.toUpperCase());
        if (data == null) {
            System.err.println("No data found for Pokémon: " + name);
            return null;
        }

        String[] types = new String[2];
        for (int i = 0; i < data.types.length; i++) {
            types[i] = data.types[i].toUpperCase();
        }

        String modelBase = "../Pokemon/TileGambar/" + data.id;
        String miniModelPath = modelBase + "_1.png";
        String allyFightPath = modelBase + "_2.png";
        String enemyFightPath = modelBase + "_3.png";

        return new Pokemon(
            data.name,
            types,
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
}
