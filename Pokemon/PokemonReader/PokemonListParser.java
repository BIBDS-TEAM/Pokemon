package Pokemon.PokemonReader;

import Pokemon.PokemonBasics.PokemonAllType.PokemonType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PokemonListParser {
    public static Map<String, PokemonData> loadPokemonFromTxt() throws IOException {
        String filePath = "../Pokemon/dataSave/PokemonList.txt";
        Map<String, PokemonData> pokemonMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        PokemonData currentPokemon = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("---")) {
                if (currentPokemon != null && currentPokemon.name != null) {
                    pokemonMap.put(currentPokemon.name, currentPokemon);
                }
                currentPokemon = null; 
                continue;
            }

            if (currentPokemon == null) {
                currentPokemon = new PokemonData();
            }

            String[] parts = line.split(":", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim().toUpperCase();
            String value = parts[1].trim();

            try {
                switch (key) {
                    case "NAME":
                        currentPokemon.name = value.toUpperCase();
                        break;
                    case "TYPE":
                        String[] typeParts = value.split(",");
                        for (int i = 0; i < typeParts.length && i < 2; i++) {
                    try {
                        currentPokemon.types[i] = PokemonType.valueOf(typeParts[i].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: Unknown Pokémon type '" + typeParts[i] + "' for Pokémon " + currentPokemon.name + ". Skipping type.");
                }
            }
                        break;
                    case "ID":
                        currentPokemon.id = Integer.parseInt(value);
                        break;
                    case "MAXHP":
                        currentPokemon.maxHp = Integer.parseInt(value);
                        break;
                    case "ATK":
                        currentPokemon.atk = Integer.parseInt(value);
                        break;
                    case "DEF":
                        currentPokemon.def = Integer.parseInt(value);
                        break;
                    case "SPATK":
                        currentPokemon.spAtk = Integer.parseInt(value);
                        break;
                    case "SPDEF":
                        currentPokemon.spDef = Integer.parseInt(value);
                        break;
                    case "SPD":
                        currentPokemon.spd = Integer.parseInt(value);
                        break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Warning: Could not parse number for key '" + key + "' with value '" + value + "'. Skipping line.");
            }
        }
        if (currentPokemon != null && currentPokemon.name != null && !pokemonMap.containsKey(currentPokemon.name)) {
            pokemonMap.put(currentPokemon.name, currentPokemon);
        }
        reader.close();
        return pokemonMap;
    }
}
