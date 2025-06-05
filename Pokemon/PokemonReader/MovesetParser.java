package Pokemon.PokemonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveCategory;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveType;

public class MovesetParser {
    public static Map<String, MoveData> loadMovesetFromTxt(String filePath) throws IOException {
        Map<String, MoveData> movesetMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        MoveData currentMove = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("---")) {
                if (currentMove != null && currentMove.moveName != null) {
                    movesetMap.put(currentMove.moveName, currentMove);
                }
                currentMove = null;
                continue;
            }

            if (currentMove == null) {
                currentMove = new MoveData();
            }

            String[] parts = line.split(":", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim().toUpperCase();
            String value = parts[1].trim();

            try {
                switch (key) {
                    case "MOVE":
                        currentMove.moveName = value;
                        break;
                    case "TYPE":
                        currentMove.type = PokemonMoveType.valueOf(value.toUpperCase());
                        break;
                    case "CATEGORY": // JSON key is "Cat"
                        currentMove.category = PokemonMoveCategory.valueOf(value);
                        break;
                    case "POWER":
                        currentMove.power = value.equalsIgnoreCase("N/A") ? null : Integer.parseInt(value);
                        break;
                    case "ACCURACY": // JSON key is "Acc."
                        currentMove.accuracy = value.equalsIgnoreCase("N/A") ? null : Integer.parseInt(value);
                        break;
                    case "PP":
                        currentMove.sp = Integer.parseInt(value);
                        break;
                    case "EFFECT":
                        currentMove.effect = value.equalsIgnoreCase("N/A") ? null : value;
                        break;
                }
            } catch (NumberFormatException e) {
                 System.err.println("Warning: Could not parse number for key '" + key + "' with value '" + value + "'. Skipping line.");
            }
        }
        if (currentMove != null && currentMove.moveName != null && !movesetMap.containsKey(currentMove.moveName)) {
            movesetMap.put(currentMove.moveName, currentMove);
        }
        reader.close();
        return movesetMap;
    }
    public static Map<String, MoveData> loadMovesetFromTxt(String filePath, int start, int end) throws IOException {
        Map<String, MoveData> movesetMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        MoveData currentMove = null;

        int i = start;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("---")) {
                if (currentMove != null && currentMove.moveName != null) {
                    
                    movesetMap.put(currentMove.moveName, currentMove);
                    i++;
                    if (i == end) break;
                }
                currentMove = null;
                continue;
            }

            if (currentMove == null) {
                currentMove = new MoveData();
            }

            String[] parts = line.split(":", 2);
            if (parts.length < 2) continue;

            String key = parts[0].trim().toUpperCase();
            String value = parts[1].trim();

            try {
                switch (key) {
                    case "MOVE":
                        currentMove.moveName = value;
                        break;
                    case "TYPE":
                        currentMove.type = PokemonMoveType.valueOf(value.toUpperCase());
                        break;
                    case "CATEGORY": // JSON key is "Cat"
                        currentMove.category = PokemonMoveCategory.valueOf(value);
                        break;
                    case "POWER":
                        currentMove.power = value.equalsIgnoreCase("N/A") ? null : Integer.parseInt(value);
                        break;
                    case "ACCURACY": // JSON key is "Acc."
                        currentMove.accuracy = value.equalsIgnoreCase("N/A") ? null : Integer.parseInt(value);
                        break;
                    case "PP":
                        currentMove.sp = Integer.parseInt(value);
                        break;
                    case "EFFECT":
                        currentMove.effect = value.equalsIgnoreCase("N/A") ? null : value;
                        break;
                }
            } catch (NumberFormatException e) {
                 System.err.println("Warning: Could not parse number for key '" + key + "' with value '" + value + "'. Skipping line.");
            }
        }
        if (currentMove != null && currentMove.moveName != null && !movesetMap.containsKey(currentMove.moveName)) {
            movesetMap.put(currentMove.moveName, currentMove);
        }
        reader.close();
        return movesetMap;
    }
}
