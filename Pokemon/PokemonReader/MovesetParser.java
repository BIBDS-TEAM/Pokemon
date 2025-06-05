package Pokemon.PokemonReader;

import Pokemon.PokemonBasics.PokemonBehavior.PokemonMove;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveCategory;
import Pokemon.PokemonBasics.PokemonBehavior.PokemonMoveType;
import Pokemon.PokemonBasics.PokemonAllType.Pokemon;
import Pokemon.PokemonBasics.PokemonAllType.PokemonType;  // changed from PokemonMoveType
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MovesetParser {
    public static Map<String, MoveData> loadMovesetFromTxt(String filePath) throws IOException {
        Map<String, MoveData> movesetMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        MoveData currentMove = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;  // skip blank lines

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

            String key = parts[0].trim().toLowerCase();  // lowercase keys
            String value = parts[1].trim();

            try {
                switch (key) {
                    case "name":
                        currentMove.moveName = value;
                        break;
                    case "type":
                        currentMove.type = PokemonMoveType.valueOf(value.toUpperCase());
                        break;
                    case "category":
                        currentMove.category = PokemonMoveCategory.valueOf(value.toUpperCase());
                        break;
                    case "power":
                        currentMove.power = value.equalsIgnoreCase("null") ? null : Integer.parseInt(value);
                        break;
                    case "accuracy":
                        currentMove.accuracy = value.equalsIgnoreCase("null") ? null : (int) (Double.parseDouble(value) * 100);
                        break;
                    case "pp":
                        currentMove.sp = Integer.parseInt(value);
                        break;
                    case "effect":
                        currentMove.effect = value;
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Invalid value for key '" + key + "': '" + value + "'. Skipping.");
            }
        }

        if (currentMove != null && currentMove.moveName != null && !movesetMap.containsKey(currentMove.moveName)) {
            movesetMap.put(currentMove.moveName, currentMove);
        }

        reader.close();
        return movesetMap;
    }

    public static void main(String[] args) {
        try {
            Map<String, MoveData> movesetMap = loadMovesetFromTxt("Pokemon\\PokemonReader\\PokemonMoveSetList.txt");
            for (Map.Entry<String, MoveData> entry : movesetMap.entrySet()) {
                MoveData moveData = entry.getValue();
                System.out.println("Move Name: " + moveData.moveName);
                System.out.println("Move Type: " + moveData.type);
                System.out.println("Move Category: " + moveData.category);
                System.out.println("Move Power: " + moveData.power);
                System.out.println("Move Accuracy: " + moveData.accuracy);
                System.out.println("Move PP: " + moveData.sp);
                System.out.println("Move Effect: " + moveData.effect);
                System.out.println("--------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
