package Pokemon;

import com.google.gson.*;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import Pokemon.PokemonBasics.PokemonAllType.*;

public class PokemonReadData {
    public static Map<String, Pokemon> loadPokemonFromJson(String path) throws Exception {
         Gson gson = new Gson();
        FileReader reader = new FileReader(path);

        // Read into a Map of name -> PokemonData
        Map<String, PokemonData> rawData = gson.fromJson(reader, 
            new com.google.gson.reflect.TypeToken<Map<String, PokemonData>>(){}.getType());

        Map<String, Pokemon> pokemonMap = new HashMap<>();

        for (Map.Entry<String, PokemonData> entry : rawData.entrySet()) {
            String name = entry.getKey();
            PokemonData data = entry.getValue();

            PokemonType type = PokemonType.valueOf(data.type);
            Pokemon p = null;

            // You can adjust this to your actual class structure
            p = new PokemonNormal(name, type, data.lvl, data.maxHp, data.atk, data.def, data.spAtk, data.spDef, data.spd);

            if (p != null) {
                pokemonMap.put(name, p);
            }
        }

        return pokemonMap;
    }

    public static void main(String[] args) {
        try {
            Map<String, Pokemon> pokemonMap = loadPokemonFromJson("C:\\Users\\hasan\\Documents\\GitHub\\Pokemon\\Pokemon\\PokemonBasics\\PokemonAllType\\PokemonList.json");
            // Do something with the pokemonMap
            for (Map.Entry<String, Pokemon> entry : pokemonMap.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
