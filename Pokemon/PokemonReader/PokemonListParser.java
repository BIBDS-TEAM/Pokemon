package Pokemon.PokemonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PokemonListParser {
    public static Map<String, PokemonData> loadPokemonFromTxt(String filePath) throws IOException {
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
                        // Handle both single type and comma-separated multiple types
                        currentPokemon.types = new ArrayList<>(Arrays.asList(value.split(",")));
                        for (int i = 0; i < currentPokemon.types.size(); i++) {
                            currentPokemon.types.set(i, currentPokemon.types.get(i).trim().toUpperCase());
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
