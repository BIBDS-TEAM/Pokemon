package Pokemon;

import com.google.gson.*;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import Pokemon.PokemonBasics.PokemonAllType.*;

public class PokemonFactory {
    public static Map<String, Pokemon> loadPokemonFromJson(String path, int start, int end) throws Exception {
        Gson gson = new Gson();
        FileReader reader = new FileReader(path);

        // Read into a Map of name -> PokemonData
        Map<String, PokemonData> rawData = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, PokemonData>>(){}.getType());

        Map<String, Pokemon> pokemonMap = new HashMap<>();

        for (Map.Entry<String, PokemonData> entry : rawData.entrySet()) {
            String name = entry.getKey();
            PokemonData data = entry.getValue();

            PokemonType type = PokemonType.valueOf(data.type.toUpperCase());
            
            Pokemon pokemon = new Pokemon(name, type, data.id, data.maxHp, data.atk, data.def, data.spAtk, data.spDef, data.spd);

            if (pokemon != null) { pokemonMap.put(name, pokemon); }
            if (start == end) break;
            start++;
        }
        return pokemonMap;
    }

    public static void main(String[] args) {
        try {
            Map<String, Pokemon> pokemonMap = loadPokemonFromJson("C:\\Users\\hasan\\Documents\\GitHub\\Pokemon\\Pokemon\\PokemonBasics\\PokemonAllType\\PokemonList.json", 0, 5);
            // Do something with the pokemonMap
            for (Map.Entry<String, Pokemon> entry : pokemonMap.entrySet()) {
                Pokemon pokemon = entry.getValue();
                System.out.print(entry.getKey() + ": ");
                System.out.print("hp (" + pokemon.getHp() + "), ");
                System.out.print("atk (" + pokemon.getAtk() + "), ");
                System.out.print("def (" + pokemon.getDef() + "), ");
                System.out.print("spAtk (" + pokemon.getSpAtk() + "), ");
                System.out.print("spDef (" + pokemon.getSpDef() + "), ");
                System.out.print("spd (" + pokemon.getSpd() + "), ");
                System.out.print("type (" + pokemon.getType() + "), ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
