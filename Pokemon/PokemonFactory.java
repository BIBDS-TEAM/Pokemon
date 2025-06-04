// package Pokemon;

// import com.google.gson.*;
// import java.io.FileReader;
// import java.util.HashMap;
// import java.util.Map;
// import Pokemon.PokemonBasics.PokemonAllType.*;
// import Pokemon.PokemonReader.PokemonData;

// public class PokemonFactory {
//     public static Map<String, Pokemon> loadPokemonFromJsonWithIndex(String path, int start, int end) throws Exception {
//         Gson gson = new Gson();
//         FileReader reader = new FileReader(path);

//         // Read into a Map of name -> PokemonData
//         Map<String, PokemonData> rawData = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, PokemonData>>(){}.getType());

//         Map<String, Pokemon> pokemonMap = new HashMap<>();

//         for (Map.Entry<String, PokemonData> entry : rawData.entrySet()) {
//             String name = entry.getKey();
//             PokemonData data = entry.getValue();

//             PokemonType[] type = new PokemonType[2];
//             type[0] = PokemonType.valueOf(data.type[0].toUpperCase());
//             type[1] = PokemonType.valueOf(data.type[1].toUpperCase());
            
//             Pokemon pokemon = new Pokemon(name, type, data.id, data.maxHp, data.atk, data.def, data.spAtk, data.spDef, data.spd);

//             if (pokemon != null) { pokemonMap.put(name, pokemon); }
//             if (start == end) break;
//             start++;
//         }
//         return pokemonMap;
//     }

//     public static Map<String, Pokemon> loadPokemonFromJsonWithNames(String path, String[] names) throws Exception {
//         Gson gson = new Gson();
//         FileReader reader = new FileReader(path);

//         // Read into a Map of name -> PokemonData
//         Map<String, PokemonData> rawData = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<Map<String, PokemonData>>(){}.getType());

//         Map<String, Pokemon> pokemonMap = new HashMap<>();

//         for (int i = 0; i < names.length; i++) {
//             String name = names[i];
//             PokemonData data = rawData.get(name);
//             PokemonType[] type = new PokemonType[2];
//             type[0] = PokemonType.valueOf(data.type[0].toUpperCase());
//             type[1] = PokemonType.valueOf(data.type[1].toUpperCase());
            
//             Pokemon pokemon = new Pokemon(name, type, data.id, data.maxHp, data.atk, data.def, data.spAtk, data.spDef, data.spd);

//             if (pokemon != null) { pokemonMap.put(name, pokemon); }
//         }
//         return pokemonMap;
//     }

//     public static void main(String[] args) {
       
//     }
// }
